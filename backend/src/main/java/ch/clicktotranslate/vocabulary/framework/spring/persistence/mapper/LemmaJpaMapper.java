package ch.clicktotranslate.vocabulary.framework.spring.persistence.mapper;

import ch.clicktotranslate.vocabulary.domain.entity.Lemma;
import ch.clicktotranslate.vocabulary.framework.spring.persistence.entity.JpaLemmaEntity;

public class LemmaJpaMapper {
  public Lemma toRecord(JpaLemmaEntity entity) {
    Lemma record = new Lemma();
    return record;
  }

  public JpaLemmaEntity toEntity(Lemma record) {
    JpaLemmaEntity entity = new JpaLemmaEntity();
    return entity;
  }
}
