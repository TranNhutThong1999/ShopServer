package kltn.util;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import lombok.Data;


@Data
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

	@Value("${upload.file.rootURL}")
	public String rootURL;
	
	@Value("${upload.file.url.show}")
	public String showImage;
}
