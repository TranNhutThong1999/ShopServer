package kltn.util;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;


@Component
public class Constants {
//	@Value("${token.enableUser.expire}")
//	private int timeTokenExpire;
	
	@Value("${security.jwt.secret}")
	private String JWT_Secret;
	
	@Value("${security.jwt.expire}")
	private int JWT_Expire ;
	
	@Value("${security.jwt.header}")
    private String JWT_header;

	@Value("${security.jwt.prefix}")
    private String prefix;

	public String getJWT_Secret() {
		return JWT_Secret;
	}

	public void setJWT_Secret(String jWT_Secret) {
		JWT_Secret = jWT_Secret;
	}

	public int getJWT_Expire() {
		return JWT_Expire;
	}

	public void setJWT_Expire(int jWT_Expire) {
		JWT_Expire = jWT_Expire;
	}

//
//	public int getTimeTokenExpire() {
//		return timeTokenExpire;
//	}
//
//	public void setTimeTokenExpire(int timeTokenExpire) {
//		this.timeTokenExpire = timeTokenExpire;
//	}

	public String getJWT_header() {
		return JWT_header;
	}

	public void setJWT_header(String jWT_header) {
		JWT_header = jWT_header;
	}

	public String getPrefix() {
		return prefix;
	}

	public void setPrefix(String prefix) {
		this.prefix = prefix;
	}

  
}
