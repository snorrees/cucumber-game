package task;

import com.artemis.Aspect;
import com.artemis.World;
import com.cucumber.workshop.logic.basic.ElapsedTimeSystem;
import com.google.inject.Inject;
import cucumber.api.java.en.Given;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;
import cucumber.runtime.java.guice.ScenarioScoped;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.data.Percentage.withPercentage;

/**
 * @author Snorre E. Brekke - snorre.e.brekke@gmail.com
 */
@ScenarioScoped
public class WorldGlue {
    private World world;

    @Inject
    public WorldGlue(World world) {
        this.world = world;
    }

    @Given("^a delta of (.+)$")
    public void a_delta_of(float delta) {
        world.setDelta(delta);
    }

    @When("^the world has been processed$")
    public void the_world_has_been_processed() {
        world.process();
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
}
