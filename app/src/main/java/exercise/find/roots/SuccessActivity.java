package exercise.find.roots;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

public class SuccessActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_success);

        Intent intentCreatedMe = getIntent();
        long originalNumber = intentCreatedMe.getLongExtra("original_number",-1);
        long root1 = intentCreatedMe.getLongExtra("root1",-1);
        long root2 = intentCreatedMe.getLongExtra("root2",-1);
        long calculationTime = intentCreatedMe.getLongExtra("calculationTime",-1);

        String originalNumberText = "Original Number: " + originalNumber;
        String root1Text = "First root: " + root1;
        String root2Text = "Second root: " + root2;
        String calculationTimeText = "Calculation time: " + calculationTime + " ms";

        ((TextView)findViewById(R.id.originalNumber)).setText(originalNumberText);
        ((TextView)findViewById(R.id.root1)).setText(root1Text);
        ((TextView)findViewById(R.id.root2)).setText(root2Text);
        ((TextView)findViewById(R.id.calculationTime)).setText(calculationTimeText);
    }
}