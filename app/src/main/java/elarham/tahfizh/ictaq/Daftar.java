package elarham.tahfizh.ictaq;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.RetryPolicy;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import elarham.tahfizh.ictaq.Global.Hashing;

public class Daftar extends AppCompatActivity {

    EditText namaTxt, emailTxt, usernameTxt, passwordTxt, password2Txt;
    String nama, email, username, password, password2, hash_pass;
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

                if(nama.equals("") || email.equals("") || username.equals("") || password.equals("")){
                    Toast.makeText(Daftar.this, getApplicationContext().getString(R.string.datanotcomplete), Toast.LENGTH_LONG).show();
                }else{
                    if(password.equals(password2)){

                        hash_pass = new Hashing().md5(password);
//                        Log.e("HASH", hash_pass);

                        final ProgressDialog progressDialog = new ProgressDialog(Daftar.this);
                        progressDialog.setMessage(getApplicationContext().getString(R.string.loading));
                        progressDialog.show();

                        RequestQueue queue = Volley.newRequestQueue(getApplicationContext());

                        String url = getApplicationContext().getString(R.string.urlmain) +
                                "/service/my_service.php?password=7ba52b255b999d6f1a7fa433a9cf7df4&aksi=regis";

                        StringRequest strRequest = new StringRequest(Request.Method.POST, url,
                                new Response.Listener<String>()
                                {
                                    @Override
                                    public void onResponse(String response)
                                    {
                                        try {
                                            JSONObject daftar = new JSONObject(response);
                                            Log.e("RESPON : ", response);
                                            if(daftar.getString("status").equals("sukses")){
                                                Toast.makeText(Daftar.this, R.string.registerok, Toast.LENGTH_SHORT).show();

                                                Intent login = new Intent(Daftar.this, Login.class);
                                                startActivity(login);

                                            }else{
                                                Toast.makeText(Daftar.this, R.string.error, Toast.LENGTH_SHORT).show();
                                            }
                                        } catch (JSONException e) {
                                            e.printStackTrace();
                                        }
                                        Log.e("Volley Success", response);
                                        progressDialog.dismiss();
                                    }
                                },
                                new Response.ErrorListener()
                                {
                                    @Override
                                    public void onErrorResponse(VolleyError error)
                                    {
                                        Toast.makeText(Daftar.this, R.string.error, Toast.LENGTH_SHORT).show();
                                        Log.e("Volley Error", error.toString());
                                        progressDialog.dismiss();;
                                    }
                                })
                        {
                            @Override
                            protected Map<String, String> getParams()
                            {

                                Map<String, String> params = new HashMap<String, String>();
                              //  params.put("field", "username,password,type,nama, kode");
                                params.put("value",username + "," + hash_pass + "," + nama + "," + email);


                                return params;
                            }
                        };

                        queue.add(strRequest).setRetryPolicy(new RetryPolicy() {
                            @Override
                            public int getCurrentTimeout() {
                                return 5000;
                            }

                            @Override
                            public int getCurrentRetryCount() {
                                return 0; //retry turn off
                            }

                            @Override
                            public void retry(VolleyError error) throws VolleyError {

                            }
                        });;


                    }else{
                        Toast.makeText(Daftar.this, getApplicationContext().getString(R.string.passwordverificationfail), Toast.LENGTH_LONG).show();
                    }
                }

            }
        });

    }




}
