package com.rahul.payumoneytestapp;

import android.annotation.SuppressLint;
import android.content.Context;
import android.net.http.SslError;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.webkit.SslErrorHandler;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Toast;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;


public class PaymentTestActivity extends ActionBarActivity {
    private static final String LOG_TAG = "PaymentTestActivity";

    public static final String EXTRA_PARAMS = "params";

    private static final String MERCHANT_KEY = "JBZaLc";
    private static final String SALT = "GQs7yium";
    private static final String BASE_URL = "https://test.payu.in";
    private static final String PAYMENT_URL = BASE_URL + "/_payment";

    public static final String PARAM_KEY = "key";
    public static final String PARAM_TRANSACTION_ID = "txnid";
    public static final String PARAM_AMOUNT = "amount";
    public static final String PARAM_FIRST_NAME = "firstname";
    public static final String PARAM_EMAIL = "email";
    public static final String PARAM_PHONE = "phone";
    public static final String PARAM_PRODUCT_INFO = "productinfo";
    public static final String PARAM_SUCCESS_URL = "surl";
    public static final String PARAM_FAILURE_URL = "furl";
    public static final String PARAM_SERVICE_PROVIDER = "service_provider";
    public static final String PARAM_HASH = "hash";
    public static final String PARAM_LAST_NAME = "lastname";
    public static final String PARAM_ADDRESS1 = "address1";
    public static final String PARAM_ADDRESS2 = "address2";
    public static final String PARAM_CITY = "city";
    public static final String PARAM_STATE = "state";
    public static final String PARAM_COUNTRY = "country";
    public static final String PARAM_ZIP_CODE = "zipcode";
    public static final String PARAM_UDF1 = "udf1";
    public static final String PARAM_UDF2 = "udf2";
    public static final String PARAM_UDF3 = "udf3";
    public static final String PARAM_UDF4 = "udf4";
    public static final String PARAM_UDF5 = "udf5";
    public static final String PARAM_PG = "pg";

    private static final String KEY_PAYU_PAISA = "payu_paisa";

    private WebView mWebView;
    private Context mContext;
    private HashMap<String, String> mInputParams;
    private String mHashValue;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContext = this;
        mWebView = new WebView(mContext);
        setContentView(mWebView);

        mInputParams = (HashMap<String, String>) getIntent().getSerializableExtra(EXTRA_PARAMS);

        if (mInputParams == null || mInputParams.isEmpty()) {
            finish();
        }

        if(TextUtils.isEmpty(mInputParams.get(PARAM_TRANSACTION_ID))) {
            Random random = new Random();
            String randomStr = Integer.toString(random.nextInt()) + (System.currentTimeMillis() / 1000L);
            String transactionId = hashCal("SHA-256", randomStr).substring(0, 20);
            mInputParams.put(PARAM_TRANSACTION_ID, transactionId);
        }

        String hashSequence = "key|txnid|amount|productinfo|firstname|email|udf1|udf2|udf3|udf4|udf5|udf6|udf7|udf8|udf9|udf10";
        String hashString = "";
        String[] hashKeys = hashSequence.split("\\|");
        for (String key : hashKeys) {
            hashString = hashString.concat(getNonNullValueFromHashMap(mInputParams, key));
            hashString = hashString.concat("|");
        }

        hashString = hashString.concat(SALT);

        mHashValue = hashCal("SHA-512", hashString);
        Log.v(LOG_TAG, "HASH: " + mHashValue);

