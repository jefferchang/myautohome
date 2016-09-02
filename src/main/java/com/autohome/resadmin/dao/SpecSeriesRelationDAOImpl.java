package com.autohome.resadmin.dao;


import com.autohome.resadmin.domain.SpecSeriesRelation;
import com.google.common.base.Optional;
import com.google.common.primitives.Ints;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import javax.sql.DataSource;


/**
 * Created by xw.luan on 2016/7/27.
 */
@Repository
public class SpecSeriesRelationDAOImpl  extends BaseCache<Integer,Optional<SpecSeriesRelation>> implements SpecSeriesRelationDAO{

    @Autowired
    private DataSource reproductionDataSource;
    private JdbcTemplate jdbcTemplate;

    @Autowired
    public SpecSeriesRelationDAOImpl(Integer dbCachePeriod) {
        setExpireAfterWriteDuration(dbCachePeriod);//设置数据存在时长
    }

    @Override
    protected Optional<SpecSeriesRelation> fetchData(Integer key) {
        SpecSeriesRelation specSeriesRelation= findSpecSeriesRelationBySpecId(key);
        return  Optional.fromNullable(specSeriesRelation);
    }

    @Override
    public Optional<SpecSeriesRelation> findSpecSeriesRelationByCache(int specId) {
        return getValue(specId);
    }

    @Override
    public SpecSeriesRelation findSpecSeriesRelationBySpecId(int specId) {
        String sql = "SELECT  " +
                "SpecId," +
                "SpecName," +
                "SeriesId," +
                "SpecYears," +
                "MinPrice," +
                "MaxPrice," +
                "ImgUrl," +
                "SpecState," +
                "SpecDesc," +
                "OrderNo," +
                "YearId," +
                "CreateTime," +
                "LastTime," +
                "ShowParam," +
                "StopTime," +
                "FuelType," +
                "FuelTypeId" +
                "  FROM [dbo].[SpecSeriesRelation] WITH(nolock) where SpecId = ? ";
        jdbcTemplate = new JdbcTemplate(reproductionDataSource);
        SpecSeriesRelation specSeriesRelation=null;
        try {
            specSeriesRelation= (SpecSeriesRelation) jdbcTemplate.queryForObject(sql, new Object[] {specId}, new BeanPropertyRowMapper(SpecSeriesRelation.class));
        } catch (EmptyResultDataAccessException e) {
            return null;
        }
        return specSeriesRelation;
    }
}
