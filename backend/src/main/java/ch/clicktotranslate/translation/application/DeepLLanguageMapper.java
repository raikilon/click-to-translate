package ch.clicktotranslate.translation.application;

import ch.clicktotranslate.translation.domain.Language;

public class DeepLLanguageMapper {

	public String toDeepLCode(Language language) {
		return switch (language) {
			case DE -> "DE";
			case EN -> "EN";
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
