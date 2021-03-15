package ru.otus;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import ru.otus.model.User;

import java.util.Map;
import java.util.WeakHashMap;

@Configuration
public class DbConfig {

    @Bean
    public Map<String, User> cache() {
        return new WeakHashMap<>();
    }
}