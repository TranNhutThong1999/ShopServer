package kltn.service.impl;

import java.io.IOException;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
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

import kltn.converter.PhotoConverter;
import kltn.dto.PhotoDTO;
import kltn.entity.Photo;
import kltn.entity.Shop;
import kltn.entity.User;
import kltn.repository.PhotoRepository;
import kltn.repository.ProductRepository;
import kltn.repository.ShopRepository;
import kltn.repository.UserRepository;
import kltn.service.IPhotoService;

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
	private Storage storage;

	public static final String rootURL = "https://firebasestorage.googleapis.com/v0/b/kltn-5e877.appspot.com/o/";

	@PostConstruct
	public void init() {
		// initialize Firebase
		try {
			ClassPathResource serviceAccount = new ClassPathResource("firebase.json");
			FirebaseOptions options = FirebaseOptions.builder()
					.setCredentials(GoogleCredentials.fromStream(serviceAccount.getInputStream()))
					.setStorageBucket("kltn-5e877.appspot.com").build();
			FirebaseApp.initializeApp(options);

			storage = StorageOptions.newBuilder().setProjectId("kltn-5e877")
					.setCredentials(ServiceAccountCredentials.fromStream(serviceAccount.getInputStream())).build()
					.getService();

		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}

	@Override
	public void savePhotosProduct(int productId, MultipartFile file, String userName) throws Exception {
		Bucket bucket = StorageClient.getInstance().bucket();
		String name = file.getOriginalFilename();
		String token = UUID.randomUUID().toString();

		BlobId blobId = BlobId.of(bucket.getName(), name);
		Map<String, String> map = new HashMap<>();
		map.put("firebaseStorageDownloadTokens", token);

		BlobInfo blobInfo = BlobInfo.newBuilder(blobId).setMetadata(map).setContentType(file.getContentType()).build();
		// excute upload
		storage.create(blobInfo, file.getBytes());
		String link = rootURL + name + "?alt=media&token=" + token;
		Photo img = new Photo();
		img.setName(name);
		img.setLink(link);
		img.setProduct(productRepository.findOneByIdAndShop_UserName(productId, userName)
				.orElseThrow(() -> new Exception("productId does't exist")));
		photoRepository.save(img);
	}

	@Override
	public List<PhotoDTO> findByProduct_Id(int productId) {
		// TODO Auto-generated method stub
		return photoRepository.findByProduct_Id(productId).stream().map(photoConverter::toDTO)
				.collect(Collectors.toList());
	}


	@Override
	public void saveAvatar(MultipartFile file, String userName) throws IOException, Exception {
		// TODO Auto-generated method stub
		Bucket bucket = StorageClient.getInstance().bucket();
		String name = file.getOriginalFilename();
		String token = UUID.randomUUID().toString();

		BlobId blobId = BlobId.of(bucket.getName(), name);
		Map<String, String> map = new HashMap<>();
		map.put("firebaseStorageDownloadTokens", token);

		BlobInfo blobInfo = BlobInfo.newBuilder(blobId).setMetadata(map).setContentType(file.getContentType()).build();
		// excute upload
		storage.create(blobInfo, file.getBytes());
		String link = rootURL + name + "?alt=media&token=" + token;
		Shop s = shopRepository.findOneByUserName(userName).get();
		s.setAvatar(link);
		shopRepository.save(s);
	}

}
