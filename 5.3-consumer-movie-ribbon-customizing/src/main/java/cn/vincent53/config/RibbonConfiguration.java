package cn.vincent53.config;

import com.netflix.loadbalancer.IRule;
import com.netflix.loadbalancer.RandomRule;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * 该类为Ribbon配置类
 * 注意：该类不应该在[主应用程序上下文]的@ComponentScan中，否则本配置会被所有@RibbonClient共享
 * 所以若想自定义某一个Ribbon客户端的配置：
 * ① 须防止本@Configuration所在的包与@ComponentScan的包重叠
 * ② 或者，显示指定@ComponentScan不扫描[本@Configuration所在的包]
 */
@Configuration
public class RibbonConfiguration {
    @Bean
    public IRule ribbonRule(){
        // 负载均衡规则：改为随机
        return new RandomRule();
    }
}
