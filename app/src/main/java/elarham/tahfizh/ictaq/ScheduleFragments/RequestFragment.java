package elarham.tahfizh.ictaq.ScheduleFragments;

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
import android.widget.Toast;

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

import elarham.tahfizh.ictaq.Global.SharedPreferenceManager;
import elarham.tahfizh.ictaq.Models.Request;
import elarham.tahfizh.ictaq.Models.User;
import elarham.tahfizh.ictaq.R;

public class RequestFragment extends Fragment {
    RecyclerView mList;
    LinearLayoutManager linearLayoutManager;
    DividerItemDecoration dividerItemDecoration;
    List<Request> reqList;
    RecyclerView.Adapter adapter;
    SharedPreferenceManager sharePrefMan;



    String url;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_list_only, container, false);

        sharePrefMan = new SharedPreferenceManager(getContext());

        mList = view.findViewById(R.id.recycleList);

        reqList = new ArrayList<>();
        adapter = new RequestAdapter(getContext(),reqList);

        linearLayoutManager = new LinearLayoutManager(getContext());
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        dividerItemDecoration = new DividerItemDecoration(mList.getContext(), linearLayoutManager.getOrientation());

        mList.setHasFixedSize(true);
        mList.setLayoutManager(linearLayoutManager);
        mList.addItemDecoration(dividerItemDecoration);
        mList.setAdapter(adapter);

        url = getContext().getString(R.string.urlmain) +
                "/service/my_service.php?password=7ba52b255b999d6f1a7fa433a9cf7df4&aksi=select&tabel=permintaan";


        getData();



        return view;
    }

    private void getData() {

        final ProgressDialog progressDialog = new ProgressDialog(getContext());
        progressDialog.setMessage(getContext().getString(R.string.loading));
        progressDialog.show();

        final RequestQueue requestQueue = Volley.newRequestQueue(getContext());

        StringRequest strRequest = new StringRequest(com.android.volley.Request.Method.POST, url,
                new Response.Listener<String>()
                {
                    @Override
                    public void onResponse(String response)
                    {
                        Log.e("URL REQUEST", url);
                        Log.e("REQUEST", response);

                        try {

                            JSONArray request = new JSONObject(response).getJSONArray("data");
                            if(request.length()==0){
                                Toast.makeText(getContext(), getContext().getString(R.string.data) + " " + getContext().getString(R.string.notavailable), Toast.LENGTH_SHORT).show();
                            }else{
                                for (int i=0; i <request.length(); i++){

                                    JSONObject jsonObj = request.getJSONObject(i);


                                    String id,status;
                                    id = jsonObj.getString("id");
                                    status = jsonObj.getString("status");



                                    Request req = new Request();
                                    req.setId(id);
                                    req.setIdRegis(jsonObj.getString("id_regis"));
                                    req.setIdPenguji(jsonObj.getString("id_penguji"));
                                    req.setTanggal(jsonObj.getString("tgl"));
                                    req.setStatus(status);
                                    reqList.add(req);



                                }
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
                if(sharePrefMan.getSpType().equals("3")){
                    params.put("where", String.format("where id_regis='%s' AND status='0' order by id desc",sharePrefMan.getSpKode() ));
                }else if(sharePrefMan.getSpType().equals("2")){
                    params.put("where", String.format("where id_penguji='%s' AND status='0' order by id desc",sharePrefMan.getSpKode() ));
                }
                return params;
            }
        };

        requestQueue.add(strRequest);


    }
}
