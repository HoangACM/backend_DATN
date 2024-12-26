package code.model.request;

import code.model.entity.ProductDetail;
import java.time.LocalDate;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
public class ProductItem {
  private long productDetailId;
  private long quantity;
  private int rentalDay;
  private String note;
  private LocalDate startDate;
}
