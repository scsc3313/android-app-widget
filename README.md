# android-app-widget

android-app-widget with collections

this sample is using StackView



# App widget

제일 중요한 것은 안드로이드 디벨롭 문서!

https://developer.android.com/guide/topics/appwidgets/index.html

##  AppWidgetProvider

- 앱 위젯은 `AppWidgetProvider`을 이용함 (BroadcastReceiver를 상속받은 클래스임)
- Broadcast로 받은 Intent를 분석해서, Action을 취하는 Callback 메서드가 존재함.
- 각 Callback 메서드에 대해 알아볼 것.



## AppWidgetProviderInfo

- 앱 위젯의 Metadata를 가지고 있는 `.xml` 파일 (res/xml 디렉토리에 존재함)
- 최소 크기, 업데이트 주기, 프리뷰 이미지, 레이아웃, configure activity, resizeMode(사이즈를 바꿀 수 있는지 없는지), 카테고리(홈 화면인지 잠금화면인지 설정) 등을 정할 수 있음.



## RemoteViews

- A class that describes a view hierarchy that can be displayed in another process. The hierarchy is inflated from a layout resource file, and this class provides some basic operations for modifying the content of the inflated hierarchy.
- 뷰의 계층을 묘사함하는 클래스. 다른 프로세스에 보여질 수 있음. 레이아웃 파일로 만들어지고, 이 클래스는 기본적인 작업을 제공함.
- AppWidgetProvider는 BroadcastReceiver이기 때문에 UI를 가지지 못하고, 뷰를 원격에서 가져와서 써야함. RemoteViews를 레이아웃으로 생성하고, 그 안에 View 객체들도 사용가능함.


## Configuration Activity

- 환경 설정을 해주는 액티비티!
- 앱 위젯이 생성될 때, 이 Activity가 실행되면서 셋팅을 해줌.
- 사용하기 위해서 `AppWidgetProviderInfo` xml파일에 등록을 해주고
- `AndroidManifest.xml` 에 intent-fileter에 액션을 등록해줘야함



# 앱 위젯을 Collection과 함께 써보자

- RemoteViewsService
- RemoteViewsFactory


# RemoteViewsService

- RemoteViews에게 요청을 보내기 위한 `Service`
- Remote data(`content provider` 같은 곳에서 제공하는) collections을 보여주기 위한 서비스
- `RemoteViewsFactory`를 리턴하는 메소드를 생성함.
- collection 뷰들은 `Adapter`를 사용해야함.
- `RemoteViewsFactory`가 `Adapter` 역할을 대신함.




## RemoteViewsFactory

- Remote Collection View와 데이터들 사이에 있는 `Adapter` 
- 각 아이템의 데이터를 `RemoteViews` 로 바인딩 해주는 역할
- `getViewAt(int postion)` 메서드가 `RemoteViews`를 리턴



## 생성과정

- `onUpdate()` 은 기본적으로 앱 위젯이 생성될 때 호출되는 Callback 메서드
- `onUpdate()` 

```java
public void onUpdate(Context context, AppWidgetManager appWidgetManager,
int[] appWidgetIds) {
    // update each of the app widgets with the remote adapter
    for (int i = 0; i < appWidgetIds.length; ++i) {

        // Set up the intent that starts the StackViewService, which will
        // provide the views for this collection.
        Intent intent = new Intent(context, StackWidgetService.class);
        // Add the app widget ID to the intent extras.
        intent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, appWidgetIds[i]);
        intent.setData(Uri.parse(intent.toUri(Intent.URI_INTENT_SCHEME)));
        // Instantiate the RemoteViews object for the app widget layout.
        RemoteViews rv = new RemoteViews(context.getPackageName(), R.layout.widget_layout);
        // Set up the RemoteViews object to use a RemoteViews adapter.
        // This adapter connects
        // to a RemoteViewsService  through the specified intent.
        // This is how you populate the data.
        rv.setRemoteAdapter(appWidgetIds[i], R.id.stack_view, intent);

        // The empty view is displayed when the collection has no items.
        // It should be in the same layout used to instantiate the RemoteViews
        // object above.
        rv.setEmptyView(R.id.stack_view, R.id.empty_view);

        //
        // Do additional processing specific to this app widget...
        //

        appWidgetManager.updateAppWidget(appWidgetIds[i], rv);
    }
    super.onUpdate(context, appWidgetManager, appWidgetIds);
}
```

- 레이아웃 RemoteViews에 각 앱 위젯들을 `setRemoteAdapter` 메서드로 셋팅해줌.
- 앱 위젯을 업데이트 하도록 `AppWidgetManager.updateAppWidget()`에 RemoteViews를 업데이트 요청.
- `updateAppWidget()` 호출되면 내부적으로 `RemoteViewsService`에서 `RemoteViewsFactory` 객체를 가져와서 데이터와 `RemoteView`를 바인드 시킴.



##  Collection의 item에 액션 넣기

##### 모두 같은 액션 넣기

- PendingIntent를 넣으면 됨.
- `RemoteViews.setPendingIntentTemplate()` 메소드를 이용

#####  아이템 마다 다른 액션 넣기

- fill-in Intent를 넣으면 됨.
- `RemoteViews.setOnClickFillInIntent() ` 메소드를 이용



#####  







