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
@RequestMapping(value = {"/", "/index"})
public class IndexController {

    @RequestMapping(method = RequestMethod.GET)
    public String displayIndex() {
        return "index";
    }
}
