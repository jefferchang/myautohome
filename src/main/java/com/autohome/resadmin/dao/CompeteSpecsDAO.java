package com.autohome.resadmin.dao;

import com.autohome.resadmin.domain.CompeteSpecs;
import com.google.common.base.Optional;

/**
 * Created by xw.luan on 2016/7/25.
 */
public interface CompeteSpecsDAO {



    /**
     * 车型ID获取竞品 字符串
     * @param specid
     * @return
     */
    public String findCompeteSpecsCstrBySeriesid(int specid);

    /**
     * 车型ID获取竞品 字符串Cache
     * @param specid
     * @return
     */
    public Optional<String> findCompeteSpecsCstrBySpecidCache(int specid);
}
