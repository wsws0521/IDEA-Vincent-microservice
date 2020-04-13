package cn.vincent7252.service;

import cn.vincent7252.pojo.User;
import feign.hystrix.FallbackFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Component
public class FeignClientFallbackFactory implements FallbackFactory<UserFeignClient> {
    private static final Logger LOGGER = LoggerFactory.getLogger(FeignClientFallbackFactory.class);

    @Override
    public UserFeignClient create(Throwable cause) {
        FeignClientFallbackFactory.LOGGER.info("fallback; reason was:", cause); // 放在这儿，服务启动就会打印这个异常...但是服务能启动成功
        return new UserFeignClient(){
            @Override
            public User findById(Long id){
//                FeignClientFallbackFactory.LOGGER.info("fallback; reason was:", cause); // 放在这儿，内部类引用外部cause必须是final，也就失去了意义
                // 问题参照 https://github.com/spring-cloud/spring-cloud-netflix/issues/1471
                User user = new User();
                user.setId(-1L);
                user.setUsername("默认用户");
                return user;
            }
        };
    }
}
