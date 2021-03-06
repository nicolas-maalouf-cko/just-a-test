package com.example.android_sdk.Input;

import android.content.Context;
import android.support.annotation.Nullable;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.view.View;

import com.example.android_sdk.Store.DataStore;

public class AddressTwoInput extends android.support.v7.widget.AppCompatEditText {

    public interface AddressTwoListener {
        void onAddressTwoInputFinish(String number);
        void clearAddressOneError();
    }

    private @Nullable
    AddressTwoInput.AddressTwoListener mAddressTwoListener;
    private Context mContext;
    private DataStore mDataStore = DataStore.getInstance();

    public AddressTwoInput(Context context) {
        this(context, null);
    }

    public AddressTwoInput(Context context, AttributeSet attrs) {
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
                if(mAddressTwoListener != null) {
                    mAddressTwoListener.onAddressTwoInputFinish(s.toString());
                }
            }
        });

        setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (mAddressTwoListener != null && hasFocus) {
                    mAddressTwoListener.clearAddressOneError();
                }
            }
        });
    }

    public void setAddressTwoListenerListener(AddressTwoInput.AddressTwoListener listener) {
        this.mAddressTwoListener = listener;
    }
}