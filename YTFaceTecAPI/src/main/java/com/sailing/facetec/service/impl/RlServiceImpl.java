package com.sailing.facetec.service.impl;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.sailing.facetec.comm.DataEntity;
import com.sailing.facetec.config.SupplyConfig;
import com.sailing.facetec.dao.RlDetailMapper;
import com.sailing.facetec.dao.RlMapper;
import com.sailing.facetec.dao.RllrDetailMapper;
import com.sailing.facetec.entity.RlEntity;
import com.sailing.facetec.remoteservice.YTApi;
import com.sailing.facetec.service.RlService;
import com.sailing.facetec.service.YTService;
import com.sailing.facetec.util.CommUtils;
import com.sailing.facetec.util.FileUtils;
import com.sailing.facetec.util.ImageUtils;
import jdk.nashorn.internal.ir.ContinueNode;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import sun.reflect.ReflectionFactory;

import javax.swing.text.html.HTMLDocument;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.*;

/**
 * Created by yunan on 2017/5/4.
 */
@Service
public class RlServiceImpl implements RlService {
    private static final Logger logger = LoggerFactory.getLogger(RlServiceImpl.class);

    @Autowired
    private RlDetailMapper rlDetailMapper;

    @Autowired
    private RllrDetailMapper rllrDetailMapper;

    @Autowired
    private RlMapper rlMapper;

    @Autowired
    private SupplyConfig supplyConfig;

    @Autowired
    private YTService ytService;

    @Value("${ytface.username}")
    private String ytUsername;

    @Value("${ytface.password}")
    private String ytPassword;


    @Value("${facepic.web-path}")
    private String webPath;

    @Value("${facepic.face-dir}")
    private String faceDir;

    @Value("${exp.root-dir}")
    private String expDir;

    @Override
    public DataEntity listRlDetail(JSONArray detailInfo) {
        StringBuilder customerFilterBuilder = new StringBuilder();
        DataEntity result = new DataEntity();
        // 判断参数中是否含有lrid 来判断搜索哪个库
        // 如果不包含lrid 则从人像库中进行检索
        if (CommUtils.isNullObject(detailInfo.getJSONObject(0).get("lrid"))) {
            // 如果不包含lrid 则从人像库中进行检索

            // for(Iterator iterator = detailInfo.iterator();iterator.hasNext();){
            //     JSONObject tmp = (JSONObject) iterator.next();
            //     String supply = tmp.getString("sfdm");
            //     customerFilterBuilder.append(" ( ");
            //     customerFilterBuilder.append(
            //             String.format(
            //                     " %s='%s' and %s = '%s' ",
            //                     supplyConfig.getSupplyMap().get(supply+"_rl_rlkid"),
            //                     tmp.getString("rlkid"),
            //                     supplyConfig.getSupplyMap().get(supply+"_rl_rlid"),
            //                     tmp.getString("rlid")
            //             )
            //     );
            //     customerFilterBuilder.append(" ) or ");
            // }

            // 重构
            detailInfo.forEach(s -> {
                JSONObject tmp = (JSONObject) s;
                // 获取算法厂商代码
                String supply = tmp.getString("sfdm");
                customerFilterBuilder.append(" ( ");
                customerFilterBuilder.append(
                        String.format(
                                " %s='%s' and %s = '%s' ",
                                // 获取算法厂商人脸库编号存储列名
                                supplyConfig.getSupplyMap().get(supply + "_rl_rlkid"),
                                // 获取算法厂商人脸库id
                                tmp.getString("rlkid"),
                                // 获取算法厂商人脸id存储列名
                                supplyConfig.getSupplyMap().get(supply + "_rl_rlid"),
                                // 获取算法厂商人脸id
                                tmp.getString("rlid")
                        )
                );
                customerFilterBuilder.append(" ) or ");
            });
            // 添加一个否条件
            customerFilterBuilder.append("1=0");
            // 获取人脸详细信息
            result.setDataContent(rlDetailMapper.listRlDetail(customerFilterBuilder.toString()));
        } else {
            // 包含lrid 则从路人库中检索记录
            List<String> idList = new ArrayList<>();
            detailInfo.forEach(s -> {
                idList.add(String.format("'%s'", ((JSONObject) s).getString("lrid")));
            });
            result.setDataContent(rllrDetailMapper.listRllrDetailsByRLIDs(String.join(",", idList)));
        }
        return result;
    }

