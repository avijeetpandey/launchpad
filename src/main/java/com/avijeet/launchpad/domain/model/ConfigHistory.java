package com.avijeet.launchpad.domain.model;

import com.fasterxml.jackson.databind.JsonNode;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "config_history", indexes = {
        @Index(name = "idx_history_config_id", columnList = "config_id")
})
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ConfigHistory {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(name = "config_id", nullable = false)
    private UUID configId;

    @JdbcTypeCode(SqlTypes.JSON)
    @Column(name = "previous_payload", columnDefinition = "jsonb")
    private JsonNode previousPayload;

    @JdbcTypeCode(SqlTypes.JSON)
    @Column(name = "diff_summary", columnDefinition = "jsonb")
    private JsonNode diffSummary;

    @Column(name = "modified_by")
    private String modifiedBy;

    @CreationTimestamp
    @Column(name = "modified_at", updatable = false)
    private LocalDateTime modifiedAt;
}
