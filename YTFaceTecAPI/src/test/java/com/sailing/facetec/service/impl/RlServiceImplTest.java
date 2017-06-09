package com.sailing.facetec.service.impl;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.sailing.facetec.comm.DataEntity;
import com.sailing.facetec.entity.RlEntity;
import com.sailing.facetec.service.RlService;
import com.sailing.facetec.util.CommUtils;
import com.sailing.facetec.util.ImageUtils;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;


/**
 * Created by yunan on 2017/5/9.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes = com.sailing.facetec.app.class)
public class RlServiceImplTest {

    @Autowired
    private RlService rlService;

    @Test
    public void listRlDetail() throws Exception {
        JSONArray jsonArray = new JSONArray();
        DataEntity result = null;
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("sfdm","yt");
        jsonObject.put("rlkid","72");
        jsonObject.put("rlid","20266198323167235");
        jsonArray.add(jsonObject);

        jsonObject = new JSONObject();
        jsonObject.put("sfdm","yt");
        jsonObject.put("rlkid","72");
        jsonObject.put("rlid","20266198323167233");
        jsonArray.add(jsonObject);

        result = rlService.listRlDetailByRlidAndSupply(jsonArray);
        Assert.assertEquals(2,result.getDataContent().size());

        jsonArray.clear();
        jsonObject = new JSONObject();
        jsonObject.put("sfdm","yt");
        jsonObject.put("rlkid","72");
        jsonObject.put("lrid","18577348462903956");
        jsonArray.add(jsonObject);

        jsonObject = new JSONObject();
        jsonObject.put("sfdm","yt");
        jsonObject.put("rlkid","72");
        jsonObject.put("lrid","18577348462903960");
        jsonArray.add(jsonObject);

        result = rlService.listRlDetailByRlidAndSupply(jsonArray);
        Assert.assertEquals(2,result.getDataContent().size());
    }

    @Test
    public void addRlData() throws Exception {
        String base64Str = ImageUtils.picToBase64("D:\\3.jpg").replaceAll("\r|\n", "");
        RlEntity rlEntity = new RlEntity();
        rlEntity.setBase64Pic(base64Str);

        rlEntity.setCSNF("1984-04-26");
        rlEntity.setXM("俞楠");
        rlEntity.setXB(1);
        rlEntity.setRLKID("2");
        rlEntity.setRLSF("上海");
        rlEntity.setRLCS("上海");
        rlEntity.setRLGJ("中国");
        rlEntity.setSFZH("310113198404260095");

        rlService.addRlData(rlEntity);
    }

    @Test
    public void impRlDatas() throws Exception {
        rlService.impRlDatas("2","D:\\test.zip");


        Thread.sleep(1000*60*60);

        //
        //
        // try {
        //     String xlsFile = DataQueue.takeFromQueue();
        //     // LOGGER.info("获取人像文件 {}",xlsFile);
        //     RlEntity[] rlEntities = getRlEntityByXls(xlsFile);
        //     // LOGGER.info("获取人像列表 {} ",rlEntities.length);
        //     Arrays.asList(rlEntities).forEach(rlEntity -> {
        //         String picPath = rlEntity.getBase64Pic();
        //         try {
        //             rlEntity.setBase64Pic(FileUtils.fileToBase64(picPath));
        //             rlEntity.setRLKID("2");
        //             rlService.addRlData(rlEntity);
        //         } catch (Exception e) {
        //             e.printStackTrace();
        //             // LOGGER.info("人像 {} 导入失败 {}",rlEntity.getXM(),e.getMessage());
        //         }
        //     });
        //     // LOGGER.info("人像导入完成");
        // } catch (Exception e) {
        //     // LOGGER.info("人像导入失败 {}",e.getMessage());
        //     e.printStackTrace();
        // }
    }

    /**
     * 获取人像库数据
     *
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
        hssfWorkbook.getSheetAt(0).forEach(r -> {
            if (0 != r.getRowNum()) {
                RlEntity rlEntity = new RlEntity();
                rlEntity.setXM(r.getCell(1).getStringCellValue());
                rlEntity.setSFZH(r.getCell(2).getStringCellValue());
                rlEntity.setCSNF(r.getCell(3).getStringCellValue());
                switch (r.getCell(4).getStringCellValue()) {
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
                rlEntity.setRLSF(r.getCell(5).getStringCellValue());
                rlEntity.setRLCS(r.getCell(6).getStringCellValue());
                rlEntity.setBase64Pic(String.format("%s\\%s.jpg",excelFile.getParent(), r.getCell(7).getStringCellValue()));
                rlEntity.setXGSJ(CommUtils.getCurrentDate());
                rlEntity.setTJSJ(CommUtils.getCurrentDate());
                rlEntities.add(rlEntity);
            }
        });
        RlEntity[] results = new RlEntity[rlEntities.size()];
        return rlEntities.toArray(results);
    }
}