package code.repository;

import code.model.entity.Order;
import code.model.entity.OrderDetail;
import code.model.entity.User;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface OrderRepository extends JpaRepository<Order, Long> {

//  Page<Order> findAllByUser(User user, Pageable pageable);
  List<Order> findByUserAAndAndPaid(User user,boolean status);
}
