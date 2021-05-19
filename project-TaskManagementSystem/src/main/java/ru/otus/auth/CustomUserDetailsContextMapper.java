package ru.otus.auth;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.ldap.core.DirContextAdapter;
import org.springframework.ldap.core.DirContextOperations;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.ldap.userdetails.LdapUserDetailsMapper;
import org.springframework.stereotype.Component;
import ru.otus.model.Role;
import ru.otus.service.UserService;

import java.util.ArrayList;
import java.util.Collection;

@Component
public class CustomUserDetailsContextMapper extends LdapUserDetailsMapper {

    private static final Logger logger = LoggerFactory.getLogger(CustomUserDetailsContextMapper.class);

    private final UserService userService;

    public CustomUserDetailsContextMapper(UserService userService) {
        this.userService = userService;
    }

    @Override
    public UserDetails mapUserFromContext(DirContextOperations ctx, String username, Collection<? extends GrantedAuthority> authorities) {
        logger.info("ctx: {}", ctx);
        byte[] jpegPhoto = (byte[]) ctx.getObjectAttribute("jpegPhoto");
        if (jpegPhoto != null) {
            logger.info("...: {}", jpegPhoto.length);
        }
        var user = userService.findByUsername(username);
        if (user.isEmpty()) {
            throw new UserNotFoundInLocalDbException("User \"" + username + "\" not found in local database");
        }
        Collection<Role> localAuthorities = new ArrayList<>();
        user.ifPresent(value -> localAuthorities.add(value.getRole()));
        return super.mapUserFromContext(ctx, username, localAuthorities);
    }

    @Override
    public void mapUserToContext(UserDetails user, DirContextAdapter ctx) {
        logger.info("mapUserFromContext(user: {}, ctx: {})", user, ctx);
        super.mapUserToContext(user, ctx);
    }
}
