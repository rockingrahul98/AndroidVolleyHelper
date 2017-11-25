package com.dmax.android_volley_helper;

import com.android.volley.DefaultRetryPolicy;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by amandeep on 29/12/15.
 */
public abstract class VolleyContentHandler {

    protected static VolleyContentHandler VOLLEY_CONTENT_HANDLER = null;
    public static final String CONTENT_TYPE_UTF8 = "UTF-8";
    public static final String CONTENT_TYPE_XFORM_URLENCODED = "application/x-www-form-urlencoded";

    public static void clean() {
        if (VOLLEY_CONTENT_HANDLER != null) {
            VOLLEY_CONTENT_HANDLER.cleanUp();
            VOLLEY_CONTENT_HANDLER = null;
        }
    }

    private HashMap<String, ContentListener> mapOfResponseListeners = new HashMap<>();
    private HashMap<String, String> requestInProcess_RequestTagAndListenerKey = new HashMap<>();

    private VolleyResponseListener responseListener = new VolleyResponseListener() {

        @Override
        public void handleResponse(String tag, String response, int responseCode) {
            if (requestInProcess_RequestTagAndListenerKey.containsKey(tag)) {
                String listenerKey = requestInProcess_RequestTagAndListenerKey.get(tag);
                ContentListener contentListener = mapOfResponseListeners.get(listenerKey);
                if (contentListener != null) {
                    contentListener.handleContent(tag, response, responseCode);
                } else {
                    //nothing
                }

                requestInProcess_RequestTagAndListenerKey.remove(tag);
            }
        }

    };


    protected VolleyContentHandler() {

    }

    public void addResponseListener(ContentListener responseListener, String listenerKey) {
        mapOfResponseListeners.put(listenerKey, responseListener);
    }

    public void removeResponseListener(String listenerKey) {
        mapOfResponseListeners.remove(listenerKey);
    }

    public abstract void requestCall(String callName, HashMap<String, String> parameters, String requestListenerKey, String requestTag);

    public void requestContent(String listenerKey, String requestTag, String url) {
        if (url != null && !requestInProcess_RequestTagAndListenerKey.containsKey(requestTag)) {

            VolleyTagRequest request = new VolleyTagRequest(requestTag, url, responseListener);
            request.setRetryPolicy(new DefaultRetryPolicy(Constants.CONNECTION_READ_TIME_OUT, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

            VolleyRequestHandler.getInstance().addToRequestQueue(request);

            requestInProcess_RequestTagAndListenerKey.put(requestTag, listenerKey);
        }
    }

    public void requestContent(String listenerKey, String requestTag, String url, String requestBody, String bodyContentType) {
        if (url != null && !requestInProcess_RequestTagAndListenerKey.containsKey(requestTag)) {

            VolleyTagRequest request = new VolleyTagRequest(requestTag, url, requestBody,bodyContentType, responseListener);
            request.setRetryPolicy(new DefaultRetryPolicy(Constants.CONNECTION_READ_TIME_OUT, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

            VolleyRequestHandler.getInstance().addToRequestQueue(request);

            requestInProcess_RequestTagAndListenerKey.put(requestTag, listenerKey);
        }
    }


    public void requestContent(String listenerKey, String requestTag, String url, String requestBody) {
        if (url != null && !requestInProcess_RequestTagAndListenerKey.containsKey(requestTag)) {

            VolleyTagRequest request = new VolleyTagRequest(requestTag, url, requestBody, responseListener);
            request.setRetryPolicy(new DefaultRetryPolicy(Constants.CONNECTION_READ_TIME_OUT, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

            VolleyRequestHandler.getInstance().addToRequestQueue(request);

            requestInProcess_RequestTagAndListenerKey.put(requestTag, listenerKey);
        }
    }

    public static String generateURL(String url, String parameters) {
        StringBuilder stringBuilder = new StringBuilder(url);
        stringBuilder.append(parameters);
        return stringBuilder.toString();
    }

    public static String getUrlEncodedBodyContent(Map<String, String> params) {
        String bodyContent = null;
        StringBuilder encodedParams = new StringBuilder();
        try {
            for (Map.Entry<String, String> entry : params.entrySet()) {
                encodedParams.append(URLEncoder.encode(entry.getKey(), "UTF-8"));
                encodedParams.append('=');
                encodedParams.append(URLEncoder.encode(entry.getValue(), "UTF-8"));
                encodedParams.append('&');
            }
            bodyContent = encodedParams.toString();
        } catch (UnsupportedEncodingException uee) {
            throw new RuntimeException("Encoding not supported: " + "UTF-8", uee);
        }
        bodyContent=bodyContent.substring(0,bodyContent.length()-1);
        return bodyContent;
    }

    private void cleanUp() {
        mapOfResponseListeners.clear();
        requestInProcess_RequestTagAndListenerKey.clear();
    }

    public interface ContentListener {

        void handleContent(String tag, String content, int responseCode);

    }

}
