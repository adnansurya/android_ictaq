package elarham.tahfizh.ictaq.ScheduleFragments;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

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
        holder.judulTxt.setText(request.getTanggal());
        holder.detailImg.setVisibility(View.GONE);
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public TextView judulTxt;
        ImageView detailImg, personImg;

        public ViewHolder(View itemView) {
            super(itemView);


            judulTxt = itemView.findViewById(R.id.judulTxt);
            detailImg = itemView.findViewById(R.id.detailImg);
            personImg = itemView.findViewById(R.id.personImg);

            personImg.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    int position = getAdapterPosition();

                    Request request = list.get(position);


                    sharePrefMan = new SharedPreferenceManager(context);
                    if(sharePrefMan.getSpType().equals("2")){
                        Toast.makeText(context, context.getString(R.string.memorizer) + " " + request.getIdRegis(), Toast.LENGTH_SHORT).show();
                    }else if(sharePrefMan.getSpType().equals("3")){
                        Toast.makeText(context, context.getString(R.string.examiner) + " " + request.getIdPenguji(), Toast.LENGTH_SHORT).show();
                    }



                }
            });
        }
    }

}
