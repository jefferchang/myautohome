package com.autohome.resadmin.dao;

import com.google.common.base.Optional;
import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.util.concurrent.TimeUnit;

/**
 * Created by xw.luan on 2016/7/25.
 */
@Repository
public class BrandsDAOImpl implements BrandsDAO {

    @Autowired
    private DataSource reproductionDataSource;
    private JdbcTemplate jdbcTemplate;

    @Override
    public Integer findBrandsBySeriesid(int seriesid) {
        String sql = " select M from [Brands] WITH(nolock) where id = ? ";
        jdbcTemplate = new JdbcTemplate(reproductionDataSource);
        Integer manuFactoryId= Integer.valueOf(0);
        try {
            manuFactoryId= jdbcTemplate.queryForObject(sql,Integer.class, new Object[] {seriesid});
        } catch (EmptyResultDataAccessException e) {
            return null;
        }
        return manuFactoryId;
    }
}
