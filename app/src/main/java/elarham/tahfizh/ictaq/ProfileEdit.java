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

import org.json.JSONException;
import org.json.JSONObject;

public class ProfileEdit extends AppCompatActivity {

    ActionBar actBar;
    EditText namaTxt, alamatTxt, tgl_lahirTxt, telpTxt, emailTxt, pekerjaanTxt, ayah_ibuTxt, tahunTxt;
    Spinner provSpin, kotaSpin;
    Button ubahBtn;

    String profileData, nama, alamat, tglLahir, telp, email, prov, kota, kerja, namaOrtu, thnMulai;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_edit);

        namaTxt = findViewById(R.id.namaTxt);
        alamatTxt = findViewById(R.id.alamatTxt);
        tgl_lahirTxt = findViewById(R.id.tgl_lahirTxt);
        telpTxt = findViewById(R.id.telpTxt);
        emailTxt = findViewById(R.id.emailTxt);
        pekerjaanTxt = findViewById(R.id.pekerjaanTxt);
        ayah_ibuTxt = findViewById(R.id.ayah_ibuTxt);
        tahunTxt = findViewById(R.id.tahunTxt);

        ubahBtn = findViewById(R.id.ubahBtn);

        provSpin = findViewById(R.id.provSpin);
        kotaSpin = findViewById(R.id.kotaSpin);


        actBar = getSupportActionBar();
        actBar.setTitle(getApplicationContext().getString(R.string.editprofile));
        actBar.setDisplayHomeAsUpEnabled(true);

        profileData = getIntent().getStringExtra("profileData");

        try {
            JSONObject profile = new JSONObject(profileData);
            namaTxt.setText(profile.getString("nama"));
            tahunTxt.setText(profile.getString("thn_menghafal"));
            //provinsiTxt.setText(profile.getString("provinsi"));
            pekerjaanTxt.setText(profile.getString("pekerjaan"));
            tgl_lahirTxt.setText(profile.getString("tgl_lahir"));
            alamatTxt.setText(profile.getString("alamat"));
            telpTxt.setText(profile.getString("telp"));
            emailTxt.setText(profile.getString("email"));
            ayah_ibuTxt.setText(profile.getString("ayah_ibu"));
        } catch (JSONException e) {
            e.printStackTrace();
        }


//        ubahBtn.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                nama = namaTxt.getText().toString();
//                alamat = alamatTxt.getText().toString();
//                tglLahir = tglLahirTxt.getText().toString();
//                telp = telpTxt.getText().toString();
//                email = emailTxt.getText().toString();
//                kerja = kerjaTxt.getText().toString();
//                namaOrtu = namaOrtuTxt.getText().toString();
//                thnMulai = thnMulaiTxt.getText().toString();
//
//                Toast.makeText(ProfileEdit.this, getApplicationContext().getString(R.string.editsuccess), Toast.LENGTH_SHORT).show();
//
//                Intent ubah = new Intent(ProfileEdit.this, MainActivity.class);
//                startActivity(ubah);
//            }
//        });
    }
}
