package ru.otus.dto;

public class ReportDto {
    private final Long taskId;
    private final int progress;
    private final String comment;

    public ReportDto(Long taskId, int progress, String comment) {
        this.taskId = taskId;
        this.progress = progress;
        this.comment = comment;
    }

    public Long getTaskId() {
        return taskId;
    }

    public int getProgress() {
        return progress;
    }

    public String getComment() {
        return comment;
    }

    @Override
    public String toString() {
        return "ReportDto{" +
                "taskId=" + taskId +
                ", progress=" + progress +
                ", comment='" + comment + '\'' +
                '}';
    }
}
