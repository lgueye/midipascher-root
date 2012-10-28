package fr.midipascher.persistence.store;

import fr.midipascher.domain.AbstractEntity;
import fr.midipascher.persistence.search.SearchEngine;
import org.hibernate.event.spi.PostDeleteEvent;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import static org.mockito.Mockito.*;

/**
 * User: lgueye Date: 24/10/12 Time: 19:27
 */
@RunWith(MockitoJUnitRunner.class)
public class PostDeleteEventListenerTest {

    @Mock
    private SearchEngine searchEngine;

    @InjectMocks
    private PostDeleteEventListener underTest;

  @Test
  public void onPostDeleteShouldSucceed() throws Exception {
      PostDeleteEvent event = mock(PostDeleteEvent.class);
      underTest.onPostDelete(event);
      verify(searchEngine).removeFromIndex((AbstractEntity) event.getEntity());
      verifyNoMoreInteractions(searchEngine);
  }
}
