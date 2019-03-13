package edu.quinnipiac.ser210.cryptousd;

import org.json.JSONException;
import org.json.JSONObject;

public class Handler {
    public String getCoinValue(String coinJsonStr) throws JSONException {
        JSONObject coinValueJSONObj = new JSONObject(coinJsonStr);
        return coinValueJSONObj.getString("last_price");
    }
}

