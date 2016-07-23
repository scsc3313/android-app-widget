package jejunu.ac.kr.appwidgettest;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.util.Log;
import android.widget.RemoteViews;
import android.widget.Toast;

/**
 * Implementation of App Widget functionality.
 */
public class NewAppWidgetProvider extends AppWidgetProvider {

    private static final String TAG = NewAppWidgetProvider.class.getSimpleName();
    public static final String TOAST_ACTION = "com.example.android.stackwidget.TOAST_ACTION";
    public static final String EXTRA_ITEM = "com.example.android.stackwidget.EXTRA_ITEM";

    @Override
    public void onReceive(Context context, Intent intent) {
        Log.d(TAG, "onReceive()");
        AppWidgetManager mgr = AppWidgetManager.getInstance(context);
        String action = intent.getAction();
        Log.d(TAG, "action : " + action);
        if (action.equals(TOAST_ACTION)) {
            //받은 액션이 TOAST_ACTION일 경우 Toast.show() 실행
            int appWidgetId = intent.getIntExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, AppWidgetManager.INVALID_APPWIDGET_ID);
            int viewIndex = intent.getIntExtra(EXTRA_ITEM, 0);
            Toast.makeText(context, "Touch View : " + viewIndex, Toast.LENGTH_SHORT).show();
        }
        super.onReceive(context, intent);
    }

    static void updateAppWidget(Context context, AppWidgetManager appWidgetManager,
                                int appWidgetId) {

        // 서비스를 위해 intent를 생성
        Intent intent = new Intent(context, MyWidgetService.class);
        intent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, appWidgetId);
        intent.setData(Uri.parse(intent.toUri(Intent.URI_INTENT_SCHEME)));

        // RemoteViews describes a view hierarchy that can be displayed in
        // another process. The hierarchy is inflated from a layout source file,
        // and this RemoteViews provides more basic operations for modifying the content of the inflated hierarchy.
        RemoteViews rv = new RemoteViews(context.getPackageName(), R.layout.new_app_widget);

        // call setRemoteViewsAdapter(Intent)
        // setRemoteViewsAdatper is set up to use a remote views adapter
        // which connects to a RemoteViewsService through the sepcified intent.
        rv.setRemoteAdapter(R.id.stack_view, intent);

        // call new SetEmptyView(int viewId, int emptyViewId)
        rv.setEmptyView(R.id.stack_view, R.id.empty_view);

        // Toast를 위해 자기 자신에게 intent을 전송
        Intent toastIntent = new Intent(context, NewAppWidgetProvider.class);
        toastIntent.setAction(TOAST_ACTION);
        toastIntent.putExtra(AppWidgetManager.EXTRA_HOST_ID, appWidgetId);

        // AppWidget Provider는 브로드캐스트 리시버를 기반으로 하기 때문에 PendingIntent.getBroadcaset()를 이용함
        // FLAG_UPDATE_CURRENT는 PendingIntent이 존재하면 FLAG만 업데이트 하는 것을 의미함.
        PendingIntent toastPendingIntent = PendingIntent.getBroadcast(context, 0, toastIntent, PendingIntent.FLAG_UPDATE_CURRENT);
        rv.setPendingIntentTemplate(R.id.stack_view, toastPendingIntent);

//        CharSequence widgetText = context.getString(R.string.appwidget_text);
//        // Construct the RemoteViews object
//        RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.new_app_widget);
//        views.setTextViewText(R.id.widget_item_textview, widgetText);

        // Instruct the widget manager to update the widget
        appWidgetManager.updateAppWidget(appWidgetId, rv);
        Log.d(TAG, "updateAppWidget()");
    }

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        // There may be multiple widgets active, so update all of them
        Log.d(TAG, "onUpdate()");
        for (int appWidgetId : appWidgetIds) {
            updateAppWidget(context, appWidgetManager, appWidgetId);
        }
        super.onUpdate(context, appWidgetManager, appWidgetIds);
    }

    @Override
    public void onEnabled(Context context) {
        Log.d(TAG, "onEnabled()");
        // Enter relevant functionality for when the first widget is created
    }

    @Override
    public void onDisabled(Context context) {
        Log.d(TAG, "onDisabled()");
        // Enter relevant functionality for when the last widget is disabled
    }
}

