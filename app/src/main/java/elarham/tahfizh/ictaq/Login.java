package elarham.tahfizh.ictaq;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextPaint;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
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

public class Login extends AppCompatActivity {

    EditText usernameTxt, passwordTxt;
    String username, password;
    TextView daftarTxt;
    Button loginBtn;
    int backButtonCount = 0;

    SharedPreferenceManager sharePrefMan;





    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        sharePrefMan = new SharedPreferenceManager(this);

        daftarTxt = findViewById(R.id.daftarTxt);
        loginBtn = findViewById(R.id.loginBtn);
        usernameTxt = findViewById(R.id.usernameTxt);
        passwordTxt = findViewById(R.id.passwordTxt);



        if(sharePrefMan.getSPSudahLogin()){
            Intent login = new Intent(Login.this, MainActivity.class);
            startActivity(login);
        }
        loginBtn.setOnClickListener(new View.OnClickListener() {
             @Override
             public void onClick(View v) {

                 username = usernameTxt.getText().toString();
                 password = passwordTxt.getText().toString();

                 if(username.equals("") || password.equals("")){
                     Toast.makeText(Login.this, R.string.datanotcomplete, Toast.LENGTH_SHORT).show();
                 }

                 final ProgressDialog progressDialog = new ProgressDialog(Login.this);
                 progressDialog.setMessage(getApplicationContext().getString(R.string.loading));
                 progressDialog.show();

                 RequestQueue queue = Volley.newRequestQueue(getApplicationContext());

                 String url = getApplicationContext().getString(R.string.urlmain) + "/index.php/login/cek_user";

                 StringRequest strRequest = new StringRequest(Request.Method.POST, url,
                         new Response.Listener<String>()
                         {
                             @Override
                             public void onResponse(String response)
                             {

                                 try {
                                     JSONObject login = new JSONObject(response);
                                     if(login.getString("status").equals("0")){
                                         Toast.makeText(Login.this, R.string.loginfail, Toast.LENGTH_SHORT).show();
                                     }else if(login.getString("status").equals("1") || login.getString("status").equals("2")){
                                         Toast.makeText(Login.this, R.string.loginok, Toast.LENGTH_SHORT).show();
                                         userLogin();
                                         Intent home = new Intent(Login.this, MainActivity.class);
                                         home.putExtra("username", username);
                                         startActivity(home);
                                     }else{
                                         Toast.makeText(Login.this, R.string.error, Toast.LENGTH_SHORT).show();
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
                         params.put("password", password);

                         return params;
                     }
                 };

                 queue.add(strRequest);


             }
         });

         daftarTxt.setOnClickListener(new View.OnClickListener() {
             @Override
             public void onClick(View v) {

                 Intent daftar = new Intent(Login.this, Daftar.class);
                 startActivity(daftar);

             }
         });


    }

    public void userLogin(){
        final ProgressDialog progressDialog = new ProgressDialog(Login.this);
        progressDialog.setMessage(getApplicationContext().getString(R.string.loading));
        progressDialog.show();

        RequestQueue queue = Volley.newRequestQueue(getApplicationContext());

        String url = getApplicationContext().getString(R.string.urlmain) +
                "/service/my_service.php?password=7ba52b255b999d6f1a7fa433a9cf7df4&aksi=select&tabel=user";;

        StringRequest strRequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>()
                {
                    @Override
                    public void onResponse(String response)
                    {
                        Log.e("Volley Success", response);


                        try {

                            JSONObject userData = new JSONObject(response).getJSONArray("data").getJSONObject(0);
                            sharePrefMan.setSPString(sharePrefMan.SP_USERDATA, userData.toString());
                            sharePrefMan.setSPString(sharePrefMan.SP_KODE, userData.getString("kode"));
                            sharePrefMan.setSPString(sharePrefMan.SP_NAMA, userData.getString("nama"));
                            sharePrefMan.setSPString(sharePrefMan.SP_USERNAME, userData.getString("username"));
                            sharePrefMan.setSPString(sharePrefMan.SP_PASSWORD, userData.getString("password"));

                            sharePrefMan.setSPString(sharePrefMan.SP_TYPE, userData.getString("type"));
                            sharePrefMan.setSPBoolean(sharePrefMan.SP_SUDAH_LOGIN, true);
                            Log.e("KODE",sharePrefMan.getSpKode());


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
                params.put("where", "where username='" + username + "'");

                return params;
            }
        };

        queue.add(strRequest);
    }
    @Override
    public void onBackPressed() {

        if(backButtonCount >= 1)
        {
            Intent intent = new Intent(Intent.ACTION_MAIN);
            intent.addCategory(Intent.CATEGORY_HOME);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
        }
        else
        {
            Toast.makeText(this, getApplicationContext().getString(R.string.backquit), Toast.LENGTH_SHORT).show();
            backButtonCount++;
        }
    }
}
