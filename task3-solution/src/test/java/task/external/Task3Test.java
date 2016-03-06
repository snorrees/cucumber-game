package task.external;

import cucumber.api.CucumberOptions;
import cucumber.api.junit.Cucumber;
import org.junit.runner.RunWith;

/**
 * @author Snorre E. Brekke - snorre.e.brekke@gmail.com
 */
@RunWith(Cucumber.class)
@CucumberOptions(
        plugin = {"pretty", "html:target/cucumber"},
        glue =  {"task"},
        features = {"classpath:task"}
)
public class Task3Test {
}
