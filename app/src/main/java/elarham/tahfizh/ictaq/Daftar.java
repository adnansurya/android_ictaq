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
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class Daftar extends AppCompatActivity {

    EditText namaTxt, emailTxt, usernameTxt, passwordTxt, password2Txt;
    String nama, email, username, password, password2;
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

                        final ProgressDialog progressDialog = new ProgressDialog(Daftar.this);
                        progressDialog.setMessage(getApplicationContext().getString(R.string.loading));
                        progressDialog.show();

                        RequestQueue queue = Volley.newRequestQueue(getApplicationContext());
                        String url ="http://elarham-tahfizh.online/service/my_service.php?password=7ba52b255b999d6f1a7fa433a9cf7df4&aksi=insert&tabel=registrasi";

                        StringRequest strRequest = new StringRequest(Request.Method.POST, url,
                                new Response.Listener<String>()
                                {
                                    @Override
                                    public void onResponse(String response)
                                    {
                                        Toast.makeText(getApplicationContext(), response, Toast.LENGTH_SHORT).show();
                                        Log.e("Volley Success", response);
                                        progressDialog.dismiss();
                                    }
                                },
                                new Response.ErrorListener()
                                {
                                    @Override
                                    public void onErrorResponse(VolleyError error)
                                    {
                                        Toast.makeText(getApplicationContext(), error.toString(), Toast.LENGTH_SHORT).show();
                                        Log.e("Volley Error", error.toString());
                                        progressDialog.dismiss();;
                                    }
                                })
                        {
                            @Override
                            protected Map<String, String> getParams()
                            {
                                Map<String, String> params = new HashMap<String, String>();
                                params.put("username", username);
                                params.put("type",String.valueOf(1));
                                params.put("nama",nama);
                                params.put("password","7ba52b255b999d6f1a7fa433a9cf7df4");
                                params.put("email",email);
                                return params;
                            }
                        };

                        queue.add(strRequest);

                    }else{
                        Toast.makeText(Daftar.this, getApplicationContext().getString(R.string.passwordverificationfail), Toast.LENGTH_LONG).show();
                    }
                }

//                Toast.makeText(Daftar.this, getApplicationContext().getString(R.string.registersuccess), Toast.LENGTH_SHORT).show();
//
//                Intent home = new Intent(Daftar.this, MainActivity.class);
//                startActivity(home);

            }
        });

    }
}
