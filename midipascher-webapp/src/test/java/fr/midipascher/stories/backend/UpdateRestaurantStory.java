/**
 *
 */
package fr.midipascher.stories.backend;

import org.jbehave.core.io.CodeLocations;
import org.jbehave.core.io.StoryFinder;
import org.jbehave.core.steps.InjectableStepsFactory;
import org.jbehave.core.steps.InstanceStepsFactory;

import java.util.Arrays;
import java.util.List;

import fr.midipascher.steps.backend.Exchange;
import fr.midipascher.steps.backend.UpdateRestaurantSteps;
import fr.midipascher.stories.AbstractJUnitStories;

/**
 * @author louis.gueye@gmail.com
 */
public class UpdateRestaurantStory extends AbstractJUnitStories {

  @Override
  public InjectableStepsFactory stepsFactory() {
    return new InstanceStepsFactory(configuration(), new UpdateRestaurantSteps(new Exchange()));
  }

  @Override
  protected List<String> storyPaths() {
    return new StoryFinder()
        .findPaths(CodeLocations.codeLocationFromClass(this.getClass()).getFile(),
                   Arrays.asList("**/update_restaurant.story"), null);
  }

}
