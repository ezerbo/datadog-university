package com.demo.tuition.error;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestControllerAdvice
public class ErrorTranslator {

	@ExceptionHandler(Exception.class)
	public ResponseEntity<ErrorVM> translate(Exception ex) {
		log.error("error {}", ex.getMessage());
		ex.printStackTrace();
		ErrorVM errorVM = toErrorVM(ex);
		return new ResponseEntity<>(errorVM, HttpStatus.INTERNAL_SERVER_ERROR);
	}


	@ResponseStatus(HttpStatus.NOT_FOUND)
	@ExceptionHandler(NoTuitionFoundException.class)
	public ErrorVM translate(NoTuitionFoundException ex) {
		log.error("error {}", ex.getMessage());
		ex.printStackTrace();
		return toErrorVM(ex);
	}
	
	@ResponseStatus(HttpStatus.BAD_REQUEST)
	@ExceptionHandler(MethodArgumentNotValidException.class)
	public ErrorVM translate(MethodArgumentNotValidException ex) {
		if(log.isDebugEnabled()) ex.printStackTrace();
		List<FieldErrorVM> errors = ex.getBindingResult().getFieldErrors().stream()
				.map(error -> FieldErrorVM.builder()
						.field(error.getField())
						.objectName(error.getObjectName())
						.message(error.getDefaultMessage())
						.rejectedValue(Objects.toString(error.getRejectedValue()))
						.build()
				).collect(Collectors.toList());
		log.error("error {}", ex.getMessage());
		return ErrorVM.builder()
				.description("Unable to process request")
				.message("Invalid payload")
				.fieldErrors(errors)
				.build();
	}
	
	private ErrorVM toErrorVM(Exception e) {
		return ErrorVM.builder()
				.description("Unable to process request")
				.message(e.getMessage())
				.build();
	}
}