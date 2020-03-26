package cn.vincent64.controller;

import cn.vincent64.pojo.User;
import cn.vincent64.service.UserFeignClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.netflix.feign.FeignClientsConfiguration;
import org.springframework.context.annotation.Import;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import feign.Client;
import feign.Contract;
import feign.Feign;
import feign.auth.BasicAuthRequestInterceptor;
import feign.codec.Decoder;
import feign.codec.Encoder;


@Import(FeignClientsConfiguration.class) // 6.4手动，FeignClientsConfiguration是Spring为Feign提供的默认配置类
@RestController
public class MovieController {
//    @Autowired
//    private UserFeignClient userFeignClient;

    private UserFeignClient userUserFeignClient;
    private UserFeignClient adminUserFeignClient;

//    @GetMapping("/user/{id}")
//    public User findById(@PathVariable Long id) {
//        return this.userFeignClient.findById(id);
//    }

    @Autowired
    public MovieController(Decoder decoder, Encoder encoder, Client client, Contract contract) {
        // 这边的decoder、encoder、client、contract，可以debug看看是什么实例。
        this.userUserFeignClient = Feign.builder().client(client).encoder(encoder).decoder(decoder).contract(contract)
                .requestInterceptor(new BasicAuthRequestInterceptor("user", "password1")).target(UserFeignClient.class, "http://microservice-provider-user/");
        this.adminUserFeignClient = Feign.builder().client(client).encoder(encoder).decoder(decoder).contract(contract)
                .requestInterceptor(new BasicAuthRequestInterceptor("admin", "password2"))
                .target(UserFeignClient.class, "http://microservice-provider-user/");
    }

    @GetMapping("/user-user/{id}")
    public User findByIdUser(@PathVariable Long id) {
        return this.userUserFeignClient.findById(id);
    }

    @GetMapping("/user-admin/{id}")
    public User findByIdAdmin(@PathVariable Long id) {
        return this.adminUserFeignClient.findById(id);
    }
}
