package com.milan.videogamestore.repository;

import com.milan.videogamestore.model.logEntry.LogEntry;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.OffsetDateTime;
import java.util.List;

public interface LogEntryRepository extends JpaRepository<LogEntry, Long> {
    @Query("""
      select a from LogEntry a
      where (:username is null or :username = '' or lower(a.username) like lower(concat('%', :username, '%')))
        and (:from is null or a.loggedInAt >= :from)
        and (:to is null or a.loggedInAt <= :to)
        and (:success is null or a.success = :success)
      order by a.loggedInAt desc
    """)
    List<LogEntry> adminSearch(@Param("username") String username,
                               @Param("from") OffsetDateTime from,
                               @Param("to") OffsetDateTime to,
                               @Param("success") Boolean success);
}
