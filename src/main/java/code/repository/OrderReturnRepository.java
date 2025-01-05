package code.repository;

import code.model.entity.OrderReturn;
import java.time.LocalDateTime;
import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface OrderReturnRepository extends JpaRepository<OrderReturn,Long> {
  @Query("""
        SELECT orr 
        FROM OrderReturn orr
        JOIN orr.orderDetail od
        JOIN od.order o
        JOIN o.user u
        WHERE u.id = :userId
    """)
  Page<OrderReturn> findAllByUserId(@Param("userId") Long userId, Pageable pageable);

  @Query("SELECT SUM(od.currentPrice - orr.damageOrLossFee - orr.overdueFee) " +
      "FROM OrderReturn orr " +
      "JOIN orr.orderDetail od " +
      "WHERE orr.isPaid = true " +
      "AND orr.createdAt BETWEEN :startDate AND :endDate")
  Long calculateTotalAmountInDateRange(@Param("startDate") LocalDateTime startDate,
      @Param("endDate") LocalDateTime endDate);

  @Query("SELECT MONTH(orr.createdAt) AS month, " +
      "SUM(od.currentPrice - orr.damageOrLossFee - orr.overdueFee) AS total " +
      "FROM OrderReturn orr " +
      "JOIN orr.orderDetail od " +
      "WHERE orr.isPaid = true " +
      "AND YEAR(orr.createdAt) = :year " +
      "GROUP BY MONTH(orr.createdAt) " +
      "ORDER BY MONTH(orr.createdAt)")
  List<Object[]> calculateMonthlyRevenueForYear(@Param("year") int year);
}
