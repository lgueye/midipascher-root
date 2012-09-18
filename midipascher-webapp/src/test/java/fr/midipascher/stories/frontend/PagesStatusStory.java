/**
 *
 */
package fr.midipascher.stories.frontend;

import fr.midipascher.steps.frontend.PagesStatusSteps;
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
public class PagesStatusStory extends AbstractJUnitStories {

    @Override
    public InjectableStepsFactory stepsFactory() {
        return new InstanceStepsFactory(configuration(), new PagesStatusSteps());
    }

    @Override
    protected List<String> storyPaths() {
        return new StoryFinder().findPaths(CodeLocations.codeLocationFromClass(this.getClass()).getFile(),
                Arrays.asList("**/pages_status.story"), null);
    }

}
