package ch.clicktotranslate.vocabulary.infrastructure.web;

import ch.clicktotranslate.vocabulary.application.EntryNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice(basePackages = "ch.clicktotranslate.vocabulary.infrastructure.web")
public class ExceptionHandler {

	@org.springframework.web.bind.annotation.ExceptionHandler(EntryNotFoundException.class)
	public ResponseEntity<Void> handleNotFound(EntryNotFoundException ex) {
		return ResponseEntity.notFound().build();
	}

	@org.springframework.web.bind.annotation.ExceptionHandler(IllegalArgumentException.class)
	public ResponseEntity<Void> handleBadRequest(IllegalArgumentException ex) {
		return ResponseEntity.badRequest().build();
	}

	@org.springframework.web.bind.annotation.ExceptionHandler(IllegalStateException.class)
	public ResponseEntity<Void> handleUnauthorized(IllegalStateException ex) {
		return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
	}

}
