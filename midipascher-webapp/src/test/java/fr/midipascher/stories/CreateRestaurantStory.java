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

import fr.midipascher.stories.steps.CreateRestaurantSteps;

/**
 * @author louis.gueye@gmail.com
 */
public class CreateRestaurantStory extends AbstractJUnitStories {

	@Override
	public InjectableStepsFactory stepsFactory() {
		return new InstanceStepsFactory(configuration(), new CreateRestaurantSteps());
	}

	@Override
	protected List<String> storyPaths() {
		return new StoryFinder().findPaths(CodeLocations.codeLocationFromClass(this.getClass()).getFile(),
				Arrays.asList("**/create_restaurant.story"), null);
	}
}
