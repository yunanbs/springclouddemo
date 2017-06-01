package com.sailing.facetec.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.parser.Feature;
import com.sailing.facetec.comm.ActionResult;
import com.sailing.facetec.comm.DataEntity;
import com.sailing.facetec.config.ActionCodeConfig;
import com.sailing.facetec.entity.*;
import com.sailing.facetec.service.*;
import com.sailing.facetec.util.CommUtils;
import com.sailing.facetec.util.FileUtils;
import com.sailing.facetec.util.PersonIDUntils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.Arrays;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

/**
 * Created by yunan on 2017/4/26.
 * api 主controller
 */
@RestController
// 跨域支持
@CrossOrigin(origins = "*", methods = {RequestMethod.POST, RequestMethod.OPTIONS, RequestMethod.GET, RequestMethod.DELETE,RequestMethod.PUT})
// 允许配置文件刷新
@RefreshScope
public class DatabaseController {

    public static final Logger LOGGER = LoggerFactory.getLogger(DatabaseController.class);

    //region Services
    @Autowired
    private RllrService rllrService;

    @Autowired
    private RlgjService rlgjService;

    @Autowired
    private RlsxtService rlsxtService;

    @Autowired
    private RlkService rlkService;

    @Autowired
    private RlService rlService;

    @Autowired
    private RedisService redisService;

    @Autowired
    private SfpjService sfpjService;

    @Autowired
    private FileService fileService;

    @Autowired
    private RlbkrwService rlbkrwService;
    //endregion

    //region 实时数据库相关key
    // 获取redis数据库中抓拍数据的缓存key
    @Value("${redis-keys.capture-data}")
    private String captureData;
    // 获取redis数据库中报警数据的缓存key
    @Value("${redis-keys.alert-data}")
    private String alertData;
    //endregion

    @Value("${facepic.repository}")
    private String repositoryRoot;

    /**
     * 测试接口
     *
     * @return
     */
    @RequestMapping("/")
    public String test() {
        return "hello sailling";
    }

    /**
     * 路人抓拍历史数据查询接口
     *
     * @param beginTime 查询开始时间 默认为当天00:00:00
     * @param endTime   查询截至时间 默认为当点 23:59:59
     * @param orderBy   排序字段 默认为主表xh字段降序排序
     * @param page      分页-页码 默认 1
     * @param size      分页-分页大小 默认 10
     * @param lrkids    路人库编号，多个编号使用 , 分割 默认为空
     * @param sex       性别编号，多个编号使用 , 分割 默认为空
     * @param age       年龄段编号，多个编号使用 , 分割 默认为空
     * @param glass     眼镜特征编号，多个编号使用 , 分割 默认为空
     * @param fringe    刘海特征编号，多个编号使用 , 分割 默认为空
     * @param uygur     种族特征编号，多个编号使用 , 分割 默认为空
     * @return content中包含查询结果
     */
    @RequestMapping("/LR/Captures")
    public ActionResult listCaptureDetails(
            @RequestParam(name = "beginTime", defaultValue = "") String beginTime,
            @RequestParam(name = "endTime", defaultValue = "") String endTime,
            @RequestParam(name = "orderby", defaultValue = "a.XH desc") String orderBy,
            @RequestParam(name = "page", defaultValue = "1") int page,
            @RequestParam(name = "size", defaultValue = "10") int size,
            @RequestParam(name = "lrkids", defaultValue = "") String lrkids,
            @RequestParam(name = "sex", defaultValue = "") String sex,
            @RequestParam(name = "age", defaultValue = "") String age,
            @RequestParam(name = "glass", defaultValue = "") String glass,
            @RequestParam(name = "fringe", defaultValue = "") String fringe,
            @RequestParam(name = "uygur", defaultValue = "") String uygur
    ) {
        DataEntity result = rllrService.listRllrDetail(beginTime, endTime, orderBy, page, size, lrkids, sex, age, glass, fringe, uygur);
        return new ActionResult(ActionCodeConfig.SUCCEED_CODE, ActionCodeConfig.SUCCEED_MSG, result, null);
    }


    /**
     * 路人抓拍实时数据查询
     *
     * @param lrkids 路人库编号，多个编号使用 , 分割 默认为空
     * @return 获取redis中缓存的查询结果
     */
    @RequestMapping("/LR/Captures/Real")
    public ActionResult listRealCaptureDetails(@RequestParam(name = "lrkids", defaultValue = "") String lrkids) {
        String result = rllrService.listRllrDetailReal(lrkids);
        return new ActionResult(ActionCodeConfig.SUCCEED_CODE, ActionCodeConfig.SUCCEED_MSG, JSONObject.parseObject(result), null);
    }

