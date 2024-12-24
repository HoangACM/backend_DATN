package code.repository;

import code.model.entity.Product;
import code.model.entity.ProductDetail;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface ProductDetailRepository extends JpaRepository<ProductDetail,Long> {
  Optional<ProductDetail> findByProductAndTypeAndColor(Product product, String type, String color);

  @Query("SELECT pd FROM ProductDetail pd WHERE pd.product.id = :productId")
  List<ProductDetail> findByProductId(@Param("productId") long productId);
}
