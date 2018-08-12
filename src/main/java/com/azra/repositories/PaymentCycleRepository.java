package com.azra.repositories;

import com.azra.entities.PaymentCycle;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

public interface PaymentCycleRepository extends CrudRepository<PaymentCycle, Long> {
    // get payment cycle
    @Query("SELECT p FROM PaymentCycle p WHERE p.isOpen = :isOpen")
    PaymentCycle getCurrentPaymentCycle(@Param("isOpen") boolean isOpen);
}
