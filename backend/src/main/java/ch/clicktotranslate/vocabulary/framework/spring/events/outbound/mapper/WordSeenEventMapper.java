package ch.clicktotranslate.vocabulary.framework.spring.events.outbound.mapper;

import ch.clicktotranslate.vocabulary.domain.event.WordSeenEvent;
import ch.clicktotranslate.vocabulary.framework.spring.events.outbound.dto.WordSeenEventDto;

public class WordSeenEventMapper {
  public WordSeenEventDto map(WordSeenEvent event) {
    return new WordSeenEventDto();
  }

}
