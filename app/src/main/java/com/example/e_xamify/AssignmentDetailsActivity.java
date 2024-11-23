package com.example.e_xamify;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

public class AssignmentDetailsActivity extends AppCompatActivity {
    private DatabaseHelper dbHelper;
    private int assignmentId;
    private int user_id;
    private TextView quizTitleText;
    private TextView instructionsText;
    private TextView attemptsText;
    private Button startAssignmentButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_assignment_details);

        quizTitleText = findViewById(R.id.quizTitleText);
        instructionsText = findViewById(R.id.instructionsText);
        attemptsText = findViewById(R.id.attemptsText);
        startAssignmentButton = findViewById(R.id.startAssignmentButton);

        assignmentId = getIntent().getIntExtra("assignmentId", -1);
        user_id = getIntent().getIntExtra("user_id", -1);

        if (assignmentId == -1 || user_id == -1) {
            Toast.makeText(this, "Error loading assignment details", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        dbHelper = new DatabaseHelper(this);
        loadAssignmentDetails();

        startAssignmentButton.setOnClickListener(v -> startAssignment());
    }

    private void loadAssignmentDetails() {
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        Cursor cursor = db.rawQuery(
                "SELECT q.quiz_title, q.instructions, a.attempt_number_left FROM quiz q " +
                        "JOIN assignment a ON q.quiz_id = a.quiz_id WHERE a.assignment_id = ?",
                new String[]{String.valueOf(assignmentId)}
        );

        if (cursor.moveToFirst()) {
            String title = cursor.getString(0);
            String instructions = cursor.getString(1);
            int attempts = cursor.getInt(2);

            quizTitleText.setText(title);
            instructionsText.setText(instructions);
            attemptsText.setText("Attempts left: " + attempts);
        }
        cursor.close();
    }

    private void startAssignment() {

        Intent intent = new Intent(this, AssignmentTakingActivity.class);
        intent.putExtra("assignmentId", assignmentId);
        intent.putExtra("user_id", user_id);
        startActivity(intent);

    }
}