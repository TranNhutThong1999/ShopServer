package kltn.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import javax.security.auth.login.CredentialException;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.support.ResourceBundleMessageSource;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
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
import org.springframework.web.server.ResponseStatusException;

import kltn.api.input.LoginInput;
import kltn.api.input.OtpInput;
import kltn.api.input.ShopDetail;
import kltn.api.input.UploadFileInput;
import kltn.api.output.ResponseValue;
import kltn.dto.PhotoDTO;
import kltn.dto.ShopDTO;
import kltn.jwt.JwtTokenProvider;
import kltn.service.IAddressService;
import kltn.service.ICommentService;
import kltn.service.IOrderService;
import kltn.service.IPhotoService;
import kltn.service.IProductService;
import kltn.service.IShopService;
import kltn.util.Common;
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

	@Autowired
	private IAddressService addressService; 
	
	@Autowired
	private ResourceBundleMessageSource resource;
	
	@Autowired
	private ICommentService commentService;
	
	@Autowired
	private IOrderService orderService;
	
	
	@PostMapping(value = "/login", consumes = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<?> login(@RequestBody LoginInput s, BindingResult bindingResult) {
//		ResponseEntity<?> error = validationBindingResult.process(bindingResult);
//		if (error != null) {
//			return error;
//		}
		ResponseValue outPut = new ResponseValue();
		try {
			Authentication auth = authenticationManager
					.authenticate(new UsernamePasswordAuthenticationToken(s.getUsername(), s.getPassword()));
			SecurityContextHolder.getContext().setAuthentication(auth);
			String JWT = jwt.generateToken(auth);
			ShopDTO shop = shopService.getOne(auth);
			shop.setToken(JWT);
			outPut.setSuccess(true);
			outPut.setData(shop);
			outPut.setCode(HttpStatus.OK);
			outPut.setSuccess(true);
			outPut.setMessage("success");
			return new ResponseEntity<ResponseValue>(outPut, HttpStatus.OK);
		
		} catch (BadCredentialsException e) {
			outPut.setSuccess(false);
			outPut.setCode(HttpStatus.BAD_REQUEST);
			outPut.setMessage(resource.getMessage("user.login.fail", null, new Locale("vi")));
			return new ResponseEntity<ResponseValue>(outPut, HttpStatus.BAD_REQUEST);
	
		} catch (DisabledException e) {
			outPut.setSuccess(false);
			outPut.setCode(HttpStatus.LOCKED);
			outPut.setMessage(resource.getMessage("user.login.notactive", null, new Locale("vi")));
			return new ResponseEntity<ResponseValue>(outPut, HttpStatus.LOCKED);
		}
	}

	@PostMapping(value = "/register", consumes = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<?> register(@Valid @RequestBody ShopDTO shop, BindingResult bindingResult) {
		ResponseEntity<?> error = validationBindingResult.process(bindingResult);
		if (error != null) {
			return error;
		}
		ResponseValue outPut = new ResponseValue(true, HttpStatus.OK.value(), "success");
		ShopDTO s = shopService.save(shop);
		Map<String,Object> m = new HashMap<String, Object>();
		m.put("id", s.getId());
		outPut = new ResponseValue(true, HttpStatus.OK.value(),
				resource.getMessage("user.register.success", null, new Locale("vi")));
		outPut.setData(m);
		return new ResponseEntity<ResponseValue>(outPut, HttpStatus.OK);
	}
	@PutMapping(value="/detail")
	public ResponseEntity<?> createShopDetail(@RequestBody ShopDetail d) {
		ResponseValue outPut = new ResponseValue(true, HttpStatus.OK.value(), "success");
		try {
			outPut.setData(shopService.createDetail(d));
		} catch (Exception e) {
			outPut.setSuccess(false);
			outPut.setMessage(e.getMessage());
			outPut.setCode(HttpStatus.BAD_REQUEST);
			return new ResponseEntity<ResponseValue>(outPut, HttpStatus.BAD_REQUEST);
		}
		return new ResponseEntity<ResponseValue>(outPut, HttpStatus.OK);
	}

	@GetMapping("/me")
	public ResponseEntity<?> getOneShop(Authentication auth) {
		try {
			ResponseValue outPut = new ResponseValue(true, HttpStatus.OK.value(), "success");
			ShopDTO shop = shopService.getOne(auth);
			shop.setToken(jwt.generateToken(shop));
			outPut.setData(shop);
			return new ResponseEntity<ResponseValue>(outPut, HttpStatus.OK);
		} catch (Exception e) {
			throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
		}
	}

	@GetMapping("/address")
	public ResponseEntity<?> getAddress(Authentication auth) {
		ResponseValue outPut = new ResponseValue(true, HttpStatus.OK.value(), "success");
		outPut.setData(shopService.getAddress(auth));
		return new ResponseEntity<ResponseValue>(outPut, HttpStatus.OK);
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
			@RequestParam(required = false, defaultValue = "0") int pageNumber, Authentication auth) {
		ResponseValue outPut = new ResponseValue(true, HttpStatus.OK.value(), "success");
		outPut.setData(productService.findByShopId(auth, pageSize, pageNumber));
		return new ResponseEntity<ResponseValue>(outPut, HttpStatus.OK);
	}


	@PostMapping("/avatar")
	public ResponseEntity<?> saveAvatar(@RequestBody UploadFileInput m, Authentication auth) {
		ResponseValue outPut = new ResponseValue(true, HttpStatus.OK.value(), "success");
		try {
			Map<String, String> s = new HashMap<String, String>();
			s.put("url", photoService.saveAvatar(m, auth));
			outPut.setData(s);
		} catch (Exception e) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage());
		}
		return new ResponseEntity<ResponseValue>(outPut, HttpStatus.OK);
	}

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
	
	@PutMapping
	public ResponseEntity<?> updateShop(@RequestBody ShopDTO shop, Authentication auth) {
		try {
			ResponseValue outPut = new ResponseValue(true, HttpStatus.OK.value(), "success");
			shopService.update(shop, auth);
			return new ResponseEntity<ResponseValue>(outPut, HttpStatus.OK);
		} catch (Exception e) {
			throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
		}
	}
	
	@GetMapping("/comment/new")
	public ResponseEntity<?> getlistCommentNew(Authentication auth, int limit) {
		ResponseValue outPut = new ResponseValue(true, HttpStatus.OK.value(), "success");
		outPut.setData(commentService.findByShopAndLimit(auth, limit));
		return new ResponseEntity<ResponseValue>(outPut, HttpStatus.OK);
	}
	
	@GetMapping("/order/list")
	public ResponseEntity<?> getListOrder(Authentication auth) {
		ResponseValue outPut = new ResponseValue(true, HttpStatus.OK.value(), "success");
		try {
			outPut.setData(orderService.getListOrder(auth));
		} catch (Exception e) {
			throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
		}
		return new ResponseEntity<ResponseValue>(outPut, HttpStatus.OK);
	}
	
	@GetMapping("/order")
	public ResponseEntity<?> getOrder(@RequestParam int id, Authentication auth) {
		ResponseValue outPut = new ResponseValue(true, HttpStatus.OK.value(), "success");
		try {
			outPut.setData(orderService.getOrder(id, auth));
			return new ResponseEntity<ResponseValue>(outPut, HttpStatus.OK);
		} catch (Exception e) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage());
		}
	}
	
	@PutMapping("/order/tranport")
	public ResponseEntity<?> updateStatusOrder(@RequestBody Map<String, String> data, Authentication auth) {
		try {
			ResponseValue outPut = new ResponseValue(true, HttpStatus.OK.value(), "success");
			orderService.updateStatus(Integer.valueOf(data.get("orderId")), auth, Common.ORDER_TRANSPORT);
			return new ResponseEntity<ResponseValue>(outPut, HttpStatus.OK);
		} catch (Exception e) {
			throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
		}
	}
	
	@PostMapping("/fcmtoken")
	public ResponseEntity<?> addFCMToken(@RequestBody Map<String, String> data, Authentication auth) {
		ResponseValue outPut = new ResponseValue(true, HttpStatus.OK.value(), "success");
		try {
			shopService.saveFCMToken(data.get("FCMToken"), auth);
			return new ResponseEntity<ResponseValue>(outPut, HttpStatus.OK);
		} catch (Exception e) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage());
		}
	}

}
