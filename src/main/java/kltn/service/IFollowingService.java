package kltn.service;

import java.security.Principal;

import kltn.dto.FollowingDTO;

public interface IFollowingService {
	void save(int shopId) throws Exception;
	void delete(int shopId) throws Exception;
	
}
