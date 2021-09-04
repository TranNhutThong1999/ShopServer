package kltn.service.impl;

import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import kltn.SHOPConstant;
import kltn.api.input.ShopDetail;
import kltn.converter.AddressConverter;
import kltn.converter.ShopConverter;
import kltn.dto.AddressDTO;
import kltn.dto.NotificationDTO;
import kltn.dto.ShopDTO;
import kltn.entity.Address;
import kltn.entity.DeviceToken;
import kltn.entity.District;
import kltn.entity.Notification;
import kltn.entity.Province;
import kltn.entity.Shop;
import kltn.entity.Wards;
import kltn.repository.AddressRepository;
import kltn.repository.DeviceTokenRepository;
import kltn.repository.DistrictRepository;
import kltn.repository.NotificationRepository;
import kltn.repository.ProvincialRepository;
import kltn.repository.ShopRepository;
import kltn.repository.WardsRepository;
import kltn.security.MyShop;
import kltn.service.IShopService;
import kltn.util.Common;
import kltn.util.EmailService;

@Service
public class ShopService implements IShopService {
	Logger logger = LoggerFactory.getLogger(ShopService.class);

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

	@Autowired
	private AddressConverter addressConverter;

	@Autowired
	private DeviceTokenRepository deviceTokenRepository;

	@Autowired
	private NotificationRepository notificationRepository;

	@Override
	public ShopDTO save(ShopDTO s) {
		// TODO Auto-generated method stub
		Shop e = new Shop();
		e.setPhone(s.getPhone());
		e.setEmail(s.getEmail());
		e.setPassword(encoder.encode(s.getPassword()));
		e.setEnable(false);
		e.generateToken();
		e.setTimeTokenFuture(SHOPConstant.TIME_OTP_EXPIRE);
		emailService.sendRegisterMessage(e.getEmail(), e.getOtp());
//		emailService.sendSimpleMessage(e.getEmail(), "HI SHOP", "Your code: " + e.getOtp());
		return shopConverter.toDTO(shopRepository.save(e));
	}

