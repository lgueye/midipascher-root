/*
 *
 */
package fr.midipascher.business;

import static org.junit.Assert.assertEquals;

import java.util.Locale;

import javax.servlet.http.HttpServletResponse;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.MessageSource;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import fr.midipascher.TestConstants;

/**
 * Authority database integration testing<br/>
 * CRUD operations are tested<br>
 * Finders are tested<br/>
 * 
 * @author louis.gueye@gmail.com
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { TestConstants.VALIDATION_CONTEXT })
public class MessageSourceTestIT {

    @Autowired
    @Qualifier("messageSources")
    private MessageSource underTest;

    @Test
    public final void messageSourceShouldConsiderEncoding() {

        assertEquals("informations de connexion erronées",
            this.underTest.getMessage(String.valueOf(HttpServletResponse.SC_UNAUTHORIZED), null, Locale.FRENCH));

    }

}
