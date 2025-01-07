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

  @Query(value = """
    SELECT COALESCE(SUM(rent_count), 0) AS total_rent_count
    FROM (
        SELECT COUNT(od.id) AS rent_count
        FROM product p
        JOIN product_detail pd ON p.id = pd.product_id
        JOIN order_detail od ON pd.id = od.product_detail_id
        WHERE p.category_id = :categoryId AND od.status = 8
        GROUP BY p.id
    ) AS product_rent_counts
    """, nativeQuery = true)
  Long calculateTotalRentCountForCategory(@Param("categoryId") Long categoryId);

}
