package code.controller.customer;

import code.model.entity.Address;
import code.model.dto.CustomerInfoDTO;
import code.model.entity.User;
import code.model.request.ChangePasswordRequest;
import code.model.request.CreateAddressRequest;
import code.model.request.UpdateCustomerRequest;
import code.security.CustomUserDetails;
import code.service.customer.AddressService;
import code.service.customer.UserService;
import code.service.more.TokenService;
import java.util.List;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/customer")
public class InformationController {

  private UserService userService;
  private AddressService addressService;
  private TokenService tokenService;

  public InformationController(UserService userService, AddressService addressService,
      TokenService tokenService) {
    this.userService = userService;
    this.tokenService = tokenService;
    this.addressService = addressService;
  }

  @PostMapping("/logout")
  public ResponseEntity<?> logout(@RequestParam String token) {
    return ResponseEntity.ok(tokenService.revokeToken(token));
  }

  @GetMapping("/profile")
  public ResponseEntity<?> getCustomerInfo(@AuthenticationPrincipal CustomUserDetails userDetail) {
    CustomerInfoDTO customerInfoDTO = userService.getCustomerInfo(userDetail.getUser().getId());
    return ResponseEntity.ok(customerInfoDTO);
  }

  @PutMapping("/profile")
  public ResponseEntity<?> updateCustomerInfo(@AuthenticationPrincipal CustomUserDetails userDetail,
      @RequestBody UpdateCustomerRequest request) {
    User user = userDetail.getUser();
    CustomerInfoDTO customerInfoDTO = userService.updateCustomerInfo(request, user);
    return ResponseEntity.ok(customerInfoDTO);
  }

  @PutMapping("/profile/password")
  public ResponseEntity<?> changePassword(@AuthenticationPrincipal CustomUserDetails userDetail,
      @RequestBody ChangePasswordRequest changePasswordRequest) {
    User user = userDetail.getUser();
    String status = userService.changePassword(changePasswordRequest, user);
    return ResponseEntity.ok(status);
  }

  @GetMapping("/addresses")
  public ResponseEntity<?> getAddress(@AuthenticationPrincipal CustomUserDetails userDetail) {
    List<Address> addresses = addressService.getAddressesByUserId(userDetail.getUser());
    return ResponseEntity.ok(addresses);
  }

  @GetMapping("/addresses/{addressId}")
  public ResponseEntity<?> getAddressByAddressId(
      @AuthenticationPrincipal CustomUserDetails userDetail, @PathVariable long addressId) {
    return ResponseEntity.ok(addressService.getAddressById(userDetail.getUser(), addressId));
  }

  @PutMapping("/addresses/{addressId}")
  public ResponseEntity<?> updateAddressByAddressId(
      @AuthenticationPrincipal CustomUserDetails userDetail,
      @PathVariable long addressId, @RequestBody CreateAddressRequest request) {
    return ResponseEntity.ok(
        addressService.updateAddress(userDetail.getUser(), addressId, request));
  }

  @PostMapping("/addresses")
  public ResponseEntity<?> updateAddress(@AuthenticationPrincipal CustomUserDetails userDetail,
      @RequestBody CreateAddressRequest request) {
    return ResponseEntity.ok(addressService.createAddress(userDetail.getUser(), request));
  }
}
