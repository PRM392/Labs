package com.example.lab10;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;

public class EditPersonActivity extends AppCompatActivity {
    EditText firstName, lastName;
    Button button;
    int mPersonId;
    AppDatabase mDb;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_person);

        firstName = findViewById(R.id.edit_first_name);
        lastName = findViewById(R.id.edit_last_name);
        button = findViewById(R.id.save_button);

        mDb = AppDatabase.getInstance(getApplicationContext());

        Intent intent = getIntent();
        if (intent != null && intent.hasExtra(Constants.UPDATE_Person_Id)) {
            button.setText("Update");
            mPersonId = intent.getIntExtra(Constants.UPDATE_Person_Id, -1);

            AppExecutors.getInstance().diskIO().execute(new Runnable() {
                @Override
                public void run() {
                    Person person = mDb.personDao().loadPersonById(mPersonId);
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            populateUI(person);
                        }
                    });
                }
            });
        }

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onSaveButtonClicked();
            }
        });
    }

    private void populateUI(Person person) {
        if (person == null) {
            return;
        }
        firstName.setText(person.getFirstName());
        lastName.setText(person.getLastName());
    }

    public void onSaveButtonClicked() {
        String first = firstName.getText().toString();
        String last = lastName.getText().toString();

        final Person person = new Person(first, last);
        AppExecutors.getInstance().diskIO().execute(new Runnable() {
            @Override
            public void run() {
                if (getIntent().hasExtra(Constants.UPDATE_Person_Id)) {
                    person.setUid(mPersonId);
                    mDb.personDao().update(person);
                } else {
                    mDb.personDao().insert(person);
                }
                finish();
            }
        });
    }
}
