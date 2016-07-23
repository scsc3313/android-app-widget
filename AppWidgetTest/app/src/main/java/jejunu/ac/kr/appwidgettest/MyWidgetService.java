package jejunu.ac.kr.appwidgettest;

import android.appwidget.AppWidgetManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.RemoteViews;
import android.widget.RemoteViewsService;

import java.util.ArrayList;
import java.util.List;

public class MyWidgetService extends RemoteViewsService {
    private static final String TAG = "MyWidgetService";

    @Override
    public RemoteViewsFactory onGetViewFactory(Intent intent) {
        Log.d(TAG, "onGetViewFactory()");
        return new MyRemoteViewsFactory(getApplicationContext(), intent);
    }
}


class MyRemoteViewsFactory implements RemoteViewsService.RemoteViewsFactory {

    private static final String TAG = "MyRemoteeViewsFactory";
    private static final int LIST_COUNT = 10;
    private List<WidgetItem> mWidgetItems = new ArrayList<WidgetItem>();
    private Context mContext;
    private int mAppWidgetId;

    public MyRemoteViewsFactory(Context context, Intent intent) {
        mContext = context;
        mAppWidgetId = intent.getIntExtra(AppWidgetManager.EXTRA_APPWIDGET_ID,
                AppWidgetManager.INVALID_APPWIDGET_ID);
    }

    @Override
    public void onCreate() {
        Log.d(TAG, "onCreate()");
        for (int i = 0; i < LIST_COUNT; i++) {
            mWidgetItems.add(new WidgetItem(i + "!"));
        }
    }

    @Override
    public void onDataSetChanged() {
    }

    @Override
    public void onDestroy() {
        Log.d(TAG, "onCreate()");
        mWidgetItems.clear();
    }

    @Override
    public int getCount() {
        return LIST_COUNT;
    }



    @Override
    public RemoteViews getViewAt(int position) {

        RemoteViews rv = new RemoteViews(mContext.getPackageName(), R.layout.new_app_widget_item);
        rv.setTextViewText(R.id.widget_item_textview, mWidgetItems.get(position).text);

        Bundle extras = new Bundle();
        extras.putInt(NewAppWidgetProvider.EXTRA_ITEM, position);
        Intent fillIntent = new Intent();
        fillIntent.putExtras(extras);
        rv.setOnClickFillInIntent(R.id.widget_item_textview, fillIntent);

        Log.d(TAG, "getViewAt position : " + position);

        return rv;
    }

    @Override
    public RemoteViews getLoadingView() {
        return null;
    }

    @Override
    public int getViewTypeCount() {
        return 1;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public boolean hasStableIds() {
        return true;
    }

    private class WidgetItem {

        public String text;

        public WidgetItem(String text) {
            this.text = text;
        }
    }
}

