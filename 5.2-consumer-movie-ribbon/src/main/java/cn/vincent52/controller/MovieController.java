package cn.vincent52.controller;

import cn.vincent52.pojo.User;
import com.netflix.client.config.IClientConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.loadbalancer.LoadBalancerClient;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

@RestController
public class MovieController {
    private static final Logger LOGGER = LoggerFactory.getLogger(MovieController.class);

    @Value("user.userServiceUrl")
    String userServiceUrl;
    @Autowired
    RestTemplate restTemplate;
    @Autowired
    private LoadBalancerClient loadBalancerClient;

    @GetMapping("/user/{id}")
    public User findById(@PathVariable Long id) {
//        HttpHeaders headers = new HttpHeaders();
//        headers.setContentType(MediaType.APPLICATION_JSON_UTF8);
//        HttpEntity<?> requestEntity = new HttpEntity<Object>(id, headers);

//        5.2 当ribbon+eureka配合使用时，会将虚拟主机名microservice-provider-user映射成微服务的网络地址
//        PS:虚拟主机名可以通过eureka.instance.virtual-host-name配置，或secure-virtual-host-name
//           虚拟主机名不能包含_下划线等...否则ribbon调用报异常...
//        如此，http请求会出现中文乱码问题，尚未解决
        return restTemplate.getForObject("http://microservice-provider-user/" + id, User.class);
    }
    @GetMapping("log-instance")
    public void logUserInstance() {
        ServiceInstance serviceInstance = this.loadBalancerClient.choose("microservice-provider-user");
//        打印当前选择的是哪个节点
        MovieController.LOGGER.info("{}:{}:{}",
                serviceInstance.getServiceId(), serviceInstance.getHost(), serviceInstance.getPort());
    }

    /*注意：此时的restTemplate实际上也是一个ribbon客户端，
    restTemplate.get**与loadBalancerClient.choose不能位于同一个方法中*/
}
