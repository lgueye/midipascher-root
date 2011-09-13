/*
 *
 */
package fr.midipascher;

import org.springframework.stereotype.Component;

/**
 * @author louis.gueye@gmail.com
 */
@Component
public class GreeterImpl implements Greeter {

    /**
     * @see org.diveintojee.poc.cucumber.Greeter#hello()
     */

    public String hello() {
        return "Have a cuke, Duke!";
    }

}
