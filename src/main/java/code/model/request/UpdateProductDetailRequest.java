package code.model.request;

import jakarta.persistence.Column;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
public class UpdateProductDetailRequest {
  private String color;
  private String type;
  private long price;
  private long deposit;
  private String condition;
  private long inventory;
  private boolean status;
}
