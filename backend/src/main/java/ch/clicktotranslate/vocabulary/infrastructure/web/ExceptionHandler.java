package ch.clicktotranslate.vocabulary.infrastructure.web;

import ch.clicktotranslate.vocabulary.application.EntryNotFoundException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

@RestControllerAdvice(basePackages = "ch.clicktotranslate")
public class ExceptionHandler {

	@org.springframework.web.bind.annotation.ExceptionHandler(EntryNotFoundException.class)
	public ResponseEntity<Void> handleNotFound(EntryNotFoundException ex) {
		return ResponseEntity.notFound().build();
	}

	@org.springframework.web.bind.annotation.ExceptionHandler(IllegalArgumentException.class)
	public ResponseEntity<Void> handleBadRequest(IllegalArgumentException ex) {
		return ResponseEntity.badRequest().build();
	}

	@org.springframework.web.bind.annotation.ExceptionHandler(AuthenticationException.class)
	public ResponseEntity<Void> handleUnauthorized(AuthenticationException ex) {
		return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
	}

	@org.springframework.web.bind.annotation.ExceptionHandler({ HttpMessageNotReadableException.class,
			MethodArgumentTypeMismatchException.class })
	public ResponseEntity<Void> handleMalformedRequest(RuntimeException ex) {
		return ResponseEntity.badRequest().build();
	}

	@org.springframework.web.bind.annotation.ExceptionHandler(IllegalStateException.class)
	public ResponseEntity<Void> handleServerError(IllegalStateException ex) {
		return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
	}

}
