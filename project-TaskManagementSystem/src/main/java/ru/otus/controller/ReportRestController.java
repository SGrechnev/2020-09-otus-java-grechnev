package ru.otus.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.otus.dto.ReportDto;
import ru.otus.model.Report;
import ru.otus.service.ReportService;
import ru.otus.service.TaskService;

import java.util.List;

@CrossOrigin
@RestController
public class ReportRestController {

    private final static Logger logger = LoggerFactory.getLogger(ReportRestController.class);

    private final ReportService reportService;
    private final TaskService taskService;

    public ReportRestController(ReportService reportService, TaskService taskService) {
        this.reportService = reportService;
        this.taskService = taskService;
    }

    @GetMapping("/api/reports")
    public ResponseEntity<List<Report>> getAllReports() {
        logger.info("getAllReports()");
        return ResponseEntity.ok(reportService.getAll());
    }

    @GetMapping("/api/reports/{id}")
    public ResponseEntity<Report> getReportById(@PathVariable(name = "id") Long id) {
        var report = reportService.get(id);
        logger.info("getting report by id({}): {}", id, report);
        return ResponseEntity.of(report);
    }

    @PostMapping("/api/reports")
    public ResponseEntity<?> saveReport(@RequestBody ReportDto reportDto) {
        logger.info("POST /api/reports {}", reportDto);
        try {
            var report = reportService.save(reportDto);
            if (report != null) {
                taskService.updateProgress(report.getTaskId(), report.getProgress());
                return ResponseEntity.ok(report);
            }
            return ResponseEntity.badRequest().build();
        } catch (IllegalArgumentException e) {
            logger.warn("{}", e.getMessage());
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    private String handleMessageException(Exception e) {
        logger.error("Exception: {}", e.getMessage());
        return "Oops! Something went wrong. Error: " + e.getClass().getSimpleName();
    }
}