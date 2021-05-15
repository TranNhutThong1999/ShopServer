package kltn.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import kltn.converter.ShopConverter;
import kltn.dto.ShopDTO;
import kltn.entity.Shop;
import kltn.repository.RoleRepository;
import kltn.repository.ShopRepository;
import kltn.service.IShopService;

@Service
public class ShopService implements IShopService{
	
	@Autowired
	private ShopConverter shopConverter;
	
	@Autowired
	private ShopRepository shopRepository;
	
	@Autowired
	private RoleRepository roleRepository;
	
	@Autowired
	private  PasswordEncoder encoder;
	
	@Override
	public ShopDTO Save(ShopDTO s) {
		// TODO Auto-generated method stub
		Shop e = shopConverter.toEntity(s);
		e.setRole(roleRepository.findOneByName("SHOP"));
		e.setPassword(encoder.encode(s.getPassword()));
		return shopConverter.toDTO(shopRepository.save(e));
	}

	@Override
	public ShopDTO findOneById(int id) throws Exception {
		// TODO Auto-generated method stub
		return shopConverter.toDTO(shopRepository.findById(id).orElseThrow(()-> new Exception("shopeId was not found")));
	}

	@Override
	public void delete(String userName)  {
		// TODO Auto-generated method stub
		Shop shop = shopRepository.findOneByUserName(userName).get();
		shopRepository.delete(shop);
	}

	@Override
	public ShopDTO findOneByUserName(String userName) {
		// TODO Auto-generated method stub
		return shopConverter.toDTO(shopRepository.findOneByUserName(userName).get());
	}
	
}
