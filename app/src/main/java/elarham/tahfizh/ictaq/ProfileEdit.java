package elarham.tahfizh.ictaq;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
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

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import elarham.tahfizh.ictaq.Models.ProvKota;

public class ProfileEdit extends AppCompatActivity {

    ActionBar actBar;
    EditText namaTxt, alamatTxt, tgl_lahirTxt, telpTxt, emailTxt, pekerjaanTxt, ayah_ibuTxt, tahunTxt;
    Spinner provSpin, kotaSpin;
    Button ubahBtn;

    String profileData, idProfile, nama, alamat, tglLahir, telp, email, provinsi, kota, kerja, namaOrtu, thnMulai;
    String selectedIdKota, selectedIdProvinsi;

    List<String> provId, provNama, kotaId, kotaNama;
    ArrayAdapter<String> adapter, adapter1;
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

        profileData = getIntent().getStringExtra("profileData").replace(":null," , ":\"\",");

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

            idProfile = profile.getString("id");
            provinsi = profile.getString("provinsi");
            kota = profile.getString("kota");
        } catch (JSONException e) {
            e.printStackTrace();
        }

        provId = new ArrayList<>();
        provNama = new ArrayList<>();
        kotaId = new ArrayList<>();
        kotaNama = new ArrayList<>();

        adapter = new ArrayAdapter<>(this,
                android.R.layout.simple_spinner_item, provNama);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        provSpin.setAdapter(adapter);


        adapter1 = new ArrayAdapter<>(this,
                android.R.layout.simple_spinner_item, kotaNama);
        adapter1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        kotaSpin.setAdapter(adapter1);


        provSpin.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                selectedIdProvinsi = provId.get(provNama.indexOf(parent.getSelectedItem().toString()));

                kotaId.clear();
                kotaNama.clear();
                getKota();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        kotaSpin.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                selectedIdKota = kotaId.get(kotaNama.indexOf(parent.getSelectedItem().toString()));

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }

        });

        getProvinsi();

        setDatePicker();




        ubahBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                nama = namaTxt.getText().toString();
                alamat = alamatTxt.getText().toString();
                tglLahir = tgl_lahirTxt.getText().toString();
                telp = telpTxt.getText().toString();
                email = emailTxt.getText().toString();
                kerja = pekerjaanTxt.getText().toString();
                namaOrtu = ayah_ibuTxt.getText().toString();
                thnMulai = tahunTxt.getText().toString();

                final ProgressDialog progressDialog = new ProgressDialog(ProfileEdit.this);
                progressDialog.setMessage(getApplicationContext().getString(R.string.loading));
                progressDialog.show();

                RequestQueue queue = Volley.newRequestQueue(getApplicationContext());

                String url = getApplicationContext().getString(R.string.urlmain) +
                        "/service/my_service.php?password=7ba52b255b999d6f1a7fa433a9cf7df4&aksi=update&tabel=registrasi";

                StringRequest strRequest = new StringRequest(Request.Method.POST, url,
                        new Response.Listener<String>()
                        {
                            @Override
                            public void onResponse(String response)
                            {
                                Log.e("PROFILE EDIT SIMPAN", response);
                                dialog.dismiss();

                                Intent ubah = new Intent(ProfileEdit.this, MainActivity.class);
                                startActivity(ubah);
                                try {
                                     JSONObject simpan = new JSONObject(response);
                                     if(simpan.getString("status").equals("sukses")){
                                         Toast.makeText(ProfileEdit.this, R.string.editsuccess, Toast.LENGTH_SHORT).show();
                                     }else{
                                         Toast.makeText(ProfileEdit.this, R.string.error, Toast.LENGTH_SHORT).show();
                                     }
                                 } catch (JSONException e) {
                                     e.printStackTrace();
                                 }


                            }
                        }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.e("Volley", error.toString());
                        progressDialog.dismiss();
                    }
                })
                {
                    @Override
                    protected Map<String, String> getParams()
                    {

                        Map<String, String> params = new HashMap<String, String>();

                        params.put("value", String.format(
                    "nama='%s',tgl_lahir='%s',email='%s',alamat='%s',telp='%s',provinsi='%s',kota='%s',pekerjaan='%s',ayah_ibu='%s',thn_menghafal='%s'",
                                nama, tglLahir, email, alamat, telp, selectedIdProvinsi, selectedIdKota, kerja, namaOrtu, thnMulai ));

                        params.put("where", String.format("where id='%s'",idProfile));
                        return params;
                    }
                };

                queue.add(strRequest);



            }
        });

    }



    DatePickerDialog.OnDateSetListener date;
    DatePickerDialog dialog;

    @SuppressLint("ClickableViewAccessibility")
    private void setDatePicker(){


        final Calendar myCalendar = Calendar.getInstance();
        date = new DatePickerDialog.OnDateSetListener() {

            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear,
                                  int dayOfMonth) {
                // TODO Auto-generated method stub
                myCalendar.set(Calendar.YEAR, year);
                myCalendar.set(Calendar.MONTH, monthOfYear);
                myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);

                String myFormat = "yyyy-MM-dd"; //In which you need put here
                SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);

                tgl_lahirTxt.setText(sdf.format(myCalendar.getTime()));
                dialog.dismiss();
            }
        };

        dialog = new DatePickerDialog(this, date, myCalendar
                .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                myCalendar.get(Calendar.DAY_OF_MONTH));

        tgl_lahirTxt.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                dialog.show();
                return false;
            }
        });
    }


    private void getProvinsi(){

        final ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setMessage(getApplicationContext().getString(R.string.loading));
        progressDialog.show();

        RequestQueue queue = Volley.newRequestQueue(getApplicationContext());

        String url = getApplicationContext().getString(R.string.urlmain) +
                "/service/my_service.php?password=7ba52b255b999d6f1a7fa433a9cf7df4&aksi=select&tabel=provinces";

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
                                provId.add(jsonObjProv.getString("id"));
                                provNama.add(jsonObjProv.getString("name"));

                            }


                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        Log.e("PROFILE EDIT PROVINSI", response);

                        adapter.notifyDataSetChanged();
                        provSpin.setSelection(provNama.indexOf(provinsi));
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


    }

    private void getKota(){

        final ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setMessage(getApplicationContext().getString(R.string.loading));
        progressDialog.show();

        RequestQueue queue = Volley.newRequestQueue(getApplicationContext());

        String url = getApplicationContext().getString(R.string.urlmain) + "/service/my_service.php?password=7ba52b255b999d6f1a7fa433a9cf7df4&aksi=select&tabel=regencies";

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
                                kotaId.add(jsonObjProv.getString("id"));
                                kotaNama.add(jsonObjProv.getString("name"));

                            }


                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        Log.e("PROFILE EDIT KOTA", response);

                        adapter1.notifyDataSetChanged();
                        kotaSpin.setSelection(kotaNama.indexOf(kota));
                        progressDialog.dismiss();
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("Volley", error.toString());
                progressDialog.dismiss();
            }
        })
        {
            @Override
            protected Map<String, String> getParams()
            {

                Map<String, String> params = new HashMap<String, String>();
                params.put("where", String.format("where province_id='%s'",selectedIdProvinsi));
                return params;
            }
        };

        queue.add(strRequest);



    }


}
