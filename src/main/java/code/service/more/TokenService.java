package code.service.more;
import code.exception.NotFoundException;
import code.model.more.AccessToken;
import code.repository.TokenRepository;
import java.time.LocalDateTime;
import org.springframework.stereotype.Service;

@Service
public class TokenService {

  private TokenRepository tokenRepository;

  public TokenService(TokenRepository tokenRepository){
    this.tokenRepository = tokenRepository;
  }

  public AccessToken saveToken(String tokenString,long userId){
    AccessToken token = new AccessToken();
    token.setId(tokenString);
    token.setUser_id(userId);
    token.setRevoked(false);
    token.setExpires_at(LocalDateTime.now().plusDays(1));
    return tokenRepository.save(token);
  }

  public AccessToken revokeToken(String tokenString){
    AccessToken accessToken = tokenRepository.findById(tokenString)
        .orElseThrow(()-> new NotFoundException("Không tìm thấy token tương ứng"));
    accessToken.setRevoked(true);
    return tokenRepository.save(accessToken);
  }

  public boolean checkValid(String tokenString){
    AccessToken accessToken = tokenRepository.findById(tokenString)
        .orElseThrow(()-> new NotFoundException("Không tìm thấy token tương ứng"));
    if(accessToken.isRevoked() == false){
      return true;
    }
    return false;
  }
}
