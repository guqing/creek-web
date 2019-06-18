package xyz.guqing.authorization.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import xyz.guqing.authorization.entity.MyUserDetails;

@Service
public class MyUserDetailsService implements UserDetailsService {

//    @Autowired
//    UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String s) throws UsernameNotFoundException {
        MyUserDetails userDetails = new MyUserDetails();
        userDetails.setUsername("admin");
        BCryptPasswordEncoder bcrypt = new BCryptPasswordEncoder();
        userDetails.setPassword(bcrypt.encode("123456"));

        return userDetails;
    }

}