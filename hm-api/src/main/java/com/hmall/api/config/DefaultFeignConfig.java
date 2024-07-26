package com.hmall.api.config;

import com.hmall.common.consts.RequestConst;
import com.hmall.common.utils.UserContext;
import feign.Logger;
import feign.RequestInterceptor;
import feign.RequestTemplate;
import org.springframework.context.annotation.Bean;

public class DefaultFeignConfig
{
    @Bean
    public Logger.Level feignLoggerLevel(){
        return Logger.Level.FULL;
    }

    @Bean
    public RequestInterceptor userInfoRequestInterceptor(){
        return new RequestInterceptor() {
            @Override
            public void apply(RequestTemplate requestTemplate) {
                //通过feign传递时，此时是能从网关收到用户token的上下文，但是微服务之间的传递不通关网关
                //所以需要通过feign拦截器传递
                Long user = UserContext.getUser();
                if (user != null){
                    requestTemplate.header(RequestConst.USER_HEADER, user.toString());
                }
            }
        };
    }
}
