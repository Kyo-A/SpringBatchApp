package com.example.springbatchapp.security;

import com.example.springbatchapp.model.User;
import com.example.springbatchapp.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {

    @Autowired
    private UserRepository userRepository;

    // verifie l'existence d'un utilisateur selon la valeur se son username
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepository.findByUserName(username);
        if(user == null)
            throw new UsernameNotFoundException("No Username Found " + username);
        return new UserDetailsImpl(user);
    }
}
