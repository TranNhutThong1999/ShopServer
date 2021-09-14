package kltn.service.impl;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
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

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import kltn.api.input.UploadFileInput;
import kltn.converter.PhotoConverter;
import kltn.dto.PhotoDTO;
import kltn.entity.Product;
import kltn.entity.Shop;
import kltn.repository.ProductRepository;
import kltn.repository.ShopRepository;
import kltn.service.IPhotoService;
import kltn.util.Common;
import kltn.util.Constants;

@Service
public class PhotoService implements IPhotoService {
	Logger logger = LoggerFactory.getLogger(PhotoService.class);

	@Autowired
	private ProductRepository productRepository;

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
//		Product p = productRepository.findOneByIdAndShop_Id(productId, Common.getIdFromAuth(auth))
//				.orElseThrow(() -> new Exception("Author"));
//		ExecutorService executor = Executors.newFixedThreadPool(5);
//		m.stream().forEach((x) -> {
//			CompletableFuture.runAsync(() -> {
//				try {
//					String[] cut = x.getName().split("\\.");
//					String random = UUID.randomUUID().toString();
//					String name = cut[0] + random + "." + cut[1];
//					FileOutputStream fos = new FileOutputStream(
//							constant.rootURL + File.separator + "/images" + File.separator + name);
//					byte[] image = Base64.getDecoder().decode(x.getBase64String());
//					fos.write(image);
//					fos.flush();
//					fos.close();
//					Photo photo = new Photo();
//					photo.setName(name);
//					photo.setProduct(p);
//					photoRepository.save(photo);
//				} catch (FileNotFoundException e) {
//				} catch (IOException e) {
//				}
//
//			}, executor);
//
//		});
//		executor.shutdown();
	}

	@Override
	public String saveOnePhotoProduct(Product p, List<PhotoDTO> m) {
		String result = p.getPhotos();
		for (PhotoDTO o : m) {
			result = addPhoto(result, o);
		}
		return result;
	}

	@Override
	public String saveAvatar(UploadFileInput m, Authentication auth) throws Exception {
		// TODO Auto-generated method stub
		Shop s = shopRepository.findById(Common.getIdFromAuth(auth))
				.orElseThrow(() -> new Exception("shop was not found"));
		String[] cut = m.getName().split("\\.");
		String random = UUID.randomUUID().toString();
		String name = "shop" + cut[0] + random + "." + cut[1];
//		File f = new File(constant.rootURL + File.separator + "images" + File.separator + "avatar" + File.separator +
//				"shop" + File.separator + name);
		FileOutputStream fos = new FileOutputStream(constant.rootURL + File.separator + "images" + File.separator
				+ "avatar" + File.separator + "shop" + File.separator + name);
		byte[] image = Base64.getDecoder().decode(m.getBase64String());
		fos.write(image);
		fos.flush();
		fos.close();
		if (s.getAvatar() != null) {
			File f = new File(constant.rootURL + File.separator + "images" + File.separator + "avatar" + File.separator
					+ "shop" + File.separator + s.getAvatar());
			f.delete();
		}
		s.setAvatar(name);
		System.out.println(s.getAvatar());
		return photoConverter.toLinkAvatarShop(shopRepository.save(s).getAvatar());
	}

	public String updatePhoto(List<PhotoDTO> photos, Product pro) {
		// TODO Auto-generated method stub
		String result = pro.getPhotos() == null ? "" : pro.getPhotos();
		for (PhotoDTO o : photos) {
			if (o.getLink() != null) {
				if(o.getLink().equals("")) 
				result = deletePhoto(result, o.getName());
			} else 
				result = addPhoto(result, o);

		}
		logger.info("result: " + result);
		return result;

	}

	private String deletePhoto(String photos, String name) {
		String[] list = photos.split(",");
		String result = "";
		for (String e : list) {
			if (e.equals(""))
				continue;
			if (e.equals(name)) {
				File f = new File(constant.rootURL + File.separator + "images" + File.separator + name);
				logger.info("delete: " + f.getAbsolutePath());
				f.delete();
				continue;
			}
			result += name + ",";
		}
		return result;
	}

	private String addPhoto(String photos, PhotoDTO dto) {
		try {
			String[] cut = dto.getName().split("\\.");
			String random = UUID.randomUUID().toString();
			String name = cut[0] + random + "." + cut[1];
			FileOutputStream fos = new FileOutputStream(
					constant.rootURL + File.separator + "images" + File.separator + name);
			byte[] image = Base64.getDecoder().decode(dto.getBase64String());
			fos.write(image);
			fos.flush();
			fos.close();
			logger.info("add photo : " + constant.rootURL + File.separator + "images" + File.separator + name);
			photos += name + ",";
			return photos;
		} catch (FileNotFoundException e) {
		} catch (IOException e) {
		}
		return null;
	}
}
