package kltn.util;

import java.util.HashMap;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;

import kltn.api.output.Errors;

@Component
public class ValidationBindingResult {
	public ResponseEntity<?> process(BindingResult bindingResult) {

		if (bindingResult.hasErrors()) {
			Map<String, String> map = new HashMap<String, String>();
			for (FieldError o : bindingResult.getFieldErrors()) {
				map.put(o.getField(), o.getDefaultMessage());
			}
			Errors error = new Errors(HttpStatus.BAD_REQUEST.value(), map);
			return new ResponseEntity<Object>(error, HttpStatus.BAD_REQUEST);
		}
		return null;
	}
}
