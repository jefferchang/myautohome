package com.autohome.resadmin.util;

/**
 * Created by hujinliang on 2016/4/22.
 */
import com.codahale.metrics.JmxReporter;
import com.codahale.metrics.MetricRegistry;
import com.timgroup.statsd.NonBlockingStatsDClient;
import com.timgroup.statsd.StatsDClient;
import org.elasticsearch.metrics.ElasticsearchReporter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.context.support.PropertySourcesPlaceholderConfigurer;
import org.springframework.core.env.Environment;
import org.springframework.core.io.ClassPathResource;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

@Configuration
@PropertySource("classpath:resadmin.properties")
public class AppConfig {

    @Bean
    public Integer dbCachePeriod() {
        return Integer.parseInt(env.getProperty("cache.dbCachePeriod"));
    }
    @Bean
    public String accessClickLoggerName() {
        return env.getProperty("accessClickLogger.Name");
    }
    @Bean
    public String accessLoggerName() {
        return env.getProperty("accessLogger.Name");
    }
    @Bean
    public String accessLoggerVersion() {
        return env.getProperty("accessLogger.Version");
    }
    @Bean
    public StatsDClient statsdClient(){
        return new NonBlockingStatsDClient(env.getProperty("statsdClient.prefix"), env.getProperty("statsdClient.host"),Integer.parseInt(env.getProperty("statsdClient.port")));
    }
    @Bean
    public String competeSize(){
     return env.getProperty("compete.Size");
    }
    //    @Bean
//    public MetricRegistry metricsLogger() throws IOException {
//        MetricRegistry metricsLogger = new MetricRegistry();
//        ElasticsearchReporter reporter = ElasticsearchReporter.forRegistry(metricsLogger).hosts(env.getProperty("elasticsearch.server")).build();
//        reporter.start(Integer.parseInt(env.getProperty("metrics.Period")), TimeUnit.SECONDS);
//        JmxReporter.forRegistry(metricsLogger).build().start();
//        return metricsLogger;
//    }
    @Autowired
    private Environment env;
}
