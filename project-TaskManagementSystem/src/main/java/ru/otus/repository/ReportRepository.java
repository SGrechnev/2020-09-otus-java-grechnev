package ru.otus.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.otus.model.Report;

@Repository
public interface ReportRepository extends JpaRepository<Report, Long> {
}