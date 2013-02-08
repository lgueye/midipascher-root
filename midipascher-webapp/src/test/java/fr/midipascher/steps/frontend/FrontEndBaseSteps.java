package fr.midipascher.steps.frontend;

import com.gargoylesoftware.htmlunit.Page;

import org.jbehave.core.annotations.Given;
import org.jbehave.core.annotations.Named;
import org.jbehave.core.annotations.Then;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.htmlunit.HtmlUnitDriver;
import org.openqa.selenium.support.PageFactory;

import java.util.ResourceBundle;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

/**
 * @author louis.gueye@gmail.com
 */
public class FrontEndBaseSteps {

    private static final String baseEndPoint = ResourceBundle.getBundle("stories-context").getString("baseEndPoint");
    private WebDriver webDriver;
    private Object page;

    public FrontEndBaseSteps(HtmlUnitDriver htmlUnitDriver) {
        this.webDriver = htmlUnitDriver;
    }

    private <T> void setPage(Class<T> clazz) {
        this.page = PageFactory.initElements(webDriver, clazz);
    }

    @Given("I navigate to the \"$pageId\" page")
    public void navigateTo(@Named("pageId") String pageId) {
        if ("index".equals(pageId)) {
            webDriver.get(baseEndPoint + "/");
            assertEquals(baseEndPoint, webDriver.getCurrentUrl());
            setPage(IndexPage.class);
        }
    }

    @Then("I should be able to click the \"$linkId\" link")
    public void findLinkById(@Named("linkId") String linkId) {
        assertTrue(webDriver.findElement(By.cssSelector("a[id='"+linkId+"']")).isDisplayed());
    }

    @Then("I should be able to click the \"$buttonId\" button")
    public void findButtonById(@Named("buttonId") String buttonId) {
        assertTrue(webDriver.findElement(By.cssSelector("input[type='submit'][id='"+buttonId+"']")).isDisplayed());
    }

    @Then("I should be able to input the \"$inputId\"")
    public void findInputById(@Named("inputId") String inputId) {
        assertTrue(webDriver.findElement(By.cssSelector("input[type='text'][id='"+inputId+"']")).isDisplayed());
    }

}
