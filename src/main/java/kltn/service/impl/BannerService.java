package kltn.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import kltn.entity.Banner;
import kltn.repository.BannerRepository;
import kltn.service.IBannerService;

@Service
public class BannerService implements IBannerService {
	@Autowired
	private BannerRepository bannerRepository;

	@Override
	public List<Banner> findAll() {
		// TODO Auto-generated method stub
		return bannerRepository.findAll();
	}

	@Override
	public Banner findOne() {
		// TODO Auto-generated method stub
		return bannerRepository.findOneRandom();
	}

}
