package es.uca.codemotionapp;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.content.Intent;
import android.widget.RemoteViews;

import es.uca.codemotionapp.activities.ProgramActivity;

/**
 * Implementation of App Widget functionality.
 */
public class CodemotionWidget extends AppWidgetProvider {

    static void updateAppWidget(Context context, AppWidgetManager appWidgetManager,
                                int appWidgetId) {

        //CharSequence widgetText = context.getString(R.string.appwidget_text);
        // Construct the RemoteViews object
        RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.codemotion_widget);

        //views.setImageViewIcon(R.id.imageView, R.drawable.logotipo );
        //views.setTextViewText(R.id.appwidget_text, widgetText);

        // Instruct the widget manager to update the widget
        appWidgetManager.updateAppWidget(appWidgetId, views);
    }

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        // There may be multiple widgets active, so update all of them
        for (int appWidgetId : appWidgetIds) {
            //updateAppWidget(context, appWidgetManager, appWidgetId); //maybe

            Intent intent = new Intent(context, ProgramActivity.class);
            PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, intent, 0);

            RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.codemotion_widget);

            //Here must throw programActivity when click on the widget.
            views.setOnClickPendingIntent(R.id.widget, pendingIntent);
            appWidgetManager.updateAppWidget(appWidgetId, views);
        }
    }

}

