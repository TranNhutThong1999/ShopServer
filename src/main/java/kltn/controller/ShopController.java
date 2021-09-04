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
import org.springframework.security.access.prepost.PreAuthorize;
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
import kltn.util.EmailService;
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
	private ResourceBundleMessageSource resource;
	
	@Autowired
	private ICommentService commentService;
	
	@Autowired
	private IOrderService orderService;
	
	@PostMapping(value = "/login", consumes = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<?> login(@RequestBody LoginInput s) {
		ResponseValue outPut = new ResponseValue();
		try {
			Authentication auth = authenticationManager
					.authenticate(new UsernamePasswordAuthenticationToken(s.getEmail(), s.getPassword()));
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
			System.out.println("er");
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
	@PreAuthorize("isAuthenticated()")
	public ResponseEntity<?> createShopDetail(@RequestBody ShopDetail d) {
		ResponseValue outPut = new ResponseValue(true, HttpStatus.OK.value(), "success");
		try {
			outPut.setData(shopService.createDetail(d));
		} catch (Exception e) {
			System.out.println(e.getLocalizedMessage());
			outPut.setSuccess(false);
			outPut.setMessage(e.getLocalizedMessage());
			outPut.setCode(HttpStatus.BAD_REQUEST);
			return new ResponseEntity<ResponseValue>(outPut, HttpStatus.BAD_REQUEST);
		}
		return new ResponseEntity<ResponseValue>(outPut, HttpStatus.OK);
	}

	@GetMapping("/me")
	@PreAuthorize("isAuthenticated()")
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
	@PreAuthorize("isAuthenticated()")
	public ResponseEntity<?> getAddress(Authentication auth) {
		ResponseValue outPut = new ResponseValue(true, HttpStatus.OK.value(), "success");
		outPut.setData(shopService.getAddress(auth));
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
	@PreAuthorize("isAuthenticated()")
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
	public ResponseEntity<?> sendCode(@RequestBody Map<String, String> data) {
		try {
			ResponseValue outPut = new ResponseValue(true, HttpStatus.OK.value(), "success");
			Map<String, String> s = new HashMap<String, String>();
			s.put("shopId", shopService.sendOTP(data.get("email")));
			outPut.setData(s);
			return new ResponseEntity<ResponseValue>(outPut, HttpStatus.OK);
		} catch (Exception e) {
			throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
		}
	}

	@PostMapping("/checkotp")
	public ResponseEntity<?> checkOTP(@RequestBody Map<String, String> data) {
		try {
			ResponseValue outPut = new ResponseValue(true, HttpStatus.OK.value(), "success");
			shopService.checkOTP(data.get("otp"), data.get("shopId"));
			return new ResponseEntity<ResponseValue>(outPut, HttpStatus.OK);
		} catch (Exception e) {
			throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
		}
	}

	@PostMapping("/verify")
	public ResponseEntity<?> verifyRegister(@RequestBody OtpInput input) {
		try {
			ResponseValue outPut = new ResponseValue(true, HttpStatus.OK.value(), "success");
			shopService.verify(input.getOtp(), input.getShopId());
			return new ResponseEntity<ResponseValue>(outPut, HttpStatus.OK);
		} catch (Exception e) {
			throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
		}
	}

	@PutMapping("changepassword")
	@PreAuthorize("isAuthenticated()")
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
			shopService.updatePassword(data.get("shopId"), data.get("password"), data.get("otp"));
			return new ResponseEntity<ResponseValue>(outPut, HttpStatus.OK);
		} catch (Exception e) {
			throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
		}
	}
	
	@PutMapping
	@PreAuthorize("isAuthenticated()")
	public ResponseEntity<?> updateShop(@RequestBody ShopDTO shop, Authentication auth) {
		try {
			ResponseValue outPut = new ResponseValue(true, HttpStatus.OK.value(), "success");
			outPut.setData(shopService.update(shop, auth));
			return new ResponseEntity<ResponseValue>(outPut, HttpStatus.OK);
		} catch (Exception e) {
			throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
		}
	}
	
	@GetMapping("/comment/new")
	@PreAuthorize("isAuthenticated()")
	public ResponseEntity<?> getlistCommentNew(Authentication auth, int limit) {
		ResponseValue outPut = new ResponseValue(true, HttpStatus.OK.value(), "success");
		outPut.setData(commentService.findByShopAndLimit(auth, limit));
		return new ResponseEntity<ResponseValue>(outPut, HttpStatus.OK);
	}
	
	@GetMapping("/order/list")
	@PreAuthorize("isAuthenticated()")
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
	@PreAuthorize("isAuthenticated()")
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
	@PreAuthorize("isAuthenticated()")
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
	@PreAuthorize("isAuthenticated()")
	public ResponseEntity<?> addFCMToken(@RequestBody Map<String, String> data, Authentication auth) {
		ResponseValue outPut = new ResponseValue(true, HttpStatus.OK.value(), "success");
		try {
			shopService.saveFCMToken(data.get("FCMToken"), auth);
			return new ResponseEntity<ResponseValue>(outPut, HttpStatus.OK);
		} catch (Exception e) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage());
		}
	}
	
	@GetMapping("/notification")
	@PreAuthorize("isAuthenticated()")
	public ResponseEntity<?> getListNoti(Authentication auth) {
		ResponseValue outPut = new ResponseValue(true, HttpStatus.OK.value(), "success");
		try {
			outPut.setData(shopService.getListNoti(auth));
		} catch (Exception e) {
			throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
		}
		return new ResponseEntity<ResponseValue>(outPut, HttpStatus.OK);
	}
	
	@PutMapping("/notification")
	@PreAuthorize("isAuthenticated()")
	public ResponseEntity<?> UpdateIsSeen(Authentication auth, @RequestBody Map<String, Integer> data) {
		ResponseValue outPut = new ResponseValue(true, HttpStatus.OK.value(), "success");
		try {
			shopService.setIsSeen(auth, data.get("id"));
		} catch (Exception e) {
			throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
		}
		return new ResponseEntity<ResponseValue>(outPut, HttpStatus.OK);
	}
	
	@PutMapping("/order/cancel")
	@PreAuthorize("isAuthenticated()")
	public ResponseEntity<?> cancelOrder(Authentication auth, @RequestBody Map<String, String> data) {
		ResponseValue outPut = new ResponseValue(true, HttpStatus.OK.value(), "success");
		try {
			orderService.updateCancelStatus(Integer.valueOf(data.get("orderId")), data.get("reason"),auth);
		} catch (Exception e) {
			throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
		}
		return new ResponseEntity<ResponseValue>(outPut, HttpStatus.OK);
	}
}
