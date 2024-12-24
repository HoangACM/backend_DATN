package code.model.more;

import code.model.entity.User;
import jakarta.persistence.*;
import java.time.LocalDateTime;
import java.util.UUID;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

@Entity
@Table(name="access_token")
@AllArgsConstructor
//@NoArgsConstructor
@Setter
@Getter
public class AccessToken {
  @Id
  @Column(name = "id", length = 100) // Đặt độ dài cho cột ID
  private String id;

  @CreationTimestamp
  @Column(name = "created_at",nullable = false)
  private LocalDateTime created_at;

  @UpdateTimestamp
  @Column(name = "updated_at",nullable = false)
  private LocalDateTime updated_at;

  @CreationTimestamp
  @Column(name = "expires_at",nullable = false)
  private LocalDateTime expires_at;

  @Column(name = "revoked",nullable = false)
  private boolean revoked ;

  @Column(name = "user_id",nullable = false)
  private long user_id ;

  public AccessToken() {
    this.id = UUID.randomUUID().toString().replace("-", ""); // Tạo ID ngẫu nhiên khi khởi tạo
  }
}
