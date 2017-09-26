package zz.com.test1.utils;

import android.content.Context;

import com.alibaba.fastjson.JSON;
import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.HttpHeaderParser;
import com.android.volley.toolbox.Volley;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

import rx.Observable;
import rx.Subscriber;
import rx.exceptions.OnErrorFailedException;
import zz.com.test1.MyApplication;

/**
 * Created by lenovo on 2017/4/10.
 */
@SuppressWarnings("ALL")
public class RXRequest<T extends Object> extends Request<T> implements Observable.OnSubscribe<T> {
    private static RequestQueue mRequestQueue;
    private Subscriber<? super T> subscriber;
    //对应的class
    private Class<T> clz;
    private Context context;
    //参数的map集合
    private Map<String, String> params = new ConcurrentHashMap<>();
    private final static String si = "e5ee0fd5d7dcca13f9d64b24a052d99b51e4ee7b";

    public static final String URL_2_4 = "http://api.e.lbl.aduer.com";

    /**
     * 私有其构造方法，让外界传入具体的参数
     *
     * @param context
     * @param url
     * @param clz
     */
    private RXRequest(Context context, String url, Class<T> clz) {
        super(Method.POST, url);
        this.context = context;
        this.clz = clz;
        setRetryPolicy(new DefaultRetryPolicy(30000, 0, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        setErrorListener(new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError volleyError) {
                if (volleyError instanceof TimeoutError)
                    subscriber.onError(new OnErrorFailedException("网络超时", volleyError));
                else
                    subscriber.onError(new OnErrorFailedException("网络异常", volleyError));
                subscriber.onCompleted();
            }
        });


    }

    /**
     * Request 的参数集合
     *
     * @return
     * @throws AuthFailureError
     */
    @Override
    protected Map<String, String> getParams() throws AuthFailureError {
        for (Map.Entry<String, String> en : params.entrySet()) {
            if (en.getValue() == null || en.getValue().length() <= 0) {
                params.remove(en.getKey());
            }

        }
        if (getUrl().contains(URL_2_4)) {
            //项目中对账户密码进行检验
//            if (!TextUtils.isEmpty(CommonData.getLogId())&&!TextUtils.isEmpty(CommonData.getPass())) {
//                params.remove("opid");//历史遗留问题，需要特殊处理
//                params.remove("opass");
//                params.put("opId", CommonData.getSiteUserId());
//                params.put("oPass", CommonData.getPass());
//            }

            params.remove("opid");//历史遗留问题，需要特殊处理
            params.remove("opass");
            params.put("opId", "2");
            params.put("oPass", "159357");
            params.put("deviceid", "android_e3c85de6-a6b1-4a75-8e14-7a6ff7a827b2");
            params.put("timeStamp", ((int) (System.currentTimeMillis() / 1000)) + "");
            params.put("siteusertype", "3");
            if (params.containsKey("sign")) {
                params.remove("sign");
            }
            String sign = getSign(params);

            params.put("sign", sign);
        }
        return params;
    }

    private String getSign(Map<String, String> mMap) {
//
//        List<Integer> intP = new ArrayList<Integer>();
//        intP.add(1);
//        intP.add(10);
//        intP.add(3);
        List<NameValuePair> params = new ArrayList<NameValuePair>();
        Set<String> set = mMap.keySet();
        Iterator<String> it = set.iterator();
        while (it.hasNext()) {
            String key = it.next();
            params.add(new BasicNameValuePair(key, mMap.get(key)));

        }
//        Collections.sort(intP, new Comparator<Integer>() {
//            public int compare(Integer arg0, Integer arg1) {
//                return arg1.compareTo(arg0);
//            }
//        });
        Collections.sort(params, new Comparator<NameValuePair>() {
            public int compare(NameValuePair arg0, NameValuePair arg1) {
                String name0 = arg0.getName();
                String name1 = arg1.getName();
                return name0.compareToIgnoreCase(name1);
            }
        });
        StringBuilder builder = new StringBuilder();
        for (NameValuePair item : params) {
            builder.append(item.getValue());
        }
        builder.append(si);
        String str = builder.toString().toLowerCase();

        try {
            str = URLEncoder.encode(str, "UTF-8").toLowerCase();
        } catch (UnsupportedEncodingException pE) {
            pE.printStackTrace();
        }

        String sign = MD5Util.MD5(str);

        return sign;

    }


    /**
     * extends Request<T>
     * volley的回调
     *
     * @param networkResponse
     * @return
     */
    @Override
    protected Response<T> parseNetworkResponse(NetworkResponse response) {

        String parsed;
        try {
            parsed = new String(response.data, HttpHeaderParser.parseCharset(response.headers));
        } catch (UnsupportedEncodingException var4) {
            parsed = new String(response.data);
        }


        if (clz.equals(String.class)) {
            T t = (T) parsed;
            return Response.success(t, HttpHeaderParser.parseCacheHeaders(response));
        }
        T t = JSON.parseObject(parsed, clz);

        return Response.success(t, HttpHeaderParser.parseCacheHeaders(response));
    }

    /**
     * extends Request<T>
     * volley的回调
     *
     * @param t
     */
    @Override
    protected void deliverResponse(T s) {
        if (s == null)
            subscriber.onError(new NullPointerException("获取数据异常"));
        else
            subscriber.onNext(s);

        subscriber.onCompleted();
    }

    /**
     * implements Observable.OnSubscribe<T>
     * rxjava的回调
     *
     * @param subscriber
     */
    @Override
    public void call(Subscriber<? super T> subscriber) {
        RXRequest.this.subscriber = subscriber;

        if (mRequestQueue == null)
            mRequestQueue = Volley.newRequestQueue(context);

        mRequestQueue.add(RXRequest.this);
    }

    public RXRequest<T> add(String key, String value) {
        params.put(key, value);
        return this;
    }

    public RXRequest<T> addll(Map<String, String> params) {
        this.params.putAll(params);
        return this;
    }

    public static <T extends Object> RXRequest<T> create(String path, Class<T> clz) {
        return new RXRequest<>(MyApplication.getGlobalApplication(), URL_2_4 + path, clz);
    }

    public static <T extends Object> RXRequest<T> createFromUrl(String url, Class<T> clz) {
        return new RXRequest<>(MyApplication.getGlobalApplication(), url, clz);
    }
}
