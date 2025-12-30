CREATE TABLE services (
  service_id UUID PRIMARY KEY,
  service_name VARCHAR(50) NOT NULL,
  owner_team VARCHAR(50) NOT NULL,
  criticality VARCHAR(50) NOT NULL,
  status VARCHAR(20) NOT NULL,
  created_at TIMESTAMP NOT NULL,
  updated_at TIMESTAMP NOT NULL
);

-- Case-insensitive unique constraint on service_name
CREATE UNIQUE INDEX uk_services_service_name_ci
    ON services (LOWER(service_name));
