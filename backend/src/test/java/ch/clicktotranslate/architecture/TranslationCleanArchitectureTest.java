package ch.clicktotranslate.architecture;

import org.junit.jupiter.api.Test;

import com.tngtech.archunit.core.domain.JavaClasses;
import com.tngtech.archunit.core.importer.ClassFileImporter;
import com.tngtech.archunit.core.importer.ImportOption.DoNotIncludeJars;
import com.tngtech.archunit.core.importer.ImportOption.DoNotIncludeTests;
import static com.tngtech.archunit.library.Architectures.layeredArchitecture;

class TranslationCleanArchitectureTest {

    @Test
    void clean_architecture_layers_are_respected() {

        JavaClasses importedClasses = new ClassFileImporter()
                .withImportOption(new DoNotIncludeTests())
                .withImportOption(new DoNotIncludeJars())
                .importPackages("ch.clicktotranslate.translation");

        layeredArchitecture()
                .consideringAllDependencies()
                .layer("Domain").definedBy("..translation.domain..")
                .layer("Infrastructure").definedBy("..translation.infrastructure..")
                .layer("Framework").definedBy("..translation.framework..")
                .whereLayer("Domain")
                .mayOnlyBeAccessedByLayers("Infrastructure", "Framework")
                .whereLayer("Infrastructure")
                .mayOnlyBeAccessedByLayers("Framework")
                .whereLayer("Framework")
                .mayNotBeAccessedByAnyLayer()
                .check(importedClasses);
    }
}
