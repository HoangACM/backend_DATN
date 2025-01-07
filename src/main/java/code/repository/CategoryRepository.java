package code.repository;

import code.model.entity.Category;
import code.model.entity.User;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface CategoryRepository extends JpaRepository<Category,Long> {
  Optional<Category> findById(long category_id) ;
  Optional<Category> findByName(String category_name) ;

  @Query("""
    SELECT COALESCE(SUM(COUNT(od)), 0)
    FROM Category c
    JOIN c.products p
    JOIN p.productDetails pd
    JOIN OrderDetail od ON pd.id = od.productDetail.id
    WHERE c.id = :categoryId AND od.status = 8
    GROUP BY c.id
    """)
  Long calculateTotalRentCountForCategory(@Param("categoryId") Long categoryId);


}
