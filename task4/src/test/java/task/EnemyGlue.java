package task;

import com.artemis.World;
import com.cucumber.workshop.EntityFactorySystem;
import com.google.inject.Inject;
import cucumber.runtime.java.guice.ScenarioScoped;

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

    private EntityFactorySystem entityFactory() {
        return world.getSystem(EntityFactorySystem.class);
    }
}
