package ru.otus.model;

import javax.persistence.*;
import java.time.LocalDate;

@Entity
@Table(name = "reports")
public class Report {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    @Column(name = "id")
    private Long id;

    @ManyToOne()
    @JoinColumn(name = "task_id", nullable = false)
    private Task task;

    @Column(name = "progress", nullable = false)
    private int progress;

    @Column(name = "comment")
    private String comment;

    @Column(name = "creation_date")
    private LocalDate creationDate;

    public Report() {
    }

    public Report(Task task, int progress, String comment, LocalDate creationDate) {
        this.task = task;
        this.progress = progress;
        this.comment = comment;
        this.creationDate = creationDate;
    }

    public Long getId() {
        return id;
    }

    public Long getTaskId() {
        return task.getId();
    }

    public int getProgress() {
        return progress;
    }

    public String getComment() {
        return comment;
    }

    @Override
    public String toString() {
        return "Report{" +
                "id=" + id +
                ", task_id=" + (task==null?"null":task.getId()) +
                ", progress=" + progress +
                ", comment='" + comment + '\'' +
                ", creation_date=" + creationDate +
                '}';
    }
}