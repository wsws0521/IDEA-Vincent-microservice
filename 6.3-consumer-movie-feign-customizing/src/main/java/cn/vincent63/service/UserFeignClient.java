package cn.vincent63.service;

import cn.vincent63.config.FeignConfiguration;
import cn.vincent63.config.FooConfiguration;
import cn.vincent63.pojo.User;
import feign.Param;
import feign.RequestLine;
import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

//客户端名字用于创建Ribbon，由于引入了Eureka，便从Eureka注册表中查找该服务
//如果想摆脱Eureka，采用5.5：service.ribbon.listOfServers属性配置
//也可以使用URL属性：主机名或url：@FeignClient(name = "microservice-provider-user", url = "http://localhost:8000/")
@FeignClient(name = "microservice-provider-user", configuration = {FeignConfiguration.class, FooConfiguration.class})
public interface UserFeignClient {
    /**
     * 使用feign原生注解：@RequestLine @Param
     * @see https://github.com/OpenFeign/feign
     * @param id
     * @return
     */
//    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    @RequestLine("GET /{id}")
    public User findById(@Param("id") Long id);
}
