package com.autohome.resadmin.dao;

import com.autohome.resadmin.domain.CompeteSeries;
import com.google.common.base.Optional;

import java.util.List;

/**
 * Created by xw.luan on 2016/7/25.
 */
public interface CompeteSeriesDAO {


    /**
     *  车系ID获取竞品 字符串 cache
     * @param seriesid
     * @return
     */
    public Optional<String> findCompeteSeriesCstrBySeriesidCache(int seriesid);
    /**
     *  车系ID获取竞品 字符串
     * @param seriesid
     * @return
     */
    public String findCompeteSeriesCstrBySeriesid(int seriesid);




}
