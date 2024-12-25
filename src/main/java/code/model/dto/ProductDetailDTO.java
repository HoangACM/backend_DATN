package code.model.dto;

import code.model.entity.Product;
import code.model.entity.ProductDetail;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.Column;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
public class ProductDetailDTO {
  private Product product;
  @JsonIgnoreProperties({"createdAt", "updatedAt"})
  private List<ProductDetail> productDetails;
}
