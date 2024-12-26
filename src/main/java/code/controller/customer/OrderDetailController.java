package code.controller.customer;

import code.controller.more.WebSocketController;
import code.model.entity.OrderDetail;
import code.model.entity.OrderReturn;
import code.model.more.Notification;
import code.model.request.CreateOrderDetailRequest;
import code.security.CustomUserDetails;
import code.service.customer.OrderDetailService;
import java.util.Map;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController("customerOrderDetailController")
@RequestMapping("/api/customer")
public class OrderDetailController {

  private OrderDetailService orderDetailService;
  private WebSocketController webSocketController;

  public OrderDetailController(OrderDetailService orderDetailService,
      WebSocketController webSocketController) {
    this.orderDetailService = orderDetailService;
    this.webSocketController = webSocketController;
  }

  //Lâyys tất cả các Order
  @GetMapping("/orders")
  public ResponseEntity<?> getOrders(@AuthenticationPrincipal CustomUserDetails userDetail,
      @RequestParam(defaultValue = "0") int page,
      @RequestParam(defaultValue = "10") int size) {
    return ResponseEntity.ok(
        orderDetailService.getOrdersByUser(userDetail.getUser(), page, size));
  }
//  Xem toàn bộ sp đã đtawj thuê trong 1 đơn
  @GetMapping("/orders/{orderId}")
  public ResponseEntity<?> getOrderById(
      @AuthenticationPrincipal CustomUserDetails userDetail,
      @PathVariable long orderId,
      @RequestParam(defaultValue = "0") int page,
      @RequestParam(defaultValue = "10") int size) {
    return ResponseEntity.ok(
        orderDetailService.getOrderById(userDetail.getUser(),orderId));
  }
  //  Lấy tất cả các đơn hàng chi tiet
  @GetMapping("/orderDetails")
  public ResponseEntity<?> getOrderDetails(@AuthenticationPrincipal CustomUserDetails userDetail,
      @RequestParam(defaultValue = "0") int page,
      @RequestParam(defaultValue = "10") int size) {
    return ResponseEntity.ok(
        orderDetailService.getOrderDetailsByUser(userDetail.getUser(), page, size));
  }

  //  Lấy tất cả các đơn hàng theo trạng thái : status
  @GetMapping("/orderDetails/")
  public ResponseEntity<?> getOrderDetailsByStatus(
      @AuthenticationPrincipal CustomUserDetails userDetail,
      @RequestParam(defaultValue = "0") int page,
      @RequestParam(defaultValue = "10") int size,
      @RequestParam int status) {
    return ResponseEntity.ok(
        orderDetailService.getAllByUserAndProductDetailStatus(userDetail.getUser(), status, page,
            size));
  }

  //  Chi tiết đơn hàng
  @GetMapping("/orderDetails/{orderDetailId}")
  public ResponseEntity<?> getOrderDetailById(
      @AuthenticationPrincipal CustomUserDetails userDetail,
      @PathVariable long orderDetailId) {
    return ResponseEntity.ok(
        orderDetailService.getByUserIdAndId(userDetail.getUser(), orderDetailId));
  }

  // Tạo mới đơn hàng dựa trên : List<productDetailId>, userId,
  @PostMapping("/orderDetails")
  public ResponseEntity<?> createOrderDetail(@AuthenticationPrincipal CustomUserDetails userDetail,
      @RequestBody CreateOrderDetailRequest request) {
    return ResponseEntity.ok(orderDetailService.createOrderDetail(userDetail.getUser(), request));
  }

  //  Huy don hang
  @PutMapping("/orderDetails/{orderDetailId}")
  public ResponseEntity<?> cancelOrderDetail(@AuthenticationPrincipal CustomUserDetails userDetail,
      @PathVariable Long orderDetailId) {
    return ResponseEntity.ok(
        orderDetailService.cancelOrderDetail(userDetail.getUser(), orderDetailId));
  }

  //  Khach muon tra hang
  @PutMapping("/orderDetails/{orderDetailId}/return")
  public ResponseEntity<?> returnOrderDetail(@AuthenticationPrincipal CustomUserDetails userDetail,
      @PathVariable Long orderDetailId) {
    Map<String, Object> response = (Map<String, Object>) orderDetailService.wanToReturnOrderDetail(
        userDetail.getUser(), orderDetailId);
    OrderDetail orderDetail = (OrderDetail) response.get("orderDetail");
    Notification notification = (Notification) response.get("notification");
    webSocketController.customerWantToReturn(notification);
    return ResponseEntity.ok(orderDetail);
  }
}
