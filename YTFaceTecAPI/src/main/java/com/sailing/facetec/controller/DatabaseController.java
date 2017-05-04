package com.sailing.facetec.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.sailing.facetec.comm.ActionResult;
import com.sailing.facetec.config.ActionCodeConfig;
import com.sailing.facetec.comm.DataEntity;
import com.sailing.facetec.config.SupplyConfig;
import com.sailing.facetec.entity.RlgjDetailEntity;
import com.sailing.facetec.entity.RlsxtEntity;
import com.sailing.facetec.service.*;
import com.sailing.facetec.util.CommUtils;
import net.sf.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Iterator;
import java.util.Map;
import java.util.Set;

/**
 * Created by yunan on 2017/4/26.
 */
@RestController
@CrossOrigin(origins = "*", methods = {RequestMethod.POST, RequestMethod.OPTIONS, RequestMethod.GET})
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

    @RequestMapping("/")
    public String test() {
        return "hello sailling";
    }

    @RequestMapping("/LR/Captures")
    public ActionResult listRealDetails(
            @RequestParam(name = "beginTime", defaultValue = "") String beginTime,
            @RequestParam(name = "endTime", defaultValue = "") String endTime,
            @RequestParam(name = "orderby", defaultValue = "a.XH") String orderBy,
            @RequestParam(name = "page", defaultValue = "1") int page,
            @RequestParam(name = "size", defaultValue = "10") int size,
            @RequestParam(name = "cameras", defaultValue = "") String cameras,
            @RequestParam(name = "xb", defaultValue = "") String xb
    ) {

        DataEntity result = rllrService.listRllrDetail(beginTime, endTime, orderBy, page, size, xb, cameras);
        return new ActionResult(ActionCodeConfig.SUCCEED_CODE, ActionCodeConfig.SUCCEED_MSG, result, null);
    }

    @RequestMapping("/LR/Alerts")
    public ActionResult listAlertDetails(
            @RequestParam(name = "beginTime", defaultValue = "") String beginTime,
            @RequestParam(name = "endTime", defaultValue = "") String endTime,
            @RequestParam(name = "orderby", defaultValue = "a.XH") String orderBy,
            @RequestParam(name = "page", defaultValue = "1") int page,
            @RequestParam(name = "size", defaultValue = "10") int size,
            @RequestParam(name = "xsd", defaultValue = "0") double xsd,
            @RequestParam(name = "bz", defaultValue = "") String bz,
            @RequestParam(name = "rlid", defaultValue = "") String rlid
    ) {

        DataEntity<RlgjDetailEntity> rlgjDetailEntityDataEntity = rlgjService.listRlgjDetail(beginTime, endTime, orderBy, page, size, xsd, bz,rlid);
        return new ActionResult(ActionCodeConfig.SUCCEED_CODE, ActionCodeConfig.SUCCEED_MSG, rlgjDetailEntityDataEntity, null);
    }

    @RequestMapping("/SXT")
    public ActionResult listSXT() {
        DataEntity<RlsxtEntity> rlsxtEntityDataEntity = rlsxtService.listAll();
        return new ActionResult(ActionCodeConfig.SUCCEED_CODE, ActionCodeConfig.SUCCEED_MSG, rlsxtEntityDataEntity, null);
    }

    @RequestMapping(value = "/LR/Rlgjbz", method = RequestMethod.POST, consumes = {"application/json"})
    public ActionResult updateRlgjBZ(@RequestBody String jsonObject) {
        JSONObject params = JSONObject.fromObject(jsonObject);
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
        ActionResult result = null;
        if (CommUtils.isNullObject(jsonArray)) {
            result = new ActionResult(
                    ActionCodeConfig.PARAMS_ERROR_CODE,
                    ActionCodeConfig.getParamsErrorMsg(),
                    null,
                    null
            );
        } else {
            result = new ActionResult(
                    ActionCodeConfig.SUCCEED_CODE,
                    ActionCodeConfig.SUCCEED_MSG,
                    rlService.listRlDetail(jsonArray),
                    null
            );
        }
        return result;
    }
}
