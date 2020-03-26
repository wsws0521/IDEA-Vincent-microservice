package cn.vincent7251.service;

import cn.vincent7251.pojo.User;
import org.springframework.stereotype.Component;

@Component
public class FeignClientFallback implements UserFeignClient {
    @Override
    public User findById(Long id) {
        User user = new User();
        user.setId(-1L);
        user.setName("默认用户");
        return user;
    }
}
