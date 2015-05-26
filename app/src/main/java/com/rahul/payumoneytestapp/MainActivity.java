package com.rahul.payumoneytestapp;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.View;

import java.util.HashMap;


public class MainActivity extends ActionBarActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void onPayClicked(View view) {
        HashMap<String,String> params = new HashMap<>();
        params.put(PaymentTestActivity.PARAM_AMOUNT, String.valueOf(123));
        params.put(PaymentTestActivity.PARAM_FIRST_NAME, "rahul");
        params.put(PaymentTestActivity.PARAM_EMAIL, "rahul@bullfin.ch");
        params.put(PaymentTestActivity.PARAM_PHONE, "8884080486");
        params.put(PaymentTestActivity.PARAM_PRODUCT_INFO, "sample_product");
        params.put(PaymentTestActivity.PARAM_SUCCESS_URL, "http://www.google.com");
        params.put(PaymentTestActivity.PARAM_FAILURE_URL, "http://www.google.com");

        Intent intent = new Intent(this, PaymentTestActivity.class);
        intent.putExtra(PaymentTestActivity.EXTRA_PARAMS, params);
        startActivity(intent);
    }
}
