package kltn.service;

import java.io.IOException;
import java.util.List;

import org.springframework.web.multipart.MultipartFile;

import kltn.dto.PhotoDTO;

public interface IPhotoService {
	List<PhotoDTO> findByProduct_Id(int productId);

	void savePhotosProduct(int productId, MultipartFile file, String userName) throws IOException, Exception;
	void saveAvatar(MultipartFile file,  String userName) throws IOException, Exception;
}
