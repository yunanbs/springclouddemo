package com.sailing.facetec.comm;

import com.sailing.facetec.config.ActionCodeConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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

    private Logger logger = LoggerFactory.getLogger(GlobalExceptionHandler.class);
    @ExceptionHandler(value = Exception.class)
    public ActionResult customerErrorHandler(HttpServletRequest req,Exception e){
        logger.error(e.getMessage());
        return new ActionResult(ActionCodeConfig.SERVER_ERROR_CODE,String.format(ActionCodeConfig.SERVER_ERROR_MSG,e.getMessage()),null,req.getServletPath());
    }
}
