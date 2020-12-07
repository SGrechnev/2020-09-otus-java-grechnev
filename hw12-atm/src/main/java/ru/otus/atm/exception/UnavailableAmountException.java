package ru.otus.atm.exception;

public class UnavailableAmountException extends ATMException {

    public UnavailableAmountException(String s) {
        super(s);
    }
}