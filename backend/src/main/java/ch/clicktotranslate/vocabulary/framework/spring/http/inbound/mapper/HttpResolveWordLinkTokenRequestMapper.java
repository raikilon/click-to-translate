package ch.clicktotranslate.vocabulary.framework.spring.http.inbound.mapper;

import ch.clicktotranslate.vocabulary.domain.usecase.input.ResolveTempRefInput;
import ch.clicktotranslate.vocabulary.framework.spring.http.inbound.dto.HttpResolveWordLinkTokenRequest;

public class HttpResolveWordLinkTokenRequestMapper {
	public ResolveTempRefInput map(HttpResolveWordLinkTokenRequest request) {
		return new ResolveTempRefInput();
	}
}
