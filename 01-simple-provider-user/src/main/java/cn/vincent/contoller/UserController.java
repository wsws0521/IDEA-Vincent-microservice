package cn.vincent.contoller;

import cn.vincent.dao.UserRepository;
import cn.vincent.pojo.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class UserController {
    @Autowired
    UserRepository userRepository;
    @GetMapping("/{id}")
    public User findUserById(@PathVariable Long id){
        User user = userRepository.findOne(id);
        return user;
    }
}
