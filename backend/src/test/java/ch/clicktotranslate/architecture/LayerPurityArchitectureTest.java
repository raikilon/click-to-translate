package ch.clicktotranslate.architecture;

import ch.clicktotranslate.ClickToTranslateApplication;
import com.tngtech.archunit.core.importer.ImportOption;
import com.tngtech.archunit.junit.AnalyzeClasses;
import com.tngtech.archunit.junit.ArchTest;
import com.tngtech.archunit.lang.ArchRule;

import static com.tngtech.archunit.lang.syntax.ArchRuleDefinition.noClasses;

@AnalyzeClasses(packagesOf = ClickToTranslateApplication.class, importOptions = ImportOption.DoNotIncludeTests.class)
class LayerPurityArchitectureTest {

	private static final String[] PURE_LAYER_PACKAGES = { "ch.clicktotranslate..application..",
			"ch.clicktotranslate..domain.." };

	@ArchTest
	static final ArchRule application_and_domain_should_not_depend_on_spring = noClasses().that()
		.resideInAnyPackage(PURE_LAYER_PACKAGES)
		.and()
		.doNotHaveFullyQualifiedName("ch.clicktotranslate.segment.domain.SegmentBundleCreatedEvent")
		.and()
		.doNotHaveFullyQualifiedName("ch.clicktotranslate.lemmatizer.domain.SegmentBundleLemmatizedEvent")
		.should()
		.dependOnClassesThat()
		.resideInAnyPackage("org.springframework..");

}
