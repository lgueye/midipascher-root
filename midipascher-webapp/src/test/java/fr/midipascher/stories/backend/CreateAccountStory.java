/**
 * 
 */
package fr.midipascher.stories.backend;

import java.util.Arrays;
import java.util.List;

import org.jbehave.core.io.CodeLocations;
import org.jbehave.core.io.StoryFinder;
import org.jbehave.core.steps.InjectableStepsFactory;
import org.jbehave.core.steps.InstanceStepsFactory;

import fr.midipascher.steps.backend.CreateAccountSteps;
import fr.midipascher.stories.AbstractJUnitStories;

/**
 * @author louis.gueye@gmail.com
 */
public class CreateAccountStory extends AbstractJUnitStories {

    @Override
    public InjectableStepsFactory stepsFactory() {
        return new InstanceStepsFactory(configuration(), new CreateAccountSteps());
    }

    @Override
    protected List<String> storyPaths() {
        return new StoryFinder().findPaths(CodeLocations.codeLocationFromClass(this.getClass()).getFile(),
            Arrays.asList("**/create_account.story"), null);
    }
}
