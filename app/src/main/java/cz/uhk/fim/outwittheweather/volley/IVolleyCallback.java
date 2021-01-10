package cz.uhk.fim.outwittheweather.volley;

import android.content.Context;

import com.android.volley.VolleyError;
import org.json.JSONObject;

public interface IVolleyCallback {
    void onSuccess(Context context, JSONObject response);
    void onError(VolleyError error);
}
