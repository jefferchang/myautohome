package com.autohome.resadmin.dao;

/**
 * Created by hujinliang on 2016/3/29.
 */

import com.autohome.resadmin.domain.EntryIndexRel;
import com.autohome.resadmin.domain.EntryInfo;
import com.autohome.resadmin.domain.EntryProductRel;
import com.google.common.base.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import javax.sql.DataSource;
import java.util.List;
import java.util.stream.Collectors;
@Repository
public class EntryInfoDAOImpl extends  BaseCache<Integer,Optional<EntryInfo>>  implements EntryInfoDAO{
    @Autowired
    private DataSource resDataSource;
    private JdbcTemplate jdbcTemplate;


    @Autowired
    public EntryInfoDAOImpl(Integer dbCachePeriod) {
        setExpireAfterWriteDuration(dbCachePeriod);//设置数据存在时长
    }

    @Override
    protected  Optional<EntryInfo>  fetchData(Integer key) {
        EntryInfo ei = findByResId(key);
        return Optional.fromNullable(ei);
    }

    @Override
    public Optional<EntryInfo> findByResIdCache(int resId) {
        return getValue(resId);

    }
    @Override
    public EntryInfo findByResId(int resId) {

        String sql = "SELECT top 1 ei.Id,EntryName,ResId,EntryTypeId,EntryTypeName,RendTemplate,UseServerTemplate,ShowCompetitive FROM [dbo].[EntryInfo] ei LEFT JOIN dbo.EntryType et ON ei.EntryTypeId = et.Id WHERE ei.ResId = ? AND ei.Status=1";
        jdbcTemplate = new JdbcTemplate(resDataSource);
        EntryInfo entryInfo=null;
        try {
            entryInfo= (EntryInfo) jdbcTemplate.queryForObject(sql, new Object[] {resId}, new BeanPropertyRowMapper(EntryInfo.class));
        } catch (EmptyResultDataAccessException e) {
           return null;
        }
        if (entryInfo!=null){
            List<EntryProductRel> productRelList=findProductList(entryInfo.getId());
            entryInfo.setProductList(productRelList);
            entryInfo.setProductMap(productRelList.stream().collect(Collectors.toMap(EntryProductRel::getProductIdentity, EntryProductRel::getTemplate)));
            List<EntryIndexRel> solrIndexList=findIndexList(entryInfo.getId());
            entryInfo.setSolrIndexList(solrIndexList);
            entryInfo.setSolrIndexMap(solrIndexList.stream().collect(Collectors.toMap(EntryIndexRel::getIndexName, EntryIndexRel::getIsSel)));
        }
        return entryInfo;
    }

    @Override
    public List<EntryIndexRel> findIndexList(int entryId) {
        String sql = "SELECT IndexName,IsSel  FROM [dbo].[EntryIndexRel] WITH(nolock) where EntryId = ?";
        jdbcTemplate = new JdbcTemplate(resDataSource);
        List<EntryIndexRel> indexRels =  jdbcTemplate.query(sql,new Object[] { entryId },new BeanPropertyRowMapper(EntryIndexRel.class));
        return indexRels;
    }

    @Override
    public List<EntryProductRel> findProductList(int entryId) {
        String sql = "SELECT ProductIdentity,Template,ProductOrder,ShowCompetitive  FROM [dbo].[EntryProductRel] WITH(nolock) where EntryId = ? ORDER BY  ProductOrder ";
        jdbcTemplate = new JdbcTemplate(resDataSource);
        List<EntryProductRel> productRels =  jdbcTemplate.query(sql,new Object[] { entryId },new BeanPropertyRowMapper( EntryProductRel.class ));
        return productRels;
    }
}
