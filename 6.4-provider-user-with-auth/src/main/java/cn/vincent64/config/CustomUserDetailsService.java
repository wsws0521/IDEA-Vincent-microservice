package cn.vincent64.config;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

@Component
public class CustomUserDetailsService implements UserDetailsService {
    /**
     * 模拟2个账户：
     * ① 账号是:user，密码是:password1，角色是user-role
     * ② 账号是:admin，密码是:password2，角色是admin-role
     * @param username
     * @return
     * @throws UsernameNotFoundException
     */
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        if("user".equals(username)){
            return new SecurityUser("user", "password1", "user-role");
        }else if("admin".equals(username)){
            return new SecurityUser("admin", "password2", "admin-role");
        }else {
            return null;
        }
    }
}
