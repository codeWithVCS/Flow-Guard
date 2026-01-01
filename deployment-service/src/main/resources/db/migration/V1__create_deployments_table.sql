CREATE TABLE deployments (
     deployment_id UUID PRIMARY KEY,
     service_id UUID NOT NULL,
     environment VARCHAR(20) NOT NULL,
     version VARCHAR(50) NOT NULL,
     deployed_at TIMESTAMP NOT NULL,
     deployed_by VARCHAR(100) NOT NULL
);

CREATE TABLE deployment_changes (
    deployment_id UUID NOT NULL,
    change_id UUID NOT NULL,
    CONSTRAINT fk_deployment_changes_deployment
        FOREIGN KEY (deployment_id)
            REFERENCES deployments (deployment_id)
            ON DELETE CASCADE
);

CREATE INDEX idx_deployments_service_env
    ON deployments (service_id, environment);

CREATE INDEX idx_deployments_service
    ON deployments (service_id);

CREATE INDEX idx_deployment_changes_deployment
    ON deployment_changes (deployment_id);
