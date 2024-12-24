package code.model.entity;
import jakarta.persistence.*;
import java.time.LocalDateTime;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

@Entity
@Table(name="reviews")
@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
public class Review {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Column(name = "star", nullable = false)
  private int star;

  @Column(name = "comment", nullable = false)
  private String comment;

  @Column(name = "created_at",nullable = false)
  @CreationTimestamp
  private LocalDateTime createdAt;

  @UpdateTimestamp
  @Column(name = "updated_at",nullable = false)
  private LocalDateTime updatedAt;

  @ManyToOne
  @JoinColumn(name = "user_id",nullable = false, foreignKey = @ForeignKey(name = "FK_USER_REVIEW"))
  private User user;

  @ManyToOne
  @JoinColumn(name = "product_detail_id",nullable = false, foreignKey = @ForeignKey(name = "FK_PRODUCT-DETAIL_REVIEW"))
  private ProductDetail productDetail;


}
