package code.controller.admin;

import code.service.admin.StatService;
import java.time.LocalDateTime;
import java.util.Map;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/admin")
public class StatController {

  private StatService statService;

  public StatController(StatService statService) {
    this.statService = statService;
  }

  //  Xem toàn bộ các giao dịch
  @GetMapping("/transactions")
  public ResponseEntity<?> getTransactions(
      @RequestParam(defaultValue = "0") int page,
      @RequestParam(defaultValue = "20") int size) {
    return ResponseEntity.ok(statService.getTransactions(page, size));
  }

  //  Xem 1 giao dich cu the
  @GetMapping("/transactions/{transactionId}")
  public ResponseEntity<?> getTransactionById(
      @RequestParam(defaultValue = "0") int page,
      @RequestParam(defaultValue = "20") int size,
      @PathVariable long transactionId) {
    return ResponseEntity.ok(statService.getTransactionById(transactionId));
  }

  //  Lọc giao dịch theo thời gian
  @GetMapping("/transactions/")
  public ResponseEntity<?> getTransactionsByTime(
      @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime startDate,
      @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime endDate,
      @RequestParam(defaultValue = "0") int page,
      @RequestParam(defaultValue = "20") int size) {
    return ResponseEntity.ok(
        statService.getTransactionsBetweenDates(startDate, endDate, page, size));
  }

  //  Thống kê số người dùng, danh mục, sản phẩm, đơn hàng
  @GetMapping("/stat/overall")
  public ResponseEntity<?> statOverall() {
    return ResponseEntity.ok(
        statService.statOverall());
  }

  // Thống kê doanh thu theo khảng thười gian
  @GetMapping("/stat/revenue")
  public ResponseEntity<Long> calculateRevenue(
      @RequestParam("startDate") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime startDate,
      @RequestParam("endDate") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime endDate) {

    Long revenue = statService.calculateRevenue(startDate, endDate);
    return ResponseEntity.ok(revenue);
  }

  //  Thống kê doanh thu theo năm
  @GetMapping("/stat/monthly-revenue")
  public ResponseEntity<?> getMonthlyRevenue(@RequestParam("year") int year) {
    Map<Integer, Long> monthlyRevenue = statService.getMonthlyRevenueForYear(year);
    return ResponseEntity.ok(monthlyRevenue);
  }

  //  Thống kê sản phẩm thuê nhiều : sap xep
  @GetMapping("/products/sort")
  public ResponseEntity<?> getProductsAndSort(
      @RequestParam(defaultValue = "0") int page,
      @RequestParam(defaultValue = "10") int size) {
    return ResponseEntity.ok(this.statService.getProductDTOsAndSort(page, size));
  }

}
