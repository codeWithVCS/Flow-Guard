CREATE TABLE approvals(
    approval_id UUID PRIMARY KEY,
    deployment_id UUID NOT NULL UNIQUE,
    approved_by VARCHAR(100) NOT NULL,
    comment VARCHAR(500),
    approved_at TIMESTAMP NOT NULL
);