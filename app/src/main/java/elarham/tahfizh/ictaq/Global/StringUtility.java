package elarham.tahfizh.ictaq.Global;

import android.content.Context;
import android.widget.Toast;

import org.ocpsoft.prettytime.PrettyTime;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.UUID;

import elarham.tahfizh.ictaq.R;

public class StringUtility {


    public String randomRoomID() {
        String uuid = UUID.randomUUID().toString();
        String roomId = uuid.replaceAll("-","").substring(0,10);
        return "ictaqroom_"+roomId;
    }

    public String relativeTime(String dateStr, Context context){
        String dateFormatted= "";
        try
        {

            DateFormat formatter = new SimpleDateFormat("yyyy-MM-dd", Locale.US);
            Date date = formatter.parse(dateStr);
            PrettyTime pTime = new PrettyTime(Locale.forLanguageTag("ID"));

            dateFormatted =  pTime.format((date));
            if(dateFormatted.contains("jam") || dateFormatted.contains("menit") || dateFormatted.contains("detik")){
                dateFormatted = context.getString(R.string.today);
//                if(dateFormatted.contains("lalu")){
//                    dateFormatted =  context.getString(R.string.recently);
//                }else{
//                    dateFormatted =  context.getString(R.string.soon);
//                }

            }

        }
        catch (Exception e)
        {
            e.printStackTrace();
            Toast.makeText(context, context.getString(R.string.wrongdataformat), Toast.LENGTH_SHORT).show();
        }

        return dateFormatted;


    }

    public String exactTime(String dateStr, Context context){
        String dateFormatted= "";

        try {
            DateFormat formatter = new SimpleDateFormat("yyyy-MM-dd", Locale.US);
            Date date = formatter.parse(dateStr);
            SimpleDateFormat newFormat = new  SimpleDateFormat("EEEE, dd MMMM yyyy", Locale.forLanguageTag("ID"));
            dateFormatted = newFormat.format(date);
        } catch (ParseException e) {
            e.printStackTrace();
            Toast.makeText(context, context.getString(R.string.wrongdataformat), Toast.LENGTH_SHORT).show();
        }
        return dateFormatted;
    }
}
