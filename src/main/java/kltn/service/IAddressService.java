package kltn.service;

import org.springframework.security.core.Authentication;

import kltn.dto.AddressDTO;

public interface IAddressService {
	AddressDTO getAddress(Authentication auth);
}
