package com.flowguard.approvalservice.repository;

import com.flowguard.approvalservice.domain.Approval;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface ApprovalRepository extends JpaRepository<Approval, UUID> {

    Optional<Approval> findByDeploymentId(UUID deploymentId);

    boolean existsByDeploymentId(UUID deploymentId);

}
