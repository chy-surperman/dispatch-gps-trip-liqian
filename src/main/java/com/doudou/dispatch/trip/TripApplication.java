package com.doudou.dispatch.trip;

import com.alibaba.fastjson.serializer.SerializerFeature;
import com.alibaba.fastjson.support.config.FastJsonConfig;
import com.alibaba.fastjson.support.spring.FastJsonHttpMessageConverter;
import com.doudou.dispatch.trip.commons.IdWorker;
import com.example.openfeign.annotation.OpenFeignClientScan;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.WebApplicationType;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.http.HttpMessageConverters;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.web.client.RestTemplate;

@SpringBootApplication
@MapperScan("com.doudou.dispatch.trip.api.mappers")
@OpenFeignClientScan(basePackages = "com.doudou.dispatch.trip.api.feigns")
public class TripApplication {

    public static void main(String[] args) {
        new SpringApplicationBuilder(TripApplication.class).web(WebApplicationType.SERVLET).run(args);
    }

    @Bean
    public HttpMessageConverters fastjsonHttpMessageConverters() {
        FastJsonHttpMessageConverter additionalConverters = new FastJsonHttpMessageConverter();

        FastJsonConfig fastJsonConfig = new FastJsonConfig();
        fastJsonConfig.setSerializerFeatures(SerializerFeature.WriteMapNullValue,
                SerializerFeature.WriteNullListAsEmpty, SerializerFeature.WriteNullStringAsEmpty,
                SerializerFeature.WriteNullBooleanAsFalse, SerializerFeature.WriteNullNumberAsZero);
        additionalConverters.setFastJsonConfig(fastJsonConfig);

        return new HttpMessageConverters(additionalConverters);
    }

    @Bean
    public RestTemplate restTemplate() {
        RestTemplate restTemplate = new RestTemplate();
        return restTemplate;
    }

    @Bean
    public IdWorker idWorker(){
        return new IdWorker(3);
    }
}
