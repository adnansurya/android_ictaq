package elarham.tahfizh.ictaq;

import android.content.Intent;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextPaint;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class Login extends AppCompatActivity {

    EditText usernameTxt, passwordTxt;
    String username, password;
    TextView daftarTxt;
    Button loginBtn;
    int backButtonCount = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

         daftarTxt = findViewById(R.id.daftarTxt);
         loginBtn = findViewById(R.id.loginBtn);
         usernameTxt = findViewById(R.id.usernameTxt);
         passwordTxt = findViewById(R.id.passwordTxt);

         loginBtn.setOnClickListener(new View.OnClickListener() {
             @Override
             public void onClick(View v) {

                 username = usernameTxt.getText().toString();
                 password = passwordTxt.getText().toString();

                 Intent home = new Intent(Login.this, MainActivity.class);
                 startActivity(home);

             }
         });

         daftarTxt.setOnClickListener(new View.OnClickListener() {
             @Override
             public void onClick(View v) {

                 Intent daftar = new Intent(Login.this, Daftar.class);
                 startActivity(daftar);

             }
         });


    }
    @Override
    public void onBackPressed() {

        if(backButtonCount >= 1)
        {
            Intent intent = new Intent(Intent.ACTION_MAIN);
            intent.addCategory(Intent.CATEGORY_HOME);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
        }
        else
        {
            Toast.makeText(this, getApplicationContext().getString(R.string.backquit), Toast.LENGTH_SHORT).show();
            backButtonCount++;
        }
    }
}
