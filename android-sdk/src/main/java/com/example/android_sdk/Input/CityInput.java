package com.example.android_sdk.Input;

import android.content.Context;
import android.support.annotation.Nullable;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.AttributeSet;

import com.example.android_sdk.Store.DataStore;

public class CityInput extends android.support.v7.widget.AppCompatEditText {

    public interface CityListener {
        void onCityInputFinish(String number);
    }

    private @Nullable
    CityInput.CityListener mCityListener;
    private Context mContext;
    private DataStore mDataStore = DataStore.getInstance();

    public CityInput(Context context) {
        this(context, null);
    }

    public CityInput(Context context, AttributeSet attrs) {
        super(context, attrs);
        mContext = context;
        init();
    }

    private void init() {



        addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                // Save state
                if(mCityListener != null) {
                    mCityListener.onCityInputFinish(s.toString());
                }
            }
        });
    }

    public void setCityListener(CityInput.CityListener listener) {
        this.mCityListener = listener;
    }
}