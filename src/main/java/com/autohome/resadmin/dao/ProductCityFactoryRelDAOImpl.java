package com.autohome.resadmin.dao;

import com.google.common.base.Optional;
import com.google.common.primitives.Ints;
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
public class ProductCityFactoryRelDAOImpl extends BaseCache<String,Optional<Integer>> implements ProductCityFactoryRelDAO {

    @Autowired
    private DataSource resDataSource;
    @Autowired
    private BrandsDAO brandsDAO;
    private JdbcTemplate jdbcTemplate;
    @Autowired
    public ProductCityFactoryRelDAOImpl(Integer dbCachePeriod) {
        setExpireAfterWriteDuration(dbCachePeriod);//设置数据存在时长
        setTimeUnit(TimeUnit.SECONDS);
    }

   @Override
   protected Optional<Integer> fetchData(String key) {
       Integer series = countProductCityFactory(key);
       return Optional.fromNullable(series);
   }

    @Override
    public Optional<Integer> countProductCityFactoryCache(String CFP) {
       return getValue(CFP);
    }
    @Override
    public Integer countProductCityFactory(String CFP) {
        String tags[]= CFP.split(",");
        int cityId= Ints.tryParse(tags[0]);
        int serId =  Ints.tryParse(tags[1]);
        Integer factoryId=brandsDAO.findBrandsBySeriesid(serId);
        if(factoryId==null||factoryId==0)return 0;
        String productIdentity=tags[2];
        String sql = " select count(1) from productCityFactoryRel rel  ,ProductMeta pm" +
                " where rel.ProductId = pm.Id and " +
                "rel.CityId in (?,0) and FactoryId  in (?,0)  and ProductIdentity = ? ";
        jdbcTemplate = new JdbcTemplate(resDataSource);
        Integer manuFactoryId= Integer.valueOf(0);
        try {
            manuFactoryId= jdbcTemplate.queryForObject(sql,Integer.class, new Object[] {cityId,factoryId,productIdentity});
        } catch (EmptyResultDataAccessException e) {
            return null;
        }
        return manuFactoryId;
    }
}
