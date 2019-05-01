package elarham.tahfizh.ictaq;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import elarham.tahfizh.ictaq.Global.SharedPreferenceManager;

public class Profile extends AppCompatActivity {

    ActionBar actBar;
    SharedPreferenceManager sharePrefMan;
    String profileData, kode;
    TextView namaTxt, typeTxt, tahunTxt, provinsiTxt, pekerjaanTxt, tgl_lahirTxt, alamatTxt, telpTxt, emailTxt, ayah_ibuTxt;


    boolean itemLoaded = false;

    String url;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        sharePrefMan = new SharedPreferenceManager(this);

        kode = sharePrefMan.getSpKode();

        actBar = getSupportActionBar();
        actBar.setTitle(getApplicationContext().getString(R.string.profile));
        actBar.setDisplayHomeAsUpEnabled(true);

        namaTxt = findViewById(R.id.namaTxt);
        typeTxt = findViewById(R.id.typeTxt);
        tahunTxt = findViewById(R.id.tahunTxt);
        provinsiTxt = findViewById(R.id.provinsiTxt);
        pekerjaanTxt = findViewById(R.id.pekerjaanTxt);
        tgl_lahirTxt = findViewById(R.id.tgl_lahirTxt);
        alamatTxt = findViewById(R.id.alamatTxt);
        telpTxt = findViewById(R.id.telpTxt);
        emailTxt = findViewById(R.id.emailTxt);
        ayah_ibuTxt = findViewById(R.id.ayah_ibuTxt);

        if(sharePrefMan.getSpType().equals("1")){
            typeTxt.setText(getApplicationContext().getString(R.string.admin));
        }else if(sharePrefMan.getSpType().equals("2")){
            typeTxt.setText(getApplicationContext().getString(R.string.examiner));
            url = getApplicationContext().getString(R.string.urlmain) +
                    "/service/my_service.php?password=7ba52b255b999d6f1a7fa433a9cf7df4&aksi=select&tabel=penguji";
        }else if(sharePrefMan.getSpType().equals("3")){
            typeTxt.setText(getApplicationContext().getString(R.string.hafizh));
            url = getApplicationContext().getString(R.string.urlmain) +
                    "/service/my_service.php?password=7ba52b255b999d6f1a7fa433a9cf7df4&aksi=select&tabel=registrasi";
        }else{
            typeTxt.setText(sharePrefMan.getSpType());
        }

        final ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setMessage(getApplicationContext().getString(R.string.loading));
        progressDialog.show();

        RequestQueue queue = Volley.newRequestQueue(getApplicationContext());


        StringRequest strRequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>()
                {
                    @Override
                    public void onResponse(String response)
                    {
                        Log.e("URL PROFILE", url);
                        Log.e("ID", kode);
                        Log.e("PROFILE ", response);


                        try {

                            JSONObject profile = new JSONObject(response).getJSONArray("data").getJSONObject(0);
                            profileData = profile.toString();
                            namaTxt.setText(profile.getString("nama"));
                            provinsiTxt.setText(profile.getString("provinsi"));
                            alamatTxt.setText(profile.getString("alamat") + ", " + profile.getString("kota"));
                            telpTxt.setText(profile.getString("telp"));


                            if(sharePrefMan.getSpType().equals("3")){
                                pekerjaanTxt.setText(profile.getString("pekerjaan"));
                                tgl_lahirTxt.setText(profile.getString("tgl_lahir"));
                                tahunTxt.setText(profile.getString("thn_menghafal"));
                                ayah_ibuTxt.setText(profile.getString("ayah_ibu"));
                                emailTxt.setText(profile.getString("email"));
                            }





                            itemLoaded = true;
                            invalidateOptionsMenu();



                        } catch (JSONException e) {
                            e.printStackTrace();
                            Toast.makeText(Profile.this, R.string.wrongdataformat, Toast.LENGTH_SHORT).show();
                            onBackPressed();
                        }

                        progressDialog.dismiss();
                    }
                },
                new Response.ErrorListener()
                {
                    @Override
                    public void onErrorResponse(VolleyError error)
                    {
                        Toast.makeText(Profile.this, R.string.error, Toast.LENGTH_SHORT).show();
                        Log.e("Volley Error", error.toString());
                        progressDialog.dismiss();
                        onBackPressed();
                    }
                })
        {
            @Override
            protected Map<String, String> getParams()
            {

                Map<String, String> params = new HashMap<String, String>();
                if(sharePrefMan.getSpType().equals("3")) {
                    params.put("where", String.format("where a.id='%s'", kode));
                }else  if(sharePrefMan.getSpType().equals("2")){
                    params.put("where", String.format("where id='%s'", kode));
                }
                return params;
            }
        };

        queue.add(strRequest);

    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.profil_menu, menu);

        MenuItem check = menu.findItem(R.id.edit_menu);
        if(itemLoaded){
            check.setVisible(true);
        }else{
            check.setVisible(false);
        }

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        Intent profil;
        switch (item.getItemId()){
            case android.R.id.home:
                onBackPressed();
                return true;
            case R.id.edit_menu:
                profil = new Intent(Profile.this, ProfileEdit.class);
                profil.putExtra("profileData", profileData);
                startActivity(profil);
                break;
        }


        return super.onOptionsItemSelected(item);
    }

}
