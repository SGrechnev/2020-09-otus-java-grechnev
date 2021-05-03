package ru.otus.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.ldap.userdetails.LdapUserDetailsImpl;
import org.springframework.stereotype.Service;
import ru.otus.model.User;

@Service
public class AuthenticatedUserInfoService {

    @Autowired
    private UserService userService;

    public User getAuthenticatedUser(){
        var ldapUserDetails = (LdapUserDetailsImpl) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        String username = ldapUserDetails.getUsername();
        var optionalUser = userService.findByUsername(username);
        return optionalUser.orElse(null);
    }
}
