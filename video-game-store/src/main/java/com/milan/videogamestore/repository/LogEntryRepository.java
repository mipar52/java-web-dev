package com.milan.videogamestore.repository;

import com.milan.videogamestore.model.logs.LogEntry;
import org.springframework.data.jpa.repository.JpaRepository;

public interface LogEntryRepository extends JpaRepository<LogEntry, Long> { }
