package kltn.service.impl;

import java.util.Optional;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import kltn.converter.ShopConverter;
import kltn.dto.ShopDTO;
import kltn.entity.Address;
import kltn.entity.District;
import kltn.entity.Provincial;
import kltn.entity.Shop;
import kltn.entity.Wards;
import kltn.repository.AddressRepository;
import kltn.repository.DistrictRepository;
import kltn.repository.ProvincialRepository;
import kltn.repository.ShopRepository;
import kltn.repository.WardsRepository;
import kltn.service.IShopService;

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

	@Override
	public ShopDTO Save(ShopDTO s) {
		// TODO Auto-generated method stub
		Shop e = shopConverter.toEntity(s);
		e.setPassword(encoder.encode(s.getPassword()));
		Address address = new Address();
		address.setLocation(s.getLocation());

		Optional<Provincial> p = provincialRepository.findOneByCode(s.getProdincial().getCode());
		if (!p.isPresent()) {
			Provincial pro = modelMapper.map(s.getProdincial(), Provincial.class);
			provincialRepository.save(pro);
			address.setProvincial(pro);
		} else
			address.setProvincial(p.get());

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
		addressRepository.save(address);
		return shopConverter.toDTO(shopRepository.save(e));
	}

	@Override
	public ShopDTO findOneById(int id) throws Exception {
		// TODO Auto-generated method stub
		return shopConverter
				.toDTO(shopRepository.findById(id).orElseThrow(() -> new Exception("shopeId was not found")));
	}

	@Override
	public void delete(String userName) {
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
