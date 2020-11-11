package ru.otus;

public interface TestLogging {
    void calculation(int param);

    void calculation(int param1, int param2);

    void calculation(int param1, String param3);

    void doNotLogCalculation(int param1, int param2);

    void doNotLogCalculation2(int param1, int param2);
}
