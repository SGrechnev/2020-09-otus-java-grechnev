package ru.otus.service;

import org.springframework.beans.factory.annotation.Autowired;
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

    @Autowired
    private ReportRepository reportRepository;

    @Autowired
    private TaskRepository taskRepository;

    public Report save(Report report) {
        return reportRepository.save(report);
    }

    @Override
    public Report save(ReportDto reportDto) throws IllegalArgumentException {
        if (reportDto.getProgress() < 0 || reportDto.getProgress() > 100) {
            throw new IllegalArgumentException(String.format("Adding report with progress %d, expected 0..100", reportDto.getProgress()));
        }
        var task = taskRepository.findById(reportDto.getTaskId());
        if (task.isEmpty()) {
            throw new IllegalArgumentException(String.format("Adding report for non-existing task with id %d", reportDto.getTaskId()));
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