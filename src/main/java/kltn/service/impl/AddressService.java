package kltn.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import kltn.converter.AddressConverter;
import kltn.dto.AddressDTO;
import kltn.repository.AddressRepository;
import kltn.service.IAddressService;

@Service
public class AddressService implements IAddressService{
	@Autowired
	private AddressRepository addressRepository;
	
	@Autowired
	private AddressConverter addressConverter;
	
	@Override
	public AddressDTO getAddress(Authentication auth) {
		// TODO Auto-generated method stub
		
		return null;
	}
	
}
