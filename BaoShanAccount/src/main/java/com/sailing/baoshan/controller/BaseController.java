package com.sailing.baoshan.controller;

import com.sailing.baoshan.comm.ActionResult;
import com.sailing.baoshan.config.ActionCodeConfig;
import com.sailing.baoshan.entity.AccountEntity;
import com.sailing.baoshan.service.IllegalAccountService;
import com.sailing.baoshan.utils.CommUtils;
import org.apache.ibatis.executor.ReuseExecutor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Date;
import java.util.List;

/**
 * Created by yunan on 2017/6/20.
 */
@RestController
public class BaseController {

    @Autowired
    private IllegalAccountService illegalAccountService;
    @RequestMapping(value = "/account/illegalAccount",method = RequestMethod.GET)
    public ActionResult getIllegalAccount(
            @RequestParam(value = "begintime",defaultValue = "") String beginTime,
            @RequestParam(value = "endtime",defaultValue = "") String endTime,
            @RequestParam(value = "top",defaultValue = "10") String top,
            @RequestParam(value = "type",defaultValue = "0") String type
    ){
        beginTime =CommUtils.isNullObject(beginTime)?CommUtils.dateToStr(new Date(),"yyyy-MM-dd 00:00:00"):beginTime;
        endTime =CommUtils.isNullObject(endTime)?CommUtils.dateToStr(new Date(),"yyyy-MM-dd 23:59:59"):endTime;
        List<AccountEntity> results = illegalAccountService.getIllegalAccountByTimeAndType(beginTime,endTime,top,type);
        return new ActionResult(ActionCodeConfig.SUCCEED_CODE,ActionCodeConfig.SUCCEED_MSG,results,null);
    }
}
