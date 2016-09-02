package com.autohome.resadmin.controller;

import com.google.common.base.Strings;

/**
 * Created by Administrator on 2016/8/23.
 */
public class Test {
    public static void main(String a[]){
        Test t = new Test();
        Test te = new Test();
        Thread t1 = new Thread(){
            @Override
            public void run() {
                t.countProductCityFactory(1,"1","1");
            }
        };
        Thread t2 = new Thread(){
            @Override
            public void run() {
                te.countProductCityFactory(2,"2","2");
            }
        };

        Thread t3 = new Thread(){
            @Override
            public void run() {
                te.countProductCityFactory(3,"3","3");
            }
        };

        Thread t4 = new Thread(){
            @Override
            public void run() {
                t.countProductCityFactory(4,"4","4");
            }
        };

        Thread t5 = new Thread(){
            @Override
            public void run() {
                te.countProductCityFactory(5,"5","5");
            }
        };

        Thread t6 = new Thread(){
            @Override
            public void run() {
                t.countProductCityFactory(6,"6","6");
                t.countProductCityFactory(7,"7","7");
                t.countProductCityFactory(8,"8","8");
            }
        };


        t1.start();
        t2.start();
        t3.start();
        t4.start();
        t5.start();
        t6.start();


    }

    public int countProductCityFactory(int cityId,String seriesIdStr,String productIndentity) {
        if(!Strings.isNullOrEmpty(seriesIdStr)){
            String s =seriesIdStr;
            for(int i=0;i<1000;i++){
                String CFP= cityId+","+s+","+productIndentity;
                System.out.println(CFP);
            }
            return 0;
        }
        return 0;
    }
}
