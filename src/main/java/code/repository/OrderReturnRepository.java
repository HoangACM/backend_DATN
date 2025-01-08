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

  @Query(value = """
    WITH months AS (
        SELECT 1 AS month UNION ALL
        SELECT 2 UNION ALL
        SELECT 3 UNION ALL
        SELECT 4 UNION ALL
        SELECT 5 UNION ALL
        SELECT 6 UNION ALL
        SELECT 7 UNION ALL
        SELECT 8 UNION ALL
        SELECT 9 UNION ALL
        SELECT 10 UNION ALL
        SELECT 11 UNION ALL
        SELECT 12
    )
    SELECT 
        m.month, 
        COALESCE(SUM(od.currentPrice - orr.damageOrLossFee - orr.overdueFee), 0) AS total
    FROM 
        months m
    LEFT JOIN OrderReturn orr ON MONTH(orr.createdAt) = m.month AND YEAR(orr.createdAt) = :year AND orr.isPaid = true
    LEFT JOIN orr.orderDetail od ON od.id = orr.orderDetail_id
    GROUP BY 
        m.month
    ORDER BY 
        m.month
    """, nativeQuery = true)
  List<Object[]> calculateMonthlyRevenueForYear(@Param("year") int year);

}
