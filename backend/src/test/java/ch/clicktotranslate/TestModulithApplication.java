package ch.clicktotranslate;

import org.junit.jupiter.api.Test;
import org.springframework.modulith.core.ApplicationModules;
import org.springframework.modulith.docs.Documenter;

class ApplicationTests {

	@Test
	void verifiesModulithStructure() {
		var modules = ApplicationModules.of(ClickToTranslateApplication.class).verify();

		new Documenter(modules).writeModulesAsPlantUml().writeIndividualModulesAsPlantUml();
	}

}
