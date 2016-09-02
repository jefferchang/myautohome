package com.autohome.resadmin.common.jsonp;

import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.AbstractJsonpResponseBodyAdvice;

/**
 * Created by zhouxiaoming on 2015/9/17.
 */
//@ControllerAdvice
//public class JsonpAdvice extends FastJsonJsonpResponseBodyAdvice {
//    public JsonpAdvice() {
//        super("callback");
//    }
//}

@ControllerAdvice
public class JsonpAdvice extends AbstractJsonpResponseBodyAdvice {
    public JsonpAdvice() {
        super("callback");
    }
}