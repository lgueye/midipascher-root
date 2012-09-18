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
@RequestMapping("/accounts")
public class AccountsController {

    @RequestMapping(value = "/{reference}/edit", method = RequestMethod.GET)
    public String edit(@PathVariable("reference") String reference) {
        return "edit_account";
    }

}
