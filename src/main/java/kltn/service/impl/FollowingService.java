package kltn.service.impl;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import kltn.converter.FollowingConverter;
import kltn.dto.FollowingDTO;
import kltn.entity.Following;
import kltn.repository.FollowingRepository;
import kltn.security.CustomUserDetail;
import kltn.service.IFollowingService;

@Service
public class FollowingService implements IFollowingService {
	@Autowired
	private FollowingRepository followingRepository;

	@Autowired
	private FollowingConverter followingConverter;

	@Autowired
	private CustomUserDetail customUserDetail;

//	@Override
//	public void save(int shopId) throws Exception {
//		// TODO Auto-generated method stub
//
//			Following follow = followingRepository.findOneByUser_IdAndShop_Id(customUserDetail.getPrincipleId(), shopId)
//					.orElseThrow(() -> new Exception("ShopId was not existed"));
//			followingRepository.save(follow);
//	}
//
//	@Override
//	public void delete(int shopId) throws Exception {
//		// TODO Auto-generated method stub
//		Following follow = followingRepository.findOneByUser_IdAndShop_Id(customUserDetail.getPrincipleId(), shopId)
//				.orElseThrow(() -> new Exception("ShopId was not existed"));
//		followingRepository.delete(follow);
//	}

}
