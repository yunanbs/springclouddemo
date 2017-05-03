package com.sailing.facetec.controller;

import com.sailing.facetec.comm.ActionResult;
import com.sailing.facetec.comm.ActionCode;
import com.sailing.facetec.comm.DataEntity;
import com.sailing.facetec.entity.RlgjDetailEntity;
import com.sailing.facetec.entity.RllrDetailEntity;
import com.sailing.facetec.entity.RlsxtEntity;
import com.sailing.facetec.service.RlgjService;
import com.sailing.facetec.service.RllrService;
import com.sailing.facetec.service.RlsxtService;
import com.sun.org.apache.xerces.internal.impl.xs.XSDDescription;
import net.sf.json.JSONObject;
import org.apache.ibatis.annotations.Param;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Created by yunan on 2017/4/26.
 */
@RestController
@CrossOrigin(origins = "*", methods = {RequestMethod.POST, RequestMethod.OPTIONS, RequestMethod.GET})
@RequestMapping("/face")
public class DatabaseController {


    @Autowired
    private RllrService rllrService;

    @Autowired
    private RlgjService rlgjService;

    @Autowired
    private RlsxtService rlsxtService;

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
        return new ActionResult(ActionCode.SUCCEED_CODE, ActionCode.SUCCEED_MSG, result, null);
    }

    @RequestMapping("/LR/Alerts")
    public ActionResult listAlertDetails(
            @RequestParam(name = "beginTime", defaultValue = "") String beginTime,
            @RequestParam(name = "endTime", defaultValue = "") String endTime,
            @RequestParam(name = "orderby", defaultValue = "a.XH") String orderBy,
            @RequestParam(name = "page", defaultValue = "1") int page,
            @RequestParam(name = "size", defaultValue = "10") int size,
            @RequestParam(name = "xsd", defaultValue = "0") double xsd,
            @RequestParam(name = "bz", defaultValue = "") String bz
    ) {

        DataEntity<RlgjDetailEntity> rlgjDetailEntityDataEntity = rlgjService.listRlgjDetail(beginTime, endTime, orderBy, page, size, xsd, bz);
        return new ActionResult(ActionCode.SUCCEED_CODE, ActionCode.SUCCEED_MSG, rlgjDetailEntityDataEntity, null);
    }

    @RequestMapping("/SXT")
    public ActionResult listSXT() {
        DataEntity<RlsxtEntity> rlsxtEntityDataEntity = rlsxtService.listAll();
        return new ActionResult(ActionCode.SUCCEED_CODE, ActionCode.SUCCEED_MSG, rlsxtEntityDataEntity, null);
    }

    @RequestMapping(value = "/LR/Rlgjbz",method = RequestMethod.POST,consumes = {"application/json"})
    public ActionResult updateRlgjBZ(@RequestBody String jsonObject){
        JSONObject params = JSONObject.fromObject(jsonObject);
        return  new ActionResult(
                ActionCode.SUCCEED_CODE,
                ActionCode.SUCCEED_MSG,
                rlgjService.updateRlgjBZ(params.getLong("xh"),params.getString("bzsfxt"),params.getString("bzbz")),
                null
        );
    }
}
