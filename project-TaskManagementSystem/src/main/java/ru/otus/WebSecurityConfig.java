package ru.otus;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import ru.otus.auth.CustomUserDetailsContextMapper;

@Configuration
@EnableWebSecurity
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

    @Value("${ldap.url}")
    private String ldapUrl;
    @Value("${ldap.base.dn}")
    private String ldapBaseDn;
    @Value("${ldap.username}")
    private String ldapSecurityPrincipal;
    @Value("${ldap.password}")
    private String ldapPrincipalPassword;
    @Value("${ldap.user.dn.pattern}")
    private String ldapUserDnPattern;
    @Value("${ldap.enabled}")
    private String ldapEnabled;

    @Autowired
    private CustomUserDetailsContextMapper customUserDetailsContextMapper;

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                .csrf().disable()
                .authorizeRequests()
                .antMatchers("/users", "/api/users").hasRole("ADMIN")
                .antMatchers(HttpMethod.POST, "/api/tasks").hasRole("MANAGER")
                .antMatchers(HttpMethod.POST, "/api/reports").hasRole("PERFORMER")
                .anyRequest().authenticated()
                .and()
                .formLogin()
                .and()
                .logout()
                .logoutRequestMatcher(new AntPathRequestMatcher("/logout"))
                .deleteCookies("JSESSIONID")
                .clearAuthentication(true)
                .invalidateHttpSession(true).permitAll()
        ;
    }

    @Override
    public void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth
                .ldapAuthentication()
                .contextSource()
                .url(ldapUrl + ldapBaseDn)
                .managerDn(ldapSecurityPrincipal)
                .managerPassword(ldapPrincipalPassword)
                .and()
                .userDnPatterns(ldapUserDnPattern)
                .passwordCompare()
                .passwordAttribute("userPassword")
                .and()
                .userDetailsContextMapper(customUserDetailsContextMapper);
    }

}
