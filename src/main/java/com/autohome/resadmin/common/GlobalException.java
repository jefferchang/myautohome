package com.autohome.resadmin.common;

import com.autohome.resadmin.domain.ResResponse;
import com.google.common.base.Throwables;
import org.apache.log4j.Logger;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

import javax.servlet.http.HttpServletRequest;
import java.util.Enumeration;

/**
 * Created by zhouxiaoming on 2015/9/2
 * 全局异常处理类
 */
@ControllerAdvice
public class GlobalException {

    private final Logger resAdminGlobalErrorLogger = Logger.getLogger("resAdminErrorLogger");

    @ExceptionHandler(Exception.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ResponseBody
    public ResResponse operateExp(HttpServletRequest req, Exception ex) {
        resAdminGlobalErrorLogger.fatal(getFullErrorString(req,ex));
        return new ResResponse(1,ex.getMessage(),null);
    }

    public static String getFullErrorString(HttpServletRequest req,Exception ex){
        StringBuilder builder = new StringBuilder();
        Enumeration headerNames = req.getHeaderNames();
        while (headerNames.hasMoreElements()) {
            String key = (String) headerNames.nextElement();
            String value = req.getHeader(key);
            builder.append(key);
            builder.append(":");
            builder.append(value);
            builder.append(System.lineSeparator());
        }
        builder.append(Throwables.getStackTraceAsString(ex));
        return  builder.toString();
    }
}