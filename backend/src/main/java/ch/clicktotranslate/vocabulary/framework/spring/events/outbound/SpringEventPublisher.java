package ch.clicktotranslate.vocabulary.framework.spring.events.outbound;


import ch.clicktotranslate.vocabulary.domain.event.WordCreatedEvent;
import ch.clicktotranslate.vocabulary.domain.event.WordDeletedEvent;
import ch.clicktotranslate.vocabulary.domain.event.WordSeenEvent;
import ch.clicktotranslate.vocabulary.domain.outbound.EventPublisher;
import ch.clicktotranslate.vocabulary.framework.spring.events.outbound.dto.WordCreatedEventDto;
import ch.clicktotranslate.vocabulary.framework.spring.events.outbound.dto.WordDeletedEventDto;
import ch.clicktotranslate.vocabulary.framework.spring.events.outbound.dto.WordSeenEventDto;
import ch.clicktotranslate.vocabulary.framework.spring.events.outbound.mapper.WordCreatedEventMapper;
import ch.clicktotranslate.vocabulary.framework.spring.events.outbound.mapper.WordDeletedEventMapper;
import ch.clicktotranslate.vocabulary.framework.spring.events.outbound.mapper.WordSeenEventMapper;
import org.springframework.context.ApplicationEventPublisher;

public class SpringEventPublisher implements EventPublisher {
  private final ApplicationEventPublisher applicationEventPublisher;
  private final WordCreatedEventMapper wordCreatedEventMapper;
  private final WordDeletedEventMapper wordDeletedEventMapper;
  private final WordSeenEventMapper wordSeenEventMapper;

  public SpringEventPublisher(ApplicationEventPublisher applicationEventPublisher,
                              WordCreatedEventMapper wordCreatedEventMapper,
                              WordDeletedEventMapper wordDeletedEventMapper,
                              WordSeenEventMapper wordSeenEventMapper) {
    this.applicationEventPublisher = applicationEventPublisher;
    this.wordCreatedEventMapper = wordCreatedEventMapper;
    this.wordDeletedEventMapper = wordDeletedEventMapper;
    this.wordSeenEventMapper = wordSeenEventMapper;
  }

  @Override
  public void publish(WordCreatedEvent event) {
    WordCreatedEventDto eventDto = wordCreatedEventMapper.map(event);
    applicationEventPublisher.publishEvent(eventDto);
  }

  @Override
  public void publish(WordDeletedEvent event) {
    WordDeletedEventDto eventDto = wordDeletedEventMapper.map(event);
    applicationEventPublisher.publishEvent(eventDto);
  }

  @Override
  public void publish(WordSeenEvent event) {
    WordSeenEventDto eventDto = wordSeenEventMapper.map(event);
    applicationEventPublisher.publishEvent(eventDto);
  }
}
