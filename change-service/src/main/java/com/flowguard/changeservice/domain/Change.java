package com.flowguard.changeservice.domain;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.time.Instant;
import java.util.UUID;

@Entity
@Table(name = "changes")
public class Change {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "change_id")
    private UUID id;

    @NotNull
    @Column(name = "service_id")
    private UUID serviceId;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "change_type")
    private ChangeType changeType;

    @NotBlank
    @Size(min = 10, max = 255)
    @Column(name = "summary", length = 255, nullable = false)
    private String summary;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "reference_type")
    private ReferenceType referenceType;

    @NotBlank
    @Size(max = 100)
    @Column(name = "reference_id", length = 100, nullable = false)
    private String referenceId;

    @Size(max = 2000)
    @Column(name = "description", length = 2000)
    private String description;

    @NotBlank
    @Size(min = 3, max = 100)
    @Column(name = "author", length = 100, nullable = false)
    private String author;

    @NotNull
    @Column(name = "created_at")
    private Instant  createdAt;

    protected Change() {}

    public Change(
            UUID serviceId,
            ChangeType changeType,
            String summary,
            ReferenceType referenceType,
            String referenceId,
            String description,
            String author) {
        this.serviceId = serviceId;
        this.changeType = changeType;
        this.summary = summary;
        this.referenceType = referenceType;
        this.referenceId = referenceId;
        this.description = description;
        this.author = author;
    }

    public UUID getId() {return id;}
    public UUID getServiceId() {return serviceId;}
    public ChangeType getChangeType() {return changeType;}
    public String getSummary() {return summary;}
    public ReferenceType getReferenceType() {return referenceType;}
    public String getReferenceId() {return referenceId;}
    public String getDescription() {return description;}
    public String getAuthor() {return author;}
    public Instant getCreatedAt() {return createdAt;}

    @PrePersist
    public void prePersist() {
        this.createdAt = Instant.now();
    }
}
