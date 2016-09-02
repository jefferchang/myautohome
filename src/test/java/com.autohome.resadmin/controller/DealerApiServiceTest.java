package com.autohome.resadmin.controller;

import com.autohome.resadmin.domain.DealerItem;
import com.autohome.resadmin.domain.ResListResponse;
import com.autohome.resadmin.service.DealerInfoService;
import com.autohome.resadmin.service.impl.DealerInfoRetrofit;
import com.autohome.resadmin.service.impl.DealerInfoRetrofitService;
import com.github.kristofa.brave.Brave;
import com.github.kristofa.brave.FixedSampleRateTraceFilter;
import com.github.kristofa.brave.TraceFilter;
import com.github.kristofa.brave.http.DefaultSpanNameProvider;
import com.github.kristofa.brave.http.SpanNameProvider;
import com.github.kristofa.brave.okhttp.BraveOkHttpRequestResponseInterceptor;
import com.github.kristofa.brave.scribe.ScribeSpanCollector;
import okhttp3.OkHttpClient;
import org.junit.Assert;
import org.junit.Test;
import retrofit2.Call;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.Proxy;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Predicate;

/**
 * Created by hujinliang on 2016/6/14.
 */
interface Converter<F, T> {
    T convert(F from);

}
public class DealerApiServiceTest {

    @Test
    public  void GetDealerList_With_Zero_Expect_ErrorMsg() throws InterruptedException, IOException {
        DealerInfoRetrofitService dealerInfoRetrofitService=new DealerInfoRetrofitService();
        ResListResponse<DealerItem> dealerItemResListResponse= dealerInfoRetrofitService.getDealersByCityAndSpec(0,0,"");
        Assert.assertNotEquals("",dealerItemResListResponse.message);
//        Predicate<String> isEmpty = String::isEmpty;
//        Converter<String, Integer> converter = (from) -> Integer.valueOf(from);
//        Integer converted = converter.convert("123");
//        System.out.println(converted);    // 123
    }
    @Test
    public  void GetDealerList_With_BeiJing_Expect_EmptyErrorMsg() throws InterruptedException, IOException {
        DealerInfoRetrofitService dealerInfoRetrofitService=new DealerInfoRetrofitService();
        ResListResponse<DealerItem> dealerItemResListResponse= dealerInfoRetrofitService.getDealersByCityAndSpec(110100,22090,"resadmin");
        Assert.assertEquals("",dealerItemResListResponse.message);
    }
}
