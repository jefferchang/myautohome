package com.autohome.resadmin.dao;

import com.autohome.resadmin.domain.EntryIndexRel;
import com.autohome.resadmin.domain.EntryInfo;
import com.autohome.resadmin.domain.EntryProductRel;
import com.google.common.base.Optional;

import java.util.List;
import java.util.concurrent.ExecutionException;

public interface EntryInfoDAO {
    Optional<EntryInfo> findByResIdCache(int resId);
    public EntryInfo findByResId(int pvId);
    public List<EntryIndexRel> findIndexList(int entryId);
    public List<EntryProductRel> findProductList(int entryId);
}