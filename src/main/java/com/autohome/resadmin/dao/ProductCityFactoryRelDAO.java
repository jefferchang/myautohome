package com.autohome.resadmin.dao;

import com.google.common.base.Optional;

/**
 * Created by xw.luan on 2016/7/25.
 */
public interface ProductCityFactoryRelDAO {



    public Integer countProductCityFactory(String CFP);


    public  Optional<Integer> countProductCityFactoryCache(String CFP);





}
