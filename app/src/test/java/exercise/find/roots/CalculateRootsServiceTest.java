package exercise.find.roots;

import android.content.Intent;

import junit.framework.TestCase;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.Robolectric;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.RuntimeEnvironment;
import org.robolectric.annotation.Config;

import java.util.List;

import static org.robolectric.Shadows.shadowOf;


@RunWith(RobolectricTestRunner.class)
@Config(sdk = 28)
public class CalculateRootsServiceTest extends TestCase {

  @Test
  public void when_calculatingRootsWithSuccess_then_serviceShouldSendBroadcastWithResults(){
    // create a service
    CalculateRootsService service = Robolectric.setupIntentService(CalculateRootsService.class);

    // create intent to send to the service
    Intent incomingIntent = new Intent();
    incomingIntent.putExtra("number_for_service", 15L);

    // let service do it's magic to calculate the roots
    service.onHandleIntent(incomingIntent);

    // capture all broadcasts
    List<Intent> broadcastedIntents = shadowOf(RuntimeEnvironment.application).getBroadcastIntents();
    if (broadcastedIntents.size() != 1) {
      fail("expecting exactly 1 broadcast to be sent");
    }

    // verify the broadcast sent by the service
    Intent intentSentByService = broadcastedIntents.get(0);
    assertNotNull(intentSentByService);
    assertEquals("found_roots", intentSentByService.getAction());
    long originalNumber = intentSentByService.getLongExtra("original_number", 0);
    long firstRoot = intentSentByService.getLongExtra("root1", 0);
    long secondRoot = intentSentByService.getLongExtra("root1", 0);
    assertEquals(15, originalNumber);
    assertEquals(15, firstRoot * secondRoot);
  }
}