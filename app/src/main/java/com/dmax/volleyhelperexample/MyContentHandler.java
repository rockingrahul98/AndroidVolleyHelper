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
    public void requestCall(String callName, HashMap<String, String> parameters, String requestListenerKey, String requestTag) {
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
