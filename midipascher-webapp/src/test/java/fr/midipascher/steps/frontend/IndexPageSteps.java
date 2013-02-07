package fr.midipascher.steps.frontend;

import org.jbehave.core.annotations.Given;
import org.jbehave.core.annotations.Named;
import org.jbehave.core.annotations.Then;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.htmlunit.HtmlUnitDriver;
import org.openqa.selenium.support.PageFactory;

import java.util.ResourceBundle;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

/**
 * @author louis.gueye@gmail.com
 */
public class IndexPageSteps {

    private static final String baseEndPoint = ResourceBundle.getBundle("stories-context").getString("baseEndPoint");
    private WebDriver webDriver;
    IndexPage indexPage;

    public IndexPageSteps(HtmlUnitDriver htmlUnitDriver) {
        this.webDriver = htmlUnitDriver;
    }

    @Given("I navigate to the \"$pageId\" page")
    public void navigateTo(@Named("pageId") String pageId) {
        if ("index".equals(pageId)) {
            webDriver.get(baseEndPoint + "/");
            assertEquals(baseEndPoint, webDriver.getCurrentUrl());
            indexPage = PageFactory.initElements(webDriver, IndexPage.class);
        }

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

}
