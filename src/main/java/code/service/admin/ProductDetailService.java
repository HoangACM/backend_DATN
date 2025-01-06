package code.service.admin;

import code.exception.ConflictException;
import code.exception.NotFoundException;
import code.model.entity.Product;
import code.model.entity.ProductDetail;
import code.model.more.Image;
import code.model.request.CreateProductDetailRequest;
import code.model.request.UpdateProductDetailRequest;
import code.repository.ImageRepository;
import code.repository.ProductDetailRepository;
import code.repository.ProductRepository;
import com.cloudinary.Cloudinary;
import com.cloudinary.Transformation;
import com.cloudinary.utils.ObjectUtils;
import java.io.IOException;
import java.util.Map;
import org.springframework.beans.BeanUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
public class ProductDetailService {

  private ProductDetailRepository productDetailRepository;
  private ProductRepository productRepository;
  private ImageRepository imageRepository;

  private final Cloudinary cloudinary;

  public ProductDetailService(ProductDetailRepository productDetailRepository,
      ProductRepository productRepository,ImageRepository imageRepository, Cloudinary cloudinary) {
    this.productDetailRepository = productDetailRepository;
    this.productRepository = productRepository;
    this.imageRepository = imageRepository;
    this.cloudinary = cloudinary;
  }

  public Page<ProductDetail> getProductDetails(int page, int size) {
    Pageable pageable = PageRequest.of(page, size);
    return this.productDetailRepository.findAll(pageable);
  }

  public ProductDetail getProductDetailById(long product_id) {
    return this.productDetailRepository.findById(product_id)
        .orElseThrow(
            () -> new NotFoundException("Không tìm thấy product_detail có id : " + product_id));
  }

  public ProductDetail createProductDetail(CreateProductDetailRequest request) {
    long product_id = request.getProduct_id();
    Product product = this.productRepository.findById(product_id)
        .orElseThrow(() -> new NotFoundException("Không thấy product có id : " + product_id));
    productDetailRepository.findByProductAndTypeAndColor(product, request.getType(),
            request.getColor())
        .ifPresent(existing -> {
          throw new ConflictException("Đã tồn tại ProductDetail với productId: "
              + request.getProduct_id() + ", type: " + request.getType() + ", và color: "
              + request.getColor());
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

  public ProductDetail updateProductDetail(UpdateProductDetailRequest request,
      long productDetailId) {
    try {
      ProductDetail productDetail = productDetailRepository.findById(productDetailId)
          .orElseThrow(() -> new NotFoundException(
              "Không tìm thấy ProductDetail có id : " + productDetailId));
      BeanUtils.copyProperties(request, productDetail);
      return productDetailRepository.save(productDetail);
    } catch (org.springframework.dao.DataIntegrityViolationException e) {
      throw new ConflictException("Đã tồn tại Type và Color");
    } catch (NotFoundException e) {
      throw e; // Giữ lại ngoại lệ này vì nó đã được định nghĩa đúng
    }
  }

  //  Thêm ảnh cho biến thể
  public ProductDetail updateImageProductDetail(MultipartFile file,long productDetailId)
      throws IOException {
    ProductDetail productDetail = productDetailRepository.findById(productDetailId)
        .orElseThrow(() -> new NotFoundException(
            "Không tìm thấy ProductDetail có id : " + productDetailId));
    if(productDetail.getImage() == null){
      Image image = new Image();
      imageRepository.save(image);
      Map<String, Object> options = ObjectUtils.asMap(
          "public_id", String.valueOf(image.getId()),      // Đặt public ID cho ảnh
          "tags", "product_detail",// Thêm các tag (danh sách thẻ)
          "transformation", new Transformation().width(512).height(512)
              .crop("pad").quality(100)
      );
      Map<?, ?> uploadResult = cloudinary.uploader().upload(file.getBytes(), options);
      image.setUrl(uploadResult.get("url").toString());

      image.setProductDetail(productDetail);;
      imageRepository.save(image);
      productDetail.setImage(image);
    }
    else{
      Image image = productDetail.getImage();
      Map<String, Object> options = ObjectUtils.asMap(
          "public_id", String.valueOf(image.getId()),      // Đặt public ID cho ảnh
          "tags", "product_detail",// Thêm các tag (danh sách thẻ)
          "transformation", new Transformation().width(512).height(512)
              .crop("pad").quality(100)
      );
      Map<?, ?> uploadResult = cloudinary.uploader().upload(file.getBytes(), options);
      image.setUrl(uploadResult.get("url").toString());
      imageRepository.save(image);
      productDetail.setImage(image);
    }
    return productDetailRepository.save(productDetail);
  }
}

