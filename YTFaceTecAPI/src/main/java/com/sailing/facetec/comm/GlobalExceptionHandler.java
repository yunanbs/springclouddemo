package com.sailing.facetec.comm;

import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;

/**
 * Created by yunan on 2017/5/3.
 */

@ControllerAdvice
@ResponseBody
public class GlobalExceptionHandler {

    @ExceptionHandler(value = Exception.class)
    public ActionResult customerErrorHandler(HttpServletRequest req,Exception e){
        return new ActionResult(ActionCode.SERVER_ERROR_CODE,String.format(ActionCode.SERVER_ERROR_MSG,e.getMessage()),null,req.getServletPath());
    }
}
