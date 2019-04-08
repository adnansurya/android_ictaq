package elarham.tahfizh.ictaq.UstadzFragment;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

import elarham.tahfizh.ictaq.Models.User;
import elarham.tahfizh.ictaq.R;

public class UstadzAdapter extends RecyclerView.Adapter<UstadzAdapter.ViewHolder> {

    private Context context;
    private List<User> list;

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
        public ImageView itemImageView;

        public ViewHolder(View itemView) {
            super(itemView);

            namaTxt = itemView.findViewById(R.id.namaTxt);
            itemImageView = itemView.findViewById(R.id.itemImageView);



//            itemView.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//
//                    int position = getAdapterPosition();
//
//                    Surah surah = list.get(position);
//
//                    Intent quran = new Intent(context, Quran.class);
//                    quran.putExtra("nama", surah.getNama());
//                    quran.putExtra("nomor", surah.getNomor());
//                    quran.putExtra("asma", surah.getAsma());
//                    quran.putExtra("arti", surah.getArti());
//                    quran.putExtra("ayat", surah.getAyat());
//                    quran.putExtra("type", surah.getType());
//                    quran.putExtra("keterangan", surah.getKeterangan());
//                    quran.putExtra("mode", "surah");
//                    context.startActivity(quran);
//                }
//            });
        }
    }

}
