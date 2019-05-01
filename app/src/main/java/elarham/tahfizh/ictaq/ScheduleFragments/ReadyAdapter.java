package elarham.tahfizh.ictaq.ScheduleFragments;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Typeface;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import elarham.tahfizh.ictaq.DetailRequest;
import elarham.tahfizh.ictaq.Global.SharedPreferenceManager;
import elarham.tahfizh.ictaq.Global.StringUtility;
import elarham.tahfizh.ictaq.Models.Jadwal;
import elarham.tahfizh.ictaq.Models.Request;
import elarham.tahfizh.ictaq.R;
import elarham.tahfizh.ictaq.VideoCall;

public class ReadyAdapter extends RecyclerView.Adapter<ReadyAdapter.ViewHolder> {

    private Context context;
    private List<Jadwal> list;
    SharedPreferenceManager sharePrefMan;
    String url;


    public ReadyAdapter(Context context, List<Jadwal> list) {
        this.context = context;
        this.list = list;
    }

    @Override
    public ReadyAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(context).inflate(R.layout.single_jadwal, parent, false);
        return new ReadyAdapter.ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(ReadyAdapter.ViewHolder holder, int position) {

        sharePrefMan = new SharedPreferenceManager(context);
        Jadwal ready = list.get(position);
        holder.tanggalTxt.setText(new StringUtility().relativeTime(ready.getTanggal(), context));
        holder.jamTxt.setText(ready.getJam());
        if(ready.getNilai().equals("null")){
            holder.nilaiTxt.setText(context.getString(R.string.notavailable));
        }else{
            holder.nilaiTxt.setTypeface(Typeface.DEFAULT_BOLD);
            holder.nilaiTxt.setTextSize(TypedValue.COMPLEX_UNIT_PX,context.getResources().getDimension(R.dimen.itemMediumTxt));
            holder.nilaiTxt.setText(context.getString(R.string.score)+ " : " + ready.getNilai());
        }


        if(sharePrefMan.getSpType().equals("3")){
            holder.editImg.setVisibility(View.INVISIBLE);
        }else{
            holder.editImg.setVisibility(View.INVISIBLE);
        }


    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public TextView tanggalTxt, jamTxt, nilaiTxt;
        ImageView personImg, editImg, nextImg;
        RelativeLayout bodyLayout;

        public ViewHolder(View itemView) {
            super(itemView);

            bodyLayout = itemView.findViewById(R.id.bodyLayout);
            tanggalTxt = itemView.findViewById(R.id.tanggalTxt);
            jamTxt = itemView.findViewById(R.id.jamTxt);
            nilaiTxt = itemView.findViewById(R.id.nilaiTxt);
            personImg = itemView.findViewById(R.id.personImg);
            editImg = itemView.findViewById(R.id.editImg);
            nextImg = itemView.findViewById(R.id.nextImg);

            sharePrefMan = new SharedPreferenceManager(context);
            if(sharePrefMan.getSpType().equals("3")) {
                nextImg.setVisibility(View.INVISIBLE);
                RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams)personImg.getLayoutParams();
                params.addRule(RelativeLayout.ALIGN_PARENT_END);
                personImg.setLayoutParams(params);
            }

            personImg.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    int position = getAdapterPosition();

                    Jadwal ready = list.get(position);
                    url = context.getString(R.string.urlmain) +
                            "/service/my_service.php?password=7ba52b255b999d6f1a7fa433a9cf7df4&aksi=select&tabel=permintaan";

                    getReqData(ready.getIdReq());



                }
            });

            bodyLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int position = getAdapterPosition();
                    Jadwal ready = list.get(position);

                    AlertDialog.Builder builder = new AlertDialog.Builder(context);

                    builder.setTitle(context.getString(R.string.notes));
                    builder.setMessage(ready.getCatatan());

                    builder.setPositiveButton(R.string.close, new DialogInterface.OnClickListener() {

                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    });

                    AlertDialog alert = builder.create();
                    alert.show();
                }
            });



            nextImg.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int position = getAdapterPosition();
                    Jadwal ready = list.get(position);

                    url = context.getString(R.string.urlmain) +
                            "/service/my_service.php?password=7ba52b255b999d6f1a7fa433a9cf7df4&aksi=select&tabel=room";

                   getRoomById(ready.getIdReq(), ready.getId(), ready.getJam(), ready.getTanggal(), ready.getNilai(), ready.getCatatan());


                }
            });
        }
    }

    public void getRoomById(final String idReq, final String idJadwal, final String jam, final String tanggal, final String nilai, final String catatan){
        final ProgressDialog progressDialog = new ProgressDialog(context);
        progressDialog.setMessage(context.getString(R.string.loading));
        progressDialog.show();

        final RequestQueue requestQueue = Volley.newRequestQueue(context);

        StringRequest strRequest = new StringRequest(com.android.volley.Request.Method.POST, url,
                new Response.Listener<String>()
                {
                    @Override
                    public void onResponse(String response)
                    {
                        Log.e("URL ROOM ID", url);
                        Log.e("ROOM ID", response);

                        try {

                            JSONObject room = new JSONObject(response).getJSONArray("data").getJSONObject(0);
                            Intent vidCall = new Intent(context, VideoCall.class);
                            vidCall.putExtra("idJadwal", idJadwal);
                            vidCall.putExtra("idRoom", room.getString("id_room"));
                            vidCall.putExtra("idReq", idReq);
                            vidCall.putExtra("jam",jam);
                            vidCall.putExtra("tanggal", tanggal);
                            vidCall.putExtra("nilai",nilai);
                            vidCall.putExtra("catatan",catatan);
                            context.startActivity(vidCall);


                        } catch (JSONException e) {
                            e.printStackTrace();
                            Toast.makeText(context, R.string.wrongdataformat, Toast.LENGTH_SHORT).show();
                        }

                        progressDialog.dismiss();

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("Volley", error.toString());
                progressDialog.dismiss();
                Toast.makeText(context, R.string.error, Toast.LENGTH_SHORT).show();
            }
        })
        {
            @Override
            protected Map<String, String> getParams()
            {

                Map<String, String> params = new HashMap<String, String>();

                params.put("where", String.format("where id_permintaan='%s'",idReq));

                return params;
            }
        };

        requestQueue.add(strRequest);


    }

    private void getReqData(final String idReq) {

        final ProgressDialog progressDialog = new ProgressDialog(context);
        progressDialog.setMessage(context.getString(R.string.loading));
        progressDialog.show();

        final RequestQueue requestQueue = Volley.newRequestQueue(context);

        StringRequest strRequest = new StringRequest(com.android.volley.Request.Method.POST, url,
                new Response.Listener<String>()
                {
                    @Override
                    public void onResponse(String response)
                    {
                        Log.e("URL REQUEST DETAIL", url);
                        Log.e("REQUEST DETAIL", response);

                        try {

                            JSONObject request = new JSONObject(response).getJSONArray("data").getJSONObject(0);
                            Intent detailReq = new Intent(context, DetailRequest.class);
                            detailReq.putExtra("id", request.getString("id"));
                            detailReq.putExtra("idRegis", request.getString("id_regis"));
                            detailReq.putExtra("idPenguji", request.getString("id_penguji"));
                            detailReq.putExtra("tanggal", request.getString("tgl"));
                            detailReq.putExtra("status",request.getString("status"));
                            context.startActivity(detailReq);



                        } catch (JSONException e) {
                            e.printStackTrace();
                            Toast.makeText(context, R.string.wrongdataformat, Toast.LENGTH_SHORT).show();
                        }

                        progressDialog.dismiss();

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("Volley", error.toString());
                progressDialog.dismiss();
                Toast.makeText(context, R.string.error, Toast.LENGTH_SHORT).show();
            }
        })
        {
            @Override
            protected Map<String, String> getParams()
            {

                Map<String, String> params = new HashMap<String, String>();

                params.put("where", String.format("where id='%s'",idReq));

                return params;
            }
        };

        requestQueue.add(strRequest);


    }

}
