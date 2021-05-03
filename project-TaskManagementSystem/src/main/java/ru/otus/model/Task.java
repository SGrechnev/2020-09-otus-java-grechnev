package ru.otus.model;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.List;

@Entity
@Table(name = "tasks")
public class Task {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    @Column(name = "id", nullable = false)
    private Long id;

    @ManyToOne()
    @JoinColumn(name = "performer_id", nullable = false)
    private User performer;

    @ManyToOne()
    @JoinColumn(name = "creator_id", nullable = false)
    private User creator;

    @Column(name = "description", nullable = false)
    private String description;

    @Column(name = "expected_result", nullable = false)
    private String expectedResult;

    @Column(name = "expected_due_date", nullable = false)
    private LocalDate expectedDueDate;

    @Column(name = "actual_due_date")
    private LocalDate actualDueDate;

    @Column(name = "progress")
    private int progress;

    @OneToMany(mappedBy = "task", orphanRemoval = true, cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Report> reports;

    public Task() {
    }

    public Task(Long id, User performer, User creator, String description, String expectedResult, LocalDate expectedDueDate, LocalDate actualDueDate, int progress, List<Report> reports) {
        this.id = id;
        this.performer = performer;
        this.creator = creator;
        this.description = description;
        this.expectedResult = expectedResult;
        this.expectedDueDate = expectedDueDate;
        this.actualDueDate = actualDueDate;
        this.progress = progress;
        this.reports = reports;
    }

    public Long getId() {
        return id;
    }

    public User getPerformer() {
        return performer;
    }

    public User getCreator() {
        return creator;
    }

    public String getDescription() {
        return description;
    }

    public String getExpectedResult() {
        return expectedResult;
    }

    public LocalDate getExpectedDueDate() {
        if (expectedDueDate != null) {
            return LocalDate.from(expectedDueDate);
        }
        return null;
    }

    public LocalDate getActualDueDate() {
        if (actualDueDate != null) {
            return LocalDate.from(actualDueDate);
        }
        return null;
    }

    public int getProgress() {
        return progress;
    }

    public List<Report> getReports() {
        return List.copyOf(reports);
    }

    public void setActualDueDate(LocalDate actualDueDate) {
        this.actualDueDate = actualDueDate;
    }

    public void setProgress(int progress) {
        this.progress = progress;
    }

    @Override
    public String toString() {
        return "Task{" +
                "id=" + id +
                ", performer=" + performer +
                ", creator=" + creator +
                ", description='" + description + '\'' +
                ", expectedResult='" + expectedResult + '\'' +
                ", expectedDueDate=" + expectedDueDate +
                ", actualDueDate=" + actualDueDate +
                ", progress=" + progress +
                '}';
    }

    public static class Builder {
        private Long id;
        private User performer;
        private User creator;
        private String description;
        private String expectedResult;
        private LocalDate expectedDueDate;
        private LocalDate actualDueDate;
        private int progress;
        private List<Report> reports;

        public Builder id(Long id) {
            this.id = id;
            return this;
        }

        public Builder performer(User performer) {
            this.performer = performer;
            return this;
        }

        public Builder creator(User creator) {
            this.creator = creator;
            return this;
        }

        public Builder description(String description) {
            this.description = description;
            return this;
        }

        public Builder expectedResult(String expectedResult) {
            this.expectedResult = expectedResult;
            return this;
        }

        public Builder expectedDueDate(LocalDate expectedDueDate) {
            this.expectedDueDate = expectedDueDate;
            return this;
        }

        public Builder actualDueDate(LocalDate actualDueDate) {
            this.actualDueDate = actualDueDate;
            return this;
        }

        public Builder progress(int progress) {
            this.progress = progress;
            return this;
        }

        public Builder reports(List<Report> reports) {
            this.reports = reports;
            return this;
        }

        public Task build() {
            return new Task(id, performer, creator, description, expectedResult, expectedDueDate, actualDueDate, progress, reports);
        }
    }
}