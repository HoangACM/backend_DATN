package code.model.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class CustomerInfoDTO {
  private long id;
  private String  name;
  private String email;
  private String  phone;
  private boolean  status;
}
