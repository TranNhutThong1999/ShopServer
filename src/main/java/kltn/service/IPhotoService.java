package kltn.service;

import java.util.List;

import org.springframework.security.core.Authentication;

import kltn.api.input.UploadFileInput;
import kltn.dto.PhotoDTO;
import kltn.entity.Product;

public interface IPhotoService {
	String saveAvatar(UploadFileInput m, Authentication auth) throws Exception;

	String saveOnePhotoProduct(Product p, List<PhotoDTO> m);
	
	String updatePhoto(List<PhotoDTO> m, Product pro);
}
