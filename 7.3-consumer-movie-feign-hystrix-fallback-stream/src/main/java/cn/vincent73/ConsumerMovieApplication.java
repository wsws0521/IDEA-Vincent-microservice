package cn.vincent73;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.circuitbreaker.EnableCircuitBreaker;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.netflix.feign.EnableFeignClients;
@EnableDiscoveryClient  // 4.5 这是spring-cloud-common项目中的高度抽象注解，可同时支持Eureka、Zookeeper、Consul等
//@EnableEurekaClient   // 4.5 这是spring-cloud-netflix项目中的注解，只能配合classpath中的Eureka一起工作
@SpringBootApplication
@EnableFeignClients     // 6.2 feign
@EnableCircuitBreaker   // 7.4 为使用/hystrix.stream节点监视
public class ConsumerMovieApplication {
//    @Bean
//    public RestTemplate restTemplate() {
//        return new RestTemplate();
//    }

    public static void main(String[] args) {
        SpringApplication.run(ConsumerMovieApplication.class, args);
    }
}
