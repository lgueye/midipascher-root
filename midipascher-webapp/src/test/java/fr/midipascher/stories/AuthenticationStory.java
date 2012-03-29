/**
 * 
 */
package fr.midipascher.stories;

import java.util.Arrays;
import java.util.List;

import org.jbehave.core.io.CodeLocations;
import org.jbehave.core.io.StoryFinder;
import org.jbehave.core.steps.InjectableStepsFactory;
import org.jbehave.core.steps.InstanceStepsFactory;

import fr.midipascher.stories.steps.AuthenticationSteps;

/**
 * @author louis.gueye@gmail.com
 */
public class AuthenticationStory extends AbstractJUnitStories {

	@Override
	public InjectableStepsFactory stepsFactory() {
		return new InstanceStepsFactory(configuration(), new AuthenticationSteps());
	}

	@Override
	protected List<String> storyPaths() {
		return new StoryFinder().findPaths(CodeLocations.codeLocationFromClass(this.getClass()).getFile(),
				Arrays.asList("**/authentication.story"), null);
	}
}
