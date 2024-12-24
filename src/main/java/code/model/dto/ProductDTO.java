package code.model.dto;

import code.model.entity.Category;
import code.model.entity.Product;
import code.model.more.Image;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.Column;
import jakarta.persistence.Lob;
import java.util.List;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.Column;
import jakarta.persistence.Lob;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
public class ProductDTO {
  @JsonIgnoreProperties({"createdAt", "updatedAt","category","productDetails"})
  private Product product;
  @JsonIgnoreProperties({"createdAt", "updatedAt"})
  private Category category;
  private long hired;
  private float star;
  private long minPrice;
  private long maxPrice;

}
