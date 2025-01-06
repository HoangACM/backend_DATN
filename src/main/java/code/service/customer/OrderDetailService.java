package code.service.customer;

import code.exception.BadRequestException;
import code.exception.NotFoundException;
import code.model.dto.OrderDTO;
import code.model.dto.ProductDTO;
import code.model.entity.Order;
import code.model.entity.OrderDetail;
import code.model.entity.OrderReturn;
import code.model.entity.Product;
import code.model.entity.ProductDetail;
import code.model.entity.User;
import code.model.more.Notification;
import code.model.request.CreateOrderDetailRequest;
import code.model.request.ProductItem;
import code.repository.NotificationRepository;
import code.repository.OrderDetailRepository;
import code.repository.OrderRepository;
import code.repository.OrderReturnRepository;
import code.repository.ProductDetailRepository;
import code.repository.UserRepository;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.springframework.beans.BeanUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.jaxb.SpringDataJaxb.OrderDto;
import org.springframework.stereotype.Service;

@Service("CustomerOrderService")
public class OrderDetailService {

  private OrderDetailRepository orderDetailRepository;
  private OrderRepository orderRepository;
  private UserRepository userRepository;
  private ProductDetailRepository productDetailRepository;
  private NotificationRepository notificationRepository;
  private OrderReturnRepository orderReturnRepository;

  public OrderDetailService(OrderDetailRepository orderDetailRepository,
      OrderReturnRepository orderReturnRepository,
      UserRepository userRepository,
      ProductDetailRepository productDetailRepository,
      OrderRepository orderRepository,
      NotificationRepository notificationRepository) {
    this.orderDetailRepository = orderDetailRepository;
    this.userRepository = userRepository;
    this.orderReturnRepository = orderReturnRepository;
    this.orderRepository = orderRepository;
    this.notificationRepository = notificationRepository;
    this.productDetailRepository = productDetailRepository;
  }

  //  Lấy tất cả các đơn hàng chi tiết (OrderDetail)
  public Page<OrderDetail> getOrderDetailsByUser(User user, int page, int size) {
    Pageable pageable = PageRequest.of(page, size);
    return orderDetailRepository.findAllByUserId(user.getId(), pageable);
  }

  public Page<OrderDTO> getOrdersByUser(User user, int page, int size) {
    Pageable pageable = PageRequest.of(page, size);
//    List<Order> orders = orderRepository.findByUserAndIsPaid(user, false);
    List<Order> orders = orderRepository.findByUser(user);
//    Tạo OrderDTO ở đây
    List<OrderDTO> orderDTOS = new ArrayList<>();
    for (Order order : orders) {
      OrderDTO orderDTO = new OrderDTO();
      orderDTO.setOrder(order);
      orderDTO.setOrderDetails(order.getOrderDetails());
      orderDTOS.add(orderDTO);
    }
    int start = (int) pageable.getOffset();
    int end = Math.min((start + pageable.getPageSize()), orderDTOS.size());
    List<OrderDTO> paginatedDTOs = orderDTOS.subList(start, end);
    // Trả về Page<ProductDTO> bằng cách sử dụng PageImpl
    return new PageImpl<>(paginatedDTOs, pageable, orderDTOS.size());
  }

  //  Xem Order theo id
  public OrderDTO getOrderById(User user, long orderId) {
    Order order = orderRepository.findById(orderId)
        .orElseThrow(() -> new NotFoundException("Không tìm thấy đơn hàng"));
    if (order.getUser().equals(user)) {
      throw new BadRequestException("Không tìm thấy đơn hàng tương ứng");
    }
    OrderDTO orderDTO = new OrderDTO();
    orderDTO.setOrder(order);
    ;
    orderDTO.setOrderDetails(order.getOrderDetails());
    return orderDTO;
  }

  //  Lấy các đơn hàng theo trạng thái
  public Page<OrderDetail> getAllByUserAndProductDetailStatus(User user, int status, int page,
      int size) {
    Pageable pageable = PageRequest.of(page, size);
    return orderDetailRepository.findAllByUserIdAndProductDetailStatus(user.getId(), status,
        pageable);
  }

