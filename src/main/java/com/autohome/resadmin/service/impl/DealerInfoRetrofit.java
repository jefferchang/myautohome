package com.autohome.resadmin.service.impl;

import com.autohome.resadmin.domain.DealerItem;
import com.autohome.resadmin.domain.ResListResponse;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

/**
 * Created by hujinliang on 2016/6/22.
 */
public interface DealerInfoRetrofit {
    @GET("dealerrest/v2/Profile/GetDealersByCityAndSpec?_encoding=utf8")
    Call<ResListResponse<DealerItem>> getDealersByCityAndSpec(@Query("cid") Integer cid, @Query("specid") Integer specid, @Query("_appId") String _appId);

}
