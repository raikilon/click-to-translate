package ch.clicktotranslate.translation.application.translation.provider.deepl;

import ch.clicktotranslate.translation.domain.Language;

public class DeepLLanguageMapper {

	public String toDeepLCode(Language language) {
		return switch (language) {
			case DE -> "de";
			case EN -> "en";
			case ES -> "es";
			case FR -> "fr";
			case IT -> "it";
			case NB -> "nb";
			case DA -> "da";
			case ET -> "et";
			case FI -> "fi";
			case PT -> "pt";
			case SL -> "sl";
			case SV -> "sv";
		};
	}

}
