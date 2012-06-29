/**
 * 
 */
package fr.midipascher.web;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

/**
 * @author louis.gueye@gmail.com
 */
@Controller
@RequestMapping("/restaurants")
public class RestaurantController {

    @RequestMapping(value = "/add", method = RequestMethod.GET)
    public String add() {
        return "index";
    }

    @RequestMapping(value = "/{id}/update", method = RequestMethod.GET)
    public String update() {
        return "index";
    }

}
