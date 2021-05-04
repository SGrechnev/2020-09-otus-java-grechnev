package ru.otus.service;

import org.springframework.stereotype.Service;
import ru.otus.dto.ReportDto;
import ru.otus.model.Report;
import ru.otus.repository.ReportRepository;
import ru.otus.repository.TaskRepository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Service
public class ReportServiceImpl implements ReportService {

    private final ReportRepository reportRepository;

    private final TaskService taskService;

    public ReportServiceImpl(ReportRepository reportRepository, TaskService taskService) {
        this.reportRepository = reportRepository;
        this.taskService = taskService;
    }

    public Report save(Report report) {
        return reportRepository.save(report);
    }

    @Override
    public Report save(ReportDto reportDto) throws IllegalArgumentException {
        if (reportDto.getProgress() < Report.PROGRESS_MIN_VALUE || reportDto.getProgress() > Report.PROGRESS_MAX_VALUE) {
            throw new IllegalArgumentException(String.format("Adding report with progress %d, expected %d..%d",
                    reportDto.getProgress(), Report.PROGRESS_MIN_VALUE, Report.PROGRESS_MAX_VALUE));
        }
        var task = taskService.get(reportDto.getTaskId());
        if (task.isEmpty()) {
            throw new IllegalArgumentException(String.format("Adding report for non-existing task with id %d",
                    reportDto.getTaskId()));
        }
        return reportRepository.save(
                new Report(task.get(), reportDto.getProgress(), reportDto.getComment(), LocalDate.now())
        );
    }

    public Optional<Report> get(Long id) {
        return reportRepository.findById(id);
    }

    public List<Report> getAll() {
        return reportRepository.findAll();
    }
}