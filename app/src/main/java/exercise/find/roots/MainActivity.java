package exercise.find.roots;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;


import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

  private BroadcastReceiver broadcastReceiverForSuccess = null;
  private BroadcastReceiver broadcastReceiverForAborted = null;
  private boolean _isAnyCalculationRunning = false;
  private String _userInput = "";
  ProgressBar _progressBar = null;
  EditText _editTextUserInput = null;
  Button _buttonCalculateRoots = null;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    System.out.println("MainActivity.onCreate called");
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_main);

    initDataMemerbrs();
    resetUI(); //Set initial UI
    initEditTextUserInputListener();
    // set click-listener to the button
    _buttonCalculateRoots.setOnClickListener(v -> calcualteRootsButtonClicked());
    createRecivers();
    registerRecivers();
  }

  @Override
  protected void onResume() {
    System.out.println("MainActivity.onResume called");
    super.onResume();
  }

  @Override
  protected void onPause() {
    System.out.println("MainActivity.onPause called");
    super.onPause();
  }

  @Override
  protected void onDestroy() {
    System.out.println("MainActivity.onDestroy called");
    unregisterReceiver(broadcastReceiverForSuccess);
    unregisterReceiver(broadcastReceiverForAborted);
    super.onDestroy();
  }

  @Override
  protected void onSaveInstanceState(@NonNull Bundle outState) {
    System.out.println("MainActivity.onSaveInstanceState called");

    outState.putString("user_input",_userInput);
    outState.putBoolean("is_any_calculation_running",_isAnyCalculationRunning);
    super.onSaveInstanceState(outState);
  }

  @Override
  protected void onRestoreInstanceState(@NonNull Bundle savedInstanceState) {
    super.onRestoreInstanceState(savedInstanceState);

    System.out.println("MainActivity.onRestoreInstanceState called");
    _userInput = savedInstanceState.getString("user_input");
    _isAnyCalculationRunning = savedInstanceState.getBoolean("is_any_calculation_running");
    if(_isAnyCalculationRunning){
      changeUIWhenButtomClicked();
    }
  }

  private void initDataMemerbrs() {
    _progressBar = findViewById(R.id.progressBar);
    _editTextUserInput = findViewById(R.id.editTextInputNumber);
    _buttonCalculateRoots = findViewById(R.id.buttonCalculateRoots);
  }

  private void initEditTextUserInputListener() {
    // set listener on the input written by the keyboard to the edit-text
    _editTextUserInput.addTextChangedListener(new TextWatcher() {
      public void beforeTextChanged(CharSequence s, int start, int count, int after) { }
      public void onTextChanged(CharSequence s, int start, int before, int count) { }
      public void afterTextChanged(Editable s) {
        // text did change
        String newText = _editTextUserInput.getText().toString();
        boolean shouldEnableButtonCalculateRoots =
                (isValidNumberInEditText(_editTextUserInput) && !isAnyCalculationRunning());
        _buttonCalculateRoots.setEnabled(shouldEnableButtonCalculateRoots);
        _userInput = _editTextUserInput.getText().toString();
      }
    });
  }

  private void createRecivers() {
    // register a broadcast-receiver to handle action "found_roots"
    broadcastReceiverForSuccess = new BroadcastReceiver() {
      @Override
      public void onReceive(Context context, Intent incomingIntent) {
        if (incomingIntent == null || !incomingIntent.getAction().equals("found_roots")){
          return;
        }
        Intent intentToSuccessScreen = new Intent(MainActivity.this, SuccessActivity.class);

        intentToSuccessScreen.putExtra("original_number",incomingIntent.getLongExtra("original_number",-1));
        intentToSuccessScreen.putExtra("root1",incomingIntent.getLongExtra("root1",-1));
        intentToSuccessScreen.putExtra("root2",incomingIntent.getLongExtra("root2",-1));
        intentToSuccessScreen.putExtra("calculationTime",incomingIntent.getLongExtra("calculationTime",-1));

        startActivity(intentToSuccessScreen);

        resetUI();
      }
    };
    // register a broadcast-receiver to handle action "abort-cal"
    broadcastReceiverForAborted = new BroadcastReceiver() {
      @Override
      public void onReceive(Context context, Intent incomingIntent) {
        if (incomingIntent == null || !incomingIntent.getAction().equals("stopped_calculations")){
          return;
        }
        Toast.makeText(context,"implement abort msg",Toast.LENGTH_SHORT).show();
        resetUI();
      }
    };
  }

  private void registerRecivers() {
    registerReceiver(broadcastReceiverForSuccess, new IntentFilter("found_roots"));
    registerReceiver(broadcastReceiverForAborted, new IntentFilter("stopped_calculations"));
  }

  private static boolean isNumeric(String s) {
    try {
      Long.parseLong(s);
      return true;
    } catch(NumberFormatException e){
      return false;
    }
  }

  /**
   * Resets the UI.
   */
  private void resetUI() {
    _progressBar.setVisibility(View.GONE); // hide progress
    _editTextUserInput.setText(""); // cleanup text in edit-text
    _editTextUserInput.setEnabled(true); // set edit-text as enabled (user can input text)
    _buttonCalculateRoots.setEnabled(false); // set button as disabled (user can't click)
    _isAnyCalculationRunning = false;
  }

  /**
   *  when "calculate roots" button is clicked:
   *  change states for the progress, edit-text and button as needed, so user can't interact with the screen
   */
  private void calcualteRootsButtonClicked() {
    Intent intentToOpenService = new Intent(MainActivity.this, CalculateRootsService.class);
    String userInputString = _editTextUserInput.getText().toString();
    long userInputLong = (isNumeric(userInputString)) ?
            Long.parseLong(userInputString): Long.MAX_VALUE;
    if (userInputLong != Long.MAX_VALUE && !isAnyCalculationRunning()){
      intentToOpenService.putExtra("number_for_service", userInputLong);
      startService(intentToOpenService);
      changeUIWhenButtomClicked();
    }
  }

  /**
   *
   */
  private void changeUIWhenButtomClicked() {
    _userInput = "";
    _buttonCalculateRoots.setEnabled(false);
    _editTextUserInput.setEnabled(false);
    _progressBar.setVisibility(View.VISIBLE);
    _isAnyCalculationRunning = true;
  }

  /**
   *
   * @return
   */
  private boolean isAnyCalculationRunning() {
    return _isAnyCalculationRunning;
  }

  /**
   *
   * @param editTextUserInput
   * @return
   */
  private boolean isValidNumberInEditText(EditText editTextUserInput) {
    return isNumeric(editTextUserInput.getText().toString());
  }
}