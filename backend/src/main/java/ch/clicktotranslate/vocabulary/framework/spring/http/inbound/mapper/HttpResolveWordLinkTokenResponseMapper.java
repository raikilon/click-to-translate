package ch.clicktotranslate.vocabulary.framework.spring.http.inbound.mapper;

import ch.clicktotranslate.vocabulary.domain.usecase.output.ResolveTempRefOutput;
import ch.clicktotranslate.vocabulary.framework.spring.http.inbound.dto.HttpResolveWorkLinkTokenResponse;

public class HttpResolveWordLinkTokenResponseMapper {
	public HttpResolveWorkLinkTokenResponse map(ResolveTempRefOutput output) {
		return new HttpResolveWorkLinkTokenResponse();
	}
}
