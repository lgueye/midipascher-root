/**
 *
 */
package fr.midipascher.web.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

/**
 * @author louis.gueye@gmail.com
 */
@Controller
@RequestMapping(AuthenticationController.PATH)
public class AuthenticationController {

    public static final String PATH = "/authenticate";

    @RequestMapping(method = RequestMethod.GET)
    public String authenticate() {
        return "authenticate";
    }

}
