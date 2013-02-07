package fr.midipascher.steps.frontend;

import org.jbehave.core.annotations.Given;
import org.jbehave.core.annotations.Named;
import org.jbehave.core.annotations.Pending;
import org.jbehave.core.annotations.Then;
import org.openqa.selenium.htmlunit.HtmlUnitDriver;

import java.util.List;

import static org.junit.Assert.assertTrue;

/**
 * @author louis.gueye@gmail.com
 */
public class IndexPageSteps {

  private HtmlUnitDriver htmlUnitDriver;

  public IndexPageSteps(HtmlUnitDriver htmlUnitDriver) {
    this.htmlUnitDriver = htmlUnitDriver;
  }

  @Given("I navigate to the \"$pageId\" page")
  public void navigateTo(@Named("pageId") String pageId) {
    assertTrue(false);
  }

  @Then("I should be able to click the \"$linkId\" link")
  public void findLinkById(@Named("linkId") String linkId) {
    assertTrue(false);
  }

  @Then("I should be able to click the \"$buttonId\" button")
  public void findButtonById(@Named("buttonId") String buttonId) {
    assertTrue(false);
  }

  @Then("I should be able to input the \"$inputId\"")
  public void findInputById(@Named("inputId") String inputId) {
    assertTrue(false);
  }

}
