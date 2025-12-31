package com.flowguard.changeservice.repository;

import com.flowguard.changeservice.domain.Change;
import com.flowguard.changeservice.domain.ReferenceType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface ChangeRepository extends JpaRepository<Change, UUID> {

    List<Change> findByServiceIdOrderByCreatedAtDesc(UUID serviceId);

    Optional<Change> findByServiceIdAndReferenceTypeAndReferenceId(
            UUID serviceId,
            ReferenceType referenceType,
            String referenceId
    );

    boolean existsByServiceIdAndReferenceTypeAndReferenceId(
            UUID serviceId,
            ReferenceType referenceType,
            String referenceId
    );

    List<Change> findAllByIdIn(Collection<UUID> ids);

}
