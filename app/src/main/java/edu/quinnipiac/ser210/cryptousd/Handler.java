/*
 * This class handles the JSON parsing as well as the dialog box used for the help menu and the
 * change background method used in the toolbar
 *
 * Created by: Kevin Sangurima
 * Last Updated: 03/22/19
 *
 */

package edu.quinnipiac.ser210.cryptousd;

import android.app.Activity;
import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.support.v7.view.ContextThemeWrapper;
import android.view.View;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

public class Handler {
    // This retrieves the JSON
    public String getCoinValue(String coinJsonStr) throws JSONException {
        JSONObject coinValueJSONObj = new JSONObject(coinJsonStr);
        return coinValueJSONObj.getString("last_price");
    }
    // This handles the dialog box that will be shown when the user clicks on the help option on the toolbar
    public void dialogBox(final Activity activity) {
        AlertDialog.Builder builder = new AlertDialog.Builder(new ContextThemeWrapper(activity, R.style.AlertDialogCustom));
        builder.setTitle("Crytpocurrency App");
        builder.setMessage("This application gives you the real time value of the most popular cryptocurrencies. You select " +
                "which cryptocurrency you would want to know the value of and then you select the currency you would like to see " +
                "it displayed in");
        builder.setNegativeButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Toast.makeText(activity,"More information about the app on github.com/kevin-96", Toast.LENGTH_LONG).show();
                dialog.dismiss();
            }
        });
        AlertDialog alert = builder.create();
        alert.show();
    }
    // This will change the color of the background of the app with the color selected from the toolbar
    public void changeBackgroundColor(View layout, int color){
       layout.setBackgroundColor(color);
    }
}

