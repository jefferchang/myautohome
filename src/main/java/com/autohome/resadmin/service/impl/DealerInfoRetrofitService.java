package com.autohome.resadmin.service.impl;

import com.autohome.resadmin.domain.DealerItem;
import com.autohome.resadmin.domain.ResListResponse;
import com.autohome.resadmin.service.DealerInfoService;
import org.springframework.stereotype.Component;
import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import java.io.IOException;

/**
 * Created by hujinliang on 2016/6/22.
 */
@Component
public class DealerInfoRetrofitService implements DealerInfoService {

    @Override
    public ResListResponse<DealerItem> getDealersByCityAndSpec(Integer cid, Integer specid, String _appId) throws IOException {
        Retrofit retrofit = new Retrofit.Builder().addConverterFactory(GsonConverterFactory.create()).baseUrl("http://dealer.api.autohome.com.cn/").build();
        DealerInfoRetrofit service = retrofit.create(DealerInfoRetrofit.class);
        Call<ResListResponse<DealerItem>> result= service.getDealersByCityAndSpec(cid,specid,_appId);
        return result.execute().body();
    }
}
