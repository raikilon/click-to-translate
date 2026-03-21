package ch.clicktotranslate.vocabulary.infrastructure.persistence;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.util.HashSet;
import java.time.Instant;
import java.util.Set;

@Getter
@Entity
@Table(uniqueConstraints = @UniqueConstraint(name = "uk_entry_user_language_term",
		columnNames = { "user_id", "language", "term" }))
@EntityListeners(AuditingEntityListener.class)
public class JpaEntryEntity {

	@Setter
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Setter
	@Column(name = "user_id", nullable = false)
	private String userId;

	@Setter
	@Column(nullable = false)
	private String language;

	@Setter
	@Column(nullable = false)
	private String term;

	@Setter
	private String termCustomization;

	@LastModifiedDate
	@Column(nullable = false)
	private Instant lastEdit;

	@CreatedDate
	@Column(nullable = false, updatable = false)
	private Instant createdAt;

	@Setter
	@ElementCollection(fetch = FetchType.EAGER)
	private Set<JpaTermTranslation> translations = new HashSet<>();

	@OneToMany(mappedBy = "entry", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.EAGER)
	private Set<JpaUsageEntity> usages = new HashSet<>();

	public void addUsage(JpaUsageEntity usage) {
		usage.setEntry(this);
		usages.add(usage);
	}

}
