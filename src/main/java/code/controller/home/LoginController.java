package code.controller.home;

import code.model.request.LoginRequest;
import code.security.CustomUserDetails;
import code.security.CustomUserDetailsService;
import code.security.JwtTokenUtil;
import code.service.more.TokenService;
import org.apache.coyote.BadRequestException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/home")
public class LoginController {

  @Autowired
  private AuthenticationManager authenticationManager;

  @Autowired
  private JwtTokenUtil jwtTokenUtil;

  @Autowired
  private CustomUserDetailsService customUserDetailsService;

  @Autowired
  private TokenService tokenService;

  @PostMapping("/login")
  public ResponseEntity<Object> login(@RequestBody LoginRequest loginRequest)
      throws BadRequestException {
    //Authenticate
    try {
      Authentication authentication = authenticationManager.authenticate(
          new UsernamePasswordAuthenticationToken(
              loginRequest.getEmail(),
              loginRequest.getPassword()
          ));

//       Lấy thông tin người dùng
      CustomUserDetails customUserDetails = (CustomUserDetails) authentication.getPrincipal();
      String name = customUserDetails.getUser().getName();
      long userId = customUserDetails.getUser().getId();
      //Gen token
      String token = jwtTokenUtil.generateToken(loginRequest.getEmail(),
          customUserDetails.getUser().getRole(), name, userId);
      Map<String, String> response = new HashMap<>();
      tokenService.saveToken(token,userId);
      response.put("token", token);
      return ResponseEntity.ok(response);
    } catch (Exception ex) {
      throw new BadRequestException("Email hoặc mật khẩu không chính xác!");
    }
  }


}
