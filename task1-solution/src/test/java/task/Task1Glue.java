package task;

import cucumber.api.java.en.Given;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * @author Snorre E. Brekke - snorre.e.brekke@gmail.com
 */
public class Task1Glue {

    private String text;
    private String result;

    @Given("^the input text (.+)$")
    public void the_text_Hello_World(String text) throws Throwable {
        this.text = text;
    }

    @When("^the text is transformed into all caps$")
    public void the_text_is_transformed_into_all_caps() throws Throwable {
        result = text.toUpperCase();
    }

    @Then("^(?:the transformed text|it) is (.+)$")
    public void then_the_transformed_text_is(String expected) throws Throwable {
        assertThat(result).isEqualTo(expected);
    }

    @Then("^(?:the transformed text|it) has length (\\d+)$")
    public void the_transformed_text_has_length(int expected) throws Throwable {
        assertThat(result.length()).isEqualTo(expected);
    }
}
