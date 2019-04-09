package elarham.tahfizh.ictaq;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
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

import elarham.tahfizh.ictaq.Global.Hashing;
import elarham.tahfizh.ictaq.Global.SharedPreferenceManager;


public class Account extends AppCompatActivity {

    ActionBar actBar;
    EditText usernameTxt, oldPasswordTxt, passwordTxt, password2Txt;
    Button ubahBtn;
    SharedPreferenceManager sharePrefMan;

    String username, oldPassword, newPassword, newPassword2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_account);

        actBar = getSupportActionBar();
        actBar.setDisplayHomeAsUpEnabled(true);
        actBar.setTitle(R.string.account);

        usernameTxt = findViewById(R.id.usernameTxt);
        oldPasswordTxt = findViewById(R.id.oldPasswordTxt);
        passwordTxt = findViewById(R.id.passwordTxt);
        password2Txt = findViewById(R.id.password2Txt);

        ubahBtn = findViewById(R.id.ubahBtn);

        sharePrefMan = new SharedPreferenceManager(this);
        usernameTxt.setText(sharePrefMan.getSpUsername());

        ubahBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                username = usernameTxt.getText().toString();
                oldPassword = oldPasswordTxt.getText().toString();
                newPassword = passwordTxt.getText().toString();
                newPassword2 = password2Txt.getText().toString();
                if(username.equals("") || oldPassword.equals("") || newPassword.equals("") || newPassword2.equals("")){
                    Toast.makeText(Account.this, getApplicationContext().getString(R.string.datanotcomplete), Toast.LENGTH_LONG).show();
                }else{
                    if(sharePrefMan.getSpPassword().equals(new Hashing().md5(oldPassword))){
                        if(newPassword.equals(newPassword2)){
                            ubahAkun();
                            Toast.makeText(Account.this, getApplicationContext().getString(R.string.editsuccess), Toast.LENGTH_SHORT).show();
                            Intent ubah = new Intent(Account.this, MainActivity.class);
                            startActivity(ubah);
                        }else{
                            Toast.makeText(Account.this, getApplicationContext().getString(R.string.newpasswordverificationfail), Toast.LENGTH_SHORT).show();
                        }

                    }else{
                        Toast.makeText(Account.this, getApplicationContext().getString(R.string.oldpasswordverificationfail), Toast.LENGTH_SHORT).show();
                    }
                }


            }
        });

    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void ubahAkun(){
        final ProgressDialog progressDialog = new ProgressDialog(Account.this);
        progressDialog.setMessage(getApplicationContext().getString(R.string.loading));
        progressDialog.show();

        RequestQueue queue = Volley.newRequestQueue(getApplicationContext());

        String url = getApplicationContext().getString(R.string.urlmain) +
                "/service/my_service.php?password=7ba52b255b999d6f1a7fa433a9cf7df4&aksi=update&tabel=user";

        StringRequest strRequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>()
                {
                    @Override
                    public void onResponse(String response)
                    {
                        Log.e("ACCOUNT EDIT SIMPAN", response);
                        progressDialog.dismiss();


                        try {
                            JSONObject simpan = new JSONObject(response);
                            if(simpan.getString("status").equals("sukses")){
                                Toast.makeText(Account.this, R.string.editsuccess, Toast.LENGTH_SHORT).show();
                                sharePrefMan.logout();
                                Intent ubah = new Intent(Account.this, Login.class);
                                startActivity(ubah);
                            }else{
                                Toast.makeText(Account.this, R.string.error, Toast.LENGTH_SHORT).show();
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

                params.put("value", String.format("username='%s',password='%s'",username, new Hashing().md5(newPassword)));

                params.put("where", String.format("where id_user='%s'",sharePrefMan.getSPIdUser()));
                return params;
            }
        };

        queue.add(strRequest);

    }
}
