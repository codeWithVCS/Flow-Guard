CREATE TABLE changes (
     change_id UUID PRIMARY KEY,
     service_id UUID NOT NULL,

     change_type VARCHAR(50) NOT NULL,
     summary VARCHAR(255) NOT NULL,
     description VARCHAR(2000),

     reference_type VARCHAR(50) NOT NULL,
     reference_id VARCHAR(100) NOT NULL,

     author VARCHAR(100) NOT NULL,
     created_at TIMESTAMP NOT NULL
);

-- Prevent duplicate change references per service
ALTER TABLE changes
    ADD CONSTRAINT uq_changes_service_reference
        UNIQUE (service_id, reference_type, reference_id);

-- Optimized lookup for service change timeline
CREATE INDEX idx_changes_service_id_created_at
    ON changes (service_id, created_at DESC);
