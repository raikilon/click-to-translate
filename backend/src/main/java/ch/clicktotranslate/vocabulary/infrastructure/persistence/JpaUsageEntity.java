package ch.clicktotranslate.vocabulary.infrastructure.persistence;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.Instant;

@Getter
@Entity
@EntityListeners(AuditingEntityListener.class)
public class JpaUsageEntity {

	@Setter
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Setter
	@ManyToOne(optional = false)
	@JoinColumn
	private JpaEntryEntity entry;

	@Setter
	@Column(nullable = false)
	private String sentence;

	@Setter
	@Column(nullable = false)
	private Integer sentenceStart;

	@Setter
	@Column(nullable = false)
	private Integer sentenceEnd;

	@Setter
	@Column(nullable = false)
	private String translation;

	@Setter
	@Column(nullable = true)
	private Integer translationStart;

	@Setter
	@Column(nullable = true)
	private Integer translationEnd;

	@Setter
	@Column(nullable = false)
	private String language;

	@Setter
	@Column(nullable = false)
	private boolean starred;

	@LastModifiedDate
	@Column(nullable = false)
	private Instant lastEdit;

	@CreatedDate
	@Column(nullable = false, updatable = false)
	private Instant createdAt;

}
