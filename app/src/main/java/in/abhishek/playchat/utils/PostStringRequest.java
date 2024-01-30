package in.abhishek.playchat.utils;

import com.android.volley.NetworkResponse;
import com.android.volley.Response;
import com.android.volley.toolbox.HttpHeaderParser;
import com.android.volley.toolbox.JsonRequest;

import org.json.JSONObject;

import java.io.UnsupportedEncodingException;

public class PostStringRequest extends JsonRequest<String> {
    private final Response.Listener<String> mListener;
    /**
     * Creates a new request with the given method.
     *
     * @param method the request {@link Method} to use
     * @param url URL to fetch the string at
     * @param listener Listener to receive the String response
     * @param errorListener Error listener, or null to ignore errors
     */
    public PostStringRequest(int method, String url, Response.Listener<String> listener,
                             Response.ErrorListener errorListener) {
        super(method, url, null , listener, errorListener);
        mListener = listener;
    }
    /**
     * Creates a new request with the given method.
     *
     * @param method the request {@link Method} to use
     * @param url URL to fetch the string at
     * @param jsonRequest body data to be sent
     * @param listener Listener to receive the String response
     * @param errorListener Error listener, or null to ignore errors
     */
    public PostStringRequest(int method, String url, JSONObject jsonRequest, Response.Listener<String> listener,
                             Response.ErrorListener errorListener) {
        super(method, url, jsonRequest.toString() , listener, errorListener);
        mListener = listener;
    }
    /**
     * Creates a new POST request.
     *
     * @param url URL to fetch the string at
     * @param listener Listener to receive the String response
     * @param errorListener Error listener, or null to ignore errors
     */
    public PostStringRequest(String url, Response.Listener<String> listener, Response.ErrorListener errorListener) {
        this(Method.POST, url, listener, errorListener);
    }
    /**
     * Creates a new POST request.
     *
     * @param url URL to fetch the string at
     * @param jsonRequest body data to be sent
     * @param listener Listener to receive the String response
     * @param errorListener Error listener, or null to ignore errors
     */
    public PostStringRequest(String url, JSONObject jsonRequest , Response.Listener<String> listener, Response.ErrorListener errorListener) {
        this(Method.POST, url, jsonRequest, listener, errorListener);
    }
    @Override
    protected void deliverResponse(String response) {
        mListener.onResponse(response);
    }
    @Override
    protected Response<String> parseNetworkResponse(NetworkResponse response) {
        String parsed;
        try {
            parsed = new String(response.data, HttpHeaderParser.parseCharset(response.headers));
        } catch (UnsupportedEncodingException e) {
            parsed = new String(response.data);
        }
        return Response.success(parsed, HttpHeaderParser.parseCacheHeaders(response));
    }
}



