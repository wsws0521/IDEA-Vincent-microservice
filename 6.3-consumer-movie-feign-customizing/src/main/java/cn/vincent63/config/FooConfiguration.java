package cn.vincent63.config;

import feign.auth.BasicAuthRequestInterceptor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * 像Ribbon配置一样，不应该出现在主应用的上下文@ComponentScan中（怕全局共享）
 * 还可以自定义feign的编码器、解码器、日志打印
 * 甚至为feign添加拦截器（一些接口基于http basic的认证才能调用）
 * 并未提示输入用户名密码.....暂未生效，参照6.4
 */
@Configuration
public class FooConfiguration {
    @Bean
    public BasicAuthRequestInterceptor basicAuthRequestInterceptor(){
        return new BasicAuthRequestInterceptor("wsws0521", "0521wsws");
    }
}
