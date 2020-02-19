package cn.vincent53.config;

import org.springframework.cloud.netflix.ribbon.RibbonClient;
import org.springframework.context.annotation.Configuration;

/**
 * 使用RibbonClient，为特定name的Ribbon-Client自定义配置
 * configuration属性指定配置类
 * 写法参照：spring.io/guides/gs/client-side-load-balancing/
 */
@Configuration
@RibbonClient(name = "microservice-provider-user", configuration = RibbonConfiguration.class)
public class TestConfiguration {

}
