package kltn.security;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
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
	
	//add context-can bas-packet
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		Shop shop = shopRepository.findOneByUserName(username).orElseThrow(()-> new UsernameNotFoundException("username was not found")) ;
		List<GrantedAuthority> authortity = new ArrayList<GrantedAuthority>();
		MyShop myShop = new MyShop(shop.getUserName(), shop.getPassword(), true, true, true, true, authortity);
		myShop.setGmail(shop.getGmail());
		myShop.setId(shop.getId());
		myShop.setUserName(shop.getUserName());
		return myShop;
		
	}
	public UserDetails loadUserById(int id) throws UsernameNotFoundException {
		Shop shop = shopRepository.findOneById(id).orElseThrow(()-> new UsernameNotFoundException("username was not found")) ;
		List<GrantedAuthority> authortity = new ArrayList<GrantedAuthority>();
		MyShop myShop = new MyShop(shop.getUserName(), shop.getPassword(), true, true, true, true, authortity);
		myShop.setGmail(shop.getGmail());
		myShop.setId(shop.getId());
		myShop.setUserName(shop.getUserName());
		return myShop;
	}
	
}
