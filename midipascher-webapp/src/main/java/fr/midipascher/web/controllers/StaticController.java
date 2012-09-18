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
public class StaticController {

    @RequestMapping(value = "/about", method = RequestMethod.GET)
    public String about() {
        return "about";
    }

    @RequestMapping(value = "/confidentiality", method = RequestMethod.GET)
    public String confidentiality() {
        return "confidentiality";
    }

    @RequestMapping(value = "/legal", method = RequestMethod.GET)
    public String legal() {
        return "legal";
    }

}
