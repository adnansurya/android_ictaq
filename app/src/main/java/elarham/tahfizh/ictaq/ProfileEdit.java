package elarham.tahfizh.ictaq;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import elarham.tahfizh.ictaq.Models.ProvKota;

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
        getProvinsi();



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
    List<ProvKota> provKotaList;

    private void getProvinsi(){
        provKotaList = new ArrayList<>();
        final ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setMessage(getApplicationContext().getString(R.string.loading));
        progressDialog.show();

        RequestQueue queue = Volley.newRequestQueue(getApplicationContext());

        String url = getApplicationContext().getString(R.string.urlmain) + "/service/my_service.php?password=7ba52b255b999d6f1a7fa433a9cf7df4&aksi=select&tabel=provinces";

        StringRequest strRequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>()
                {
                    @Override
                    public void onResponse(String response)
                    {

                        try {
                            JSONArray provinsi = new JSONObject(response).getJSONArray("data");
                            for(int i=0; i<provinsi.length(); i++ ){

                                JSONObject jsonObjProv = provinsi.getJSONObject(i);
                                ProvKota provkota = new ProvKota();
                                provkota.setIdProvKota(jsonObjProv.getString("id"));
                                provkota.setNamaProvKota(jsonObjProv.getString("nama"));
                                provKotaList.add(provkota);



                        }

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        Log.e("PROFILE EDIT", response);
                        progressDialog.dismiss();
                    }
                },
                new Response.ErrorListener()
                {
                    @Override
                    public void onErrorResponse(VolleyError error)
                    {

                        Log.e("Volley Error", error.toString());
                        progressDialog.dismiss();;
                    }
                });

        queue.add(strRequest);

        ArrayAdapter<ProvKota> adapter = new ArrayAdapter<ProvKota>(this,
                android.R.layout.simple_spinner_item, provKotaList);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        provSpin.setAdapter(adapter);

        provSpin.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                ProvKota provkota = (ProvKota) parent.getSelectedItem();
                //displayUserData(user);

                Toast.makeText(ProfileEdit.this, provkota.getNamaProvKota().toString(), Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });


    }
}
