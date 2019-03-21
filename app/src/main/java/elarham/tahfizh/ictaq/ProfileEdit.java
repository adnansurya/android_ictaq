package elarham.tahfizh.ictaq;

import android.content.Intent;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

public class ProfileEdit extends AppCompatActivity {

    ActionBar actBar;
    EditText namaTxt, alamatTxt, tglLahirTxt, telpTxt, emailTxt, kerjaTxt, namaOrtuTxt, thnMulaiTxt,
            usernameTxt, passwordTxt, password2Txt;
    Spinner provSpin, kotaSpin;
    Button ubahBtn;

    String nama, alamat, tglLahir, telp, email, prov, kota, kerja, namaOrtu, thnMulai, username, password, password2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_edit);

        namaTxt = findViewById(R.id.namaTxt);
        alamatTxt = findViewById(R.id.alamatTxt);
        tglLahirTxt = findViewById(R.id.tglLahirTxt);
        telpTxt = findViewById(R.id.telpTxt);
        emailTxt = findViewById(R.id.emailTxt);
        kerjaTxt = findViewById(R.id.kerjaTxt);
        namaOrtuTxt = findViewById(R.id.namaOrtuTxt);
        thnMulaiTxt = findViewById(R.id.thnMulaiTxt);

        usernameTxt = findViewById(R.id.usernameTxt);
        passwordTxt = findViewById(R.id.passwordTxt);
        password2Txt = findViewById(R.id.password2Txt);

        provSpin = findViewById(R.id.provSpin);
        kotaSpin = findViewById(R.id.kotaSpin);

        ubahBtn = findViewById(R.id.ubahBtn);

        actBar = getSupportActionBar();
        actBar.setTitle(getApplicationContext().getString(R.string.editprofile));
        actBar.setDisplayHomeAsUpEnabled(true);

        ubahBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                nama = namaTxt.getText().toString();
                alamat = alamatTxt.getText().toString();
                tglLahir = tglLahirTxt.getText().toString();
                telp = telpTxt.getText().toString();
                email = emailTxt.getText().toString();
                kerja = kerjaTxt.getText().toString();
                namaOrtu = namaOrtuTxt.getText().toString();
                thnMulai = thnMulaiTxt.getText().toString();

                username = usernameTxt.getText().toString();
                password = passwordTxt.getText().toString();
                password2 = password2Txt.getText().toString();

                Toast.makeText(ProfileEdit.this, getApplicationContext().getString(R.string.editsuccess), Toast.LENGTH_SHORT).show();

                Intent ubah = new Intent(ProfileEdit.this, MainActivity.class);
                startActivity(ubah);
            }
        });
    }
}
