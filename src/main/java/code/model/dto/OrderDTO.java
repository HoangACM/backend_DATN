package code.model.dto;

import code.model.entity.Order;
import code.model.entity.OrderDetail;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
public class OrderDTO {
  @JsonIgnoreProperties({"orderDetails"})
  private Order order;
  @JsonIgnoreProperties({"order"})
  private List<OrderDetail> orderDetails;
}
