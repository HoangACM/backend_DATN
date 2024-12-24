package code.service.customer;

import code.model.entity.Category;
import code.repository.CategoryRepository;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service("CustomerCategoryService")
public class CategoryService {
  private CategoryRepository categoryRepository;
  public CategoryService(CategoryRepository categoryRepository){
    this.categoryRepository = categoryRepository;
  }
  public Page<Category> getCategories(int page, int size) {
    Pageable pageable = PageRequest.of(page, size);
    return this.categoryRepository.findAll(pageable);
  }
}