    /**
     * 添加人像记录
     *
     * @param rlEntity
     * @return
     */
    @Override
    @Transactional
    public int addRlData(RlEntity rlEntity) throws Exception {
        int result = 0;
        List<Integer> results = new ArrayList();
        String sid = loginToYT();
        JSONObject jsonObject = JSONObject.parseObject(ytService.uploadPicToReopsitory(sid, rlEntity));
        String rtn = jsonObject.getString("rtn");
        if (!"0".equals(rtn)) {
            // 接口失败抛出异常进行统一处理
            throw new Exception(String.format("上传人像图片记录失败：%s", jsonObject.getString("message")));
        }
        // 图片uri
        String ytPictureUri = jsonObject.getString("picture_uri");
        JSONArray ytResults = jsonObject.getJSONArray("results");

        ytResults.forEach(s->{
            // 获取人脸结果对象
            JSONObject faceObject = (JSONObject)s;
            // 人脸id
            String ytFaceID = faceObject.getString("face_image_id");
            // 人脸uri
            String ytFaceUri = faceObject.getString("face_image_uri");
            // 获取图片本地路径及发布路径
            String[] picPaths = getPathByPersonID(rlEntity.getRLKID(), rlEntity.getSFZH(), ytFaceID + "-pic");
            // 获取人脸照片物理路径及发布路径
            String[] facePaths = getPathByPersonID(rlEntity.getRLKID(), rlEntity.getSFZH(), ytFaceID + "-face");

            try {
                // 生成本地大图及小图地址
                if (FileUtils.base64ToFile(rlEntity.getBase64Pic(), picPaths[0])
                        && ytService.downLoadPic(ytFaceUri,facePaths[0])) {
                    // 保存依图uri地址
                    rlEntity.setDTDZ(ytPictureUri);
                    rlEntity.setRLDZ(ytFaceUri);
                    // 保存原图本地地址及发布地址
                    rlEntity.setXZDTDZ(picPaths[1]);
                    rlEntity.setXZGXDTDZ(picPaths[0]);
                    // 保存人脸本地地址及发布地址
                    rlEntity.setXZRLDZ(facePaths[1]);
                    rlEntity.setXZGXRLDZ(facePaths[0]);
                    // 保存人脸id
                    rlEntity.setRLID(ytFaceID);

                    rlEntity.setRKSJ(CommUtils.getCurrentDate());
                    rlEntity.setTJSJ(CommUtils.getCurrentDate());
                    rlEntity.setXGSJ(CommUtils.getCurrentDate());

                    results.add(rlMapper.insertRl(rlEntity));
                }
            } catch (IOException e) {
                logger.error("faile to create repository pic, faceid:{}  error:{}",ytFaceID,e.getMessage());
            }
        });

        for(Iterator iterator = results.iterator();iterator.hasNext();){
            result = result+ (int )iterator.next();
        }
        return result;
    }

    /**
     * 使用身份证来获取文件路径
     *
     * @param personID
     * @param faceid
     * @return
     */
    private String[] getPathByPersonID(String repositoryID, String personID, String faceid) {
        String path = (CommUtils.isNullObject(personID) || personID.length() < 10) ? String.format("%s\\%s\\", repositoryID, "000000") : String.format("%s\\%s\\%s\\", repositoryID, personID.substring(0, 6),personID.substring(6,10));
        List<String> results = new ArrayList<>();
        results.add(String.format("%s%s%s.jpg", faceDir, path, faceid));
        results.add(String.format("%s%s%s.jpg", webPath, path, faceid));
        String[] result = new String[results.size()];
        results.toArray(result);
        return result;
    }

    /**
     * 批量导入人像记录
     *
     * @param repositoryID
     * @param zipFile
     * @return
     */
    @Override
    @Transactional
    public int impRlDatas(String repositoryID, String zipFile) throws IOException {
        String parent = String.format("%s%s\\",expDir, UUID.randomUUID().toString());
        // 解压缩文件
        FileUtils.upZipFile(zipFile,parent);
        String xlsFile = findExcel(parent);
        RlEntity[] rlEntities = getRlEntityByXls(xlsFile);
        Arrays.asList(rlEntities).forEach(rlEntity -> {
            String picPath = rlEntity.getBase64Pic();
            try {
                rlEntity.setBase64Pic(FileUtils.fileToBase64(picPath));
                addRlData(rlEntity);
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
        return 0;
    }

    /**
     * 获取导入xls文件
     * @param path
     * @return
     */
    private String findExcel(String path){
        String result = "";
        File parent = new File(path);
        File[] subFiles = parent.listFiles();
        for(File subFile:subFiles){
            if(subFile.isDirectory()){
                result = findExcel(subFile.getPath());
                if(!CommUtils.isNullObject(result)){
                    break;
                }
            }

            if(subFile.getName().endsWith("\\.xls")){
                result = subFile.getPath();
                break;
            }
        }
        return  result;
    }

    /**
     * 获取人像库数据
     * @param excelPath
     * @return
     * @throws IOException
     */
    private RlEntity[] getRlEntityByXls(String excelPath) throws IOException {
        File excelFile = new File(excelPath);
        // 返回结果集
        List<RlEntity> rlEntities = new ArrayList();
        // 打开工作簿
        HSSFWorkbook hssfWorkbook = new HSSFWorkbook(new FileInputStream(excelFile));
        hssfWorkbook.getSheetAt(0).forEach(r->{
            if(0!=r.getRowNum()){
                RlEntity rlEntity = new RlEntity();
                rlEntity.setXM(r.getCell(1).getStringCellValue());
                rlEntity.setSFZH(r.getCell(2).getStringCellValue());
                rlEntity.setCSNF(r.getCell(3).getStringCellValue());
                switch (r.getCell(4).getStringCellValue()){
                    case "男":
                        rlEntity.setXB(1);
                        break;
                    case "女":
                        rlEntity.setXB(2);
                        break;
                    default:
                        rlEntity.setXB(0);
                        break;
                }
                rlEntity.setRLGJ(r.getCell(5).getStringCellValue());
                rlEntity.setRLSF(r.getCell(6).getStringCellValue());
                rlEntity.setRLCS(r.getCell(7).getStringCellValue());
                rlEntity.setBase64Pic(String.format(excelFile.getParent(),r.getCell(8).getStringCellValue()));
                rlEntity.setXGSJ(CommUtils.getCurrentDate());
                rlEntity.setTJSJ(CommUtils.getCurrentDate());
                rlEntities.add(rlEntity);
            }
        });
        RlEntity[] results = new RlEntity[rlEntities.size()];
        return rlEntities.toArray(results);
    }


    /**
     * 登录依图平台
     *
     * @return
     */
    private String loginToYT() {
        JSONObject jsonObject;
        jsonObject = JSONObject.parseObject(ytService.login(ytUsername, ytPassword));
        return jsonObject.getString("session_id");
    }

}
