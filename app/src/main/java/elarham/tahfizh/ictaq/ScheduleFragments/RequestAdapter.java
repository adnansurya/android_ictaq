package elarham.tahfizh.ictaq.ScheduleFragments;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

import elarham.tahfizh.ictaq.DetailRequest;
import elarham.tahfizh.ictaq.Global.SharedPreferenceManager;
import elarham.tahfizh.ictaq.Models.Request;
import elarham.tahfizh.ictaq.R;

public class RequestAdapter extends RecyclerView.Adapter<RequestAdapter.ViewHolder> {

    private Context context;
    private List<Request> list;
    SharedPreferenceManager sharePrefMan;


    public RequestAdapter(Context context, List<Request> list) {
        this.context = context;
        this.list = list;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(context).inflate(R.layout.single_request, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Request request = list.get(position);
        sharePrefMan = new SharedPreferenceManager(context);
        holder.judulTxt.setText(context.getString(R.string.sent) + " : " +request.getTanggal());
        if(sharePrefMan.getSpType().equals("3")){
            holder.detailImg.setImageDrawable(context.getDrawable(R.drawable.ic_person_pin));
        }

    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public TextView judulTxt;
        ImageView detailImg;

        public ViewHolder(View itemView) {
            super(itemView);


            judulTxt = itemView.findViewById(R.id.judulTxt);
            detailImg = itemView.findViewById(R.id.detailImg);


            detailImg.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    int position = getAdapterPosition();

                    Request request = list.get(position);
                    Log.e("UPDATE PERMINTAAN ID", request.getId());
                    Intent open = new Intent(context, DetailRequest.class);
                    open.putExtra("id",request.getId());
                    open.putExtra("idRegis", request.getIdRegis());
                    open.putExtra("idPenguji", request.getIdPenguji());
                    open.putExtra("tanggal", request.getTanggal());
                    open.putExtra("status", request.getStatus());
                    context.startActivity(open);


                }
            });
        }
    }

}
