package kltn.controller;

import java.security.Principal;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import kltn.api.output.ResponseValue;
import kltn.dto.ProductDTO;
import kltn.service.ICommentService;
import kltn.service.IProductService;
import kltn.service.IRatingService;

@RestController
@RequestMapping("/product")
public class ProductController {

	@Autowired
	private IProductService productService;

	@Autowired
	private ICommentService commentService;

	@Autowired
	private IRatingService ratingService;

//	@GetMapping(produces = "application/json")
//	public ResponseEntity<?> getOneProduct(@RequestParam(required = true) int id) {
//		try {
//			ResponseValue outPut = new ResponseValue(true, HttpStatus.OK.value(), "success");
//			ProductDTO product = productService.findOneById(id);
//			outPut.setData(product);
//			return new ResponseEntity<ResponseValue>(outPut, HttpStatus.OK);
//		} catch (Exception e) {
//			throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
//		}
//	}
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
	@PostMapping(produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<?> createProduct(@RequestBody ProductDTO product, Principal principal){
		ResponseValue outPut = new ResponseValue(true, HttpStatus.OK.value(), "success");
		outPut.setData(productService.save(product, principal.getName()));
		return new ResponseEntity<ResponseValue>(outPut, HttpStatus.OK);
	} 
}
