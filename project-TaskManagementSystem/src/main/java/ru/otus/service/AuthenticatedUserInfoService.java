package ru.otus.service;

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.ldap.userdetails.LdapUserDetailsImpl;
import org.springframework.stereotype.Service;
import ru.otus.model.User;

@Service
public class AuthenticatedUserInfoService {

    private final UserService userService;

    public AuthenticatedUserInfoService(UserService userService) {
        this.userService = userService;
    }

    public User getAuthenticatedUser(){
        var ldapUserDetails = (LdapUserDetailsImpl) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        String username = ldapUserDetails.getUsername();
        var optionalUser = userService.findByUsername(username);
        return optionalUser.orElse(null);
    }
}
