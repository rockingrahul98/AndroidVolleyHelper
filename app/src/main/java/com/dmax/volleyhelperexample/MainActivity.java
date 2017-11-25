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
