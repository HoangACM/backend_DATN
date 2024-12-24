package code.model.request;

import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
public class LoginRequest {
  @NotBlank(message = "Email trống")
  @Email(message = "Email không đúng định dạng")
  private String email;
  @NotBlank(message = "Mật khẩu trống")
  @Size(min = 6, max = 20,message = "Mật khẩu phải chứa từ 6-20 ký tự")
  private String password;
}
