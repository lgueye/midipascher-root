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
@RequestMapping("/search")
public class SearchController {

    @RequestMapping(value = "/new", method = RequestMethod.GET)
    public String add() {
        return "search";
    }

}
