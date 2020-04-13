package cn.vincent882;

import cn.vincent882.filter.pre.PreRequestLogFilter;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.zuul.EnableZuulProxy;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
@EnableZuulProxy    // 8.3 开启网关代理（内部使用Ribbon来定位Eureka上的微服务，同时整合了Hystrix容错）
public class ZuulApplication {
    // 8.8.2 自定义过滤器
    @Bean
    public PreRequestLogFilter preRequestLogFilter() {
        return new PreRequestLogFilter();
    }

    public static void main(String[] args) {
        SpringApplication.run(ZuulApplication.class, args);
    }
}
