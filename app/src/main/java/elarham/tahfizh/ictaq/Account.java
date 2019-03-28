package elarham.tahfizh.ictaq;

import android.content.Intent;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import elarham.tahfizh.ictaq.Global.Hashing;
import elarham.tahfizh.ictaq.Global.SharedPreferenceManager;


public class Account extends AppCompatActivity {

    ActionBar actBar;
    EditText usernameTxt, oldPasswordTxt, passwordTxt, password2Txt;
    Button ubahBtn;
    SharedPreferenceManager sharePrefMan;

    String username, oldPassword, newPassword, newPassword2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_account);

        actBar = getSupportActionBar();
        actBar.setDisplayHomeAsUpEnabled(true);
        actBar.setTitle(R.string.account);

        usernameTxt = findViewById(R.id.usernameTxt);
        oldPasswordTxt = findViewById(R.id.oldPasswordTxt);
        passwordTxt = findViewById(R.id.passwordTxt);
        password2Txt = findViewById(R.id.password2Txt);

        ubahBtn = findViewById(R.id.ubahBtn);

        sharePrefMan = new SharedPreferenceManager(this);
        usernameTxt.setText(sharePrefMan.getSpUsername());

        ubahBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                username = usernameTxt.getText().toString();
                oldPassword = oldPasswordTxt.getText().toString();
                newPassword = passwordTxt.getText().toString();
                newPassword2 = password2Txt.getText().toString();
                if(username.equals("") || oldPassword.equals("") || newPassword.equals("") || newPassword2.equals("")){
                    Toast.makeText(Account.this, getApplicationContext().getString(R.string.datanotcomplete), Toast.LENGTH_LONG).show();
                }else{
                    if(sharePrefMan.getSpPassword().equals(new Hashing().md5(oldPassword))){
                        if(newPassword.equals(newPassword2)){
                            Toast.makeText(Account.this, getApplicationContext().getString(R.string.editsuccess), Toast.LENGTH_SHORT).show();
                            Intent ubah = new Intent(Account.this, MainActivity.class);
                            startActivity(ubah);
                        }else{
                            Toast.makeText(Account.this, getApplicationContext().getString(R.string.passwordverificationfail), Toast.LENGTH_SHORT).show();
                        }

                    }else{
                        Toast.makeText(Account.this, getApplicationContext().getString(R.string.oldpasswordverificationfail), Toast.LENGTH_SHORT).show();
                    }
                }


            }
        });

    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
