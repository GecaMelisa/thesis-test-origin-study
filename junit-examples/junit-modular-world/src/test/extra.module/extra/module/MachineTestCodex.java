package extra.module;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import ice.cream.Machine;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.platform.engine.ConfigurationParameters;
import org.junit.platform.engine.EngineDiscoveryRequest;
import org.junit.platform.engine.EngineExecutionListener;
import org.junit.platform.engine.TestDescriptor;
import org.junit.platform.engine.UniqueId;
import org.junit.platform.engine.support.descriptor.EngineDescriptor;
import org.junit.platform.engine.DiscoverySelector;
import org.junit.platform.engine.DiscoveryFilter;

@Tag("codex")
class MachineTestCodex {

	private static final UniqueId ROOT_ID = UniqueId.forEngine("ice-cream-machine");

	@Test
	void captionReflectsIdWithoutVersionOrArtifact() {
		Machine machine = new Machine();

		EngineDescriptor descriptor = (EngineDescriptor) machine.discover(
				new StubDiscoveryRequest(new StubConfigurationParameters(null)), ROOT_ID);

		String expected = "Ice Cream Machine";
		if (machine.getVersion().isPresent()) {
			expected += " " + machine.getVersion().get();
		}
		if (machine.getArtifactId().isPresent()) {
			expected += " (" + machine.getArtifactId().get() + ")";
		}

		assertEquals(expected, descriptor.getDisplayName(),
				"Caption should include optional version/artifact when available");
	}

	@Test
	void getScoopsUsesDefaultAndConfigurationOverride() {
		Machine machine = new Machine();
		EngineDiscoveryRequest defaultRequest = new StubDiscoveryRequest(new StubConfigurationParameters(null));
		EngineDiscoveryRequest overriddenRequest = new StubDiscoveryRequest(new StubConfigurationParameters("3"));

		EngineDescriptor defaultDescriptor = (EngineDescriptor) machine.discover(defaultRequest, ROOT_ID);
		EngineDescriptor overriddenDescriptor = (EngineDescriptor) machine.discover(overriddenRequest, ROOT_ID);

		assertEquals(5, defaultDescriptor.getChildren().size(),
				"Default scoops should be used when no parameter set");
		assertEquals(3, overriddenDescriptor.getChildren().size(),
				"Configuration parameter should override default scoops");
	}

	@Test
	void discoverCreatesChildrenForRequestedScoops() {
		Machine machine = new Machine();
		EngineDiscoveryRequest request = new StubDiscoveryRequest(new StubConfigurationParameters("2"));

		TestDescriptor descriptor = machine.discover(request, ROOT_ID);

		assertEquals(2, descriptor.getChildren().size(), "Two scoops should be created when configured to 2");
		assertTrue(descriptor.getDisplayName().startsWith("Ice Cream Machine"), "Engine display name should match caption");
	}

	@Test
	void executePublishesStartAndFinishEventsForEngineAndChildren() {
		Machine machine = new Machine();
		EngineDescriptor descriptor = (EngineDescriptor) machine.discover(
				new StubDiscoveryRequest(new StubConfigurationParameters("2")), ROOT_ID);

		EventRecordingListener listener = new EventRecordingListener();
		machine.execute(new org.junit.platform.engine.ExecutionRequest(descriptor, listener, new StubConfigurationParameters(null)));

		List<String> events = listener.events;
		assertTrue(events.size() >= 4, "Should record start/finish for engine and two children");
		assertEquals("engine-start", events.get(0));
		assertEquals("engine-finish", events.get(events.size() - 1));
		assertTrue(events.stream().anyMatch(e -> e.startsWith("child-start")), "Child start events expected");
		assertTrue(events.stream().anyMatch(e -> e.startsWith("child-finish")), "Child finish events expected");
	}

	private static final class StubConfigurationParameters implements ConfigurationParameters {
		private final String scoops;

		private StubConfigurationParameters(String scoops) {
			this.scoops = scoops;
		}

		@Override
		public Optional<String> get(String key) {
			if ("scoops".equals(key) && scoops != null) {
				return Optional.of(scoops);
			}
			return Optional.empty();
		}

		@Override
		public Optional<Boolean> getBoolean(String key) {
			return Optional.empty();
		}

		@Override
		public <T> Optional<T> get(String key, java.util.function.Function<String, T> transformer) {
			return get(key).map(transformer);
		}

		@Override
		public int size() {
			return scoops == null ? 0 : 1;
		}

		@Override
		public java.util.Set<String> keySet() {
			return scoops == null ? java.util.Set.of() : java.util.Set.of("scoops");
		}
	}

	private static final class StubDiscoveryRequest implements EngineDiscoveryRequest {
		private final ConfigurationParameters parameters;

		private StubDiscoveryRequest(ConfigurationParameters parameters) {
			this.parameters = parameters;
		}

		@Override
		public <T extends DiscoverySelector> List<T> getSelectorsByType(Class<T> selectorClass) {
			return List.of();
		}

		@Override
		public <T extends DiscoveryFilter<?>> List<T> getFiltersByType(Class<T> filterType) {
			return List.of();
		}

		@Override
		public ConfigurationParameters getConfigurationParameters() {
			return parameters;
		}
	}

	private static final class EventRecordingListener implements EngineExecutionListener {
		private final List<String> events = new ArrayList<>();

		@Override
		public void executionStarted(TestDescriptor testDescriptor) {
			if (testDescriptor instanceof EngineDescriptor) {
				events.add("engine-start");
			} else {
				events.add("child-start:" + testDescriptor.getDisplayName());
			}
		}

		@Override
		public void executionFinished(TestDescriptor testDescriptor,
				org.junit.platform.engine.TestExecutionResult testExecutionResult) {
			if (testDescriptor instanceof EngineDescriptor) {
				events.add("engine-finish");
			} else {
				events.add("child-finish:" + testDescriptor.getDisplayName());
			}
			assertFalse(testExecutionResult.getStatus() == org.junit.platform.engine.TestExecutionResult.Status.FAILED,
					"No failures expected during execution");
		}
	}
}