    /**
     * 报警历史数据查询
     *
     * @param beginTime 查询开始时间 默认为当天00:00:00
     * @param endTime   查询截止时间 默认为当天23:59:59
     * @param orderBy   排序字段 默认按照报警记录xh字段降序排列
     * @param page      分页-页码
     * @param size      分页-分页大小
     * @param xsd       最小相似度 默认为0
     * @param bz        标注编号，多个编号使用 , 分割 默认为空
     * @param rlid      人脸编号 默认为空
     * @param lrkids    路人库编号，多个编号使用 , 分割 默认为空
     * @param sex       性别编号 多个编号使用 , 分割 默认为空
     * @param age       年龄段编号 多个编号使用 , 分割 默认为空
     * @param glass     眼镜特征编号 多个编号使用 , 分割 默认为空
     * @param fringe    刘海特征编号 多个编号使用 , 分割 默认为空
     * @param uygur     种族特征编号 多个编号使用 , 分割 默认为空
     * @return datacontent中保存查询结果
     */
    @RequestMapping("/LR/Alerts")
    public ActionResult listAlertDetails(
            @RequestParam(name = "beginTime", defaultValue = "") String beginTime,
            @RequestParam(name = "endTime", defaultValue = "") String endTime,
            @RequestParam(name = "orderby", defaultValue = "a.XH desc") String orderBy,
            @RequestParam(name = "page", defaultValue = "1") int page,
            @RequestParam(name = "size", defaultValue = "10") int size,
            @RequestParam(name = "xsd", defaultValue = "0") double xsd,
            @RequestParam(name = "bz", defaultValue = "") String bz,
            @RequestParam(name = "rlid", defaultValue = "") String rlid,
            @RequestParam(name = "lrkids", defaultValue = "") String lrkids,
            @RequestParam(name = "sex", defaultValue = "") String sex,
            @RequestParam(name = "age", defaultValue = "") String age,
            @RequestParam(name = "glass", defaultValue = "") String glass,
            @RequestParam(name = "fringe", defaultValue = "") String fringe,
            @RequestParam(name = "uygur", defaultValue = "") String uygur
    ) {

        DataEntity<RlgjDetailEntity> rlgjDetailEntityDataEntity = rlgjService.listRlgjDetail(beginTime, endTime, orderBy, page, size, xsd, bz, rlid, lrkids, sex, age, glass, fringe, uygur);
        return new ActionResult(ActionCodeConfig.SUCCEED_CODE, ActionCodeConfig.SUCCEED_MSG, rlgjDetailEntityDataEntity, null);
    }

    /**
     * 实时报警数据
     *
     * @return redis中缓存的当天报警数据
     */
    @RequestMapping("/LR/Alerts/Real")
    public ActionResult listRealAlertDetails() {
        String jsonStr = redisService.getVal(alertData);
        return new ActionResult(ActionCodeConfig.SUCCEED_CODE, ActionCodeConfig.SUCCEED_MSG, JSONObject.parseObject(jsonStr), null);
    }

    /**
     * 获取摄像头列表
     *
     * @return
     */
    @RequestMapping("/SXT")
    public ActionResult listSXT(@RequestParam(value = "name",defaultValue = "")String name) {
        DataEntity<SxtDetailEntity> rlsxtEntityDataEntity = rlsxtService.listAllXST(name);
        return new ActionResult(ActionCodeConfig.SUCCEED_CODE, ActionCodeConfig.SUCCEED_MSG, rlsxtEntityDataEntity, null);
    }

    /**
     * 获取摄像头所属单位信息及摄像头信息
     *
     * @return datacontent中保存包含摄像头的单位信息，tag中保存相关摄像头信息
     */
    @RequestMapping("/SXT/DW")
    public ActionResult listSXTDW() {
        DataEntity rlsxtdwEntityDataEntity = rlsxtService.listAllSXTDW();
        DataEntity rlsxtDataEntity = rlsxtService.listAllXST("");
        return new ActionResult(ActionCodeConfig.SUCCEED_CODE, ActionCodeConfig.SUCCEED_MSG, rlsxtdwEntityDataEntity, rlsxtDataEntity);
    }

    /**
     * 添加摄像头
     *
     * @param sxtEntity 摄像头相关参数
     * @return 操作结果 1 表示添加成功
     */
    @RequestMapping(value = "/SXT", consumes = "application/json", method = {RequestMethod.POST})
    public ActionResult addSXT(@RequestBody SxtEntity sxtEntity) {
        return new ActionResult(ActionCodeConfig.SUCCEED_CODE, ActionCodeConfig.SUCCEED_MSG, rlsxtService.addSXT(sxtEntity), null);
    }

