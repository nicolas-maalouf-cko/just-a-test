package com.example.android_sdk.Input;

import android.content.Context;
import android.support.annotation.Nullable;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.view.View;

public class NameInput extends android.support.v7.widget.AppCompatEditText {

    public interface NameListener {
        void onNameInputFinish(String number);
        void clearNameError();
    }

    private @Nullable
    NameInput.NameListener mNameListener;
    private Context mContext;

    public NameInput(Context context) {
        this(context, null);
    }

    public NameInput(Context context, AttributeSet attrs) {
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

                // Clear error if the user starts typing
                if (mNameListener != null) {
                    mNameListener.clearNameError();
                }
                // Save state
                if(mNameListener != null) {
                    mNameListener.onNameInputFinish(s.toString());
                }
            }
        });

    }

    public void setNameListener(NameInput.NameListener listener) {
        mNameListener = listener;
    }
}
