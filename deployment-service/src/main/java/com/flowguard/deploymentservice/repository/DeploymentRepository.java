package com.flowguard.deploymentservice.repository;

import com.flowguard.deploymentservice.domain.Deployment;
import com.flowguard.deploymentservice.domain.Environment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface DeploymentRepository extends JpaRepository<Deployment, UUID> {

    Optional<Deployment> findTopByServiceIdAndEnvironmentOrderByDeployedAtDesc(
            UUID serviceId,
            Environment environment
    );

    List<Deployment> findByServiceIdAndEnvironmentOrderByDeployedAtDesc(
            UUID serviceId,
            Environment environment
    );

    List<Deployment> findByServiceIdOrderByDeployedAtDesc(UUID serviceId);

    boolean existsByServiceIdAndEnvironmentAndVersion(
            UUID serviceId,
            Environment environment,
            String version
    );

}
