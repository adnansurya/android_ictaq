package elarham.tahfizh.ictaq.Global;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
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

    public String relativeDate(String dateStr, Context context){
        String dateFormatted= "";
        try
        {

            DateFormat formatter = new SimpleDateFormat("yyyy-MM-dd", Locale.US);
            Date date = formatter.parse(dateStr);
            PrettyTime pTime = new PrettyTime(Locale.getDefault());

            dateFormatted =  pTime.format((date));
            if(dateFormatted.contains(context.getString(R.string.hour).toLowerCase()) ||
                    dateFormatted.contains(context.getString(R.string.minute)) || dateFormatted.contains(context.getString(R.string.second))){
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
            return dateStr;

        }


        return dateFormatted;


    }

    public void simpleDialog(String title, String message, Context context){
        AlertDialog.Builder builder = new AlertDialog.Builder(context);

        builder.setTitle(title);
        builder.setMessage(message);

        builder.setPositiveButton(context.getString(R.string.close), new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });

        AlertDialog alert = builder.create();
        alert.show();
    }

    public String exactDate(String dateStr, Context context){
        String dateFormatted= "";

        try {
            DateFormat formatter = new SimpleDateFormat("yyyy-MM-dd", Locale.US);
            Date date = formatter.parse(dateStr);
            SimpleDateFormat newFormat = new  SimpleDateFormat("EEEE, dd MMMM yyyy", Locale.getDefault());
            dateFormatted = newFormat.format(date);
        } catch (ParseException e) {
            e.printStackTrace();
            Toast.makeText(context, context.getString(R.string.wrongdataformat), Toast.LENGTH_SHORT).show();
            return dateStr;
        }
        return dateFormatted;
    }

    public String relativeDateTime(String dateTimeStr, Context context){
        String dateFormatted= "";
        try
        {

            SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd h:mm a", Locale.US);
            Date date = formatter.parse(dateTimeStr);
            PrettyTime pTime = new PrettyTime(Locale.getDefault());

            dateFormatted =  pTime.format((date));
            if(dateFormatted.contains(context.getString(R.string.hour).toLowerCase()) ||
                    dateFormatted.contains(context.getString(R.string.minute)) || dateFormatted.contains(context.getString(R.string.second))){
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
            return dateTimeStr;

        }


        return dateFormatted;


    }
}
