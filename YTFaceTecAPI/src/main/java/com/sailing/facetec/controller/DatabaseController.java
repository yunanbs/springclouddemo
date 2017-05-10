package com.sailing.facetec.controller;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.sailing.facetec.comm.ActionResult;
import com.sailing.facetec.comm.DataEntity;
import com.sailing.facetec.config.ActionCodeConfig;
import com.sailing.facetec.entity.RlgjDetailEntity;
import com.sailing.facetec.entity.SxtDetailEntity;
import com.sailing.facetec.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.web.bind.annotation.*;

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

    @Value("${redis-keys.capture-data}")
    private String captureData;
    @Value("${redis-keys.alert-data}")
    private String alertData;

    @RequestMapping("/")
    public String test() {
        return "hello sailling";
    }

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

    @RequestMapping("/LR/Captures/Real")
    public ActionResult listRealCaptureDetails(@RequestParam(name = "lrkids", defaultValue = "") String lrkids) {
        String result = rllrService.listRllrDetailReal(lrkids);
        return new ActionResult(ActionCodeConfig.SUCCEED_CODE, ActionCodeConfig.SUCCEED_MSG, JSONObject.parseObject(result), null);
    }

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

    @RequestMapping("/LR/Alerts/Real")
    public ActionResult listRealAlertDetails() {
        String jsonStr = redisService.getVal(alertData);
        return new ActionResult(ActionCodeConfig.SUCCEED_CODE, ActionCodeConfig.SUCCEED_MSG, JSONObject.parseObject(jsonStr), null);
    }

    @RequestMapping("/SXT")
    public ActionResult listSXT() {
        DataEntity<SxtDetailEntity> rlsxtEntityDataEntity = rlsxtService.listAllXST();
        return new ActionResult(ActionCodeConfig.SUCCEED_CODE, ActionCodeConfig.SUCCEED_MSG, rlsxtEntityDataEntity, null);
    }

    @RequestMapping("/SXT/DW")
    public ActionResult listSXTDW() {
        DataEntity rlsxtdwEntityDataEntity = rlsxtService.listAllSXTDW();
        DataEntity rlsxtDataEntity = rlsxtService.listAllXST();
        return new ActionResult(ActionCodeConfig.SUCCEED_CODE, ActionCodeConfig.SUCCEED_MSG, rlsxtdwEntityDataEntity, rlsxtDataEntity);
    }

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

    @RequestMapping("/RLK")
    public ActionResult listRLK() {
        return new ActionResult(
                ActionCodeConfig.SUCCEED_CODE,
                ActionCodeConfig.SUCCEED_MSG,
                rlkService.listAllRlk(),
                null);
    }

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

    @RequestMapping(value = "/SF/SFPJ/Avg")
    public ActionResult listSfAvg(@RequestParam(value = "pjflag", defaultValue = "0") int pjflag, @RequestParam(value = "sfdm", defaultValue = "") String sfdm) {
        return new ActionResult(
                ActionCodeConfig.SUCCEED_CODE,
                ActionCodeConfig.SUCCEED_MSG,
                sfpjService.getSfAvg(pjflag, sfdm),
                null
        );
    }
}
