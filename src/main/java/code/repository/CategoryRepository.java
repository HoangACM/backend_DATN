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
    SELECT COUNT(od)
    FROM OrderDetail od
    JOIN od.productDetail pd
    JOIN pd.product p
    JOIN p.category c
    WHERE c.id = :categoryId AND od.status = 8
    """)
  Long calculateTotalRentCountForCategory(@Param("categoryId") Long categoryId);




}
