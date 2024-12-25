package code.repository;

import code.model.entity.Order;
import code.model.entity.OrderDetail;
import java.time.LocalDateTime;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface OrderRepository extends JpaRepository<Order,Long> {
//  Page<OrderDetail> findByOrderDateBetween(
//      LocalDateTime startDate, LocalDateTime endDate, Pageable pageable);
}