    /**
     * @param sxtid 需删除的摄像头id
     * @return 操作结果不为 0 表示操作成功
     */
    @RequestMapping(value = "/SXT", method = RequestMethod.DELETE)
    public ActionResult delSXT(@RequestParam("sxtid") String sxtid) {
        return new ActionResult(ActionCodeConfig.SUCCEED_CODE, ActionCodeConfig.SUCCEED_MSG, rlsxtService.removeCamera(sxtid), null);
    }

    /**
     * 启停摄像头
     * @param jsonObject
     * @return
     */
    @RequestMapping(value = "/SXT", consumes = "application/json", method = {RequestMethod.PUT})
    public ActionResult enableSXT(@RequestBody JSONObject jsonObject) {
        String sxtid=jsonObject.getString("sxtid");
        String enable=jsonObject.getString("enable");
        return new ActionResult(ActionCodeConfig.SUCCEED_CODE, ActionCodeConfig.SUCCEED_MSG, rlsxtService.enableCamera(sxtid,enable), null);
    }


    /**
     * 报警标注
     *
     * @param jsonObject {xh:报警记录序号,bzsfxt:是否相同编码,bzbz:备注信息}
     * @return datacontent中记录操作结果
     */
    @RequestMapping(value = "/LR/Rlgjbz", method = RequestMethod.POST, consumes = {"application/json"})
    public ActionResult updateRlgjBZ(@RequestBody String jsonObject) {
        JSONObject params = JSONObject.parseObject(jsonObject);
        return new ActionResult(ActionCodeConfig.SUCCEED_CODE, ActionCodeConfig.SUCCEED_MSG, rlgjService.updateRlgjBZ(params.getLong("xh"), params.getString("bzsfxt"), params.getString("bzbz")), null
        );
    }

    /**
     * 获取人脸库信息
     *
     * @return 返回人脸库信息
     */
    @RequestMapping("/RLK")
    public ActionResult listRLK() {
        return new ActionResult(ActionCodeConfig.SUCCEED_CODE, ActionCodeConfig.SUCCEED_MSG, rlkService.listAllRlk(), null);
    }

    /**
     * 新增人脸库
     *
     * @param jsonObject {rlkmc:人脸库名称,bz:备注信息}
     * @return 返回值不为 0 操作成功
     */
    @RequestMapping(value = "/RLK", method = RequestMethod.POST, consumes = {"application/json"})
    public ActionResult addRLK(@RequestBody String jsonObject) {
        JSONObject params = JSONObject.parseObject(jsonObject);
        RlkEntity rlkEntity = new RlkEntity();
        rlkEntity.setRLKMC(params.getString("rlkmc"));
        if (params.containsKey("bz")) {
            rlkEntity.setBZ(params.getString("bz"));
        }
        return new ActionResult(ActionCodeConfig.SUCCEED_CODE, ActionCodeConfig.SUCCEED_MSG, rlkService.addFaceLib(rlkEntity), null);
    }

    /**
     * 删除人脸库
     *
     * @param rlkid 需删除的人脸库id
     * @return 返回值不为 0 成功
     */
    @RequestMapping(value = "/RLK", method = RequestMethod.DELETE)
    public ActionResult delRLK(@RequestParam("rlkid") String rlkid) {
        return new ActionResult(ActionCodeConfig.SUCCEED_CODE, ActionCodeConfig.SUCCEED_MSG, rlkService.delFaceLib(rlkid), null);
    }

    /**
     * 查询人脸信息
     *
     * @param params
     * @return 返回人脸库人脸记录查询结果
     */
    @RequestMapping(value = "/RL", consumes = "application/json", method = {RequestMethod.POST})
    public ActionResult listRL(@RequestBody String params) {
        JSONArray jsonArray = JSONArray.parseArray(params);
        ActionResult result = new ActionResult(ActionCodeConfig.SUCCEED_CODE, ActionCodeConfig.SUCCEED_MSG, rlService.listRlDetailByRlidAndSupply(jsonArray), null
        );
        return result;
    }

    /**
     * 修改人脸信息
     *
     * @param params 参数中rlid存在
     * @return
     */
    @RequestMapping(value = "/RL", consumes = "application/json", method = {RequestMethod.PUT})
    public ActionResult altPersonalInfo(@RequestBody String params) {
        JSONObject jsonObject = JSONObject.parseObject(params);
        ActionResult result;
        if (jsonObject.containsKey("rlid")) {
            result = new ActionResult(ActionCodeConfig.SUCCEED_CODE, ActionCodeConfig.SUCCEED_MSG, rlService.altPersonalInfo(jsonObject), null
            );
        } else {
            result = new ActionResult(ActionCodeConfig.PARAMS_ERROR_CODE, ActionCodeConfig.PARAMS_ERROR_MSG, "", "rlid错误"
            );
        }
        return result;
    }

