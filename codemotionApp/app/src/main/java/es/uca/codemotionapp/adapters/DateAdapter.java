package es.uca.codemotionapp.adapters;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.BitmapFactory;
import android.media.RingtoneManager;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.core.app.NotificationCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.snackbar.Snackbar;

import java.util.ArrayList;

import es.uca.codemotionapp.items.Date;
import es.uca.codemotionapp.R;
import es.uca.codemotionapp.activities.LocationActivity;

public class DateAdapter extends RecyclerView.Adapter<DateAdapter.MyViewHolder>{
    private ArrayList<Date> _dates;
    private Context context;
    private NotificationManager _myNotificationManager; //Needed to allow push notifications.
    private static final int NOTIF_ALERTA_ID = 1;

    public static class MyViewHolder extends RecyclerView.ViewHolder{
        TextView date;
        Button dateButton;

        public MyViewHolder(View v) {
            super(v);
            date = (TextView) v.findViewById(R.id.date);
            dateButton = (Button) v.findViewById(R.id.dateButton);
        }
    }

    public DateAdapter(ArrayList<Date> myDataset, NotificationManager myNotificationManager){
        _myNotificationManager = myNotificationManager;
        _dates = myDataset;
        _dates.add(new Date("20/5/2020"));
        _dates.add(new Date("21/7/2020"));
        _dates.add(new Date("25/7/2020"));

    }

    @Override //Inflate layout date.xml. Also asing value to the variable context.
    public DateAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType){

        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.date_item, parent, false);

        MyViewHolder vh = new MyViewHolder(v);
        context = parent.getContext();
        return vh;
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, final int position){
        holder.date.setText(_dates.get(position).getDate());
        holder.dateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(_dates.get(position).getDate().equals("25/7/2020")){ //The date picked is the event date.
                    NotificationCompat.Builder notification =
                            new NotificationCompat.Builder(context, "default")
                                    .setSmallIcon(R.drawable.calendar)
                                    .setLargeIcon(BitmapFactory.decodeResource
                                            (Resources.getSystem(), R.drawable.calendar))
                                    .setContentTitle("Inicio del evento")
                                    .setContentText("Pulse esta notificación para abrir la ubicación")
                                    .setTicker("Alerta");
                    notification.setVibrate(new long[]{1000, 1000, 1000, 1000, 1000});
                    notification.setOnlyAlertOnce(true);
                    notification.setSound(RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION));
                    notification.setAutoCancel(true);

                    //Open the activity program if the user press the notification.
                    Intent notIntent = new Intent(context, LocationActivity.class); //We need the actual context.
                    PendingIntent contInten = PendingIntent.getActivity(context, 0, notIntent, 0);
                    notification.setContentIntent(contInten); //Add the intent to the notification

                    NotificationManager mNotificationManager = _myNotificationManager;

                    // Since android Oreo notification channel is needed.
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                        NotificationChannel channel = new NotificationChannel("default",
                                "Default channel", NotificationManager.IMPORTANCE_DEFAULT);
                        mNotificationManager.createNotificationChannel(channel);
                    }
                    mNotificationManager.notify(NOTIF_ALERTA_ID, notification.build());
                }else{
                    Date datePicked = new Date(_dates.get(position).getDate()),
                         dateActual = new Date();

                    switch (dateActual.compare(datePicked)){
                        case 1: //The date picked belongs to the past :). We must send a Toast
                            Toast myToast = Toast.makeText(context, "Este evento ya ha ocurrido", Toast.LENGTH_SHORT);
                            myToast.show();
                            break;

                        default: //The date picked belongs to the future or it's today.
                            //calculate the difference between the future date and now.
                            int result = (int) ((datePicked.getTimeInMillis() - dateActual.getTimeInMillis()) / (24 * 60 * 60 * 1000));
                            String text;
                            //The message depends of the number of days to the date.
                            switch (result) {
                                case 0:
                                    text = "El evento es hoy, corre!";
                                    break;
                                case 1:
                                    text = "Falta sólo 1 día";
                                    break;
                                default:
                                    text = "Faltan " + result + " dias ";
                            }
                            Snackbar.make(view, text, Snackbar.LENGTH_LONG).show();
                            break;
                    }
                }
            }
        });
    }

    @Override
    public int getItemCount(){
        return _dates.size();
    }
}