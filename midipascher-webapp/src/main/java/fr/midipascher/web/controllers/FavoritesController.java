/**
 *
 */
package fr.midipascher.web.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

/**
 * @author louis.gueye@gmail.com
 */
@Controller
@RequestMapping("/favorite-searches")
public class FavoritesController {

    @RequestMapping(method = RequestMethod.GET)
    public String list() {
        return "favorite_searches";
    }

    @RequestMapping(value = "/add", method = RequestMethod.GET)
    public String add() {
        return "add_favorite";
    }

    @RequestMapping(value = "/{reference}/edit", method = RequestMethod.GET)
    public String edit(@PathVariable("reference") String reference) {
        return "edit_favorite";
    }

}
