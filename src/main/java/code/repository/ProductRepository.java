package code.repository;

import code.model.entity.Category;
import code.model.entity.Product;
import code.model.entity.ProductDetail;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface ProductRepository extends JpaRepository<Product, Long> {

  Optional<Product> findByName(String name);

  Optional<Product> findBySlug(String slug);

  boolean existsBySlug(String slug);

  List<Product> findByCategory(Category category);

  List<Product> findByNameContainingIgnoreCase(String name);

  List<Product> findByCategoryAndBrand(Category category, String brand);

  //  Tinh so luot thue
  @Query("SELECT COUNT(od) FROM OrderDetail od WHERE od.productDetail IN :productDetails AND od.status = 8")
  Long totalHired(@Param("productDetails") List<ProductDetail> productDetails);

  @Query("""
    SELECT p FROM Product p 
    LEFT JOIN p.productDetails pd 
    LEFT JOIN OrderDetail od ON pd.id = od.productDetail.id AND od.status = 8
    GROUP BY p.id 
    ORDER BY COUNT(od.id) DESC
    """)
  Page<Product> findProductsSortedByRentCount(Pageable pageable);


}
