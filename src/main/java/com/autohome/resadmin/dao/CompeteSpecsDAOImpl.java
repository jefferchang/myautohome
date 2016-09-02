package com.autohome.resadmin.dao;


import com.google.common.base.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import javax.sql.DataSource;


/**
 * Created by xw.luan on 2016/7/25.
 */
@Repository
public class CompeteSpecsDAOImpl  extends BaseCache<Integer,Optional<String>> implements  CompeteSpecsDAO {

    @Autowired
    private DataSource reproductionDataSource;
    private JdbcTemplate jdbcTemplate;
    @Autowired
    public CompeteSpecsDAOImpl(Integer dbCachePeriod) {
        setExpireAfterWriteDuration(dbCachePeriod);//设置数据存在时长
    }

    @Override
    protected Optional<String>  fetchData(Integer key) {
        String series = findCompeteSpecsCstrBySeriesid(key);
        return Optional.fromNullable(series);
    }

    @Override
    public Optional<String> findCompeteSpecsCstrBySpecidCache(int specid){
        return  getValue(specid);
    }
    @Override
    public String findCompeteSpecsCstrBySeriesid(int specid) {
        String sql = "SELECT  CAST(ISNULL(c1, '') as varchar  ) +','+CAST(ISNULL(c2, '')  as varchar  ) +','" +
                                " +CAST(ISNULL(c3, '') as varchar  ) +','+CAST(ISNULL(c4, '') as varchar  ) +','" +
                                " +CAST(ISNULL(c5, '') as varchar  ) +','+CAST(ISNULL(c6, '') as varchar  ) +','" +
                                " +CAST(ISNULL(c7, '') as varchar  ) +','+CAST(ISNULL(c8, '') as varchar  ) +','" +
                                " +CAST(ISNULL(c9, '') as varchar  ) +','+CAST(ISNULL(c10, '') as varchar  )  as jp  FROM [dbo].[dxp_CompeteSpecs] WITH(nolock) where specId = ? ";
        jdbcTemplate = new JdbcTemplate(reproductionDataSource);
        String competeSpecs="";
        try {
            competeSpecs=  jdbcTemplate.queryForObject(sql,String.class, new Object[] {specid});
            competeSpecs=  competeSpecs.replace(",0","");
        } catch (EmptyResultDataAccessException e) {
           return null;
        }
        return competeSpecs;
    }
}
