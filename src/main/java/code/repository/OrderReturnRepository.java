package code.repository;

import code.model.entity.OrderReturn;
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
}
