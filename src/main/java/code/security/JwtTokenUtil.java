package code.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import io.jsonwebtoken.security.SignatureException;
import java.util.Collections;
import java.util.List;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.beans.factory.annotation.Value;
import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import org.springframework.web.server.ResponseStatusException;

@Component
public class JwtTokenUtil {
  @Value("${SECRET_KEY}")
  private String SECRET_KEY ;
  @Value("${EXPIRATION_TIME}")
  private int EXPIRATION_TIME ;

  public String generateToken(String username, String role,String name,long userId) {
    Map<String, Object> claims = new HashMap<>();
    claims.put("roles", role);
    claims.put("name",name);
    claims.put("userId",userId);
    return createToken(claims, username);
  }

  private String createToken(Map<String, Object> claims, String subject) {
    // Chuyển SECRET_KEY thành Key sử dụng thuật toán HMAC-SHA
    Key key = Keys.hmacShaKeyFor(SECRET_KEY.getBytes());

    return Jwts.builder()
        .setClaims(claims)
        .setSubject(subject)
        .setIssuedAt(new Date(System.currentTimeMillis()))
        .setExpiration(new Date(System.currentTimeMillis() + EXPIRATION_TIME))
//        custom them o day
        .signWith(key, SignatureAlgorithm.HS256)  // Sử dụng key và thuật toán HS256
        .compact();
  }

  public boolean validateToken(String token, String username) {
    final String extractedUsername = extractUsername(token);
    return (extractedUsername.equals(username) && !isTokenExpired(token));
  }

  private boolean isTokenExpired(String token) {
    return extractExpiration(token).before(new Date());
  }

  private Date extractExpiration(String token) {
    // Chuyển đổi chuỗi SECRET_KEY thành Key
    Key key = Keys.hmacShaKeyFor(SECRET_KEY.getBytes());
    // Phân tích token
    Claims claims = Jwts.parserBuilder()
        .setSigningKey(key) // Thiết lập khóa
        .build()
        .parseClaimsJws(token) // Phân tích JWT
        .getBody();
    // Lấy thời gian hết hạn
    return claims.getExpiration();
  }

  public String extractUsername(String token) {
    try {
      // Chuyển SECRET_KEY thành Key để sử dụng với JJWT mới
      Key key = Keys.hmacShaKeyFor(SECRET_KEY.getBytes());

      // Sử dụng parserBuilder để phân tích JWT
      Claims claims = Jwts.parserBuilder()
          .setSigningKey(key)  // Cung cấp khóa bí mật
          .build()
          .parseClaimsJws(token) // Phân tích token
          .getBody();

      // Trả về subject, là email hoặc tên người dùng
      return claims.getSubject();

    } catch (ExpiredJwtException e) {
      // Nếu token hết hạn, ném ra lỗi Forbidden
      throw new ResponseStatusException(HttpStatus.FORBIDDEN, "JWT token has expired");
    } catch (MalformedJwtException e) {
      // Nếu token không hợp lệ, ném ra lỗi Forbidden
      throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Invalid JWT token");
    } catch (SignatureException e) {
      // Nếu chữ ký không hợp lệ, ném ra lỗi Forbidden
      throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Invalid JWT signature");
    } catch (JwtException e) {
      // Xử lý các lỗi JWT khác
      throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Invalid JWT token");
    }
  }

  public Claims getClaimsFromToken(String token) {
    try {
      Key key = Keys.hmacShaKeyFor(SECRET_KEY.getBytes());
      return Jwts.parserBuilder()
          .setSigningKey(key)
          .build()
          .parseClaimsJws(token)
          .getBody(); // Lấy Claims từ token
    } catch (Exception e) {
      return null; // Nếu có lỗi khi parse token, trả về null
    }
  }

  public List<String> getRolesFromToken(String token) {
    Claims claims = getClaimsFromToken(token);
    if (claims != null) {
      // Giả sử roles được lưu trong claim có key là "roles"
      return (List<String>) claims.get("roles");
    }
    return Collections.emptyList(); // Nếu không có roles, trả về list rỗng
  }


}