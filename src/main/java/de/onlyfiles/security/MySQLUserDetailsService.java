package de.onlyfiles.security;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import de.onlyfiles.repository.UserRepository;

@Service
public class MySQLUserDetailsService implements UserDetailsService {
    
    @Autowired
    UserRepository userRepository;
    
    public MySQLUserDetailsService() {
    }
    
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        de.onlyfiles.model.User user = userRepository.findByName(username);
        if(user == null) {
            throw new UsernameNotFoundException("User not present");
        }

        List<GrantedAuthority> authorities = new ArrayList<>();
        authorities.add(new SimpleGrantedAuthority("ROLE_"+user.getPermission().toString()));
        User newUser = new MySQLUser(user.getId(), user.getName(), user.getPassword(), true, true, true, true, authorities);
        
        return newUser;
    }
    
}
