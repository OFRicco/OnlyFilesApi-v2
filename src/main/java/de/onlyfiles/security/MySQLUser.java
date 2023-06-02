package de.onlyfiles.security;

import java.util.Collection;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;

public class MySQLUser extends User {

    private static final long serialVersionUID = 5878028392186111019L;
    private final long id;
    
    public MySQLUser(Long id, String username, String password, boolean enabled, boolean accountNonExpired, boolean credentialsNonExpired, boolean accountNonLocked,
            Collection<? extends GrantedAuthority> authorities) {
        super(username, password, enabled, accountNonExpired, credentialsNonExpired, accountNonLocked, authorities);
        this.id = id;
    }
    
    public long getId() {
        return id;
    }
    
}
