package fr.midipascher.stories.frontend;

import fr.midipascher.steps.frontend.IndexPageSteps;
import fr.midipascher.stories.AbstractJUnitStories;
import org.jbehave.core.io.CodeLocations;
import org.jbehave.core.io.StoryFinder;
import org.jbehave.core.steps.InjectableStepsFactory;
import org.jbehave.core.steps.InstanceStepsFactory;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.htmlunit.HtmlUnitDriver;

import java.util.Arrays;
import java.util.List;
import java.util.ResourceBundle;

/**
 * User: lgueye Date: 07/02/13 Time: 17:46
 */
public class IndexPageStory extends AbstractJUnitStories {

    @Override
    public InjectableStepsFactory stepsFactory() {
        return new InstanceStepsFactory(configuration(), new IndexPageSteps());
    }

    @Override
    protected List<String> storyPaths() {
        return new StoryFinder().findPaths(CodeLocations.codeLocationFromClass(this.getClass()).getFile(),
                Arrays.asList("**/index_page.story"), null);
    }

}
