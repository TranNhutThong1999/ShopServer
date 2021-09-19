package kltn.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import kltn.entity.Country;
import kltn.repository.CountryRepository;
import kltn.service.ICountryService;

@Service
public class CountryService implements ICountryService {
	@Autowired
	private CountryRepository countryRepository;

	@Override
	public List<Country> findAll() {
		// TODO Auto-generated method stub
		return countryRepository.findAll();
	}

}
