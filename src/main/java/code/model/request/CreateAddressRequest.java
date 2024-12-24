package code.model.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
public class CreateAddressRequest {
  private int cityCode;
  private int districtCode;
  private int wardCode;
  private String phoneAddress;
  private String detail;
  private boolean defaultAddress;
}
