package kltn.controller;

import java.security.Principal;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

import kltn.api.input.LoginInput;
import kltn.api.input.OtpInput;
import kltn.api.input.UploadFileInput;
import kltn.api.output.ResponseValue;
import kltn.dto.PhotoDTO;
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
		try {
			ResponseValue outPut = new ResponseValue(true, HttpStatus.OK.value(), "success");
			Authentication auth = authenticationManager
					.authenticate(new UsernamePasswordAuthenticationToken(s.getUsername(), s.getPassword()));
			SecurityContextHolder.getContext().setAuthentication(auth);
			String JWT = jwt.generateToken(auth);
			ShopDTO shop = shopService.getOne(auth);
			shop.setToken(JWT);
			outPut.setData(shop);
			return new ResponseEntity<ResponseValue>(outPut, HttpStatus.OK);
		} catch (DisabledException e) {
			throw new ResponseStatusException(HttpStatus.LOCKED, "User is disabled");
			
		} catch (UsernameNotFoundException e) {
			throw new ResponseStatusException(HttpStatus.METHOD_NOT_ALLOWED, e.getMessage());
			
		} catch (Exception e) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage());
		}
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
	public ResponseEntity<?> getOneShop(Authentication auth) {
		try {
			ResponseValue outPut = new ResponseValue(true, HttpStatus.OK.value(), "success");
			ShopDTO shop = shopService.getOne(auth);
			outPut.setData(shop);
			return new ResponseEntity<ResponseValue>(outPut, HttpStatus.OK);
		} catch (Exception e) {
			throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
		}
	}


	@GetMapping("/photo")
	public ResponseEntity<?> getlistPhoto(@RequestParam int id) {
		ResponseValue outPut = new ResponseValue(true, HttpStatus.OK.value(), "success");
		List<PhotoDTO> photo = photoService.findByProduct_Shop_id(id);
		outPut.setData(photo);
		return new ResponseEntity<ResponseValue>(outPut, HttpStatus.OK);
	}

	@GetMapping("/product")
	public ResponseEntity<?> getlistProduct(@RequestParam(required = false, defaultValue = "5") int pageSize,
			@RequestParam(required = false, defaultValue = "0") int pageNumber,@RequestParam int id) {
		ResponseValue outPut = new ResponseValue(true, HttpStatus.OK.value(), "success");
		outPut.setData(productService.findByShopId(id, pageSize, pageNumber));
		return new ResponseEntity<ResponseValue>(outPut, HttpStatus.OK);
	}


	@PostMapping("/avatar")
	public ResponseEntity<?> saveAvatar(@RequestBody UploadFileInput m, Authentication auth) {
		ResponseValue outPut = new ResponseValue(true, HttpStatus.OK.value(), "success");
		try {
			photoService.saveAvatar(m, auth);
		} catch (Exception e) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage());
		}
		return new ResponseEntity<ResponseValue>(outPut, HttpStatus.OK);

	}
//
//	@DeleteMapping
//	public ResponseEntity<?> deleteShope(Principal principal) {
//		try {
//			ResponseValue outPut = new ResponseValue(true, HttpStatus.OK.value(), "success");
//			shopService.delete(principal.getName());
//			return new ResponseEntity<ResponseValue>(outPut, HttpStatus.OK);
//		} catch (Exception e) {
//			throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
//		}
//	}

	@PostMapping("/sendotp")
	public ResponseEntity<?> sendCode(@RequestParam String email) {
		try {
			ResponseValue outPut = new ResponseValue(true, HttpStatus.OK.value(), "success");
			shopService.sendOTP(email);
			return new ResponseEntity<ResponseValue>(outPut, HttpStatus.OK);
		} catch (Exception e) {
			throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
		}
	}

	@PostMapping("/checkotp")
	public ResponseEntity<?> checkOTP(@RequestBody Map<String, String> data) {
		try {
			ResponseValue outPut = new ResponseValue(true, HttpStatus.OK.value(), "success");
			outPut.setData(shopService.checkOTP(data.get("otp"), data.get("email")));
			return new ResponseEntity<ResponseValue>(outPut, HttpStatus.OK);
		} catch (Exception e) {
			throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
		}
	}

	@PostMapping("/verify")
	public ResponseEntity<?> verifyRegister(@RequestBody OtpInput input) {
		try {
			ResponseValue outPut = new ResponseValue(true, HttpStatus.OK.value(), "success");
			shopService.verify(input.getOtp(), input.getEmail());
			return new ResponseEntity<ResponseValue>(outPut, HttpStatus.OK);
		} catch (Exception e) {
			throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
		}
	}

	@PutMapping("changepassword")
	public ResponseEntity<?> changePassword(@RequestBody Map<String, String> data, Authentication auth) {
		try {
			ResponseValue outPut = new ResponseValue(true, HttpStatus.OK.value(), "success");
			shopService.changePassword(data.get("password"), auth);
			return new ResponseEntity<ResponseValue>(outPut, HttpStatus.OK);
		} catch (Exception e) {
			throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
		}
	}
	
	@PutMapping("forgotpassword")
	public ResponseEntity<?> forgotPassword(@RequestBody Map<String, String> data) {
		try {
			ResponseValue outPut = new ResponseValue(true, HttpStatus.OK.value(), "success");
			shopService.updatePassword(Integer.valueOf(data.get("userId")), data.get("password"), data.get("otp"));
			return new ResponseEntity<ResponseValue>(outPut, HttpStatus.OK);
		} catch (Exception e) {
			throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
		}
	}
}
