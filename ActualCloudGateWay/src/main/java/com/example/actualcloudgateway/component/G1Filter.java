package com.example.actualcloudgateway.component;

import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

@Component
public class G1Filter implements GlobalFilter, Ordered {


    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {

        System.out.println("pre global filter order -1");

        return chain.filter(exchange)
                .then(Mono.fromRunnable(() -> {

                    System.out.println("post global filter order -1");
                }));
    }

    @Override
    public int getOrder() {

        return -1;
    }
}