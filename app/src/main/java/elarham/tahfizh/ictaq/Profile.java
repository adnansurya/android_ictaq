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
    String username, kode;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        sharePrefMan = new SharedPreferenceManager(this);

        kode = sharePrefMan.getSpKode();

        actBar = getSupportActionBar();
        actBar.setTitle(getApplicationContext().getString(R.string.profile));
        actBar.setDisplayHomeAsUpEnabled(true);

        final ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setMessage(getApplicationContext().getString(R.string.loading));
        progressDialog.show();

        RequestQueue queue = Volley.newRequestQueue(getApplicationContext());

        String url = getApplicationContext().getString(R.string.urlmain) +
                "/service/my_service.php?password=7ba52b255b999d6f1a7fa433a9cf7df4&aksi=select&tabel=registrasi";;

        StringRequest strRequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>()
                {
                    @Override
                    public void onResponse(String response)
                    {
                        Log.e("PROFILE", response);


                        try {

                            JSONObject profileData = new JSONObject(response).getJSONArray("data").getJSONObject(0);



                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

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
                })
        {
            @Override
            protected Map<String, String> getParams()
            {

                Map<String, String> params = new HashMap<String, String>();
                params.put("where", "where id='" + kode + "'");

                return params;
            }
        };

        queue.add(strRequest);

    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.profil_menu, menu);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        Intent profil;
        switch (item.getItemId()){
            case R.id.edit_menu:
                profil = new Intent(Profile.this, ProfileEdit.class);
                startActivity(profil);
                break;
        }


        return super.onOptionsItemSelected(item);
    }

}
