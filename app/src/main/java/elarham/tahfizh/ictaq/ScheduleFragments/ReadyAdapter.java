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
import elarham.tahfizh.ictaq.Models.Jadwal;
import elarham.tahfizh.ictaq.R;

public class ReadyAdapter extends RecyclerView.Adapter<ReadyAdapter.ViewHolder> {

    private Context context;
    private List<Jadwal> list;
    SharedPreferenceManager sharePrefMan;

    public ReadyAdapter(Context context, List<Jadwal> list) {
        this.context = context;
        this.list = list;
    }

    @Override
    public ReadyAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(context).inflate(R.layout.single_request, parent, false);
        return new ReadyAdapter.ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(ReadyAdapter.ViewHolder holder, int position) {
        Jadwal ready = list.get(position);
        holder.judulTxt.setText(ready.getTanggal());
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

                    Jadwal ready = list.get(position);
                    Toast.makeText(context, ready.getIdReq(), Toast.LENGTH_SHORT).show();

//                    sharePrefMan = new SharedPreferenceManager(context);
//                    if(sharePrefMan.getSpType().equals("2")){
//                        Toast.makeText(context, context.getString(R.string.memorizer) + " " + ready.getTanggal(), Toast.LENGTH_SHORT).show();
//                    }else if(sharePrefMan.getSpType().equals("3")){
//                        Toast.makeText(context, context.getString(R.string.examiner) + " " + ready.getJ, Toast.LENGTH_SHORT).show();
//                    }



                }
            });
        }
    }

}
