package kltn.service.impl;

import java.util.Optional;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import kltn.SHOPConstant;
import kltn.converter.ShopConverter;
import kltn.dto.ShopDTO;
import kltn.entity.Address;
import kltn.entity.District;
import kltn.entity.Province;
import kltn.entity.Shop;
import kltn.entity.Wards;
import kltn.repository.AddressRepository;
import kltn.repository.DistrictRepository;
import kltn.repository.ProvincialRepository;
import kltn.repository.ShopRepository;
import kltn.repository.WardsRepository;
import kltn.security.MyShop;
import kltn.service.IShopService;
import kltn.util.EmailService;

@Service
public class ShopService implements IShopService {

	@Autowired
	private ShopConverter shopConverter;

	@Autowired
	private ShopRepository shopRepository;

	@Autowired
	private PasswordEncoder encoder;

	@Autowired
	private ModelMapper modelMapper;

	@Autowired
	private AddressRepository addressRepository;

	@Autowired
	private ProvincialRepository provincialRepository;

	@Autowired
	private DistrictRepository districtRepository;

	@Autowired
	private WardsRepository wardsRepository;

	@Autowired
	private EmailService emailService;
	
	@Override
	public ShopDTO Save(ShopDTO s) {
		// TODO Auto-generated method stub
		Shop e = shopConverter.toEntity(s);
		e.setPassword(encoder.encode(s.getPassword()));
		e.setEnable(false);
		Address address = new Address();
		address.setLocation(s.getLocation());

		Optional<Province> p = provincialRepository.findOneByCode(s.getProdincial().getCode());
		if (!p.isPresent()) {
			Province pro = modelMapper.map(s.getProdincial(), Province.class);
			provincialRepository.save(pro);
			address.setProvince(pro);
		} else
			address.setProvince(p.get());

		Optional<District> d = districtRepository.findOneByCode(s.getDistrict().getCode());
		if (!d.isPresent()) {
			District dis = modelMapper.map(s.getDistrict(), District.class);
			districtRepository.save(dis);
			address.setDistrict(dis);
		} else
			address.setDistrict(d.get());

		Optional<Wards> w = wardsRepository.findOneByCode(s.getWards().getCode());
		if (!w.isPresent()) {
			Wards wards = modelMapper.map(s.getWards(), Wards.class);
			wardsRepository.save(wards);
			address.setWards(wards);
		} else
			address.setWards(w.get());

	//	address.setDistrict(modelMapper.map(s.getDistrict(), District.class));
	//	address.setWards(modelMapper.map(s.getWards(), Wards.class));
		e.setAddress(address);
		e.generateToken();
		e.setTimeTokenFuture(60*12);
		emailService.sendSimpleMessage(e.getEmail(), "HI SHOP", "Your code: "+ e.getOtp());
		addressRepository.save(address);
		return shopConverter.toDTO(shopRepository.save(e));
	}

	@Override
	public ShopDTO getOne(Authentication auth)  {
		// TODO Auto-generated method stub
		return shopConverter
				.toDTO(shopRepository.findById(getIdFromAuth(auth)).get());
	}

	@Override
	public void delete(Authentication auth) {
		// TODO Auto-generated method stub
		Shop shop = shopRepository.findById(getIdFromAuth(auth)).get();
		shopRepository.delete(shop);
	}

	@Override
	public ShopDTO findOneByUserName(String userName) {
		// TODO Auto-generated method stub
		return shopConverter.toDTO(shopRepository.findOneByUserName(userName).get());
	}

	@Override
	public void sendOTP(String mail) throws Exception {
		// TODO Auto-generated method stub
		Shop shop = shopRepository.findOneByEmail(mail).orElseThrow(()-> new Exception("Email was not found"));
		shop.generateToken();
		shop.setTimeTokenFuture(SHOPConstant.TIME_OTP_EXPIRE);
		shopRepository.save(shop);
		emailService.sendSimpleMessage(mail, "HI SHOP", "Your Code: "+ shop.getOtp());
	}

	@Override
	public int checkOTP(String otp, String email) throws Exception {
		// TODO Auto-generated method stub
		Shop s =shopRepository.findOneByEmailAndOtp(email, otp).orElseThrow(()-> new Exception("otp was not found"));
		if(!s.isAfterTime()) throw new Exception("Opt expired");
		s.setOtp("");
		shopRepository.save(s);
		return s.getId();
	}

	@Override
	public void verify(String otp, String email) throws Exception {
		Shop s =shopRepository.findOneByEmailAndOtp(email, otp).orElseThrow(()-> new Exception("otp was not found"));
		s.setEnable(true);
		s.setOtp("");
		shopRepository.save(s);
	}

	@Override
	public void changePassword(String password, Authentication auth) {
		// TODO Auto-generated method stub
		Shop s = shopRepository.findById(getIdFromAuth(auth)).get();
		s.setPassword(encoder.encode(password));
		shopRepository.save(s);
	}
	
	private int getIdFromAuth(Authentication auth) {
		MyShop u = (MyShop)auth.getPrincipal();
		return u.getId();
	}

	@Override
	public void updatePassword(int shopId, String password, String otp) throws Exception {
		// TODO Auto-generated method stub
		Shop s = shopRepository.findOneByIdAndOtp(shopId, otp).orElseThrow(() -> new Exception("shopId was not found"));
		s.setPassword(encoder.encode(password));
		s.setOtp("");
		shopRepository.save(s);
	}
}
