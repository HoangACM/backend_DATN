package code.model.dto;

import code.model.entity.Category;
<<<<<<< HEAD
import code.model.more.Image;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.Column;
import jakarta.persistence.Lob;
import java.util.List;
=======
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.Column;
import jakarta.persistence.Lob;
>>>>>>> e95ac2416cb78d52425e90160251882723f7602a
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
public class ProductDTO {
  private Long id;
  private String name;
  private String brand;
  private String description;
  private String slug;
  @JsonIgnoreProperties({"createdAt", "updatedAt"})
  private Category category;
  private long hired;
  private float star;
  private long minPrice = 0;
  private long maxPrice = 0;
<<<<<<< HEAD
  private List<Image> image;
  private Image thumbnail;
=======
>>>>>>> e95ac2416cb78d52425e90160251882723f7602a
}
