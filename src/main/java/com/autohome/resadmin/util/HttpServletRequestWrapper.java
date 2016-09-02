package com.autohome.resadmin.util;

import javax.servlet.http.HttpServletRequest;
import java.util.*;

public class HttpServletRequestWrapper extends javax.servlet.http.HttpServletRequestWrapper {
    public HttpServletRequestWrapper(HttpServletRequest request) {
        super(request);
    }
    @Override
    public String getHeader(String name) {
        if("ACCEPT".equals(name.toUpperCase())){
            return "*/*";
        }else{
          return  super.getHeader(name);
        }
    }
}
