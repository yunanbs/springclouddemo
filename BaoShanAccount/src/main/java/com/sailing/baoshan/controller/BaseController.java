package com.sailing.baoshan.controller;

import com.alibaba.fastjson.JSONObject;
import com.sailing.baoshan.comm.ActionResult;
import com.sailing.baoshan.config.ActionCodeConfig;
import com.sailing.baoshan.entity.AccountEntity;
import com.sailing.baoshan.service.ExportService;
import com.sailing.baoshan.service.IllegalAccountService;
import com.sailing.baoshan.utils.CommUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.Date;
import java.util.List;

/**
 * Created by yunan on 2017/6/20.
 */
@RestController
@CrossOrigin(origins = "*", methods = {RequestMethod.POST, RequestMethod.OPTIONS, RequestMethod.GET})
public class BaseController {

    @Autowired
    private ActionCodeConfig actionCodeConfig;

    @Autowired
    private IllegalAccountService illegalAccountService;
    @Autowired
    private ExportService exportService;

    @RequestMapping(value = "/account/illegalAccount", method = RequestMethod.GET)
    public ActionResult getIllegalAccount(
            @RequestParam(value = "begintime", defaultValue = "") String beginTime,
            @RequestParam(value = "endtime", defaultValue = "") String endTime,
            @RequestParam(value = "top", defaultValue = "10") String top,
            @RequestParam(value = "type", defaultValue = "0") String type
    ) {
        // 默认设置为当天开始时间
        beginTime = CommUtils.isNullObject(beginTime) ? CommUtils.dateToStr(new Date(), "yyyy-MM-dd 00:00:00") : beginTime;
        // 默认设置为当天结束时间
        endTime = CommUtils.isNullObject(endTime) ? CommUtils.dateToStr(new Date(), "yyyy-MM-dd 23:59:59") : endTime;
        // 获取统计结果
        List<AccountEntity> results = illegalAccountService.getIllegalAccountByTimeAndType(beginTime, endTime, top, type);
        return new ActionResult(actionCodeConfig.getSUCCEED_CODE(), actionCodeConfig.getSUCCEED_MSG(), results, null);
    }


    @RequestMapping(value = "/exp/illegalAccount", method = RequestMethod.POST)
    public ActionResult expIllegalAccount(@RequestBody String params) throws IOException {
        JSONObject reqParam = JSONObject.parseObject(params);
        return new ActionResult(actionCodeConfig.getSUCCEED_CODE(), actionCodeConfig.getSUCCEED_MSG(), exportService.expIllegalAccountData(reqParam.getJSONObject("data"), reqParam.getString("fileName")), null);
    }
}
