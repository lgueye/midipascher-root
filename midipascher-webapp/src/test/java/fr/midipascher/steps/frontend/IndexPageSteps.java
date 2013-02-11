package fr.midipascher.steps.frontend;

import org.jbehave.core.annotations.BeforeScenario;
import org.jbehave.core.annotations.Given;
import org.jbehave.core.annotations.Named;
import org.jbehave.core.annotations.Then;
import org.jbehave.core.annotations.When;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.htmlunit.HtmlUnitDriver;

import static org.junit.Assert.assertTrue;

/**
 * @author louis.gueye@gmail.com
 */
public class IndexPageSteps {

    private WebDriver webDriver;
    IndexPage indexPage;

    @Given("I navigate to the \"$pageId\" page")
    public void navigateTo(@Named("pageId") String pageId) {
        if ("index".equals(pageId)) {
            indexPage = new IndexPage(new HtmlUnitDriver());
            indexPage.visit();
        }

    }

    @Then("my outcome should be the \"$pageId\" page")
    public void assertPageIdendity(@Named("pageId") String pageId) {
        indexPage.assertIdentity(pageId);
    }

    @Then("I should be able to click the \"$linkId\" link")
    public void findLinkById(@Named("linkId") String linkId) {
        assertTrue(indexPage.findLinkById(linkId).isDisplayed());
    }

    @Then("I should be able to click the \"$buttonId\" button")
    public void findButtonById(@Named("buttonId") String buttonId) {
        assertTrue(indexPage.findButtonById(buttonId).isDisplayed());
    }

    @Then("I should be able to input the \"$inputId\"")
    public void findInputById(@Named("inputId") String inputId) {
        assertTrue(indexPage.findInputById(inputId).isDisplayed());
    }

    @When("I click on the \"$linkId\" link")
    public void clickLink(@Named("linkId") String linkId) {
        WebElement link = indexPage.findLinkById(linkId);
        link.click();
    }

    @BeforeScenario
    public void beforeScenario() {
        if (webDriver != null) webDriver.quit();
        webDriver = new HtmlUnitDriver();
    }

}