    /**
     * 删除人员
     *
     * @param rlid
     * @return
     */
    @RequestMapping(value = "/RL", method = {RequestMethod.DELETE})
    public ActionResult delPersonal(@RequestParam("rlid") String rlid) {
        ActionResult result = new ActionResult(ActionCodeConfig.SUCCEED_CODE, ActionCodeConfig.SUCCEED_MSG, rlService.delPersonal(rlid), null
        );
        return result;
    }

    /**
     * 单条人像数据添加
     *
     * @param rlEntity
     * @return
     */
    @RequestMapping(value = "/RL/face", consumes = "application/json", method = RequestMethod.POST)
    public ActionResult addFace(@RequestBody RlEntity rlEntity) throws Exception {
        int result = 0;
        String filename = rlEntity.getXM();
        String[] filenameStruct = FileUtils.dealPicFileName(filename);
        PersonIDEntity personIDEntity = PersonIDUntils.getPersonInfo(filenameStruct[0], filenameStruct[1]);
        rlEntity.setSFZH(personIDEntity.getId());
        rlEntity.setXM(personIDEntity.getName());
        rlEntity.setXB("男".equals(personIDEntity.getGender()) ? 1 : 2);
        rlEntity.setCSNF(personIDEntity.getBirthDay());
        rlEntity.setRLSF(personIDEntity.getProvince());
        rlEntity.setRLCS(personIDEntity.getCity());
        rlEntity.setTJSJ(CommUtils.getCurrentDate());
        rlEntity.setXGSJ(CommUtils.getCurrentDate());
        rlEntity.setRKSJ(CommUtils.getCurrentDate());
        result = rlService.addRlData(rlEntity);
        return new ActionResult(ActionCodeConfig.SUCCEED_CODE, ActionCodeConfig.SUCCEED_MSG, result, null);
    }

    /**
     *模糊查询人脸信息
     * @param rlkid 人脸库id
     * @param status 人脸库标志位
     * @param key 模糊查询条件
     * @param page 页码
     * @param size 分页大小
     * @return
     */
    @RequestMapping(value = "/RL/faces", method = RequestMethod.GET)
    public ActionResult listRlDetail(
                 @RequestParam(name = "rlkid") String rlkid,
                 @RequestParam(name = "status", defaultValue = "1") String status,
                 @RequestParam(name = "key", defaultValue = "") String key,
                 @RequestParam(name = "page", defaultValue = "1") int page,
                 @RequestParam(name = "size", defaultValue = "10") int size) {
        return new ActionResult(ActionCodeConfig.SUCCEED_CODE, ActionCodeConfig.SUCCEED_MSG, rlService.listRlShowDetail(rlkid,status,key,page,size), null);

    }


    /**
     *查询人脸库人脸
     * @param rlkid 人脸库id
     * @param status 人脸库标志位
     * @param page 页码
     * @param size 分页大小
     * @return
     */
    @RequestMapping(value = "/RL/Getfaces", method = RequestMethod.GET)
    public ActionResult listQueryRls(
            @RequestParam(name = "rlkid") String rlkid,
            @RequestParam(name = "status", defaultValue = "1") String status,
            @RequestParam(name = "page", defaultValue = "1") int page,
            @RequestParam(name = "size", defaultValue = "10") int size) {
        return new ActionResult(ActionCodeConfig.SUCCEED_CODE, ActionCodeConfig.SUCCEED_MSG, rlService.listQueryRlShowDetail(rlkid,status,page,size), null);

    }

    /**
     * 人像记录批量导入 zip格式
     * @param multipartFile
     * @param repositoryid
     * @return
     * @throws IOException
     */
    @RequestMapping(value = "/RL/faces",consumes = "multipart/form-data",method = RequestMethod.POST)
    public ActionResult impFaces(@RequestParam("facezip")MultipartFile multipartFile,@RequestParam("repositoryid")String repositoryid) throws IOException {
        Files.copy(multipartFile.getInputStream(), Paths.get(repositoryRoot,String.format("%s-%s-%s",repositoryid,CommUtils.dateToStr(new Date(),"yyyyMMddHHmmss"),multipartFile.getOriginalFilename())), StandardCopyOption.REPLACE_EXISTING);
        return new ActionResult(ActionCodeConfig.SUCCEED_CODE, ActionCodeConfig.SUCCEED_MSG, "1", null);
    }

