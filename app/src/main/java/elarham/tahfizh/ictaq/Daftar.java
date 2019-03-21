package elarham.tahfizh.ictaq;

import android.content.Intent;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class Daftar extends AppCompatActivity {

    EditText namaTxt, emailTxt, usernameTxt, passwordTxt, password2Txt;
    String nama, email, username, password, password2;
    Button daftarBtn;
    ActionBar actBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_daftar);

        namaTxt = findViewById(R.id.namaTxt);
        emailTxt = findViewById(R.id.emailTxt);
        usernameTxt = findViewById(R.id.usernameTxt);
        passwordTxt = findViewById(R.id.passwordTxt);
        password2Txt = findViewById(R.id.password2Txt);

        daftarBtn = findViewById(R.id.daftarBtn);

        actBar = getSupportActionBar();
        actBar.setTitle(getApplicationContext().getString(R.string.newaccount));
        actBar.setDisplayHomeAsUpEnabled(true);

        daftarBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                nama = namaTxt.getText().toString();
                email = emailTxt.getText().toString();
                username = usernameTxt.getText().toString();
                password = passwordTxt.getText().toString();
                password2 = password2Txt.getText().toString();

                Intent home = new Intent(Daftar.this, MainActivity.class);
                startActivity(home);

            }
        });

    }
}
