package ch.clicktotranslate.architecture;

import static com.tngtech.archunit.library.Architectures.layeredArchitecture;

import org.junit.jupiter.api.Test;

import com.tngtech.archunit.core.domain.JavaClasses;
import com.tngtech.archunit.core.importer.ClassFileImporter;
import com.tngtech.archunit.core.importer.ImportOption.DoNotIncludeJars;
import com.tngtech.archunit.core.importer.ImportOption.DoNotIncludeTests;

class VocabularyCleanArchitectureTest {

    @Test
    void clean_architecture_layers_are_respected() {

        JavaClasses importedClasses = new ClassFileImporter()
                .withImportOption(new DoNotIncludeTests())
                .withImportOption(new DoNotIncludeJars())
                .importPackages("ch.clicktotranslate.vocabulary");

        layeredArchitecture()
                .consideringAllDependencies()
                .layer("Domain").definedBy("..vocabulary.domain..")
                .layer("Infrastructure").definedBy("..vocabulary.infrastructure..")
                .layer("Framework").definedBy("..vocabulary.framework..")
                .whereLayer("Domain")
                .mayOnlyBeAccessedByLayers("Infrastructure", "Framework")
                .whereLayer("Infrastructure")
                .mayOnlyBeAccessedByLayers("Framework")
                .whereLayer("Framework")
                .mayNotBeAccessedByAnyLayer()
                .check(importedClasses);
    }
}
