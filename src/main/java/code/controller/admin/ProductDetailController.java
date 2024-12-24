package code.controller.admin;

import code.model.entity.ProductDetail;
import code.model.request.CreateProductDetailRequest;
import code.service.admin.ProductDetailService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController("adminProductDetailController")
@RequestMapping("/api/admin")
public class ProductDetailController {
  private ProductDetailService productDetailService;

  public ProductDetailController(ProductDetailService productDetailService){
    this.productDetailService = productDetailService;
  }

  @GetMapping("/product_details")
  public ResponseEntity<?> getProductDetails(@RequestParam(defaultValue = "0") int page,
      @RequestParam(defaultValue = "10") int size){
    return ResponseEntity.ok(this.productDetailService.getProductDetails(page,size));
  }

  @GetMapping("/product_details/{productDetailId}")
  public ResponseEntity<ProductDetail> getProductDetailById(@PathVariable long productDetailId){
    return ResponseEntity.ok(this.productDetailService.getProductDetailById(productDetailId));
  }

  @PostMapping("/product_details")
  public ResponseEntity<ProductDetail> createProductDetail(@RequestBody CreateProductDetailRequest createProductDetailRequest){
    return ResponseEntity.ok(this.productDetailService.createProductDetail(createProductDetailRequest));
  }
}
