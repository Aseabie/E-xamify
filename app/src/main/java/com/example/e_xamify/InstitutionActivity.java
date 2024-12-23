package com.example.e_xamify;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

public class InstitutionActivity extends AppCompatActivity {

    private DatabaseHelper dbHelper;
    private int user_id; // User ID received from SignInActivity
    private Button showEnrollmentKeyButton, createModuleButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_institution);

        dbHelper = new DatabaseHelper(this);
        user_id = getIntent().getIntExtra("user_id", -1); // Retrieve user ID from Intent

        if (user_id == -1) {
            Toast.makeText(this, "User ID not found.", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        showEnrollmentKeyButton = findViewById(R.id.showEnrollmentKeyButton);
        createModuleButton = findViewById(R.id.createModuleButton);



        showEnrollmentKeyButton.setOnClickListener(v -> showEnrollmentKey());
        createModuleButton.setOnClickListener(v -> {
            Intent intent = new Intent(InstitutionActivity.this, ModuleActivity.class);
            intent.putExtra("user_id", user_id);  // Pass the user_id to ModuleActivity
            startActivity(intent);
        });
    }

    private void showEnrollmentKey() {
        // Inflate the enrollment key layout dynamically
        LayoutInflater inflater = LayoutInflater.from(this);
        LinearLayout enrollmentKeyLayout = (LinearLayout) inflater.inflate(R.layout.enrollment_key_layout, null);

        // Fetch the enrollment key from the database
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT institution_enrolment_key FROM institution WHERE user_id = ?", new String[]{String.valueOf(user_id)});

        if (cursor.moveToFirst()) {
            String enrollmentKey = cursor.getString(0);
            TextView enrollmentKeyTextView = enrollmentKeyLayout.findViewById(R.id.enrollmentKeyTextView);
            enrollmentKeyTextView.setText(enrollmentKey);
        } else {
            Toast.makeText(this, "No enrollment key found for this institution.", Toast.LENGTH_SHORT).show();
            cursor.close();
            return;
        }
        cursor.close();

        // Display the inflated view with the enrollment key
        LinearLayout mainLayout = findViewById(R.id.main_layout); // Assume main_layout is a LinearLayout in your activity's layout
        mainLayout.addView(enrollmentKeyLayout);
    }
}
