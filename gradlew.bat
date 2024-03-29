package sg.edu.rp.c346.p05_ps;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;

public class EditActivity extends AppCompatActivity {

    Button btnCancel, btnDelete, btnUpdate;
    EditText etTitle, etSinger;
    RadioGroup radGroup;
    Task data;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit);

        btnDelete = findViewById(R.id.btnDelete);
        btnUpdate = findViewById(R.id.btnInsert);

        etTitle = findViewById(R.id.etTask);
        etSinger = findViewById(R.id.etDate);

        Intent i = getIntent();
        data = (Task) i.getSerializableExtra("data");
        etTitle.setText(data.getName());
        etSinger.setText(data.getDate());

        btnDelete.setOnClickListener((v)->{
            DBHelper dbh = new DBHelper(EditActivity.this);
            dbh.deleteTask(data.getId());
            dbh.close();
        });

        btnUpdate.setOnClickListener((v)->{
            DBHelper db = new DBHelper(EditActivity.this);

            data.setName(etTitle.getText().toString());
            data.setDate(etSinger.getText().toString());

            db.updateTask(data);
            db.close();

            Intent o = new Intent();
            setResult(RESULT_OK, o);
            finish();
        });

    }
}
                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                   