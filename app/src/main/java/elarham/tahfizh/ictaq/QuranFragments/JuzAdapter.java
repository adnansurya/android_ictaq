package elarham.tahfizh.ictaq.QuranFragments;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import elarham.tahfizh.ictaq.Models.Juz;
import elarham.tahfizh.ictaq.Quran;
import elarham.tahfizh.ictaq.R;

public class JuzAdapter extends RecyclerView.Adapter<JuzAdapter.ViewHolder> {

    private Context context;
    private List<Juz> list;

    public JuzAdapter(Context context, List<Juz> list) {
            this.context = context;
            this.list = list;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View v = LayoutInflater.from(context).inflate(R.layout.single_juz, parent, false);
            return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Juz juz = list.get(position);
        holder.nomorTxt.setText("Juz "+ juz.getNomor());
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public TextView nomorTxt;

        public ViewHolder(View itemView) {
            super(itemView);


            nomorTxt = itemView.findViewById(R.id.nomorTxt);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    int position = getAdapterPosition();

                    Juz juz = list.get(position);

                    Intent quran = new Intent(context, Quran.class);
                    quran.putExtra("nomor", juz.getNomor());
                    quran.putExtra("mode", "juz");
                    context.startActivity(quran);
                }
            });
        }
    }

}