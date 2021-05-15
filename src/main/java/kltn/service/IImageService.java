package kltn.service;

import java.io.IOException;

import org.springframework.web.multipart.MultipartFile;

public interface IImageService {
	void saveImageProduct(int productId, MultipartFile file) throws IOException, Exception;
}
