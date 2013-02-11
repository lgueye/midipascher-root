package fr.midipascher.steps.frontend;

import org.openqa.selenium.WebDriver;

/**
 * @author louis.gueye@gmail.com
 */
public class IndexPage extends AbstractPage {

    public IndexPage(WebDriver driver) {
      super(driver);
    }

    @Override
    protected void visitInternal() {
        getDriver().get(BASE_END_POINT);
    }

}
