package com.autohome.resadmin.service;

import com.autohome.resadmin.domain.DealerItem;
import com.autohome.resadmin.domain.ResListResponse;

import java.io.IOException;

/**
 * Created by hujinliang on 2016/6/14.
 */
public interface DealerInfoService {
    ResListResponse<DealerItem> getDealersByCityAndSpec(Integer cid, Integer specid,String _appId) throws IOException;
}
