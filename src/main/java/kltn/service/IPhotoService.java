package kltn.service;

import java.io.IOException;
import java.util.List;

import org.springframework.security.core.Authentication;

import kltn.api.input.UploadFileInput;
import kltn.dto.PhotoDTO;
import kltn.entity.Photo;
import kltn.entity.Product;

public interface IPhotoService {
	List<PhotoDTO> findByProduct_Shop_id(int shopId);

	void savePhotosProduct(int productId, List<UploadFileInput> m, Authentication auth) throws IOException, Exception;

	String saveAvatar(UploadFileInput m, Authentication auth) throws Exception;

	List<Photo> saveOnePhotoProduct(Product p, List<PhotoDTO> m);
	
	List<Photo> updatePhoto(List<PhotoDTO> m, Product pro);
}
