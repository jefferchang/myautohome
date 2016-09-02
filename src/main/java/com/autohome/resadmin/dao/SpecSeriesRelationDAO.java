package com.autohome.resadmin.dao;


import com.autohome.resadmin.domain.SpecSeriesRelation;
import com.google.common.base.Optional;

/**
 * Created by xw.luan on 2016/7/27.
 */
public interface SpecSeriesRelationDAO {

    /**
     * 查询缓存
     * @param specId
     * @return
     */
    Optional<SpecSeriesRelation> findSpecSeriesRelationByCache(int specId);

    /**
     * 车型获取车型车系对应关系
     * @param specId
     * @return
     */
    public SpecSeriesRelation findSpecSeriesRelationBySpecId(int specId);

}
