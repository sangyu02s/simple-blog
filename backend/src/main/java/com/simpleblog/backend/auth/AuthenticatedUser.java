package com.simpleblog.backend.auth;

import java.util.Collection;
import lombok.Getter;
import org.springframework.security.core.GrantedAuthority;

@Getter
public class AuthenticatedUser extends org.springframework.security.core.userdetails.User {

    private final com.simpleblog.backend.user.User sourceUser;

    public AuthenticatedUser(com.simpleblog.backend.user.User sourceUser,
                             Collection<? extends GrantedAuthority> authorities) {
        super(sourceUser.getUsername(), sourceUser.getPasswordHash(), authorities);
        this.sourceUser = sourceUser;
    }
}
