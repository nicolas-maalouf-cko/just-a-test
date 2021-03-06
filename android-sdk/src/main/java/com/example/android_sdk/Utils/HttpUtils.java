package com.example.android_sdk.Utils;

import android.content.Context;
import android.support.annotation.Nullable;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.android_sdk.CheckoutKit;
import com.example.android_sdk.Response.CardTokenisationFail;
import com.example.android_sdk.Response.CardTokenisationResponse;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class HttpUtils {

    private @Nullable
    CheckoutKit.OnTokenGenerated mTokenListener;
    private Context mContext;

    public HttpUtils(Context context) {
        //empty constructor
        mContext = context;
    }

    public void generateToken(final String key, String url, String body) throws JSONException {

        RequestQueue queue = Volley.newRequestQueue(mContext);

        JsonObjectRequest portRequest = new JsonObjectRequest(Request.Method.POST, url, new JSONObject(body),
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            if (mTokenListener != null) {
                                CardTokenisationResponse responseBody = new CardTokenisationResponse();
                                JSONObject card = response.getJSONObject("card");
                                JSONObject billing = card.getJSONObject("billingDetails");
                                JSONObject phone = billing.getJSONObject("phone");
                                responseBody
                                        .setId(response.getString("id"))
                                        .setLiveMode(response.getString("liveMode"))
                                        .setCreated(response.getString("created"))
                                        .setUsed(response.getString("used"))
                                        .setExpiryMonth(card.getString("expiryMonth"))
                                        .setExpiryYear(card.getString("expiryYear"))
                                        .setCardId(card.getString("id"))
                                        .setCardLast4(card.getString("last4"))
                                        .setCardBin(card.getString("bin"))
                                        .setCardPaymentMethod(card.getString("paymentMethod"))
                                        .setAddressLine1(billing.getString("addressLine1"))
                                        .setAddressLine2(billing.getString("addressLine2"))
                                        .setPostoce(billing.getString("postcode"))
                                        .setCountry(billing.getString("country"))
                                        .setCity(billing.getString("city"))
                                        .setState(billing.getString("state"))
                                        .setAddressLine1(billing.getString("addressLine1"));
                                try {
                                    responseBody.setPhoneCountryCode(phone.getString("countryCode"));
                                } catch (Exception e) {
                                    // null value
                                }

                                try {
                                    responseBody.setPhoneNumber(phone.getString("number"));
                                } catch (Exception e) {
                                    // null value
                                }
                                mTokenListener.onTokenGenerated(responseBody);
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        NetworkResponse networkResponse = error.networkResponse;
                        if (networkResponse != null && networkResponse.data != null) {
                            try {
                                JSONObject jsonError = new JSONObject(new String(networkResponse.data));

                                JSONArray errorMessageCodes = jsonError.getJSONArray("errorMessageCodes");
                                String[] codes = new String[errorMessageCodes.length()];
                                for (int i = 0; i < errorMessageCodes.length(); i++)
                                    codes[i] = errorMessageCodes.getString(i);

                                JSONArray errors = jsonError.getJSONArray("errors");
                                String[] err = new String[errors.length()];
                                for (int i = 0; i < errors.length(); i++)
                                    err[i] = errors.getString(i);


                                CardTokenisationFail cardTokenisationFail = new CardTokenisationFail();
                                cardTokenisationFail
                                        .setEventId(jsonError.getString("eventId"))
                                        .setErrorCode(jsonError.getString("errorCode"))
                                        .setMessage(jsonError.getString("message"))
                                        .setErrorMessageCodes(codes)
                                        .setErrors(err);
                                if (mTokenListener != null) {
                                    mTokenListener.onError(cardTokenisationFail);
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    }
                }
        ) {
            @Override
            public Map<String, String> getHeaders() {
                Map<String, String> params = new HashMap<>();
                params.put("Authorization", key);
                return params;
            }
        };
        portRequest.setRetryPolicy(new DefaultRetryPolicy(10000, 10, 1.0f));
        queue.add(portRequest);
    }

    public void setTokenListener(CheckoutKit.OnTokenGenerated listener) {
        mTokenListener = listener;
    }
}
