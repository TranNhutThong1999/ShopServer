package kltn.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import kltn.api.output.ResponseValue;
import kltn.service.ICategoryService;
import kltn.service.ICountryService;

@RestController
@RequestMapping("/basic")
public class BasicController {
	@Autowired
	private ICategoryService categoryService;

	@Autowired
	private ICountryService countryService;
	
	@GetMapping("/category")
	public ResponseEntity<?> getlistCategory() {
		ResponseValue outPut = new ResponseValue(true, HttpStatus.OK.value(), "success");
		outPut.setData(categoryService.findAll());
		return new ResponseEntity<ResponseValue>(outPut, HttpStatus.OK);
	}
	
	@GetMapping("/category/parent")
	public ResponseEntity<?> getlistCategoryParrent() {
		ResponseValue outPut = new ResponseValue(true, HttpStatus.OK.value(), "success");
		outPut.setData(categoryService.findAllCategoryParrent());
		return new ResponseEntity<ResponseValue>(outPut, HttpStatus.OK);
	}
	
	@GetMapping("/category/childrent")
	public ResponseEntity<?> getlistCategoryChildrent(@RequestParam int parentId) {
		ResponseValue outPut = new ResponseValue(true, HttpStatus.OK.value(), "success");
		outPut.setData(categoryService.findAllCategorychildrent(parentId));
		return new ResponseEntity<ResponseValue>(outPut, HttpStatus.OK);
	}
	
	@GetMapping("/country")
	public ResponseEntity<?> getCountry() {
		ResponseValue outPut = new ResponseValue(true, HttpStatus.OK.value(), "success");
		outPut.setData(countryService.findAll());
		return new ResponseEntity<ResponseValue>(outPut, HttpStatus.OK);
	}
}
