package code.repository;

import code.model.entity.Order;
import code.model.entity.OrderDetail;
import code.model.entity.ProductDetail;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface OrderDetailRepository extends JpaRepository<OrderDetail, Long> {

  @Query("SELECT od FROM OrderDetail od "
      + "JOIN od.order o "
      + "JOIN o.user u "
      + "WHERE u.id = :userId "
      + "AND od.status <> 1 "
      + "AND od.status <> 0 "
      + "ORDER BY od.updatedAt DESC")
  Page<OrderDetail> findAllByUserId(@Param("userId") Long userId, Pageable pageable);

  @Query("SELECT od FROM OrderDetail od "
      + "JOIN od.order o "
      + "JOIN o.user u "
      + "JOIN od.productDetail pd "
      + "WHERE u.id = :userId "
      + "AND od.status = :status "
      + "ORDER BY od.updatedAt DESC")
  Page<OrderDetail> findAllByUserIdAndProductDetailStatus(
      @Param("userId") Long userId,
      @Param("status") int status,
      Pageable pageable);

  @Query("SELECT od FROM OrderDetail od WHERE od.id = :orderDetailId AND od.order.user.id = :userId")
  Optional<OrderDetail> findByOrderDetailIdAndUserId(@Param("orderDetailId") Long orderDetailId,
      @Param("userId") Long userId);

  List<OrderDetail> findByOrder(Order order);

  @Query("SELECT o FROM OrderDetail o WHERE o.createdAt BETWEEN :startDate AND :endDate")
  Page<OrderDetail> findByCreatedAtBetween(LocalDateTime startDate, LocalDateTime endDate,
      Pageable pageable);
}