	@Override
	public ShopDTO createDetail(ShopDetail s) throws Exception {
		Shop shop = shopRepository.findOneById(s.getId()).orElseThrow(() -> {
			logger.error("shopId was not found " + s.getId());
			return new Exception("shopId was not found " + s.getId());
		});
		if(shop.getCode()!=null){
			logger.error("code existed");
			throw new Exception("code existed " + s.getId());
		}
		shop.setName(s.getName());
		shop.setCode(UUID.randomUUID().toString());
		shop.setHotLine(s.getHotLine());
		shop.setWebsite(s.getWebsite());
		Address address = new Address();
		address.setLocation(s.getLocation());

		Optional<Province> p = provincialRepository.findOneByCode(s.getProvince().getCode());
		if (!p.isPresent()) {
			Province pro = modelMapper.map(s.getProvince(), Province.class);
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
		shop.setAddress(address);
		Shop sh = shopRepository.save(shop);
		addressRepository.save(address);
		return shopConverter.toDTO(sh);
	}

	@Override
	public ShopDTO getOne(Authentication auth) {
		// TODO Auto-generated method stub
		return shopConverter.toDTO(shopRepository.findById(Common.getIdFromAuth(auth)).get());
	}

	@Override
	public void delete(Authentication auth) {
		// TODO Auto-generated method stub
		Shop shop = shopRepository.findById(Common.getIdFromAuth(auth)).get();
		shopRepository.delete(shop);
	}

	@Override
	public String sendOTP(String mail) throws Exception {
		// TODO Auto-generated method stub
		Shop shop = shopRepository.findOneByEmail(mail).orElseThrow(() -> new Exception("Email was not found"));
		shop.generateToken();
		shop.setTimeTokenFuture(SHOPConstant.TIME_OTP_EXPIRE);
		shop = shopRepository.save(shop);
		emailService.sendOtpMessage(mail, shop.getOtp());
		return shop.getId();
	}

	@Override
	public String checkOTP(String otp, String shopId) throws Exception {
		// TODO Auto-generated method stub
		Shop s = shopRepository.findOneByIdAndOtp(shopId, otp).orElseThrow(() -> new Exception("otp was not found"));
		if (s.isAfterTime())
			throw new Exception("Opt expired");
		shopRepository.save(s);
		return s.getId();
	}

	@Override
	public void verify(String otp, String shopId) throws Exception {
		Shop s = shopRepository.findOneByIdAndOtp(shopId, otp).orElseThrow(() -> new Exception("otp was not found"));
		s.setEnable(true);
		s.setOtp("");
		shopRepository.save(s);
	}

	@Override
	public void changePassword(String password, Authentication auth) {
		// TODO Auto-generated method stub
		Shop s = shopRepository.findById(Common.getIdFromAuth(auth)).get();
		s.setPassword(encoder.encode(password));
		shopRepository.save(s);
	}

	@Override
	public void updatePassword(String shopId, String password, String otp) throws Exception {
		// TODO Auto-generated method stub
		Shop s = shopRepository.findOneByIdAndOtp(shopId, otp).orElseThrow(() -> new Exception("shopId was not found"));
		s.setPassword(encoder.encode(password));
		s.setOtp("");
		shopRepository.save(s);
	}

	@Override
	public AddressDTO getAddress(Authentication auth) {
		// TODO Auto-generated method stub
		Address ad = shopRepository.findOneById(Common.getIdFromAuth(auth)).get().getAddress();
		return addressConverter.toDTO(ad);
	}

	@Override
	public ShopDTO update(ShopDTO shop, Authentication auth) {
		// TODO Auto-generated method stub
		Shop old = shopRepository.findById(Common.getIdFromAuth(auth)).get();
		// Shop s = shopConverter.toEntity(shop);
		old.setName(shop.getName());
		old.setPhone(shop.getPhone());
		old.setHotLine(shop.getHotLine());
		old.setWebsite(shop.getWebsite());

		Address address = old.getAddress();
		address.setLocation(shop.getLocation());

		Optional<Province> p = provincialRepository.findOneByCode(shop.getProvince().getCode());
		if (!p.isPresent()) {
			Province pro = modelMapper.map(shop.getProvince(), Province.class);
			pro = provincialRepository.save(pro);
			address.setProvince(pro);
		} else
			address.setProvince(p.get());

		Optional<District> d = districtRepository.findOneByCode(shop.getDistrict().getCode());
		if (!d.isPresent()) {
			District dis = modelMapper.map(shop.getDistrict(), District.class);
			dis = districtRepository.save(dis);
			address.setDistrict(dis);
		} else
			address.setDistrict(d.get());

		Optional<Wards> w = wardsRepository.findOneByCode(shop.getWards().getCode());
		if (!w.isPresent()) {
			Wards wards = modelMapper.map(shop.getWards(), Wards.class);
			wards = wardsRepository.save(wards);
			address.setWards(wards);
		} else
			address.setWards(w.get());

		old.setAddress(address);

		return shopConverter.toDTO(shopRepository.save(old));
	}

	@Override
	public void saveFCMToken(String token, Authentication auth) {
		// TODO Auto-generated method stub
		Optional<DeviceToken> de = deviceTokenRepository.findOneByShop_Id(Common.getIdFromAuth(auth));
		DeviceToken d = null;
		if(!de.isPresent()) {
			 d = new DeviceToken();
			d.setFCMToken(token);
			d.setShop(shopRepository.findById(Common.getIdFromAuth(auth)).get());
			d.setCreateDate(new Date());
			deviceTokenRepository.save(d);
		}else {
			d = de.get();
			d.setFCMToken(token);
			d.setCreateDate(new Date());
			deviceTokenRepository.save(d);
		}

	}

	@Override
	public List<NotificationDTO> getListNoti(Authentication auth) {
		// TODO Auto-generated method stub
		Sort sort = Sort.by(Sort.Direction.ASC,"createDate");
		return notificationRepository.findByShop_Id(Common.getIdFromAuth(auth), sort).stream()
				.map((x) -> modelMapper.map(x, NotificationDTO.class)).collect(Collectors.toList());
	}

	@Override
	public void setIsSeen(Authentication auth, int id) {
		// TODO Auto-generated method stub
		Optional<Notification> no = notificationRepository.findOneByIdAndShop_Id(id, Common.getIdFromAuth(auth));
		if (no.isPresent()) {
			Notification noti = no.get();
			noti.setIsSeen(1);
			notificationRepository.save(noti);
		}
	}

}
