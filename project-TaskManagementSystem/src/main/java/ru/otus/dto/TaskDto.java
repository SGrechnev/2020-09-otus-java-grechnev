package ru.otus.dto;

import java.time.LocalDate;

public class TaskDto {
    private final Long performerId;
    private final String description;
    private final String expectedResult;
    private final LocalDate expectedDueDate;

    public TaskDto(Long performerId, String description, String expectedResult, LocalDate expectedDueDate) {
        this.performerId = performerId;
        this.description = description;
        this.expectedResult = expectedResult;
        this.expectedDueDate = expectedDueDate;
    }

    public Long getPerformerId() {
        return performerId;
    }

    public String getDescription() {
        return description;
    }

    public String getExpectedResult() {
        return expectedResult;
    }

    public LocalDate getExpectedDueDate() {
        return expectedDueDate;
    }

    @Override
    public String toString() {
        return "TaskDto{" +
                "performerId=" + performerId +
                ", description='" + description + '\'' +
                ", expectedResult='" + expectedResult + '\'' +
                ", expectedDueDate=" + expectedDueDate +
                '}';
    }
}
