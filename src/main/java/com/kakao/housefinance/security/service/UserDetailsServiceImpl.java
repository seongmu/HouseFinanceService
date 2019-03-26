package com.kakao.housefinance.security.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.kakao.housefinance.model.User;
import com.kakao.housefinance.repo.UserRepository;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {

    @Autowired
    UserRepository userRepository;

    @Override
    @Transactional
    public UserDetails loadUserByUsername(String username)
            throws UsernameNotFoundException {
    	
    	User user = userRepository.findByUserid(username)
                	.orElseThrow(() -> 
                        new UsernameNotFoundException("User Not Found with -> username or email : " + username)
        );

        return UserPrinciple.build(user);
    }
    
    @Transactional
    public UserDetails loadUserByUserid(String userid)
            throws UsernameNotFoundException {
    	
        User user = userRepository.findByUserid(userid)
                	.orElseThrow(() -> 
                        new UsernameNotFoundException("User Not Found with -> userid or email : " + userid)
        );

        return UserPrinciple.build(user);
    }
}