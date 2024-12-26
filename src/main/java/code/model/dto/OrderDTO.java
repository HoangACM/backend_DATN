package code.model.dto;

import code.model.entity.Order;
import code.model.entity.OrderDetail;
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
  private Order order;
  private List<OrderDetail> orderDetails;
}
