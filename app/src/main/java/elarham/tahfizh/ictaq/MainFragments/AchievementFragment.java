package elarham.tahfizh.ictaq.MainFragments;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import elarham.tahfizh.ictaq.AchievementFragments.AchievementAdapter;
import elarham.tahfizh.ictaq.Global.SharedPreferenceManager;
import elarham.tahfizh.ictaq.Models.Hafalan;
import elarham.tahfizh.ictaq.Models.Jadwal;
import elarham.tahfizh.ictaq.R;
import elarham.tahfizh.ictaq.ScheduleFragments.ReadyAdapter;

public class AchievementFragment extends Fragment {


    String hafizhMedal;
    ImageView medalImg;
    TextView medalTxt;
    SharedPreferenceManager sharePrefMan;

    RecyclerView mList;
    LinearLayoutManager linearLayoutManager;
    DividerItemDecoration dividerItemDecoration;
    List<Hafalan> hafalanList;
    RecyclerView.Adapter adapter;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_achievement, container, false);

        medalImg = view.findViewById(R.id.medalImg);
        medalTxt = view.findViewById(R.id.medalTxt);

        mList = view.findViewById(R.id.recycleList);

        hafalanList = new ArrayList<>();
        adapter = new AchievementAdapter(getContext(),hafalanList);

        linearLayoutManager = new LinearLayoutManager(getContext());
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        dividerItemDecoration = new DividerItemDecoration(mList.getContext(), linearLayoutManager.getOrientation());


        mList.setHasFixedSize(true);
        mList.setLayoutManager(linearLayoutManager);
        mList.addItemDecoration(dividerItemDecoration);
        mList.setAdapter(adapter);

        sharePrefMan = new SharedPreferenceManager(getContext());

        getMedal(sharePrefMan.getSpKode());
        getHafalan(sharePrefMan.getSpKode());



        return view;

    }

    private void getMedal(final String id_regis){
        final ProgressDialog progressDialog = new ProgressDialog(getContext());
        progressDialog.setMessage(getContext().getString(R.string.loading));
        progressDialog.show();

        RequestQueue queue = Volley.newRequestQueue(getContext());

        String url = getContext().getString(R.string.urlmain) +
                "/service/my_service.php?password=7ba52b255b999d6f1a7fa433a9cf7df4&aksi=medal";

        StringRequest strRequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>()
                {
                    @Override
                    public void onResponse(String response)
                    {
                        Log.e("MEDALI", response);
                        progressDialog.dismiss();

                        try {
                            medalTxt.setText("");
                            hafizhMedal = new JSONObject(response).getString("medal");
                            if(hafizhMedal.equals("GREEN")){
                                medalImg.setImageResource(R.mipmap.medal_green);
                            }else if(hafizhMedal.equals("SILVER")){
                                medalImg.setImageResource(R.mipmap.medal_silver);
                            }else if(hafizhMedal.equals("GOLD")){
                                medalImg.setImageResource(R.mipmap.medal_gold);
                            }else{
                                medalImg.setImageResource(R.mipmap.locked_logo);
                                medalTxt.setText(getContext().getString(R.string.notavailable));
                            }



                        } catch (JSONException e) {
                            e.printStackTrace();
                            medalImg.setImageResource(R.mipmap.locked_logo);
                            medalTxt.setText(getContext().getString(R.string.notavailable));
                            Toast.makeText(getContext(), getContext().getString(R.string.error), Toast.LENGTH_SHORT).show();
                        }


                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("Volley", error.toString());
                Toast.makeText(getContext(), getContext().getString(R.string.error), Toast.LENGTH_SHORT).show();
                progressDialog.dismiss();
            }
        })
        {
            @Override
            protected Map<String, String> getParams()
            {

                Map<String, String> params = new HashMap<String, String>();

                params.put("id_regis", id_regis);


                return params;
            }
        };

        queue.add(strRequest);
    }

    private void getHafalan(final String id_regis) {

        final ProgressDialog progressDialog = new ProgressDialog(getContext());
        progressDialog.setMessage(getContext().getString(R.string.loading));
        progressDialog.show();

        final RequestQueue requestQueue = Volley.newRequestQueue(getContext());


        String url = getContext().getString(R.string.urlmain) +
                "/service/my_service.php?password=7ba52b255b999d6f1a7fa433a9cf7df4&aksi=select&tabel=hafalan";

        StringRequest strRequest = new StringRequest(com.android.volley.Request.Method.POST, url,
                new Response.Listener<String>()
                {
                    @Override
                    public void onResponse(String response)
                    {

                        Log.e("JUZ HAFALAN", response);

                        try {


                            JSONObject jsonObj = new JSONObject(response).getJSONArray("data").getJSONObject(0);


                            for(int i=0; i<30; i++){
                                Hafalan hafalan = new Hafalan();
                                String currentJuz = String.valueOf(i+1);
                                String nilaiJuz = jsonObj.getString("juz_"+currentJuz);
                                hafalan.setJuzHafalan("Juz " + currentJuz);
                                if((nilaiJuz.equals("null") || nilaiJuz.equals(""))){
                                    hafalan.setNilaiHafalan("0");
                                }else {
                                    Log.e("tes", currentJuz);
                                    hafalan.setNilaiHafalan(nilaiJuz);
                                }
                                hafalanList.add(hafalan);

                            }






                        } catch (JSONException e) {
                            e.printStackTrace();
                            Toast.makeText(getContext(), getContext().getString(R.string.wrongdataformat), Toast.LENGTH_SHORT).show();
                        }



                        adapter.notifyDataSetChanged();
                        progressDialog.dismiss();

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("Volley", error.toString());
                progressDialog.dismiss();
                Toast.makeText(getContext(), R.string.error, Toast.LENGTH_SHORT).show();
            }
        })
        {
            @Override
            protected Map<String, String> getParams()
            {

                Map<String, String> params = new HashMap<String, String>();
                params.put("where", String.format("where id_regis='%s' ",id_regis));
                return params;
            }
        };

        requestQueue.add(strRequest);


    }
}
