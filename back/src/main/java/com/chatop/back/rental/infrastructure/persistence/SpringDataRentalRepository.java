package com.chatop.back.rental.infrastructure.persistence;

import org.springframework.data.jpa.repository.JpaRepository;

interface SpringDataRentalRepository extends JpaRepository<JpaRentalEntity, Long> {
}
