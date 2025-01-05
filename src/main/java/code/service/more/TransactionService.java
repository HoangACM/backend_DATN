package code.service.more;

import code.exception.BadRequestException;
import code.exception.NotFoundException;
import code.model.entity.Order;
import code.model.entity.OrderDetail;
import code.model.entity.OrderReturn;
import code.model.more.Notification;
import code.model.more.Transaction;
import code.model.request.WebHookRequest;
import code.repository.NotificationRepository;
import code.repository.OrderDetailRepository;
import code.repository.OrderRepository;
import code.repository.OrderReturnRepository;
import code.repository.TransactionRepository;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

@Service
public class TransactionService {

  private TransactionRepository transactionRepository;
  private OrderDetailRepository orderDetailRepository;
  private OrderRepository orderRepository;
  private OrderReturnRepository orderReturnRepository;
  private NotificationRepository notificationRepository;

  public TransactionService(TransactionRepository transactionRepository,
      OrderDetailRepository orderDetailRepository,
      OrderRepository orderRepository,
      NotificationRepository notificationRepository,
      OrderReturnRepository orderReturnRepository) {
    this.transactionRepository = transactionRepository;
    this.orderDetailRepository = orderDetailRepository;
    this.orderRepository = orderRepository;
    this.notificationRepository = notificationRepository;
    this.orderReturnRepository = orderReturnRepository;
  }

  //  lấy giá trị webhook lưu vào db
  public Map<String, Object> addTransactionFromWebHook(WebHookRequest request) {
    Notification notification = new Notification();
    Transaction transaction = new Transaction();
    Map<String, Object> map = new HashMap<>();
    BeanUtils.copyProperties(request, transaction);
    transaction.setIdSepay(request.getId());
    transaction.setId(null);
    transactionRepository.save(transaction);
//    Thay doi trang thai đơn hàng : 1 -> 2
//    Lay thong tin tu noi dung chuyen khoan
//    Thanh toan don hang(*******SEVQRKH****DH***)
//    Nop phu phi qua han, mat, hong san pham(*******SEVQR_02_makhachhang_madonhang)
    System.out.println(transaction.getContent());
    String regex = ".*SEVQR(\\d+)KH(\\d+)DH(\\d+)";
    Pattern pattern = Pattern.compile(regex);
    Matcher matcher = pattern.matcher(transaction.getContent());

    if (matcher.find()) {
      long typePayment = Long.parseLong(matcher.group(1));
//    Neu ma la ******SEVQR_01_****** thi la chuyen khoan thanh toan don hang
      if (typePayment == 1) {
        long totalPayment = 0;
        long customerId = Long.parseLong(matcher.group(2));
        long orderId = Long.parseLong(matcher.group(3));
        Order order = orderRepository.findById(orderId)
            .orElseThrow(() -> new NotFoundException("Không tìm thấy Order có id : " + orderId));
        for (OrderDetail orderDetail : order.getOrderDetails()) {
          totalPayment +=
              orderDetail.getCurrentPrice();
        }
        if (totalPayment != request.getTransferAmount()) {
          throw new BadRequestException(
              "Số tiền giao dịch không khớp. Không thể chuyển trạng thái đơn hàng");
        }
        order.setPaid(true);
        List<OrderDetail> orderDetails = orderDetailRepository.findByOrder(order);
        for (OrderDetail orderDetail : orderDetails) {
          orderDetail.setStatus(2);
          orderDetailRepository.save(orderDetail);
        }
        notification.setOrderId(orderId);
        notification.setRoleReceive("admin");
        notification.setContent("Đơn hàng " + orderId + " đã được khách hàng thanh toán");
        notification.setUserReceiveId(0);
        notification.setStatus(false);
        notificationRepository.save(notification);
        map.put("notification", notification);
        map.put("transaction", transaction);
        return map;

      }
//      if (typePayment == 2) {
//        long customerId = Long.parseLong(matcher.group(2));
//        long orderDetailId = Long.parseLong(matcher.group(3));
//        OrderDetail orderDetail = orderDetailRepository.findById(orderDetailId)
//            .orElseThrow(
//                () -> new NotFoundException("Không tìm thấy OrderDetail có id : " + orderDetailId));
//        orderDetail.setStatus(8);
//        OrderReturn orderReturn = orderDetail.getOrderReturn();
//        orderReturn.setPaid(true);
//        orderDetailRepository.save(orderDetail);
//        orderReturnRepository.save(orderReturn);
//
//        notification.setOrderId(orderDetailId);
//        notification.setRoleReceive("admin");
//        notification.setContent("Đơn hàng " + orderDetailId + " đã được hoàn tiênf cho khách");
//        notification.setUserReceiveId(0);
//        notification.setStatus(false);
//        notificationRepository.save(notification);
//        map.put("notification", notification);
//        map.put("transaction", transaction);
//        return map;
//
//      }
    } else {
      System.out.println("No match found");
    }
    return null;
  }
//  mở QR
//  ng dùng quét -> lưu đc giao dịch vào db -> socket gửi về fe để xác nhận thành công ->đóng QR
//  -> chuyển đến orders

//  Mã nội dung ck : yêu cầu chứa "SEVQR" : SEVQR + Mã KH + Mã Order
//diện thoại + máy chiếu
//  Tạo 1 Order -> orderDetail
}