  // Xem đơn hàng cụ thể có id là orderDetailId
  public OrderDetail getByUserIdAndId(User user, long orderDetailId) {
    OrderDetail orderDetail = orderDetailRepository.findByOrderDetailIdAndUserId(orderDetailId,
            user.getId())
        .orElseThrow(() -> new NotFoundException(
            "Không tìm thấy đơn hàng tương ứng "));
    return orderDetail;
  }

  //  Tạo mới đơn hàng : Tạo đối tượng order -> xửa lí List<productDetailId> và trạng thái 1 là chưa thanh toán
  public Order createOrderDetail(User user, CreateOrderDetailRequest request) {
    //    1 - Tạo Order mới
    Order order = new Order();
    order.setUser(user);
    order.setPayment(request.getPayment());
    order.setShipment(request.getShipment());
    order.setCurrentAddress(request.getCurrentAddress());
    order.setCurrentPhone(request.getCurrentPhone());
    orderRepository.save(order);
    //      Xử lý danh sách các ProductDetail x Quantity
    List<OrderDetail> orderDetails = new ArrayList<>();
    for (ProductItem productItem : request.getProductItems()) {
      long productDetailId = productItem.getProductDetailId();
      ProductDetail productDetail = productDetailRepository.findById(productDetailId)
          .orElseThrow(
              () -> new NotFoundException("Không thấy ProductDetail có id : " + productDetailId));
      OrderDetail orderDetail = new OrderDetail();
      orderDetail.setStartDate(productItem.getStartDate());
      orderDetail.setRentalDay(productItem.getRentalDay());

      orderDetail.setCurrentDeposit(productDetail.getDeposit() * productItem.getQuantity());

      orderDetail.setCurrentPrice(
          productDetail.getPrice() * productItem.getRentalDay() * productItem.getQuantity());
      //      Check số lượng đặt hàng với số trong kho
      if (productDetail.getInventory() < productItem.getQuantity()) {
        throw new BadRequestException("Số lượng quá giới hạn");
      }
      orderDetail.setQuantity(productItem.getQuantity());
      orderDetail.setCurrentCondition(productDetail.getCondition());
      orderDetail.setNote(productItem.getNote());
      orderDetail.setStatus(1);
      orderDetail.setOrder(order);
      orderDetail.setProductDetail(productDetail);
      orderDetails.add(orderDetail);
      orderDetailRepository.save(orderDetail);
    }
    return order;
  }

  public Order cancelOrder(User user, long orderId) {
    Order order = orderRepository.findById(orderId)
        .orElseThrow(() -> new NotFoundException("Không tìm thấy Order có id :" + orderId));
    for (OrderDetail orderDetail : order.getOrderDetails()) {
      orderDetail.setStatus(0);
      orderDetailRepository.save(orderDetail);
    }
    order.setCancel(true);
    return orderRepository.save(order);
  }

  //  đơn hàng ở stt4 : đã nhận được hàng và khách hàng có nhu cầu trả hàng -> chuyển status từ 4->5
  public Map<String, Object> wanToReturnOrderDetail(User user, long orderDetailId) {
    OrderDetail orderDetail = orderDetailRepository.findByOrderDetailIdAndUserId(orderDetailId,
            user.getId())
        .orElseThrow(() -> new NotFoundException(
            "Không tìm thấy đơn hàng tương ứng "));
    Notification notification = new Notification();
    if (orderDetail.getStatus() == 4) {
      orderDetail.setStatus(5);
      notification.setOrderId(orderDetailId);
      notification.setRoleReceive("admin");
      notification.setContent("Đơn hàng " + orderDetailId + " đang được người dùng muốn trả");
      notification.setUserReceiveId(0);
      notification.setStatus(false);
      notificationRepository.save(notification);
    } else {
      throw new BadRequestException("Lỗi khi thay đổi trạng thái.");
    }
    Map<String, Object> map = new HashMap<>();
    map.put("notification", notification);
    map.put("orderDetail", orderDetail);
    return map;
  }

//  Xem hóa đơn hoàn trả
  public Page<OrderReturn> getOrderReturns(User user, int page, int size){
    Pageable pageable = PageRequest.of(page, size);
    return orderReturnRepository.findAllByUserId(user.getId(), pageable);
  }

  public OrderReturn getOrderReturnById(User user,long orderReturnId){
    return orderReturnRepository.findById(orderReturnId)
        .orElseThrow(()-> new NotFoundException("Không tìm thấy OrderReturn có id : "+orderReturnId));
  }
}
