package com.example.android_sdk.Input;

import android.content.Context;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;

import com.example.android_sdk.Store.DataStore;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class BillingInput extends android.support.v7.widget.AppCompatSpinner {

    public interface BillingListener {
        void onGoToBilling();
    }

    private @Nullable
    BillingInput.BillingListener mBillingListener;
    private Context mContext;
    private DataStore mDatastore = DataStore.getInstance();

    public BillingInput(Context context) {
        this(context, null);
    }

    public BillingInput(Context context, AttributeSet attrs) {
        super(context, attrs);
        mContext = context;
        init();
    }

    private void init() {

        // Options needed for focus context switching
        setFocusable(true);
        setFocusableInTouchMode(true);

        populateSpinner();

        setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    performClick();
                    @Nullable InputMethodManager imm = (InputMethodManager) mContext.getSystemService(Context.INPUT_METHOD_SERVICE);
                    if(imm != null) {
                        imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
                    }
                }
            }
        });
    }

    private void populateSpinner() {
        if(mDatastore != null && !mDatastore.getCustomerAddress1().equals("")) {
            List<String> billingElement = new ArrayList<>();

            billingElement.add(mDatastore.getCustomerAddress1());
            billingElement.add("Edit");

            ArrayAdapter<String> dataAdapter = new ArrayAdapter<>(mContext,
                    android.R.layout.simple_spinner_item, billingElement);
            dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            setAdapter(dataAdapter);
        } else {
            List<String> billingElement = new ArrayList<>();

            billingElement.add("SELECT");
            billingElement.add("  + ADD");

            ArrayAdapter<String> dataAdapter = new ArrayAdapter<>(mContext,
                    android.R.layout.simple_spinner_item, billingElement);
            dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            setAdapter(dataAdapter);
        }
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        if(mBillingListener != null && this.getSelectedItemPosition() == 1){
            mBillingListener.onGoToBilling();
        }
        super.onLayout(changed, l, t, r, b);
    }


    public void setBillingListener(BillingInput.BillingListener listener) {
        this.mBillingListener = listener;
    }
}
