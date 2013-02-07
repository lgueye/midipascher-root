package fr.midipascher.steps.frontend;

import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

/**
 * @author louis.gueye@gmail.com
 */
public class IndexPage {

    @FindBy(css = "a[id='authenticate']")
    private WebElement authenticateLink;

    @FindBy(css = "a[id=favorite-searches]")
    private WebElement favoriteSearchesLink;

    @FindBy(css = "input[type=submit][id=find-by-address]")
    private WebElement findByAddressButton;

    @FindBy(css = "input[type=submit][id=find-by-current-location]")
    private WebElement findByCurrentLocationButton;

    @FindBy(css = "input[type=text][id=street-address]")
    private WebElement streetAddressInput;

    @FindBy(css = "input[type=submit][id=postal-code]")
    private WebElement postalCodeInput;


    public WebElement findLinkById(String linkId) {
        if ("authenticate".equals(linkId)) return authenticateLink;
        else if ("favorite-searches".equals(linkId)) return favoriteSearchesLink;
        throw new IllegalArgumentException("No link with id " + linkId + " found on index page");
    }

    public WebElement findButtonById(String buttonId) {
        if ("find-by-address".equals(buttonId)) return findByAddressButton;
        else if ("find-by-current-location".equals(buttonId)) return findByCurrentLocationButton;
        throw new IllegalArgumentException("No button with id " + buttonId + " found on index page");
    }

    public WebElement findInputById(String inputId) {
        if ("street-address".equals(inputId)) return streetAddressInput;
        else if ("postal-code".equals(inputId)) return postalCodeInput;
        throw new IllegalArgumentException("No button with id " + inputId + " found on index page");
    }
}
