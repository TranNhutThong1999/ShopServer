package kltn.service.impl;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Base64;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.stream.Collectors;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ClassPathResource;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.auth.oauth2.ServiceAccountCredentials;
import com.google.cloud.storage.BlobId;
import com.google.cloud.storage.BlobInfo;
import com.google.cloud.storage.Bucket;
import com.google.cloud.storage.Storage;
import com.google.cloud.storage.StorageOptions;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import com.google.firebase.cloud.StorageClient;

import io.netty.util.Constant;
import kltn.api.input.UploadFileInput;
import kltn.converter.PhotoConverter;
import kltn.dto.PhotoDTO;
import kltn.entity.Photo;
import kltn.entity.Product;
import kltn.entity.Shop;
import kltn.entity.User;
import kltn.repository.PhotoRepository;
import kltn.repository.ProductRepository;
import kltn.repository.ShopRepository;
import kltn.repository.UserRepository;
import kltn.security.MyShop;
import kltn.service.IPhotoService;
import kltn.util.Constants;

@Service
public class PhotoService implements IPhotoService {
	@Autowired
	private ProductRepository productRepository;

	@Autowired
	private PhotoRepository photoRepository;

	@Autowired
	private PhotoConverter photoConverter;

	@Autowired
	private ShopRepository shopRepository;

	@Autowired
	private Constants constant;

	@PostConstruct
	public void run() {
		File f = new File(constant.rootURL + File.separator + "images");
		if (!f.exists()) {
			f.mkdir();
		}
	}

	@Override
	public void savePhotosProduct(int productId, List<UploadFileInput> m, Authentication auth) throws Exception {
		Product p = productRepository.findOneByIdAndShop_Id(productId, getIdFromAuth(auth))
				.orElseThrow(() -> new Exception("Author"));
		ExecutorService executor = Executors.newFixedThreadPool(5);
		m.stream().forEach((x) -> {
			CompletableFuture.runAsync(() -> {
				try {
					String[] cut = x.getName().split("\\.");
					String random = UUID.randomUUID().toString();
					String name = cut[0] + random + "." + cut[1];
					FileOutputStream fos = new FileOutputStream(
							constant.rootURL + File.separator + "/images" + File.separator + name);
					byte[] image = Base64.getDecoder().decode(x.getBase64String());
					fos.write(image);
					fos.flush();
					fos.close();
					Photo photo = new Photo();
					photo.setName(name);
					photo.setProduct(p);
					photoRepository.save(photo);
				} catch (FileNotFoundException e) {
				} catch (IOException e) {
				}

			}, executor);

		});
		executor.shutdown();

	}

	@Override
	public List<PhotoDTO> findByProduct_Shop_id(int shopId) {
		// TODO Auto-generated method stub
		return photoRepository.findByProduct_Shop_id(shopId).stream().map(photoConverter::toDTO)
				.collect(Collectors.toList());
	}

	@Override
	public void saveAvatar(UploadFileInput m, Authentication auth) throws Exception {
		// TODO Auto-generated method stub
		Shop s = shopRepository.findById(getIdFromAuth(auth)).orElseThrow(() -> new Exception("shop was not found"));
		String[] cut = m.getName().split("\\.");
		String random = UUID.randomUUID().toString();
		String name = cut[0] + random + "." + cut[1];
		FileOutputStream fos = new FileOutputStream(
				constant.rootURL + File.separator + "/images" + File.separator + name);
		byte[] image = Base64.getDecoder().decode(m.getBase64String());
		fos.write(image);
		fos.flush();
		fos.close();
		s.setAvatar(constant.showImage + File.separator + "images" + File.separator + name);
		shopRepository.save(s);
	}
	
	private int getIdFromAuth(Authentication auth) {
		MyShop u = (MyShop)auth.getPrincipal();
		return u.getId();
	}
}
