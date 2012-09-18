/**
 *
 */
package fr.midipascher.stories.backend;

import fr.midipascher.steps.backend.AuthenticationSteps;
import fr.midipascher.steps.backend.Exchange;
import fr.midipascher.stories.AbstractJUnitStories;
import org.jbehave.core.io.CodeLocations;
import org.jbehave.core.io.StoryFinder;
import org.jbehave.core.steps.InjectableStepsFactory;
import org.jbehave.core.steps.InstanceStepsFactory;

import java.util.Arrays;
import java.util.List;

/**
 * @author louis.gueye@gmail.com
 */
public class AuthenticationStory extends AbstractJUnitStories {

    @Override
    public InjectableStepsFactory stepsFactory() {
        return new InstanceStepsFactory(configuration(), new AuthenticationSteps(new Exchange()));
    }

    @Override
    protected List<String> storyPaths() {
        return new StoryFinder().findPaths(CodeLocations.codeLocationFromClass(this.getClass()).getFile(),
                Arrays.asList("**/authentication.story"), null);
    }
}
