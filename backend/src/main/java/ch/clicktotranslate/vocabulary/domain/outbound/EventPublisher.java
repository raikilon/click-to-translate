package ch.clicktotranslate.vocabulary.domain.outbound;

import ch.clicktotranslate.vocabulary.domain.event.WordCreatedEvent;
import ch.clicktotranslate.vocabulary.domain.event.WordDeletedEvent;
import ch.clicktotranslate.vocabulary.domain.event.WordSeenEvent;

public interface EventPublisher {
  void publish(WordCreatedEvent event);

  void publish(WordDeletedEvent event);

  void publish(WordSeenEvent event);
}
