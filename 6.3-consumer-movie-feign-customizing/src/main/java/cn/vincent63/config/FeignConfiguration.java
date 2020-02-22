package cn.vincent63.config;

import feign.Contract;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * feign的配置类
 * 像Ribbon配置一样，不应该出现在主应用的上下文@ComponentScan中（怕全局共享）
 * ① 须防止本@Configuration所在的包与@ComponentScan的包重叠
 * ② 或者，显示指定@ComponentScan不扫描[本@Configuration所在的包]
 */
@Configuration
public class FeignConfiguration {
    /**
     * 将契约改为原生（默认SpringMVCContract）这样就可以使用feign自带的注解了
     * @return  默认的feign契约
     */
    @Bean
    public Contract feignContract(){
        return new feign.Contract.Default();
    }
}
