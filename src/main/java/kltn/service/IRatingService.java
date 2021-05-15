package kltn.service;

import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

import kltn.dto.RatingDTO;

@Service
public interface IRatingService {
	Page<RatingDTO> findAllByProduct_Id(int productId, int pageSize, int pageNumber);
}
