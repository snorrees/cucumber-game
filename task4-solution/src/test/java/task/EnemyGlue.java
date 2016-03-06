package task;

import com.artemis.Aspect;
import com.artemis.Entity;
import com.artemis.EntitySubscription;
import com.artemis.World;
import com.artemis.utils.IntBag;
import com.cucumber.workshop.EntityFactorySystem;
import com.cucumber.workshop.InteractionSystem;
import com.cucumber.workshop.logic.enemy.Enemy;
import com.cucumber.workshop.logic.enemy.InteractionType;
import com.cucumber.workshop.logic.enemy.Type;
import com.cucumber.workshop.logic.spatial.Position;
import com.google.inject.Inject;
import cucumber.api.DataTable;
import cucumber.api.java.en.Given;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;
import cucumber.runtime.java.guice.ScenarioScoped;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * @author Snorre E. Brekke - snorre.e.brekke@gmail.com
 */
@ScenarioScoped
public class EnemyGlue {

    private World world;

    @Inject
    public EnemyGlue(World world) {
        this.world = world;
    }

    @Given("^a world with a player who can interact with ([^ ]+) enemies$")
    public void a_world_with_a_player_who_can_interact_with_enemies(Type type) throws Throwable {
        Entity player = entityFactory().player(0, 0);
        player.getComponent(InteractionType.class).type(type);
        world.process();
    }

    @Given("^(?:the world|it) contains enemies with the following state:$")
    public void it_contains_enemies_with_the_following_state(List<Map<String, String>> enemyValues) throws Throwable {
        EntityStateBuilder stateBuilder = new EntityStateBuilder();
        enemyValues.stream().forEach(values -> {
            Entity enemy = entityFactory().enemy(0, 0);
            stateBuilder.modifyEntity(enemy, values);
        });
    }

    @When("^the user clicks at (.+)$")
    public void the_user_clicks_at(Click c) throws Throwable {
        world.getSystem(InteractionSystem.class).interaction(c.x, c.y);
    }

    @When("^the world is processed$")
    public void the_world_is_processed() throws Throwable {
        world.process();
    }

    @Then("^the world should contain (?:an enemy|enemies) with the following state:$")
    public void the_world_should_contain_enemies_with_the_following_state(
            DataTable expectedEntityState) throws Throwable {

        EntitySubscription enemies = world.getAspectSubscriptionManager()
                                          .get(Aspect.all(
                                                  Enemy.class,
                                                  Position.class)
                                          );

        List<Map<String, String>> actualEntityState = streamSubscription(enemies)
                .map(this::enemyState)
                .collect(Collectors.toList());

        expectedEntityState.unorderedDiff(actualEntityState);
    }

    private Stream<Entity> streamSubscription(EntitySubscription subscription) {
        IntBag entities = subscription.getEntities();
        Stream.Builder<Entity> streamBuilder = Stream.builder();
        for (int i = 0; i < entities.size(); i++) {
            Entity entity = world.getEntity(entities.get(i));
            streamBuilder.add(entity);
        }
        return streamBuilder.build();
    }

    private Map<String, String> enemyState(Entity e) {
        Map<String, String> values = new HashMap<>();
        Position pos = e.getComponent(Position.class);
        Enemy enemy = e.getComponent(Enemy.class);
        values.put("x", String.valueOf(pos.x));
        values.put("y", String.valueOf(pos.y));
        values.put("Enemy type", String.valueOf(enemy.type).toLowerCase());
        return values;
    }

    private EntityFactorySystem entityFactory() {
        return world.getSystem(EntityFactorySystem.class);
    }




}