        configureWebView();
    }

    @SuppressLint("SetJavaScriptEnabled")
    private void configureWebView() {
        mWebView.setVisibility(View.VISIBLE);
        mWebView.getSettings().setBuiltInZoomControls(true);
        mWebView.getSettings().setCacheMode(2);
        mWebView.getSettings().setDomStorageEnabled(true);
        mWebView.clearHistory();
        mWebView.clearCache(true);
        mWebView.getSettings().setJavaScriptEnabled(true);
        mWebView.getSettings().setSupportZoom(true);
        mWebView.getSettings().setUseWideViewPort(false);
        mWebView.getSettings().setLoadWithOverviewMode(false);
        // mWebView.addJavascriptInterface(new PayUJavaScriptInterface(activity), "PayUMoney");
        mWebView.setWebViewClient(new WebViewClient() {
            @Override
            public void onReceivedError(WebView view, int errorCode, String description, String failingUrl) {
                showToast("Oh no! " + description);
            }

            @Override
            public void onReceivedSslError(WebView view, SslErrorHandler handler, SslError error) {
                showToast("SslError! " + error);
                handler.proceed();
            }

            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                return super.shouldOverrideUrlLoading(view, url);
            }
        });

        HashMap<String,String> formParams = new HashMap<>();
        formParams.put(PARAM_KEY, MERCHANT_KEY);
        formParams.put(PARAM_HASH, mHashValue);
        formParams.put(PARAM_TRANSACTION_ID, getNonNullValueFromHashMap(mInputParams, PARAM_TRANSACTION_ID));
        formParams.put(PARAM_AMOUNT, getNonNullValueFromHashMap(mInputParams, PARAM_AMOUNT));
        formParams.put(PARAM_FIRST_NAME, getNonNullValueFromHashMap(mInputParams, PARAM_FIRST_NAME));
        formParams.put(PARAM_EMAIL, getNonNullValueFromHashMap(mInputParams, PARAM_EMAIL));
        formParams.put(PARAM_PHONE, getNonNullValueFromHashMap(mInputParams, PARAM_PHONE));
        formParams.put(PARAM_PRODUCT_INFO, getNonNullValueFromHashMap(mInputParams, PARAM_PRODUCT_INFO));
        formParams.put(PARAM_SUCCESS_URL, getNonNullValueFromHashMap(mInputParams, PARAM_SUCCESS_URL));
        formParams.put(PARAM_FAILURE_URL, getNonNullValueFromHashMap(mInputParams, PARAM_FAILURE_URL));
        formParams.put(PARAM_LAST_NAME, getNonNullValueFromHashMap(mInputParams, PARAM_LAST_NAME));
        formParams.put(PARAM_ADDRESS1, getNonNullValueFromHashMap(mInputParams, PARAM_ADDRESS1));
        formParams.put(PARAM_ADDRESS2, getNonNullValueFromHashMap(mInputParams, PARAM_ADDRESS2));
        formParams.put(PARAM_CITY, getNonNullValueFromHashMap(mInputParams, PARAM_CITY));
        formParams.put(PARAM_STATE, getNonNullValueFromHashMap(mInputParams, PARAM_STATE));
        formParams.put(PARAM_COUNTRY, getNonNullValueFromHashMap(mInputParams, PARAM_COUNTRY));
        formParams.put(PARAM_ZIP_CODE, getNonNullValueFromHashMap(mInputParams, PARAM_ZIP_CODE));
        formParams.put(PARAM_UDF1, getNonNullValueFromHashMap(mInputParams, PARAM_UDF1));
        formParams.put(PARAM_UDF2, getNonNullValueFromHashMap(mInputParams, PARAM_UDF2));
        formParams.put(PARAM_UDF3, getNonNullValueFromHashMap(mInputParams, PARAM_UDF3));
        formParams.put(PARAM_UDF4, getNonNullValueFromHashMap(mInputParams, PARAM_UDF4));
        formParams.put(PARAM_UDF5, getNonNullValueFromHashMap(mInputParams, PARAM_UDF5));
        formParams.put(PARAM_PG, getNonNullValueFromHashMap(mInputParams, PARAM_PG));
        formParams.put(PARAM_SERVICE_PROVIDER, KEY_PAYU_PAISA);

        if (hasMandatoryParamsPresent(formParams)) {
            runPOSTFromWebView(mWebView, PAYMENT_URL, formParams.entrySet());
        } else {
            showToast("Mandatory parameter(s) missing.");
        }
    }

    private void showToast(String message) {
        Toast.makeText(mContext,
                message,
                Toast.LENGTH_SHORT)
                .show();
    }

    public boolean hasMandatoryParamsPresent(HashMap<String,String> paramsHash) {
        return !(TextUtils.isEmpty(paramsHash.get(PARAM_KEY))
                || TextUtils.isEmpty(paramsHash.get(PARAM_TRANSACTION_ID))
                || TextUtils.isEmpty(paramsHash.get(PARAM_AMOUNT))
                || TextUtils.isEmpty(paramsHash.get(PARAM_PRODUCT_INFO))
                || TextUtils.isEmpty(paramsHash.get(PARAM_FIRST_NAME))
                || TextUtils.isEmpty(paramsHash.get(PARAM_EMAIL))
                || TextUtils.isEmpty(paramsHash.get(PARAM_PHONE))
                || TextUtils.isEmpty(paramsHash.get(PARAM_SERVICE_PROVIDER))
                || TextUtils.isEmpty(paramsHash.get(PARAM_FAILURE_URL))
                || TextUtils.isEmpty(paramsHash.get(PARAM_SUCCESS_URL)));
    }

    public String getNonNullValueFromHashMap(HashMap<String, String> paramsHash, String key) {
        String value = paramsHash.get(key);
        return (value == null) ? "" : value;
    }

    public void runPOSTFromWebView(WebView webView, String url, Collection<Map.Entry<String, String>> postData){
        StringBuilder sb = new StringBuilder();

        sb.append("<html><head></head>");
        sb.append("<body onload='payment_form.submit()'>");
        sb.append(String.format("<form id='payment_form' action='%s' method='%s'>", url, "post"));
        for (Map.Entry<String, String> item : postData) {
            sb.append(String.format("<input name='%s' type='hidden' value='%s' />", item.getKey(), item.getValue()));
        }
        sb.append("</form></body></html>");

        Log.d(LOG_TAG, "runPOSTFromWebView called");

        webView.loadData(sb.toString(), "text/html", "utf-8");
    }

    public String hashCal(String algo,String input) {
        byte[] hashseq = input.getBytes();
        StringBuilder hexString = new StringBuilder();
        try {
            MessageDigest algorithm = MessageDigest.getInstance(algo);
            algorithm.reset();
            algorithm.update(hashseq);
            byte messageDigest[] = algorithm.digest();

            for (byte aMessageDigest : messageDigest) {
                String hex = Integer.toHexString(0xFF & aMessageDigest);
                if (hex.length() == 1) hexString.append("0");
                hexString.append(hex);
            }

        } catch (NoSuchAlgorithmException nsae) {
            Log.e(LOG_TAG, "Invalid algorithm " + algo);
        }

        return hexString.toString();
    }
}
