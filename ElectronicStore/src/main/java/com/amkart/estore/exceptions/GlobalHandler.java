package com.amkart.estore.exceptions;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.amkart.estore.dtos.UserResponse;

@RestControllerAdvice
public class GlobalHandler {

	@ExceptionHandler(ResourseNotFoundException.class)
	public ResponseEntity<UserResponse> resourseNotFoundExceptionHandler(ResourseNotFoundException e) {
		UserResponse resp = UserResponse.builder().message(e.getMessage()).status("fail").build();
		return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(resp);

	}
	
	@ExceptionHandler(OutOfStockException.class)
	public ResponseEntity<UserResponse> outOfStockExceptionHandler(OutOfStockException e) {
		UserResponse resp = UserResponse.builder().message(e.getMessage()).status("fail").build();
		return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(resp);

	}

	@ExceptionHandler(MethodArgumentNotValidException.class)

	public ResponseEntity<Map<Object, String>> MethodArgumentNotValidExceptionHandler(
			MethodArgumentNotValidException e) {
		Map<Object, String> errordata = new HashMap<>();
		List<ObjectError> errlist = e.getBindingResult().getAllErrors();
		errlist.stream().forEach((error) -> {
            String fieldName = ((FieldError) error).getField();
            String errorMessage = error.getDefaultMessage();
            errordata.put(fieldName, errorMessage);
        });
		return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errordata);

	}

}
