//package kltn.service.impl;
//
//import java.io.File;
//import java.io.FileOutputStream;
//import java.io.IOException;
//import java.nio.ByteBuffer;
//import java.util.HashMap;
//import java.util.Map;
//import java.util.UUID;
//
//import javax.annotation.PostConstruct;
//
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.core.io.ClassPathResource;
//import org.springframework.stereotype.Service;
//import org.springframework.web.multipart.MultipartFile;
//
//import com.google.auth.oauth2.GoogleCredentials;
//import com.google.auth.oauth2.ServiceAccountCredentials;
//import com.google.cloud.storage.BlobId;
//import com.google.cloud.storage.BlobInfo;
//import com.google.cloud.storage.Bucket;
//import com.google.cloud.storage.Storage;
//import com.google.cloud.storage.StorageOptions;
//import com.google.firebase.FirebaseApp;
//import com.google.firebase.FirebaseOptions;
//import com.google.firebase.cloud.StorageClient;
//
//import kltn.entity.Photo;
//import kltn.repository.PhotoRepository;
//import kltn.repository.ProductRepository;
//import kltn.service.IImageService;
//
//@Service
//public class ImageService implements IImageService {
//	@Autowired
//	private ProductRepository productRepository;
//
//	@Autowired
//	private PhotoRepository imageRepository;
//
//	private Storage storage;
//
//	public static final String rootURL = "https://firebasestorage.googleapis.com/v0/b/kltn-5e877.appspot.com/o/";
//
//	@PostConstruct
//	public void init() {
//		// initialize Firebase
//		try {
//			ClassPathResource serviceAccount = new ClassPathResource("firebase.json");
//			FirebaseOptions options = FirebaseOptions.builder()
//					.setCredentials(GoogleCredentials.fromStream(serviceAccount.getInputStream()))
//					.setStorageBucket("kltn-5e877.appspot.com").build();
//			FirebaseApp.initializeApp(options);
//
//			storage = StorageOptions.newBuilder().setProjectId("kltn-5e877")
//					.setCredentials(ServiceAccountCredentials.fromStream(serviceAccount.getInputStream())).build()
//					.getService();
//
//		} catch (Exception ex) {
//			ex.printStackTrace();
//		}
//	}
//
//	@Override
//	public void saveImageProduct(int productId, MultipartFile file) throws Exception {
//		Bucket bucket = StorageClient.getInstance().bucket();
//		String name = file.getOriginalFilename();
//		String token = UUID.randomUUID().toString();
//
//		BlobId blobId = BlobId.of(bucket.getName(), name);
//		Map<String, String> map = new HashMap<>();
//		map.put("firebaseStorageDownloadTokens", token);
//		
//		BlobInfo blobInfo = BlobInfo.newBuilder(blobId).setMetadata(map).setContentType(file.getContentType()).build();
//		//excute upload
//		storage.create(blobInfo, file.getBytes());
//		String link = rootURL + name + "?alt=media&token=" + token;
//		Photo img = new Photo();
//		img.setName(name);
//		img.setLink(link);
//		img.setProduct( productRepository.findOneById(productId).orElseThrow(()-> new Exception("productId does't exist")));
//		imageRepository.save(img);
//	}
//
//}
