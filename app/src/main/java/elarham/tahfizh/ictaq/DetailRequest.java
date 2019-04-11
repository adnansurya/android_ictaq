package elarham.tahfizh.ictaq;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
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

public class DetailRequest extends AppCompatActivity {

    ActionBar actBar;
    String id, idRegis, idPenguji, tanggal, status;
    TextView tanggalTxt, namaTxt, pekerjaanTxt, alamatTxt, tgl_LahirTxt, ayah_ibuTxt, tahunTxt, emailTxt, telpTxt;

    String nama;

    SharedPreferenceManager sharePrefMan;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_request);


        actBar = getSupportActionBar();
        actBar.setTitle(getApplicationContext().getString(R.string.request));
        actBar.setDisplayHomeAsUpEnabled(true);



        id = getIntent().getStringExtra("id");
        idRegis = getIntent().getStringExtra("idRegis");
        idPenguji = getIntent().getStringExtra("idPenguji");
        tanggal = getIntent().getStringExtra("tanggal");
        status = getIntent().getStringExtra("status");

        tanggalTxt = findViewById(R.id.tanggalTxt);
        namaTxt = findViewById(R.id.namaTxt);
        pekerjaanTxt = findViewById(R.id.pekerjaanTxt);
        alamatTxt = findViewById(R.id.alamatTxt);
        tgl_LahirTxt = findViewById(R.id.tgl_lahirTxt);
        ayah_ibuTxt = findViewById(R.id.ayah_ibuTxt);
        tahunTxt = findViewById(R.id.tahunTxt);
        emailTxt = findViewById(R.id.emailTxt);
        telpTxt = findViewById(R.id.telpTxt);

        tanggalTxt.setText(getApplicationContext().getString(R.string.sent)+ " : " + tanggal);


        sharePrefMan = new SharedPreferenceManager(this);

        getUserData();



    }

    private void getUserData(){
        final ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setMessage(getApplicationContext().getString(R.string.loading));
        progressDialog.show();

        RequestQueue queue = Volley.newRequestQueue(getApplicationContext());

        final String url = getApplicationContext().getString(R.string.urlmain) +
                "/service/my_service.php?password=7ba52b255b999d6f1a7fa433a9cf7df4&aksi=select&tabel=registrasi";

        StringRequest strRequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>()
                {
                    @Override
                    public void onResponse(String response)
                    {
                        Log.e("URL DETAIL REQ", url);
                        Log.e("DETAIL REQ ", response);


                        try {

                            JSONObject profile = new JSONObject(response).getJSONArray("data").getJSONObject(0);
                            nama = profile.getString("nama");
                            namaTxt.setText(nama);
                            tahunTxt.setText(profile.getString("thn_menghafal"));
                            pekerjaanTxt.setText(profile.getString("pekerjaan"));
                            tgl_LahirTxt.setText(profile.getString("tgl_lahir"));
                            alamatTxt.setText(profile.getString("alamat") + ", " + profile.getString("kota") + ", " + profile.getString("provinsi"));
                            telpTxt.setText(profile.getString("telp"));
                            emailTxt.setText(profile.getString("email"));
                            ayah_ibuTxt.setText(profile.getString("ayah_ibu"));


                        } catch (JSONException e) {
                            e.printStackTrace();
                            Toast.makeText(DetailRequest.this, R.string.wrongdataformat, Toast.LENGTH_SHORT).show();
                        }

                        progressDialog.dismiss();
                    }
                },
                new Response.ErrorListener()
                {
                    @Override
                    public void onErrorResponse(VolleyError error)
                    {
                        Toast.makeText(DetailRequest.this, R.string.error, Toast.LENGTH_SHORT).show();
                        Log.e("Volley Error", error.toString());
                        progressDialog.dismiss();
                    }
                })
        {
            @Override
            protected Map<String, String> getParams()
            {

                Map<String, String> params = new HashMap<String, String>();
                if(sharePrefMan.getSpType().equals("2")){
                    params.put("where", String.format("where a.id='%s'",idRegis));
                }else{
                    params.put("where", String.format("where a.id='%s'",idPenguji));
                }


                return params;
            }
        };

        queue.add(strRequest);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {


        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.detail_request_menu, menu);

        if(sharePrefMan.getSpType().trim().equals("3")){
            MenuItem check = menu.findItem(R.id.accept_menu);
            check.setVisible(false);
        }

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {


        switch (item.getItemId()){
            case R.id.accept_menu:

                AlertDialog.Builder builder = new AlertDialog.Builder(this);

                builder.setTitle(getApplicationContext().getString(R.string.examrequest));
                builder.setMessage(getApplicationContext().getString(R.string.examrequestdialog) + " " + nama + " ?");

                builder.setPositiveButton(R.string.yes, new DialogInterface.OnClickListener() {

                    public void onClick(DialogInterface dialog, int which) {

//                        requestJadwal(sharePrefMan.getSpKode(),user.getKode(),String.valueOf(sdf.format(myCalendar.getTime())),"0");
                        Intent terima = new Intent(DetailRequest.this, MainActivity.class);
                        startActivity(terima);
                        dialog.dismiss();
                    }
                });

                builder.setNegativeButton(R.string.no, new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        // Do nothing
                        dialog.dismiss();
                    }
                });

                AlertDialog alert = builder.create();
                alert.show();

                break;
        }


        return super.onOptionsItemSelected(item);
    }
}
