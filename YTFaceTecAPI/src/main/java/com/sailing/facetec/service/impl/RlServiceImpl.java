package com.sailing.facetec.service.impl;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.sailing.facetec.comm.DataEntity;
import com.sailing.facetec.config.SupplyConfig;
import com.sailing.facetec.dao.RlDetailMapper;
import com.sailing.facetec.dao.RlMapper;
import com.sailing.facetec.dao.RllrDetailMapper;
import com.sailing.facetec.entity.RlDetailEntity;
import com.sailing.facetec.entity.RlEntity;
import com.sailing.facetec.queue.DataQueue;
import com.sailing.facetec.remoteservice.YTApi;
import com.sailing.facetec.service.RlService;
import com.sailing.facetec.service.YTService;
import com.sailing.facetec.util.CommUtils;
import com.sailing.facetec.util.FileUtils;
import com.sailing.facetec.util.ImageUtils;
import jdk.nashorn.internal.ir.ContinueNode;
import org.apache.ibatis.annotations.Results;
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
                boolean createPic= FileUtils.base64ToFile(rlEntity.getBase64Pic(), picPaths[0]);
                boolean createFace = ytService.downLoadPic(ytFaceUri,facePaths[0]);
                // 生成本地大图及小图地址
                if (createPic&&createFace ) {
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
    public int impRlDatas(String repositoryID, String zipFile) throws IOException, InterruptedException {
        int result = 0;
        String parent = String.format("%s\\zip\\%s\\",expDir, UUID.randomUUID().toString());
        // 解压缩文件
        FileUtils.upZipFile(zipFile,parent);
        String xlsFile = findExcel(parent);
        if(CommUtils.isNullObject(xlsFile)){
            logger.info("添加批量导入任务失败，未获取xls文件");
        }else{
            logger.info("添加批量导入任务 任务文件 {}",xlsFile);
            DataQueue.putToQueue(xlsFile);
            result =1;
        }
        return result;
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

            if(subFile.getName().endsWith(".xls")){
                result = subFile.getPath();
                break;
            }
        }
        return  result;
    }

    /**
     * 修改人员信息
     * @param jsonObject
     * @return 返回不为 0 成功
     */
    @Override
    public int altPersonalInfo(JSONObject jsonObject) {
        int result =0;

        // 登录 获取sid
        String sid = loginToYT();
        RlDetailEntity rlDetailEntity = new RlDetailEntity();

        rlDetailEntity.setRLID(jsonObject.getLong("rlid").toString());
        if(jsonObject.containsKey("xm")) {
            rlDetailEntity.setXM(jsonObject.getString("xm"));
        }
        if(jsonObject.containsKey("qybh"))
        {
            rlDetailEntity.setYLZD2(jsonObject.getInteger("qybh")+"");
        }
        if(jsonObject.containsKey("csnf"))
        {
            rlDetailEntity.setCSNF(jsonObject.getString("csnf"));
        }
        if(jsonObject.containsKey("xb"))
        {
            rlDetailEntity.setXB(jsonObject.getInteger("xb"));
        }
        if(jsonObject.containsKey("mz"))
        {
            rlDetailEntity.setYLZD3(jsonObject.getInteger("mz")+"");
        }
        if(jsonObject.containsKey("sfzh"))
        {
            rlDetailEntity.setSFZH(jsonObject.getString("sfzh"));
        }

        // jsonObject = JSONObject.parseObject(ytService.altPersonalInfo(sid,jsonObject));
        // if("0".equals(jsonObject.getString("rtn"))){
        //
        //     result =rlDetailMapper.altPersonalInfo(rlDetailEntity);
        // }
        return result;
    }


    @Override
    public int delPersonal(String rlid) {
        int result =0;

        // 登录 获取sid
        String sid = loginToYT();
        RlDetailEntity rlDetailEntity = new RlDetailEntity();

        rlDetailEntity.setRLID(rlid);
        rlDetailEntity.setYLZD1("0");
        JSONObject jsonObject = JSONObject.parseObject(ytService.delPersonal(sid,rlid));
        if("0".equals(jsonObject.getString("rtn"))){

            result =rlDetailMapper.delProsonal(rlDetailEntity);
        }
        return result;
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
