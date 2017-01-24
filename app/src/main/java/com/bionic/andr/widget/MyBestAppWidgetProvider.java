package com.bionic.andr.widget;

import com.bionic.andr.R;

import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.widget.RemoteViews;

import static android.widget.RemoteViews.*;

/**  */
public class MyBestAppWidgetProvider extends AppWidgetProvider {

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        int id = appWidgetIds[0];
        RemoteViews v = new RemoteViews(context.getPackageName(),
                R.layout.app_widget);
        v.setTextViewText(R.id.login_email, "bob@example.com");
        v.setTextViewText(R.id.login_pass, "bobtothepresident");
        appWidgetManager.updateAppWidget(id, v);
    }

}
