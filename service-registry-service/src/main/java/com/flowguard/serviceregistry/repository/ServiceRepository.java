package com.flowguard.serviceregistry.repository;

import com.flowguard.serviceregistry.domain.Service;
import com.flowguard.serviceregistry.domain.ServiceStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface ServiceRepository extends JpaRepository<Service, UUID> {

    Optional<Service> findByServiceNameIgnoreCase(String serviceName);

    boolean existsByServiceNameIgnoreCase(String serviceName);

    List<Service> findAllByStatus(ServiceStatus status);

}
