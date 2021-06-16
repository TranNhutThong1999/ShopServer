package kltn.service;

import java.io.IOException;
import java.util.List;

import org.springframework.web.multipart.MultipartFile;

import kltn.api.input.UploadFileInput;
import kltn.dto.PhotoDTO;

public interface IPhotoService {
	List<PhotoDTO> findByProduct_Id(int productId);

	void savePhotosProduct(int productId, List<UploadFileInput> m, String userName) throws IOException, Exception;
	void saveAvatar(UploadFileInput m,  String userName) throws Exception;
}
