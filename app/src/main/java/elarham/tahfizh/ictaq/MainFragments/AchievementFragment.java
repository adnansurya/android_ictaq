package elarham.tahfizh.ictaq.MainFragments;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
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

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import elarham.tahfizh.ictaq.Global.SharedPreferenceManager;
import elarham.tahfizh.ictaq.R;

public class AchievementFragment extends Fragment {


    String hafizhMedal;
    ImageView medalImg;
    TextView medalTxt;
    SharedPreferenceManager sharePrefMan;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_achievement, container, false);

        medalImg = view.findViewById(R.id.medalImg);
        medalTxt = view.findViewById(R.id.medalTxt);

        sharePrefMan = new SharedPreferenceManager(getContext());

        getMedal(sharePrefMan.getSpKode());



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
                        Log.e("PENGUJI", response);
                        progressDialog.dismiss();

                        try {
                            medalTxt.setText("");
                            hafizhMedal = new JSONObject(response).getString("medal");
                            if(hafizhMedal.equals("hijau")){
                                medalImg.setImageResource(R.mipmap.medal_green);
                            }else if(hafizhMedal.equals("silver")){
                                medalImg.setImageResource(R.mipmap.medal_silver);
                            }else if(hafizhMedal.equals("gold")){
                                medalImg.setImageResource(R.mipmap.medal_gold);
                            }else{
                                medalImg.setImageResource(R.mipmap.locked_logo);
                                medalTxt.setText(getContext().getString(R.string.notavailable));
                            }



                        } catch (JSONException e) {
                            e.printStackTrace();
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
}
