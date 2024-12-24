package code.controller.customer;

import code.model.entity.Category;
import code.service.customer.CategoryService;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController("CustomerCategoryController")
@RequestMapping("/api/customer")
public class CategoryController {
  private CategoryService categoryService;

  public CategoryController(CategoryService categoryService){
    this.categoryService = categoryService;
  }

  @GetMapping("/categories")
  @JsonIgnoreProperties({"createdAt", "updatedAt"})
  public ResponseEntity<Page<Category>> getCategories(){
    return ResponseEntity.ok(this.categoryService.getCategories(0,10));
  }
}
