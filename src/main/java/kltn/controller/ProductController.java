package kltn.controller;

import java.security.Principal;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import com.google.rpc.context.AttributeContext.Auth;

import kltn.api.input.UploadFileInput;
import kltn.api.output.CommentOuput;
import kltn.api.output.ResponseValue;
import kltn.dto.ProductDTO;
import kltn.service.ICommentService;
import kltn.service.IPhotoService;
import kltn.service.IProductService;
import kltn.service.IRatingService;
import kltn.service.impl.PhotoService;
import lombok.Delegate;

@RestController
@RequestMapping("/product")
public class ProductController {

	@Autowired
	private IProductService productService;

	@Autowired
	private ICommentService commentService;

	@Autowired
	private IRatingService ratingService;

	@Autowired
	private IPhotoService photoService;

	@GetMapping(produces = "application/json")
	public ResponseEntity<?> getOneProduct(@RequestParam(required = true) int id) {
		try {
			ResponseValue outPut = new ResponseValue(true, HttpStatus.OK.value(), "success");
			ProductDTO product = productService.findOneById(id);
			outPut.setData(product);
			return new ResponseEntity<ResponseValue>(outPut, HttpStatus.OK);
		} catch (Exception e) {
			throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
		}
	}
//
//	
//	@GetMapping(value ="/comment", produces = "application/json")
//	public ResponseEntity<?> getCommentByProductId(@RequestParam(required = true) int productId,
//			@RequestParam(required = false, defaultValue = "5") int pageSize,
//			@RequestParam(required = false, defaultValue = "0") int pageNumber) {
//		ResponseValue outPut = new ResponseValue(true, HttpStatus.OK.value(), "success");
//		outPut.setData(commentService.findByProductId(productId, pageSize, pageNumber));
//		return new ResponseEntity<ResponseValue>(outPut, HttpStatus.OK);
//	}
	@PostMapping
	public ResponseEntity<?> createProduct(@RequestBody ProductDTO product, Authentication auth) {
		ResponseValue outPut = new ResponseValue(true, HttpStatus.OK.value(), "success");
		try {
			outPut.setData(productService.save(product, auth));
		} catch (Exception e) {
			System.out.println(e.getLocalizedMessage());
			throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
		}
		return new ResponseEntity<ResponseValue>(outPut, HttpStatus.OK);
	}

	@PostMapping(value = "/comment")
	public ResponseEntity<?> comment(Principal principal, @RequestBody CommentOuput m) {
		ResponseValue outPut = new ResponseValue(true, HttpStatus.OK.value(), "success");
		try {
			outPut.setData(commentService.save(m, principal.getName()));
		} catch (Exception e) {
			throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
		}
		return new ResponseEntity<ResponseValue>(outPut, HttpStatus.OK);
	}

	@PostMapping(value = "photo")
	public ResponseEntity<?> addPhoto(Authentication auth, @RequestBody List<UploadFileInput> m, @RequestParam int id) {
		ResponseValue outPut = new ResponseValue(true, HttpStatus.OK.value(), "success");
		try {
			photoService.savePhotosProduct(id, m, auth);
		} catch (Exception e) {
			throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
		}
		return new ResponseEntity<ResponseValue>(outPut, HttpStatus.OK);
	}
	
	@DeleteMapping(value = "photo")
	public ResponseEntity<?> deletePhoto(Authentication auth, @RequestBody Map<String, String> data) {
		ResponseValue outPut = new ResponseValue(true, HttpStatus.OK.value(), "success");
		try {
			productService.deletePhoto(auth, Integer.valueOf(data.get("photoId")), Integer.valueOf(data.get("productId")));
		} catch (Exception e) {
			throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
		}
		return new ResponseEntity<ResponseValue>(outPut, HttpStatus.OK);
	}
	
	@PutMapping
	public ResponseEntity<?> UpdateProduct(@RequestBody ProductDTO product, Authentication auth) {
		ResponseValue outPut = new ResponseValue(true, HttpStatus.OK.value(), "success");
		try {
			productService.save(product, auth);
		} catch (Exception e) {
			throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
		}
		return new ResponseEntity<ResponseValue>(outPut, HttpStatus.OK);
	}
}
