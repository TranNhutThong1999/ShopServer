package kltn.jwt;

import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.SignatureException;
import io.jsonwebtoken.UnsupportedJwtException;
import kltn.dto.ShopDTO;
import kltn.dto.UserDTO;
import kltn.security.MyShop;
import kltn.util.Constants;

@Component
public class JwtTokenProvider {
	@Autowired
	private Constants constants;

//	@Autowired
//	private Cache cache;
	
	// Tạo ra token từ chuỗi authentication
	public String generateToken(Authentication authentication) {
		MyShop myShop = (MyShop) authentication.getPrincipal();
		Date now = new Date();
		Date expiryDate = new Date(now.getTime() + constants.getJWT_Expire());
		// mã hóa token
		return constants.getPrefix()+" "+ Jwts.builder().setSubject(Long.toString(myShop.getId())).setIssuedAt(new Date()).setExpiration(expiryDate)
				.signWith(SignatureAlgorithm.HS512, constants.getJWT_Secret()).compact();
	}
	public String generateToken(ShopDTO shop) {
		Date now = new Date();
		Date expiryDate = new Date(now.getTime() + constants.getJWT_Expire());
		// mã hóa token
		return constants.getPrefix()+" "+ Jwts.builder().setSubject(Long.toString(shop.getId())).setIssuedAt(new Date()).setExpiration(expiryDate)
				.signWith(SignatureAlgorithm.HS512, constants.getJWT_Secret()).compact();
	}
	// Lấy id_user từ token đã được mã hóa
	public int getUserIdFromJWT(String token) {
		Claims claims = Jwts.parser().setSigningKey(constants.getJWT_Secret()).parseClaimsJws(token).getBody();
		return (int) Long.parseLong(claims.getSubject());
	}
	public Date getTimeExprire(String token) {
		Claims claims = Jwts.parser().setSigningKey(constants.getJWT_Secret()).parseClaimsJws(token).getBody();
		return claims.getExpiration();
	}
	// check token
	public boolean validateToken(String authToken) throws SignatureException {
		try {
			Jwts.parser().setSigningKey(constants.getJWT_Secret()).parseClaimsJws(authToken);
		//	boolean a =!cache.contains(authToken);
			return true;
		} catch (MalformedJwtException ex) {
			// logger.error("Invalid JWT token");
		} catch (ExpiredJwtException ex) {
			// logger.error("Expired JWT token");
		} catch (UnsupportedJwtException ex) {
			// logger.error("Unsupported JWT token");
		} catch (IllegalArgumentException ex) {
			// logger.error("JWT claims string is empty.");
		}
		
		return false;
	}
}
