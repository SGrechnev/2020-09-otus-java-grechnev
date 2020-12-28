package ru.otus.gson;

public class UnsupportedFieldTypeException extends IllegalArgumentException {
    public UnsupportedFieldTypeException(String s) {
        super(s);
    }
}
