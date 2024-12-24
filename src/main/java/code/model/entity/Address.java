package code.model.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;
@Entity
@Table(name="addresses")
@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
public class Address {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Column(name = "city_code", nullable = false)
  private int cityCode;

  @Column(name = "district_code", nullable = false)
  private int districtCode;

  @Column(name = "ward_code", nullable = false)
  private int wardCode;

  @Column(name = "detail", length = 255, nullable = false)
  private String detail;

  @Column(name = "phone_address", length = 20, nullable = false)
  private String phoneAddress;

  @Column(name = "defaultAddress", nullable = false)
  private boolean defaultAddress = false;

  @JsonIgnore
  @ManyToOne
  @JoinColumn(name = "user_id",nullable = false, foreignKey = @ForeignKey(name = "FK_USER_ADDRESS"))
  private User user;
}