    /**
     * 算法评分
     *
     * @param params
     * @return 返回评分操作结果
     */
    @RequestMapping(value = "/SF/SFPJ", consumes = "application/json", method = {RequestMethod.POST})
    public ActionResult addSfpj(@RequestBody String params) {
        JSONObject jsonObject = JSONObject.parseObject(params);
        return new ActionResult(
                ActionCodeConfig.SUCCEED_CODE,
                ActionCodeConfig.SUCCEED_MSG,
                sfpjService.insertSfpj(
                        jsonObject.getInteger("pjflag"),
                        jsonObject.getString("cxdm"),
                        jsonObject.getString("sfdm"),
                        jsonObject.getDouble("fz"),
                        jsonObject.getString("bz")
                ),
                null);
    }

    /**
     * 算法评分统计
     *
     * @param pjflag 评价标记 0-评价手动评价结果 1-统计自动评价结果
     * @param sfdm   算法代码编号 默认为空
     * @return 返回算法评分统计结果
     */
    @RequestMapping("/SF/SFPJ/Avg")
    public ActionResult listSfAvg(@RequestParam(value = "pjflag", defaultValue = "0") int pjflag, @RequestParam(value = "sfdm", defaultValue = "") String sfdm) {
        return new ActionResult(ActionCodeConfig.SUCCEED_CODE, ActionCodeConfig.SUCCEED_MSG, sfpjService.getSfAvg(pjflag, sfdm), null);
    }

    /**
     * 通用数据导出
     *
     * @param params
     * @return
     * @throws IOException
     */
    @RequestMapping(value = "/comm/exp", consumes = "application/json", method = RequestMethod.POST)
    public ActionResult expByArray(@RequestBody String params) throws IOException {
        JSONArray jsonArray = (JSONArray) JSON.parse(params, Feature.OrderedField);
        return new ActionResult(
                ActionCodeConfig.SUCCEED_CODE,
                ActionCodeConfig.SUCCEED_MSG,
                fileService.expDataWithPic(jsonArray),
                null
        );
    }

    /**
     * 创建布控任务
     *
     * @param bkrwEntities
     * @return
     * @throws IOException
     */
    @RequestMapping(value = "/BK/BKRW", consumes = "application/json", method = RequestMethod.POST)
    public ActionResult createMonitorTask(@RequestBody BkrwEntity[] bkrwEntities) throws IOException {

        int count = 0;
        List<BkrwEntity> bkrwEntityList = Arrays.asList(bkrwEntities);
        for (Iterator iterator = bkrwEntityList.iterator(); iterator.hasNext(); ) {
            try {
                count = count + rlsxtService.addMonitorByCamera((BkrwEntity) iterator.next());
            } catch (Exception e) {
                e.printStackTrace();
                continue;
            }
        }
        return new ActionResult(ActionCodeConfig.SUCCEED_CODE, ActionCodeConfig.SUCCEED_MSG, count, null);
    }


    /**
     * 增加布控任务
     * @param bkrwEntity
     * @return
     * @throws IOException
     */
    @RequestMapping(value = "/BK/RLBKRW", consumes = "application/json", method = RequestMethod.POST)
    public ActionResult createRlMonitorTask(@RequestBody BkrwEntity bkrwEntity) throws IOException {
        int result=0;
        try {
            result=rlbkrwService.addMonitorReposity(bkrwEntity);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return new ActionResult(ActionCodeConfig.SUCCEED_CODE, ActionCodeConfig.SUCCEED_MSG, result,null);
    }

    @RequestMapping(value = "/BK/RLBKRW", consumes = "application/json", method = RequestMethod.DELETE)
    public ActionResult delMonitorTask(@RequestParam String bkid) throws IOException {
        return new ActionResult(ActionCodeConfig.SUCCEED_CODE, ActionCodeConfig.SUCCEED_MSG, rlbkrwService.delMonitorReposity(bkid), null);
    }

    /**
     * 解析身份证信息
     *
     * @param params
     * @return
     */
    @RequestMapping(value = "/comm/personinfo", consumes = "application/json", method = RequestMethod.POST)
    public ActionResult getPersonInfo(@RequestBody String params) {
        JSONObject jsonObject = JSONObject.parseObject(params);
        String fileName = jsonObject.getString("filename");
        String[] personInfo = FileUtils.dealPicFileName(fileName);
        return new ActionResult(ActionCodeConfig.SUCCEED_CODE, ActionCodeConfig.SUCCEED_MSG, PersonIDUntils.getPersonInfo(personInfo[0], personInfo[1]), null);
    }


}
