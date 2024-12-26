package code.model.request;

import java.time.LocalDate;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
public class CreateOrderDetailRequest {
  private  List<ProductItem> productItems;
  private String currentAddress;
  private String currentPhone;
  private String payment;
  private String shipment;
}
