package com.sailing.facetec.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.parser.Feature;
import com.sailing.facetec.comm.ActionResult;
import com.sailing.facetec.comm.DataEntity;
import com.sailing.facetec.config.ActionCodeConfig;
import com.sailing.facetec.entity.BkrwEntity;
import com.sailing.facetec.entity.RlgjDetailEntity;
import com.sailing.facetec.entity.SxtDetailEntity;
import com.sailing.facetec.entity.SxtEntity;
import com.sailing.facetec.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

/**
 * Created by yunan on 2017/4/26.
 */
@RestController
@CrossOrigin(origins = "*", methods = {RequestMethod.POST, RequestMethod.OPTIONS, RequestMethod.GET})
@RefreshScope
public class DatabaseController {


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

    // 获取redis数据库中抓拍数据的缓存key
    @Value("${redis-keys.capture-data}")
    private String captureData;
    // 获取redis数据库中报警数据的缓存key
    @Value("${redis-keys.alert-data}")
    private String alertData;

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
     * 路人抓拍数据查询接口
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
     * 报警数据查询
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
    public ActionResult listSXT() {
        DataEntity<SxtDetailEntity> rlsxtEntityDataEntity = rlsxtService.listAllXST();
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
        DataEntity rlsxtDataEntity = rlsxtService.listAllXST();
        return new ActionResult(ActionCodeConfig.SUCCEED_CODE, ActionCodeConfig.SUCCEED_MSG, rlsxtdwEntityDataEntity, rlsxtDataEntity);
    }

    /**
     * 添加摄像头
     *
     * @param sxtEntity
     * @return
     */
    @RequestMapping(value = "/SXT", consumes = "application/json", method = {RequestMethod.POST})
    public ActionResult addSXT(@RequestBody SxtEntity sxtEntity) {
        return new ActionResult(ActionCodeConfig.SUCCEED_CODE, ActionCodeConfig.SUCCEED_MSG, rlsxtService.addSXT(sxtEntity), null);
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
        return new ActionResult(
                ActionCodeConfig.SUCCEED_CODE,
                ActionCodeConfig.SUCCEED_MSG,
                rlgjService.updateRlgjBZ(params.getLong("xh"), params.getString("bzsfxt"), params.getString("bzbz")),
                null
        );
    }

    /**
     * 获取人脸库信息
     *
     * @return 返回人脸库信息
     */
    @RequestMapping("/RLK")
    public ActionResult listRLK() {
        return new ActionResult(
                ActionCodeConfig.SUCCEED_CODE,
                ActionCodeConfig.SUCCEED_MSG,
                rlkService.listAllRlk(),
                null);
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
        ActionResult result = new ActionResult(
                ActionCodeConfig.SUCCEED_CODE,
                ActionCodeConfig.SUCCEED_MSG,
                rlService.listRlDetail(jsonArray),
                null
        );

        return result;
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

        // JSONArray jsonArray = JSONArray.parseArray(params);
        // DataEntity result = new DataEntity();

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
        return new ActionResult(
                ActionCodeConfig.SUCCEED_CODE,
                ActionCodeConfig.SUCCEED_MSG,
                sfpjService.getSfAvg(pjflag, sfdm),
                null
        );
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

    @RequestMapping(value = "/BK/BKRW", consumes = "application/json", method = RequestMethod.POST)
    public ActionResult expByArray(@RequestBody BkrwEntity[] bkrwEntities) throws IOException {

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


}
