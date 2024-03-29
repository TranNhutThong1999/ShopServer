package kltn.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
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


import kltn.api.input.UpdateDetailProduct;
import kltn.api.input.UpdateInforProduct;
import kltn.api.output.CommentOuput;
import kltn.api.output.ResponseValue;
import kltn.dto.PhotoDTO;
import kltn.dto.ProductDTO;
import kltn.service.ICommentService;
import kltn.service.IProductService;

@RestController
@RequestMapping("/product")
public class ProductController {

	@Autowired
	private IProductService productService;

	@Autowired
	private ICommentService commentService;

	@GetMapping(produces = "application/json")
	public ResponseEntity<?> getOneProduct(@RequestParam(required = true) int id) {
		try {
			ResponseValue outPut = new ResponseValue(true, HttpStatus.OK.value(), "success");
			outPut.setData(productService.findOneById(id));
			
			return new ResponseEntity<ResponseValue>(outPut, HttpStatus.OK);
		} catch (Exception e) {
			throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
		}
	}
	@PostMapping
	@PreAuthorize("isAuthenticated()")
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
	@PreAuthorize("isAuthenticated()")
	public ResponseEntity<?> comment(Authentication auth, @RequestBody CommentOuput m) {
		ResponseValue outPut = new ResponseValue(true, HttpStatus.OK.value(), "success");
		try {
			outPut.setData(commentService.save(m, auth));
		} catch (Exception e) {
			throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
		}
		return new ResponseEntity<ResponseValue>(outPut, HttpStatus.OK);
	}

	@GetMapping(value = "/comment", produces = "application/json")
	@PreAuthorize("isAuthenticated()")
	public ResponseEntity<?> getCommentByProductId(@RequestParam(required = true) int productId, Authentication auth) {
		ResponseValue outPut = new ResponseValue(true, HttpStatus.OK.value(), "success");
		outPut.setData(commentService.getListCommentProduct(productId, auth));
		return new ResponseEntity<ResponseValue>(outPut, HttpStatus.OK);
	}
	
//	@PostMapping(value = "photo")
//	public ResponseEntity<?> addPhoto(Authentication auth, @RequestBody List<UploadFileInput> m, @RequestParam int id) {
//		ResponseValue outPut = new ResponseValue(true, HttpStatus.OK.value(), "success");
//		try {
//			photoService.savePhotosProduct(id, m, auth);
//		} catch (Exception e) {
//			throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
//		}
//		return new ResponseEntity<ResponseValue>(outPut, HttpStatus.OK);
//	}
//	
//	@DeleteMapping(value = "photo")
//	public ResponseEntity<?> deletePhoto(Authentication auth, @RequestBody Map<String, String> data) {
//		ResponseValue outPut = new ResponseValue(true, HttpStatus.OK.value(), "success");
//		try {
//			productService.deletePhoto(auth, Integer.valueOf(data.get("photoId")), Integer.valueOf(data.get("productId")));
//		} catch (Exception e) {
//			throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
//		}
//		return new ResponseEntity<ResponseValue>(outPut, HttpStatus.OK);
//	}
	
	@PutMapping("/infor")
	@PreAuthorize("isAuthenticated()")
	public ResponseEntity<?> UpdateProduct(@RequestBody UpdateInforProduct product, Authentication auth) {
		ResponseValue outPut = new ResponseValue(true, HttpStatus.OK.value(), "success");
		try {
			productService.updateInfor(product, auth);
		} catch (Exception e) {
			throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
		}
		return new ResponseEntity<ResponseValue>(outPut, HttpStatus.OK);
	}
	
	@PutMapping("/detail")
	@PreAuthorize("isAuthenticated()")
	public ResponseEntity<?> UpdateProductDetail(@RequestBody UpdateDetailProduct detail, Authentication auth) {
		ResponseValue outPut = new ResponseValue(true, HttpStatus.OK.value(), "success");
		try {
			productService.updateDetail(detail, auth);
		} catch (Exception e) {
			throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
		}
		return new ResponseEntity<ResponseValue>(outPut, HttpStatus.OK);
	}
	
	@DeleteMapping
	@PreAuthorize("isAuthenticated()")
	public ResponseEntity<?> deleteProduct(Authentication auth, @RequestParam int id ) {
		ResponseValue outPut = new ResponseValue(true, HttpStatus.OK.value(), "success");
		try {
			productService.delete(id, auth);
		} catch (Exception e) {
			throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
		}
		return new ResponseEntity<ResponseValue>(outPut, HttpStatus.OK);
	}
	
	@GetMapping("/photo")
	@PreAuthorize("isAuthenticated()")
	public ResponseEntity<?> getlistPhoto(Authentication auth) {
		ResponseValue outPut = new ResponseValue(true, HttpStatus.OK.value(), "success");
		List<PhotoDTO> photo = productService.getListPhotoByShop(auth);
		outPut.setData(photo);
		return new ResponseEntity<ResponseValue>(outPut, HttpStatus.OK);
	}
}
