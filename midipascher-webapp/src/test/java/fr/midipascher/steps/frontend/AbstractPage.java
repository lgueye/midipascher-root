package fr.midipascher.steps.frontend;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import java.util.ResourceBundle;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

/**
 * @author louis.gueye@gmail.com
 */
public abstract class AbstractPage {

    public static final String
        BASE_END_POINT = ResourceBundle.getBundle("stories-context").getString("baseEndPoint");

    private final WebDriver driver;

    public AbstractPage(WebDriver driver) {
        this.driver = driver;
    }

    public void visit() {
        visitInternal();
        //checkCss();
        //checkScripts();
    }

    protected  abstract void visitInternal();

    public WebDriver getDriver() {
        return this.driver;
    }

    public WebElement findLinkById(String linkId) {
      return getDriver().findElement(By.cssSelector("a[id='"+linkId+"']"));
    }

    public WebElement findButtonById(String buttonId) {
      return getDriver().findElement(By.cssSelector("button[id='"+buttonId+"']"));
    }

    public WebElement findInputById(String inputId) {
      return getDriver().findElement(By.cssSelector("input[id='"+inputId+"']"));
    }

    public WebElement findBodyId(String bodyId) {
      return getDriver().findElement(By.cssSelector("body[id='"+ bodyId +"']"));
    }

    public void assertIdentity(String pageId) {
        assertTrue(findBodyId(pageId).isDisplayed());
    }


}
