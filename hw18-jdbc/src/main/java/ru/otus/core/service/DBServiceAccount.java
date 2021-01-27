package ru.otus.core.service;

import ru.otus.core.model.Account;

import java.util.Optional;

public interface DBServiceAccount {

    String saveAccount(Account account);

    Optional<Account> getAccount(String no);

    //List<Account> findAll();
}
