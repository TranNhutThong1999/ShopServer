package kltn.security;
import java.io.IOException;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import kltn.jwt.JwtTokenProvider;
import kltn.util.Constants;


public class JwtAuthenticationFilter extends OncePerRequestFilter {

	@Autowired
	private Constants constants;

	@Autowired
	private JwtTokenProvider jwt;

	@Autowired
	private CustomUserDetail customUserDetail;

	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
			throws ServletException, IOException {
		// TODO Auto-generated method stub
		String token = getTokenFromHeader(request);
		if (jwt.validateToken(token)) {
			int id = jwt.getUserIdFromJWT(token);
			UserDetails user = customUserDetail.loadUserById(id);
			if (user != null) {
				UsernamePasswordAuthenticationToken auth = new UsernamePasswordAuthenticationToken(user,null,
						user.getAuthorities());
				auth.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
				SecurityContextHolder.getContext().setAuthentication(auth);
			} else {
				System.out.println("have not token");
				SecurityContextHolder.clearContext();
			}

		}
		filterChain.doFilter(request, response);
	}

	public String getTokenFromHeader(HttpServletRequest r) {
		String key = r.getHeader(constants.getJWT_header());
		if (StringUtils.hasText(key) && key.startsWith(constants.getPrefix())) {
			return key.substring(constants.getPrefix().length());
		}
		return null;
	}
}
