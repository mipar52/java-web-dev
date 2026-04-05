package com.milan.videogamestore.model.logEntry;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Table;
import lombok.Data;
import jakarta.persistence.*;

import java.time.OffsetDateTime;
@Data
@Table(name="log_entry")
@Entity
public class LogEntry {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(length = 200)
    private String username;

    @Column(nullable = false)
    private OffsetDateTime loggedInAt;

    @Column(length = 64)
    private String ipAddress;

    @Column(length = 1000)
    private String userAgent;

    @Column(nullable = false)
    private boolean success;

    @Column(length = 500)
    private String failureReason;
}
