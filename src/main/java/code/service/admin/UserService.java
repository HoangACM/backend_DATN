package code.service.admin;

import code.model.entity.User;
import code.exception.NotFoundException;
import code.model.request.UpdateUserRequest;
import code.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service("adminUserService")
public class UserService {
  @Autowired
  private UserRepository userRepository;

  public UserService(UserRepository userRepository){
    this.userRepository = userRepository;
  }
//Lấy tất cả user phân trang
  public Page<User> getUsers(int page, int size) {
    Pageable pageable = PageRequest.of(page, size);
    return this.userRepository.findAll(pageable);
  }
//  Lấy user theo id
  public User getUserById(long user_id){
    return this.userRepository.findById(user_id)
        .orElseThrow(() -> new NotFoundException("Không tìm thấy user có id : " + user_id));
  }

//  Thay đổi thông tin user
  public User updateUser(UpdateUserRequest userRequest){
    return null;
  }
}
