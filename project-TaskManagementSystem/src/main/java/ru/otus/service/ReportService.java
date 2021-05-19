package ru.otus.service;

import ru.otus.dto.ReportDto;
import ru.otus.model.Report;

import java.util.List;
import java.util.Optional;

public interface ReportService {

    Report save(Report report);

    Report save(ReportDto reportDto);

    Optional<Report> get(Long id);

    List<Report> getAll();

}