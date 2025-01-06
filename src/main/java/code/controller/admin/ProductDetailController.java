package code.controller.admin;

import code.model.entity.ProductDetail;
import code.model.request.CreateProductDetailRequest;
import code.model.request.UpdateProductDetailRequest;
import code.service.admin.ProductDetailService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


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

//  @PostMapping("/products/{productId}/product_details")
//  public ResponseEntity<ProductDetail> createProductDetail1(@RequestBody CreateProductDetailRequest createProductDetailRequest){
//    return ResponseEntity.ok(this.productDetailService.createProductDetail(createProductDetailRequest));
//  }

  @PutMapping("/product_details/{productDetailId}")
  public ResponseEntity<ProductDetail> updateProductDetailById(@PathVariable long productDetailId,
      @RequestBody UpdateProductDetailRequest request){
    return ResponseEntity.ok(this.productDetailService.updateProductDetail(request,productDetailId));
  }
}
