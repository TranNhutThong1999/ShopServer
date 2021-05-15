package kltn.repository;

import org.springframework.data.jpa.repository.JpaRepository;


import kltn.entity.PaymentHistory;

public interface PaymentHistoryRepository extends JpaRepository<PaymentHistory, Integer>{

}
