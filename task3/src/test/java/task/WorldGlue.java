package task;

import com.artemis.Aspect;
import com.artemis.Component;
import com.artemis.Entity;
import com.artemis.World;
import com.artemis.utils.Bag;
import com.cucumber.workshop.logic.basic.ElapsedTimeSystem;
import com.cucumber.workshop.logic.spatial.Position;
import com.cucumber.workshop.logic.spatial.Velocity;
import com.cucumber.workshop.logic.spatial.VelocitySystem;
import cucumber.api.java.Before;
import cucumber.api.java.en.Given;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Collectors;

import static com.cucumber.workshop.WorldConfigurationFactory.headlessWorldConfiguration;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.data.Percentage.withPercentage;

/**
 * @author Snorre E. Brekke - snorre.e.brekke@gmail.com
 */
public class WorldGlue {

    private World world;
    private Optional<Entity> entity = Optional.empty();

    private final Map<String, Class<? extends Component>> componentMap;

    public WorldGlue() {
        componentMap = new HashMap<>();
        componentMap.put("position", Position.class);
        componentMap.put("velocity", Velocity.class);
    }

    @Before
    public void setUp() throws Exception {
        world = new World(
                headlessWorldConfiguration()
        );
    }

    @Given("^a delta of (.+)$")
    public void a_delta_of(float delta) {
        world.setDelta(delta);
    }

    @Given("^an entity (?:is created) ?(?:in|by) the world$")
    public void an_entity_in_the_world() {
        Entity entity = world.createEntity();
        setEntityUnderTest(entity.getId());
    }

    @Given("^(?:another|an) entity with the following components: (.+)$")
    public void an_entity_in_the_world(List<String> components) {
        Entity entity = world.createEntity();
        components.stream().forEach(c -> addComponent(entity, c));
    }

    @Given("^it is deleted by the world$")
    public void entity_is_deleted() {
        world.deleteEntity(entity());
    }

    @Given("^(?:that )?(.+) is added$")
    public void it_has_component(String componentName) {
        addComponent(entity(), componentName);
    }

    @Given("^(?:entity )?position(?: is set to)? (.+), (.+)$")
    public void entity_position(float x, float y) {
        requiredComponent(Position.class).x(x).y(y);
    }

    @Given("^(?:entity )?velocity(?: is set to)? (.+), (.+)$")
    public void entity_velocity(float dx, float dy) {
        requiredComponent(Velocity.class).dx(dx).dy(dy);
    }


    @When("^the world has been processed$")
    public void the_world_has_been_processed() {
        world.process();
    }

    @When("^the velocity system should have (\\d+) (?:entities|entity) registered$")
    public void the_velocity_system_entity_count(int count) {
        assertThat(
                world.getSystem(VelocitySystem.class).getSubscription().getEntities().size()
        )
                .isEqualTo(count);
    }

    @When("^the world has been processed (\\d+) times$")
    public void world_processed_n_times(int n) {
        for (int i = 0; i < n; i++) {
            world.process();
        }
    }

    @Then("^the elapsed time system has accumulated (.+) seconds$")
    public void world_processed_n_times(double elapsedTime) {
        assertThat(world.getSystem(ElapsedTimeSystem.class).time())
                .isCloseTo(elapsedTime, withPercentage(0.00001f));
    }

    @Then("the entity count in the world is (\\d+)")
    public void entity_count_in_the_world(int count) {
        assertThat(
                world.getAspectSubscriptionManager().get(Aspect.all()).getEntities().size()
        )
                .as("entitycount in the world")
                .isEqualTo(count);
    }

    @Then("^(?:it|the entity) should have (\\d+) components?$")
    public void entity_component_count(int componentCount) {
        assertThat(
                entity().getComponents(new Bag<>()).size()
        ).isEqualTo(componentCount);
    }

    @Then("^(?:the entity|it) should have position (.+), (.+)")
    public void entity_should_have_position(float x, float y) {
        Position pos = component(Position.class);
        assertThat(pos).as("entity should have position").isNotNull();
        assertThat(pos.x).as("expected x postion").isEqualTo(x);
        assertThat(pos.y).as("expected y postion").isEqualTo(y);
    }

    @Then("^(?:the entity|it) should have velocity (.+), (.+)")
    public void entity_should_have_velocity(float dx, float dy) {
        Velocity vel = component(Velocity.class);
        assertThat(vel).as("entity should have velocity").isNotNull();
        assertThat(vel.dx).as("expected dx velocity").isEqualTo(dx);
        assertThat(vel.dy).as("expected dy velocity").isEqualTo(dy);
    }

    private Entity entity() {
        return entity.orElseThrow(() -> new IllegalStateException("The entity under test has not been created yet."));
    }

    private void setEntityUnderTest(int entityId) {
        assertThat(entity).as("entity should not have been created yet").isEmpty();

        try {
            entity = Optional.of(world.getEntity(entityId));
        } catch (ArrayIndexOutOfBoundsException e) {
            entity = Optional.empty();
        }

        assertThat(entity).as("entity should exist").isPresent();
    }

    private void addComponent(Entity entity, String componentName) {
        Class<? extends Component> componentClass =
                componentMap.computeIfAbsent(componentName, throwUnknownComponentException());
        entity.edit().create(componentClass);
    }

    private <T extends Component> T component(Class<T> type) {
        return entity().getComponent(type);
    }

    private <T extends Component> T requiredComponent(Class<T> type) {
        return Optional.ofNullable(entity().getComponent(type))
                       .orElseThrow(() -> new IllegalStateException("The entity does not have a " +
                                                                    type.getSimpleName().toLowerCase() +
                                                                    " component."));
    }

    private Function<String, Class<? extends Component>> throwUnknownComponentException() {
        return key -> {
            throw new IllegalArgumentException("Tried to add unknown component '" + key +
                                               "' - Available component names:\n" +
                                               componentMap.keySet().stream().collect(Collectors.joining("\n")));
        };
    }

}
