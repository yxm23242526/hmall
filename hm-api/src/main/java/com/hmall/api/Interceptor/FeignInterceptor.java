package com.hmall.api.Interceptor;

import com.hmall.common.consts.RequestConst;
import com.hmall.common.utils.UserContext;
import feign.RequestInterceptor;
import feign.RequestTemplate;
import org.springframework.context.annotation.Bean;

public class FeignInterceptor {
    @Bean
    public RequestInterceptor userInfoRequestInterceptor(){
        return new RequestInterceptor() {
            @Override
            public void apply(RequestTemplate requestTemplate) {
                Long user = UserContext.getUser();
                if (user != null){
                    requestTemplate.header(RequestConst.USER_HEADER, user.toString());
                }
            }
        };
    }
}
