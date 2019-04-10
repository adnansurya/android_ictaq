package elarham.tahfizh.ictaq.UstadzFragment;

import android.app.ProgressDialog;
import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
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
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import elarham.tahfizh.ictaq.Global.SharedPreferenceManager;
import elarham.tahfizh.ictaq.Models.User;
import elarham.tahfizh.ictaq.R;

public class UstadzAdapter extends RecyclerView.Adapter<UstadzAdapter.ViewHolder> {

    private Context context;
    private List<User> list;
    SharedPreferenceManager sharePrefMan;

    public UstadzAdapter(Context context, List<User> list) {
        this.context = context;
        this.list = list;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(context).inflate(R.layout.single_ustadz, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        User user = list.get(position);

        holder.namaTxt.setText(user.getNama());

    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public TextView namaTxt;
        public ImageView itemImageView, reqJadwalImg;

        public ViewHolder(View itemView) {
            super(itemView);

            namaTxt = itemView.findViewById(R.id.namaTxt);
            itemImageView = itemView.findViewById(R.id.itemImageView);
            reqJadwalImg = itemView.findViewById(R.id.reqJadwalImg);

            reqJadwalImg.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    final User user = list.get(getAdapterPosition());
                    sharePrefMan = new SharedPreferenceManager(context);
//                    Toast.makeText(context, user.getKode(), Toast.LENGTH_SHORT).show();

                    final Calendar myCalendar = Calendar.getInstance();

                    String myFormat = "yyyy-MM-dd"; //In which you need put here
                    final SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);

                    final ProgressDialog progressDialog = new ProgressDialog(context);
                    progressDialog.setMessage(context.getString(R.string.loading));
                    progressDialog.show();

                    RequestQueue queue = Volley.newRequestQueue(context);

                    String url = context.getString(R.string.urlmain) +
                            "/service/my_service.php?password=7ba52b255b999d6f1a7fa433a9cf7df4&aksi=insert&tabel=permintaan";

                    StringRequest strRequest = new StringRequest(Request.Method.POST, url,
                            new Response.Listener<String>()
                            {
                                @Override
                                public void onResponse(String response)
                                {
                                    Log.e("PROFILE EDIT SIMPAN", response);
                                    progressDialog.dismiss();

//                        Intent ubah = new Intent(ProfileEdit.this, MainActivity.class);
//                        startActivity(ubah);
                                    try {
                                        JSONObject simpan = new JSONObject(response);
                                        if(simpan.getString("status").equals("sukses")){
                                            Toast.makeText(context, R.string.registerok, Toast.LENGTH_SHORT).show();
                                        }else{
                                            Toast.makeText(context, R.string.error, Toast.LENGTH_SHORT).show();
                                        }
                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                    }


                                }
                            }, new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            Log.e("Volley", error.toString());
                            Toast.makeText(context, context.getString(R.string.error), Toast.LENGTH_SHORT).show();
                            progressDialog.dismiss();
                        }
                    })
                    {
                        @Override
                        protected Map<String, String> getParams()
                        {

                            Map<String, String> params = new HashMap<String, String>();

                            params.put("field", "id_regis,id_penguji,tgl,status");

                            params.put("value", String.format("'%s','%s','%s','%s'",sharePrefMan.getSpKode(), user.getKode(), sdf.format(myCalendar.getTime()), "0"));
                            return params;
                        }
                    };

                    queue.add(strRequest);


//                    requestJadwal(sharePrefMan.getSpKode(),user.getKode(),sdf.format(myCalendar.getTime().toString()),"0");
                }
            });




        }
    }

    public void requestJadwal(final String id_regis, final String id_penguji, final String tanggal, final String status){


    }

}
