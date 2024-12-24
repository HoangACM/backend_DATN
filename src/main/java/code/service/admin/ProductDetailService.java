package code.service.admin;

import code.exception.ConflictException;
import code.exception.NotFoundException;
import code.model.entity.Product;
import code.model.entity.ProductDetail;
import code.model.request.CreateProductDetailRequest;
import code.repository.CategoryRepository;
import code.repository.ProductDetailRepository;
import code.repository.ProductRepository;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
public class ProductDetailService {
  private ProductDetailRepository productDetailRepository;
  private ProductRepository productRepository;

  public ProductDetailService(ProductDetailRepository productDetailRepository,ProductRepository productRepository){
    this.productDetailRepository = productDetailRepository;
    this.productRepository = productRepository;
  }

  public Page<ProductDetail> getProductDetails(int page, int size){
    Pageable pageable = PageRequest.of(page, size);
    return this.productDetailRepository.findAll(pageable);
  }

  public ProductDetail getProductDetailById(long product_id){
    return this.productDetailRepository.findById(product_id)
        .orElseThrow(() -> new NotFoundException("Không tìm thấy product_detail có id : " + product_id));
  }

  public ProductDetail createProductDetail(CreateProductDetailRequest request){
      long product_id = request.getProduct_id();
      Product product = this.productRepository.findById(product_id)
          .orElseThrow(() -> new NotFoundException("Không thấy product có id : " + product_id));
      productDetailRepository.findByProductAndTypeAndColor(product, request.getType(), request.getColor())
          .ifPresent(existing -> {
            throw new ConflictException("Đã tồn tại ProductDetail với productId: "
                + request.getProduct_id() + ", type: " + request.getType() + ", và color: " + request.getColor());
          });
      ProductDetail productDetail = new ProductDetail();
      productDetail.setProduct(product);
      productDetail.setColor(request.getColor());
      productDetail.setType(request.getType());
      productDetail.setPrice(request.getPrice());
      productDetail.setCondition(request.getCondition());
      productDetail.setInventory(request.getInventory());
      productDetail.setStatus(true);
      return productDetailRepository.save(productDetail);
    }
  }

