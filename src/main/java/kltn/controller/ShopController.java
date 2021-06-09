package kltn.controller;

import java.security.Principal;
import java.util.Arrays;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

import kltn.api.input.LoginInput;
import kltn.api.output.ResponseValue;
import kltn.dto.ProductDTO;
import kltn.dto.ShopDTO;
import kltn.jwt.JwtTokenProvider;
import kltn.service.IPhotoService;
import kltn.service.IProductService;
import kltn.service.IShopService;
import kltn.util.ValidationBindingResult;

@RestController
@RequestMapping("/shop")
public class ShopController {
	@Autowired
	private IShopService shopService;

	@Autowired
	private IPhotoService photoService;

	@Autowired
	private IProductService productService;

	@Autowired
	private ValidationBindingResult validationBindingResult;

	@Autowired
	private AuthenticationManager authenticationManager;

	@Autowired
	private JwtTokenProvider jwt;

	@PostMapping(value = "/login", consumes = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<?> login(@RequestBody LoginInput s, BindingResult bindingResult) {
		ResponseEntity<?> error = validationBindingResult.process(bindingResult);
		if (error != null) {
			return error;
		}

		ResponseValue outPut = new ResponseValue(true, HttpStatus.OK.value(), "success");
		Authentication auth = authenticationManager
				.authenticate(new UsernamePasswordAuthenticationToken(s.getUsername(), s.getPassword()));
		SecurityContextHolder.getContext().setAuthentication(auth);
		String JWT = jwt.generateToken(auth);
		ShopDTO shop = shopService.findOneByUserName(auth.getName());
		shop.setToken(JWT);
		outPut.setData(shop);
		return new ResponseEntity<ResponseValue>(outPut, HttpStatus.OK);
	}

	@PostMapping(value = "/register", consumes = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<?> register(@Valid @RequestBody ShopDTO shop, BindingResult bindingResult) {
		ResponseEntity<?> error = validationBindingResult.process(bindingResult);
		if (error != null) {
			return error;
		}
		ResponseValue outPut = new ResponseValue(true, HttpStatus.OK.value(), "success");
		ShopDTO s = shopService.Save(shop);
		outPut.setData(s);
		return new ResponseEntity<ResponseValue>(outPut, HttpStatus.OK);
	}

	@GetMapping("/me")
	public ResponseEntity<?> getOneShop(Principal principal) {
		try {
			ResponseValue outPut = new ResponseValue(true, HttpStatus.OK.value(), "success");
			ShopDTO shop = shopService.findOneByUserName(principal.getName());
			outPut.setData(shop);
			return new ResponseEntity<ResponseValue>(outPut, HttpStatus.OK);
		} catch (Exception e) {
			throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
		}
	}

//	@PutMapping("/unfollow")
//	@PreAuthorize("hasAnyRole('ROLE_SHOP')")
//	public ResponseEntity<?> unFollowShopByUser(@RequestParam(required = true) int id, Principal principal) {
//		try {
//			ResponseValue outPut = new ResponseValue(true, HttpStatus.OK.value(), "success");
//			followingService.delete(id);
//			return new ResponseEntity<ResponseValue>(outPut, HttpStatus.OK);
//		} catch (Exception e) {
//			throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
//		}
//	}

//	@GetMapping("/photo")
//	@PreAuthorize("hasAnyRole('ROLE_SHOP')")
//	public ResponseEntity<?> getlistPhoto(Principal principal) {
//		ResponseValue outPut = new ResponseValue(true, HttpStatus.OK.value(), "success");
//		List<PhotoDTO> photo = photoService.findByProduct_Id(customUserDetail.getPrincipleId());
//		outPut.setData(photo);
//		return new ResponseEntity<ResponseValue>(outPut, HttpStatus.OK);
//	}

	@GetMapping("/product")
	public ResponseEntity<?> getlistProduct(@RequestParam(required = false, defaultValue = "5") int pageSize,
			@RequestParam(required = false, defaultValue = "0") int pageNumber, Principal principal) {
		ResponseValue outPut = new ResponseValue(true, HttpStatus.OK.value(), "success");
		Page<ProductDTO> photo = productService.findProductByShop(principal.getName(), pageSize, pageNumber);
		outPut.setData(photo);
		return new ResponseEntity<ResponseValue>(outPut, HttpStatus.OK);
	}

	@PostMapping("/photo")
	public ResponseEntity<?> savePhotos(@RequestParam(required = true) MultipartFile[] files,
			@RequestParam(required = true) int productId, Principal principal) {
		ResponseValue outPut = new ResponseValue(true, HttpStatus.OK.value(), "success");
		Arrays.asList(files).stream().forEach(file -> {
			try {
				photoService.savePhotosProduct(productId, file, principal.getName());
			} catch (Exception e) {
				throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage());
			}
		});
		return new ResponseEntity<ResponseValue>(outPut, HttpStatus.OK);

	}

	@PostMapping("/avatar")
	public ResponseEntity<?> saveAvatar(@RequestParam(required = true) MultipartFile file, Principal principal) {
		ResponseValue outPut = new ResponseValue(true, HttpStatus.OK.value(), "success");
		try {
			photoService.saveAvatar(file, principal.getName());
		} catch (Exception e) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage());
		}
		return new ResponseEntity<ResponseValue>(outPut, HttpStatus.OK);

	}

	@DeleteMapping
	public ResponseEntity<?> deleteShope(Principal principal) {
		try {
			ResponseValue outPut = new ResponseValue(true, HttpStatus.OK.value(), "success");
			shopService.delete(principal.getName());
			return new ResponseEntity<ResponseValue>(outPut, HttpStatus.OK);
		} catch (Exception e) {
			throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
		}
	}
}
