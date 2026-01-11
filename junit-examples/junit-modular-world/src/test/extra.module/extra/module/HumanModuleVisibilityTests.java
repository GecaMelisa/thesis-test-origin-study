package extra.module;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.lang.module.ModuleDescriptor;
import java.util.Set;
import java.util.stream.Collectors;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

@Tag("human")
class HumanModuleVisibilityTests {

	@Test
	void applicationModuleIsPresent() {
		Module appModule = ModuleLayer.boot()
				.findModule("com.example.application")
				.orElse(null);

		assertNotNull(appModule, "Module 'com.example.application' should be present on the boot layer");
	}

	@Test
	void toolModuleIsPresent() {
		Module toolModule = ModuleLayer.boot()
				.findModule("com.example.tool")
				.orElse(null);

		assertNotNull(toolModule, "Module 'com.example.tool' should be present on the boot layer");
	}

	@Test
	void iceCreamModuleIsPresent() {
		Module iceCreamModule = ModuleLayer.boot()
				.findModule("ice.cream")
				.orElse(null);

		assertNotNull(iceCreamModule, "Module 'ice.cream' should be present on the boot layer");
	}

	@Test
	void applicationModuleExportsItsApi() {
		Module appModule = ModuleLayer.boot()
				.findModule("com.example.application")
				.orElseThrow(() -> new IllegalStateException("Module 'com.example.application' must be present for this test"));

		ModuleDescriptor descriptor = appModule.getDescriptor();

		Set<String> exportedPackages = descriptor.exports().stream()
				.map(ModuleDescriptor.Exports::source)
				.collect(Collectors.toSet());

		assertTrue(exportedPackages.contains("com.example.application"),
				"Module 'com.example.application' should export 'com.example.application'");
	}
}
