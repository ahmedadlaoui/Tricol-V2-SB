package com.example.tricolv2sb.Controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor // This injects the JdbcTemplate
public class DatabaseCheckController {

    private final JdbcTemplate jdbcTemplate;

    @GetMapping("/db-status")
    public ResponseEntity<String> checkDatabaseStatus() {
        try {
            jdbcTemplate.queryForObject("SELECT 1", Integer.class);
            return ResponseEntity.ok("✅ Database connection is active and OK!");

        } catch (Exception e) {

            return ResponseEntity
                    .status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("❌ Database connection FAILED: " + e.getMessage());
        }
    }
}