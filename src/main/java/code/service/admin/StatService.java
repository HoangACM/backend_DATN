package code.service.admin;

import code.exception.NotFoundException;
import code.model.dto.ProductDTO;
import code.model.entity.Category;
import code.model.entity.Product;
import code.model.entity.ProductDetail;
import code.model.more.Transaction;
import code.repository.CategoryRepository;
import code.repository.OrderRepository;
import code.repository.OrderReturnRepository;
import code.repository.ProductRepository;
import code.repository.TransactionRepository;
import code.repository.UserRepository;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
public class StatService {

  private TransactionRepository transactionRepository;
  private UserRepository userRepository;
  private CategoryRepository categoryRepository;
  private OrderRepository orderRepository;
  private ProductRepository productRepository;
  private OrderReturnRepository orderReturnRepository;

  public StatService(TransactionRepository transactionRepository,
      OrderReturnRepository orderReturnRepository,
      CategoryRepository categoryRepository,
      OrderRepository orderRepository,
      ProductRepository productRepository,
      UserRepository userRepository) {
    this.transactionRepository = transactionRepository;
    this.userRepository = userRepository;
    this.categoryRepository = categoryRepository;
    this.orderRepository = orderRepository;
    this.productRepository = productRepository;
    this.orderReturnRepository = orderReturnRepository;
  }

  //  Lấy toàn bộ các giao dịch
  public Page<Transaction> getTransactions(int page, int size) {
    Pageable pageable = PageRequest.of(page, size);
    return transactionRepository.findAll(pageable);
  }

  //  Xem chi tiết giao dịch
  public Transaction getTransactionById(long transactionId) {
    return transactionRepository.findById(transactionId)
        .orElseThrow(() -> new NotFoundException("Không tìm thấy giao dịch tương ứng"));
  }

  // Tìm các giao dịch trong khoảng thời gian
  public Page<Transaction> getTransactionsBetweenDates(LocalDateTime startDate,
      LocalDateTime endDate, int page, int size) {
    Pageable pageable = PageRequest.of(page, size);
    return transactionRepository.findByTransactionDateBetween(startDate, endDate, pageable);
  }

  //  Thống kê : số lượng tài khoản, số lượng danh mục, số lượng mặt hàng(Product), số lượng đơn hàng(Order)
  public Map<String, Long> statOverall() {
    long totalUsers = userRepository.count();
    long totalCategories = categoryRepository.count();
    long totalProducts = productRepository.count();
    long totalOrders = orderRepository.count();
    Map<String, Long> map = new HashMap<>();
    map.put("totalCategories", totalCategories);
    map.put("totalUsers", totalUsers);
    map.put("totalProducts", totalProducts);
    map.put("totalOrders", totalOrders);
    return map;
  }

  //  Thống kê doanh thu theo khoảng thười gian dựa trên hóa đơn OrderDetail
  public Long calculateRevenue(LocalDateTime startDate, LocalDateTime endDate) {
    return orderReturnRepository.calculateTotalAmountInDateRange(startDate, endDate);
  }

  //  Thống kê doanh thu theo năm
  public Map<Integer, Long> getMonthlyRevenueForYear(int year) {
    // Lấy dữ liệu thô từ repository
    List<Object[]> rawResults = orderReturnRepository.calculateMonthlyRevenueForYear(year);

    // Xử lý dữ liệu thô thành Map
    Map<Integer, Long> monthlyRevenue = new HashMap<>();
    for (Object[] result : rawResults) {
      Integer month = (Integer) result[0]; // Tháng
      Long total = (Long) result[1];       // Tổng doanh thu
      monthlyRevenue.put(month, total);
    }
    return monthlyRevenue;
  }
//Thong ke san pham ban chay
  private List<ProductDTO> convert(List<Product> products) {
    List<ProductDTO> productDTOs = new ArrayList<>();
    for (Product product : products) {
      ProductDTO productDTO = new ProductDTO();
      productDTO.setProduct(product);
//      System.out.println("da co" + productDTO.getProduct().getId());
      productDTO.setCategory(product.getCategory());

//     set giá thuê min-max
      for (ProductDetail productDetail : product.getProductDetails()) {
        if (productDTO.getMaxPrice() < productDetail.getPrice()) {
          productDTO.setMaxPrice(productDetail.getPrice());
        }
        if (productDTO.getMinPrice() == 0) {
          productDTO.setMinPrice(productDetail.getPrice());
        }
        if (productDTO.getMinPrice() > productDetail.getPrice()) {
          productDTO.setMinPrice(productDetail.getPrice());
        }
      }
//      Tính số lượt đã thuê
      productDTO.setHired(productRepository.totalHired(product.getProductDetails()));

      productDTOs.add(productDTO);
    }
    return productDTOs;
  }

  public Page<ProductDTO> getProductDTOsAndSort(int page, int size) {
    // Tạo Pageable
    Pageable pageable = PageRequest.of(page, size);

    // Chuyển đổi Product entities sang ProductDTO
    List<ProductDTO> productDTOs = this.convert(productRepository.findAll());

    // Sắp xếp danh sách ProductDTO theo thuộc tính `hired` (giảm dần)
    productDTOs.sort(Comparator.comparingLong(ProductDTO::getHired).reversed());

    // Áp dụng phân trang
    int start = (int) pageable.getOffset();
    int end = Math.min((start + pageable.getPageSize()), productDTOs.size());
    List<ProductDTO> paginatedDTOs = productDTOs.subList(start, end);

    // Trả về Page<ProductDTO>
    return new PageImpl<>(paginatedDTOs, pageable, productDTOs.size());
  }

//  Thong ke ban chay theo danh muc cua tat ca thoi gian
  public Map<Map,Long> statHiredByCategory(){
    Map<Map,Long> data = new HashMap<>();
    for(Category category : categoryRepository.findAll()){
      Map<Long,String> categoryDTO = new HashMap<>();
      categoryDTO.put(category.getId(),category.getName());
      data.put(categoryDTO,categoryRepository.calculateTotalRentCountForCategory(category.getId()));
    }
    return data;
  }
}
