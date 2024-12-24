package code.controller.admin;

import code.model.entity.User;
import code.model.request.UpdateUserRequest;
import code.service.admin.*;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/admin")
public class UserController {
  private UserService userService;

  public UserController(UserService userService){
    this.userService = userService;
  }

  @GetMapping("/users")
  public ResponseEntity<Page<User>> getUsers(@RequestParam(defaultValue = "0") int page,
      @RequestParam(defaultValue = "10") int size){
    return ResponseEntity.ok(this.userService.getUsers(page,size));
  }

  @GetMapping("/users/{user_id}")
  public ResponseEntity<User> getUserByUserId(@PathVariable long user_id){
    return ResponseEntity.ok(this.userService.getUserById(user_id));
  }

  @PutMapping("/users/{user_id}")
  public ResponseEntity<String> updateUser(@PathVariable long user_id,@RequestBody
      UpdateUserRequest updateUserRequest){
    return ResponseEntity.ok("OK");
  }
}
