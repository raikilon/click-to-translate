package ch.clicktotranslate.auth;

import org.jmolecules.ddd.types.ValueObject;

public record UserId(String value) implements ValueObject {

	public static UserId of(String value) {
		return new UserId(value);
	}

	public UserId {
		if (value == null || value.isBlank()) {
			throw new IllegalArgumentException("userId must not be blank");
		}
		value = value.trim();
	}

}
