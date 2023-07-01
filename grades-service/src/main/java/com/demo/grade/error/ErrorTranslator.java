package com.demo.grade.error;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Slf4j
@RestControllerAdvice
public class ErrorTranslator {

	@ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
	@ExceptionHandler(Exception.class)
	public ErrorVM translate(Exception ex) {
		log.error("error {}", ex.getMessage());
		ex.printStackTrace();
		return toErrorVM(ex);
	}

	@ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
	@ExceptionHandler(UnpaidTuitionException.class)
	public ErrorVM translate(UnpaidTuitionException ex) {
		log.error("error {}", ex.getMessage());
		ex.printStackTrace();
		return toErrorVM(ex, ErrorCode.UNPAID_TUITION);
	}

	@ResponseStatus(HttpStatus.NOT_FOUND)
	@ExceptionHandler(NoGradeFoundException.class)
	public ErrorVM translate(NoGradeFoundException ex) {
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
		return toErrorVM(e, null);
	}

	private ErrorVM toErrorVM(Exception e, ErrorCode errorCode) {
		return ErrorVM.builder()
				.errorCode(errorCode)
				.description("Unable to process request")
				.message(e.getMessage())
				.build();
	}
}