package com.hmall.gateway.filter;

import com.hmall.common.consts.RequestConst;
import com.hmall.gateway.config.AuthProperties;
import com.hmall.gateway.util.JwtTool;
import lombok.RequiredArgsConstructor;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.util.AntPathMatcher;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.util.List;

@Component
@RequiredArgsConstructor
public class AuthFilter implements GlobalFilter, Ordered  {


    private final AuthProperties authProperties;

    private final JwtTool jwtTool;

    private final AntPathMatcher antPathMatcher = new AntPathMatcher();
    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain){
        //1. 获取request
        ServerHttpRequest request = exchange.getRequest();
        if (isExcluded(request.getPath().toString())){
            return chain.filter(exchange);
        }
        //2. 登录放行

        //3. 验证token
        String token = null;
        List<String> headers = request.getHeaders().get("authorization");
        if (headers != null && !headers.isEmpty()){
            token =  headers.get(0);
        }
        Long userId = null;
        //4. 解析token
        try {
            userId = jwtTool.parseToken(token);
        } catch (Exception e) {
            ServerHttpResponse response = exchange.getResponse();
            response.setStatusCode(HttpStatus.UNAUTHORIZED);
            return response.setComplete();
        }
        String userInfo = userId.toString();
        //5. 保存用户id
        ServerWebExchange webExchange = exchange.mutate()
                .request(builder -> builder.header(RequestConst.USER_HEADER, userInfo))
                .build();
        //6. 放行
        return chain.filter(webExchange);
    }

    private boolean isExcluded(String path) {
        for (String excludePath : authProperties.getExcludePaths()) {
            if (antPathMatcher.match(path, excludePath)) {
                return true;
            }
        }
        return false;
    }

    @Override
    public int getOrder() {
        return 0;
    }

}
