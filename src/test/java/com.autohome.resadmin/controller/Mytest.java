package com.autohome.resadmin.controller;


import com.autohome.resadmin.domain.ProductActivityInfo;
import com.fasterxml.jackson.databind.util.JSONPObject;
import com.google.common.base.MoreObjects;
import com.google.common.base.Splitter;
import com.google.common.base.Strings;
import com.google.common.collect.Lists;
import com.google.common.collect.Range;
import com.google.common.primitives.Ints;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Created by Administrator on 2016/7/22.
 */
public class Mytest {

    public static  void main(String ars[]){

        new JSONPObject("","");
        ArrayList listrange = Lists.newArrayList(4,3,10,30,20);
        Range<Integer> range = Range.encloseAll(listrange);
        System.out.println(range.lowerEndpoint().intValue());
        System.out.println(range.upperEndpoint().intValue());


        String sasdf = Strings.repeat("Hello " ,3);
        System.out.println(sasdf);



        int test1 = 1;
        Integer test2 = new Integer(1);
        System.out.println("result:="+(new Integer(test1).intValue()==test2));








        String s =null;
        String ss = com.google.common.base.Optional.fromNullable(s).or("12");
        String a=MoreObjects.firstNonNull(null,"");
        String a1=MoreObjects.firstNonNull(null,"1");
        String a2=MoreObjects.firstNonNull("2","1");
        String a3=MoreObjects.firstNonNull("2",null);
        int a5 =  Ints.tryParse("10");
        System.out.println(ss);

        List<TestVo> list =new ArrayList<TestVo>();
        TestVo vo1= new TestVo();
        vo1.setId("3");
        list.add(vo1);
        TestVo vo2= new TestVo();
        vo2.setId("2");
        list.add(vo2);
        TestVo vo3= new TestVo();
        vo3.setId("1");
        list.add(vo3);



        List<TestBVo> list1 = new ArrayList<>();
        TestBVo vob1 = new TestBVo();
        vob1.setId("11111");
        list1.add(vob1);
        TestBVo vob2 = new TestBVo();
        vob2.setId("11");
        list1.add(vob2);
        TestBVo vob3 = new TestBVo();
        vob3.setId("111");
        list1.add(vob3);
        TestBVo vob4 = new TestBVo();
        vob4.setId("1111");
        list1.add(vob4);
        TestBVo vob5 = new TestBVo();
        vob5.setId("1");
        list1.add(vob5);

        for(int i=0;i<list.size();i++){
         String id = list.get(i).getId();
            Optional<TestBVo> testBVo= list1.stream().findFirst().filter((tBVo)-> tBVo.getId().equals(id));
            System.out.println(testBVo.isPresent());
            break;
        }























        //TestVo
        List<String> myList= list.stream().map(TestVo::getId).collect(Collectors.toList());
        Map map= list.stream().collect(Collectors.toMap(TestVo::getId, TestVo::getId));
        Splitter.on(',').trimResults().omitEmptyStrings().splitToList("0");
        List<String> lists = Splitter.on(',').trimResults().omitEmptyStrings().splitToList("1,2,3");



       List<String> l= lists.stream().filter((w) -> "1".equals(w)).collect(Collectors.toList());


       String sql = "265,2902,0,0,0,0,0,0,0,0";
       sql= sql.replace(",0","");
        System.out.println("sql:"+sql);



































    }
}
