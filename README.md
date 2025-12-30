# FlowGuard – Platform Foundation

This repository is the monorepo root for the FlowGuard platform.

At this stage, it contains only the shared project foundation and local infrastructure
required to support all FlowGuard services.

No application services are included yet.

---

## Prerequisites

Ensure the following are installed on your machine:

- Java 17
- Maven 3.9+
- Docker & Docker Compose (v2)

---

## Included Infrastructure

The local infrastructure is managed using Docker Compose and includes:

- PostgreSQL (single instance)
- Apache Kafka
- Zookeeper

This infrastructure is shared by all FlowGuard services.

---

## Starting Local Infrastructure

From the repository root, run:

```bash
docker compose up
