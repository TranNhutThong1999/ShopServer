package kltn.security;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

import kltn.entity.Shop;
import kltn.repository.ShopRepository;

@Component
public class CustomUserDetail implements UserDetailsService{
	@Autowired
	private ShopRepository shopRepository;
	
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		Shop shop = shopRepository.findOneByEmail(username).orElseThrow(()-> new UsernameNotFoundException("email was not found")) ;
		List<GrantedAuthority> authortity = new ArrayList<GrantedAuthority>();
		MyShop myShop = new MyShop(shop.getEmail(), shop.getPassword(), shop.isEnable(), true, true, true, authortity);
		myShop.setId(shop.getId());
		return myShop;
		
	}
	public UserDetails loadUserById(String id) throws UsernameNotFoundException {
		Shop shop = shopRepository.findOneById(id).orElseThrow(()-> new UsernameNotFoundException("id was not found")) ;
		List<GrantedAuthority> authortity = new ArrayList<GrantedAuthority>();
		MyShop myShop = new MyShop(shop.getEmail(), shop.getPassword(),  shop.isEnable(), true, true, true, authortity);
		myShop.setId(shop.getId());
		return myShop;
	}
	
}
