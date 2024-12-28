package code.repository;

import code.model.more.Transaction;
import java.time.LocalDateTime;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface TransactionRepository extends JpaRepository<Transaction, Long> {

  Page<Transaction> findByTransactionDateBetween(LocalDateTime startDate, LocalDateTime endDate,
      Pageable pageable);

  @Query("SELECT SUM(t.transferAmount) " +
      "FROM Transaction t " +
      "WHERE FUNCTION('MONTH', t.transactionDate) = :month " +
      "AND FUNCTION('YEAR', t.transactionDate) = :year " +
      "AND t.content LIKE CONCAT('%SEVQR_0', :x, '_%')")
  Long getTotalAmountByMonthYearAndX(@Param("month") int month,
      @Param("year") int year,
      @Param("x") int x);

  @Query("SELECT SUM(t.transferAmount) FROM Transaction t " +
      "JOIN Order o ON o.id = CAST(SUBSTRING(t.content, " +
      "LOCATE('DH', t.content) + 2, LENGTH(t.content)) AS LONG) " + // Trích xuất orderId từ content
      "JOIN o.orderDetails od " +
      "JOIN od.productDetail pd " +
      "JOIN pd.product p " +
      "JOIN p.category c " +
      "WHERE c.id = :categoryId " +
      "AND t.transactionDate BETWEEN :startDate AND :endDate")
  Long findTotalTransferAmountByCategoryAndDateRange(
      Long categoryId, LocalDateTime startDate, LocalDateTime endDate);

  @Query("""
        SELECT 
            COALESCE(SUM(CASE WHEN t.transferType = 'in' THEN t.transferAmount ELSE 0 END), 0) 
            - 
            COALESCE(SUM(CASE WHEN t.transferType = 'out' THEN t.transferAmount ELSE 0 END), 0) 
        FROM Transaction t 
        WHERE t.transactionDate BETWEEN :startDate AND :endDate
    """)
  Long calculateRevenueBetweenDates(
      @Param("startDate") LocalDateTime startDate,
      @Param("endDate") LocalDateTime endDate
  );

}
