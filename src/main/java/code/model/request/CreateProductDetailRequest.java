package code.model.request;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
public class CreateProductDetailRequest {
  private String color;
  private String type;
  private long price;
  private String condition;
  private long inventory;
//  private long product_id;
}
