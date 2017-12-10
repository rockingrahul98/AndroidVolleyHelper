# Android Volley Helper
A helper library to easily implement [Volley](https://developer.android.com/training/volley/index.html) library for network calls and make VolleyStringRequest very efficiently in few steps.

# Version

1.0.0

# Installation

To use this library in your Android project, just simply add the following dependency into your build.gradle

```android
dependencies {
    compile 'com.dmax.android_volley_helper:android-volley-helper:1.0.0'
    }
```


# Usage
Create a Class which extends `VolleyContentHandler`.
We write the definition of every network request here and call this class's method from activities.
```java
    public class MyContentHandler extends VolleyContentHandler {

   }
```

Write some static final strings for different calls.

```java

    public static final String CALL_NAME_TEST_GET = "call_test_get";
    public static final String CALL_NAME_TEST_POST_UTF8 = "call_test_post_utf8";
    public static final String CALL_NAME_TEST_POST_XFORMURLINCODED = "call_test_post_xformurlencode";
```

Then make its constructor private and call super method 

```java
     private MyContentHandler() {
        super();
    }
```
Write a `getInstance()` method and return super calss's object in it.

```java
    public static VolleyContentHandler getInstance() {
        if (VOLLEY_CONTENT_HANDLER == null) {
            VOLLEY_CONTENT_HANDLER = new MyContentHandler();
        }
        return VOLLEY_CONTENT_HANDLER;
    }
```
Now override `requestCall()` method and write your code here to make different calls.

```java
 @Override
    public void requestCall(String callName, HashMap<String, String> parameters, String requestListenerKey, String 
    requestTag) {
        if (callName.equals(CALL_NAME_TEST_GET)) {
            requestGetCall(parameters, requestListenerKey, requestTag);
        } else if (callName.equals(CALL_NAME_TEST_POST_UTF8)) {
            requestPostCallUTF8(parameters, requestListenerKey, requestTag);
        } else if (callName.equals(CALL_NAME_TEST_POST_XFORMURLINCODED)) {
            requestPostCallXFORMURLENCODE(parameters, requestListenerKey, requestTag);
        }
    }
```

and finally to make a request call `super.requestContent(requestListenerKey, requestTag, url, body);` where body is optional if its a POST call only then add body.

```java
    private void requestPostCallUTF8(HashMap<String, String> parameters, String requestListenerKey, String requestTag) {
        String url = "http://httpbin.org/post";
        JSONObject object = new JSONObject();
        String userName = parameters.get("name");
        String userNumber = parameters.get("number");
        try {
            object.put("UserName", userName);
            object.put("UserNumber", userNumber);
        } catch (Exception e) {

        }
        String body = object.toString();
        super.requestContent(requestListenerKey, requestTag, url, body);
    }

    private void requestPostCallXFORMURLENCODE(HashMap<String, String> parameters, String requestListenerKey, String 
    requestTag) {
        String url = "https://postman-echo.com/post";
        String body = getUrlEncodedBodyContent(parameters);
        super.requestContent(requestListenerKey, requestTag, url, body, 
    VolleyContentHandler.CONTENT_TYPE_XFORM_URLENCODED);
    }

    private void requestGetCall(HashMap<String, String> parameters, String requestListenerKey, String requestTag) {
        String getUrl = "https://postman-echo.com/get?";
        String url = getUrl + getUrlEncodedBodyContent(parameters);
        super.requestContent(requestListenerKey, requestTag, url);
    }
```




Here is the final code:
```java
	package com.dmax.volleyhelperexample;

	import com.dmax.android_volley_helper.VolleyContentHandler;

	import org.json.JSONObject;

	import java.util.HashMap;

	/**
	 * Created by rockingrahul98 on 25-11-2017.
	 */

	public class MyContentHandler extends VolleyContentHandler {

	    public static final String CALL_NAME_TEST_GET = "call_test_get";
	    public static final String CALL_NAME_TEST_POST_UTF8 = "call_test_post_utf8";
	    public static final String CALL_NAME_TEST_POST_XFORMURLINCODED = "call_test_post_xformurlencode";

	    public static VolleyContentHandler getInstance() {
	        if (VOLLEY_CONTENT_HANDLER == null) {
	            VOLLEY_CONTENT_HANDLER = new MyContentHandler();
	        }
	        return VOLLEY_CONTENT_HANDLER;
	    }

	    private MyContentHandler() {
	        super();
	    }

	    @Override
	    public void requestCall(String callName, HashMap<String, String> parameters, String requestListenerKey, String 
            requestTag) {
	        if (callName.equals(CALL_NAME_TEST_GET)) {
	            requestGetCall(parameters, requestListenerKey, requestTag);

	        } else if (callName.equals(CALL_NAME_TEST_POST_UTF8)) {
	            requestPostCallUTF8(parameters, requestListenerKey, requestTag);
	        } else if (callName.equals(CALL_NAME_TEST_POST_XFORMURLINCODED)) {
	            requestPostCallXFORMURLENCODE(parameters, requestListenerKey, requestTag);
	        }
	    }

	    private void requestPostCallUTF8(HashMap<String, String> parameters, String requestListenerKey, String requestTag) {
	        String url = "http://httpbin.org/post";
	        JSONObject object = new JSONObject();
	        String userName = parameters.get("name");
	        String userNumber = parameters.get("number");
	        try {
	            object.put("UserName", userName);
	            object.put("UserNumber", userNumber);
	        } catch (Exception e) {

	        }
	        String body = object.toString();
	        super.requestContent(requestListenerKey, requestTag, url, body);
	    }

	    private void requestPostCallXFORMURLENCODE(HashMap<String, String> parameters, String requestListenerKey, String requestTag) {
	        String url = "https://postman-echo.com/post";
	        String body = getUrlEncodedBodyContent(parameters);
	        super.requestContent(requestListenerKey, requestTag, url, body, VolleyContentHandler.CONTENT_TYPE_XFORM_URLENCODED);
	    }

	    private void requestGetCall(HashMap<String, String> parameters, String requestListenerKey, String requestTag) {
	        String getUrl = "https://postman-echo.com/get?";
	        String url = getUrl + getUrlEncodedBodyContent(parameters);
	        super.requestContent(requestListenerKey, requestTag, url);
	    }


	}

```

There are 2 keys to make a request from any activity
1- Listener key - By which we identify the activity to deliver response.
2- Request tag - By which we differentiate the requests.

Thus each activity can have only one listener key and multiple Request Tags.

```java
    private static final String LISTENER_KEY = "main_listener_key";
    private static final String REQUEST_TAG_GET = "main_request_tag_get";
    private static final String REQUEST_TAG_POST_UTF8 = "main_request_tag_post_utf8";
    private static final String REQUEST_TAG_POST_XFORM = "main_request_tag_post_xform";
```
To make a request from an activity first we override the `MyContentHandler.ContentListener` in that activty.

```java
 private MyContentHandler.ContentListener contentListener = new MyContentHandler.ContentListener() {
        @Override
        public void handleContent(String tag, String content, int responseCode) {
         
        }
    };
```
Attach the listener in `onResume()` and remove the listener in `onPause()` of the activity.

```java
    @Override
    protected void onPause() {
        super.onPause();
        MyContentHandler.getInstance().removeResponseListener(LISTENER_KEY);

    }

    @Override
    protected void onResume() {
        super.onResume();
        MyContentHandler.getInstance().addResponseListener(contentListener, LISTENER_KEY);

    }
```

And here are the calls we defined in MyContentHandler class

```java
private void requestGetCall() {
        HashMap<String, String> parameters = new HashMap<>();
        parameters.put("name", "TestUSer");
        parameters.put("number", "TestNumber");
        MyContentHandler.getInstance().requestCall(MyContentHandler.CALL_NAME_TEST_GET, parameters, LISTENER_KEY, REQUEST_TAG_GET);
    }

    private void requestPostUtf8Call() {
        HashMap<String, String> parameters = new HashMap<>();
        parameters.put("name", "TestUSer");
        parameters.put("number", "TestNumber");
        MyContentHandler.getInstance().requestCall(MyContentHandler.CALL_NAME_TEST_POST_UTF8, parameters, LISTENER_KEY, REQUEST_TAG_POST_UTF8);
    }

    private void requestPostXformurlencodeCall() {
        HashMap<String, String> parameters = new HashMap<>();
        parameters.put("name", "TestUSer");
        parameters.put("number", "TestNumber");
        MyContentHandler.getInstance().requestCall(MyContentHandler.CALL_NAME_TEST_POST_XFORMURLINCODED, parameters, LISTENER_KEY, REQUEST_TAG_POST_XFORM);
    }

```


Here is the final code of MainActivity

```java

package com.dmax.volleyhelperexample;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;

import java.util.HashMap;

public class MainActivity extends AppCompatActivity {

    private static final String LISTENER_KEY = "main_listener_key";
    private static final String REQUEST_TAG_GET = "main_request_tag_get";
    private static final String REQUEST_TAG_POST_UTF8 = "main_request_tag_post_utf8";
    private static final String REQUEST_TAG_POST_XFORM = "main_request_tag_post_xform";

    private View.OnClickListener clickListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            switch (view.getId()) {
                case R.id.get:
                    requestGetCall();
                    break;

                case R.id.post_utf8:
                    requestPostUtf8Call();
                    break;

                case R.id.post_xform:
                    requestPostXformurlencodeCall();
                    break;
            }
        }
    };
    private MyContentHandler.ContentListener contentListener = new MyContentHandler.ContentListener() {
        @Override
        public void handleContent(String tag, String content, int responseCode) {
                if(responseCode==200){
                    TextView textView= (TextView) findViewById(R.id.text);
                    if(tag.equals(REQUEST_TAG_GET)){
                        textView.setText("GET Content is\n"+content);
                    }else if(tag.equals(REQUEST_TAG_POST_UTF8)){
                        textView.setText("POST UTF 8 Content is\n"+content);
                    }else if(tag.equals(REQUEST_TAG_POST_XFORM)){
                        textView.setText("POST XFORM Content is\n"+content);
                    }
                }else{
                    //Show error
                }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        findViewById(R.id.get).setOnClickListener(clickListener);
        findViewById(R.id.post_utf8).setOnClickListener(clickListener);
        findViewById(R.id.post_xform).setOnClickListener(clickListener);

    }

    @Override
    protected void onPause() {
        super.onPause();
        MyContentHandler.getInstance().removeResponseListener(LISTENER_KEY);

    }

    @Override
    protected void onResume() {
        super.onResume();
        MyContentHandler.getInstance().addResponseListener(contentListener, LISTENER_KEY);

    }

    private void requestGetCall() {
        HashMap<String, String> parameters = new HashMap<>();
        parameters.put("name", "TestUSer");
        parameters.put("number", "TestNumber");
        MyContentHandler.getInstance().requestCall(MyContentHandler.CALL_NAME_TEST_GET, parameters, LISTENER_KEY, REQUEST_TAG_GET);
    }

    private void requestPostUtf8Call() {
        HashMap<String, String> parameters = new HashMap<>();
        parameters.put("name", "TestUSer");
        parameters.put("number", "TestNumber");
        MyContentHandler.getInstance().requestCall(MyContentHandler.CALL_NAME_TEST_POST_UTF8, parameters, LISTENER_KEY, REQUEST_TAG_POST_UTF8);
    }

    private void requestPostXformurlencodeCall() {
        HashMap<String, String> parameters = new HashMap<>();
        parameters.put("name", "TestUSer");
        parameters.put("number", "TestNumber");
        MyContentHandler.getInstance().requestCall(MyContentHandler.CALL_NAME_TEST_POST_XFORMURLINCODED, parameters, LISTENER_KEY, REQUEST_TAG_POST_XFORM);
    }

}

```
