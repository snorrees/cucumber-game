package task;

import com.artemis.World;
import com.google.inject.Binder;
import com.google.inject.Module;
import com.google.inject.Provides;
import cucumber.runtime.java.guice.ScenarioScoped;

import static com.cucumber.workshop.WorldConfigurationFactory.headlessWorldConfiguration;

/**
 * @author Snorre E. Brekke - snorre.e.brekke@gmail.com
 */
public class StateModule implements Module {

    @Override
    public void configure(Binder binder) {

    }

    @Provides
    @ScenarioScoped
    public World provideWorld() {
        return new World(
                headlessWorldConfiguration()
        );
    }
}
