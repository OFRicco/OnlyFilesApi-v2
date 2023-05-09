package de.onlyfiles.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import de.onlyfiles.repository.UserRepository;

@Service
public class MySQLUserDetailsService implements UserDetailsService {

    @Autowired
    private UserRepository userRepository;
    
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        de.onlyfiles.model.User user = userRepository.findUserByName(username);
        if(user == null) {
            throw new UsernameNotFoundException("User not present");
        }
        return User.builder()
                .username(user.getName())
                .password(user.getPassword())
                .roles(user.getPermission().toString())
            .build();
    }
    
}
