package kltn.service.impl;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import kltn.converter.RatingConverter;
import kltn.dto.RatingDTO;
import kltn.repository.RatingRepository;
import kltn.service.IRatingService;

@Service
public class RatingService implements IRatingService {
	@Autowired
	private RatingRepository ratingRepository;
	
	@Autowired
	private RatingConverter ratingConverter;
	
	@Override
	public Page<RatingDTO> findAllByProduct_Id(int productId, int pageSize, int pageNumber) {
		// TODO Auto-generated method stub
		Pageable pageable = PageRequest.of(pageNumber, pageSize);
		return ratingRepository.findAllByProduct_Id(productId, pageable).map(ratingConverter::toDTO);
	}

}
