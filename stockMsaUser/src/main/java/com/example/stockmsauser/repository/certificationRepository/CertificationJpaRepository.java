package com.example.stockmsauser.repository.certificationRepository;

import com.example.stockmsauser.domain.certification.Certification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CertificationJpaRepository extends JpaRepository<Certification, Long> {
    Optional<Certification> findByEmail(String email);

    void deleteByEmail(String email);

}
