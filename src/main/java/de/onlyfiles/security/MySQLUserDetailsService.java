package de.onlyfiles.security;

import java.util.Arrays;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import de.onlyfiles.Permission;
import de.onlyfiles.repository.UserRepository;

@Service
public class MySQLUserDetailsService implements UserDetailsService {

    @Autowired
    private UserRepository userRepository;
    
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        de.onlyfiles.model.User user = userRepository.findUserByDisplayName(username);
        if(user == null) {
            throw new UsernameNotFoundException("User not present");
        }
        return User.builder()
                .username(user.getDisplayName())
                .password(user.getPassword())
                .roles(permissionToRoles(user.getPermission()))
            .build();
    }
    
    /*
     * Make roles hierarchical
     */
    private String[] permissionToRoles(Permission userPermission) {
        return Arrays.stream(
                Arrays.copyOfRange(Permission.class.getEnumConstants(), userPermission.ordinal(), Permission.class.getEnumConstants().length)
                ).map(Enum::name).toArray(String[]::new);
    }

}
