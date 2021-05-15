package kltn.service;

import java.util.List;

import kltn.entity.Banner;

public interface IBannerService {
	List<Banner> findAll();
	Banner findOne();
}
