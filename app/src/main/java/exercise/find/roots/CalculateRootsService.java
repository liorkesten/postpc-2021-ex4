package exercise.find.roots;

import android.app.IntentService;
import android.content.Intent;
import android.util.Log;

import static java.lang.Math.sqrt;

/**
 *
 */
public class CalculateRootsService extends IntentService {

  private static final int MAXIMUM_TIME_TO_FIND_ROOTS = 20000;

  /**
   *
   */
  public CalculateRootsService() {
    super("CalculateRootsService");
  }

  @Override
  protected void onHandleIntent(Intent intent) {
    Intent intentResult = new Intent();
    if (intent == null) {
      return;
    }
    long timeStartMs = System.currentTimeMillis();
    long numberToCalculateRootsFor = intent.getLongExtra("number_for_service", 0);
    if (numberToCalculateRootsFor <= 0) {
      Log.e("CalculateRootsService", "can't calculate roots for non-positive input" + numberToCalculateRootsFor);
      return;
    }
    RootResult result = findRoot(numberToCalculateRootsFor,timeStartMs);
    if (result._isValidRootFound){
      intentResult.setAction("found_roots");
      intentResult.putExtra("original_number",numberToCalculateRootsFor);
      intentResult.putExtra("root1",result._root1);
      intentResult.putExtra("root2",result._root2);
      intentResult.putExtra("calculationTime",result._calculationTime);
    }
    else{
      intentResult.setAction("stopped_calculations");
      intentResult.putExtra("original_number",numberToCalculateRootsFor);
      intentResult.putExtra("time_until_give_up_seconds",result._calculationTime);
    }
    sendBroadcast(intentResult);
  }

  /**
   *
   * @param target
   * @param timeStartMs
   * @return
   */
  private static RootResult findRoot(long target, long timeStartMs) {
    if (target == 0){
      return RootResultFactory.createRootResultForTargetZero();
    }
    long root1 = 1, root2 = 1;
    long sqrtOfTarget = (long)Math.ceil(sqrt(target));

    while (root1 <= sqrtOfTarget){
      while (root2 <= sqrtOfTarget){
        if (System.currentTimeMillis() - timeStartMs > MAXIMUM_TIME_TO_FIND_ROOTS || root1*root2 == target){
          break;
        }
        root2 += 1;
      }
      if (System.currentTimeMillis() - timeStartMs > MAXIMUM_TIME_TO_FIND_ROOTS || root1*root2 == target){
        break;
      }
      root1 += 1;
      root2 = root1;
    }

    long calculationTime = System.currentTimeMillis() - timeStartMs;
    if(calculationTime > MAXIMUM_TIME_TO_FIND_ROOTS){
      return RootResultFactory.createRootResultNotFound(calculationTime);
    }
    // Root Found
    else if (root1 * root2 == target){
      return RootResultFactory.createRootResultFound(root1,root2,calculationTime);
    }
    //Prime
    else{
      return RootResultFactory.createRootResultPrimeFound(target,calculationTime);
    }
  }
}


