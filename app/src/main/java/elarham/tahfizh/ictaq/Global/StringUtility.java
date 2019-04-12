package elarham.tahfizh.ictaq.Global;

import android.content.Context;
import android.widget.Toast;

import org.ocpsoft.prettytime.PrettyTime;

import java.text.DateFormat;
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

            DateFormat formatter;
            Date date;
            formatter = new SimpleDateFormat("yyyy-MM-dd", Locale.US);
            date = (Date)formatter.parse(dateStr);

            PrettyTime pTime = new PrettyTime(Locale.forLanguageTag("ID"));

            dateFormatted =  pTime.format((date));

        }
        catch (Exception e)
        {
            Toast.makeText(context, context.getString(R.string.wrongdataformat), Toast.LENGTH_SHORT).show();
        }
        return dateFormatted;

    }
}
