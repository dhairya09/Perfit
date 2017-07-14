package com.example.dhairyapujara.perfit_submit;

/**
 * Created by Dhairya Pujara on 04-03-2017.
 */

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;

public class AlertDialogManager
{
    public void showAlertDialog(Context context,String title,String message,Boolean status)
    {
        AlertDialog alertDialog = new AlertDialog.Builder(context).create();
        alertDialog.setTitle(title);
        alertDialog.setMessage(message);
        if(status!=null)
        {
            //alertDialog.setIcon((status)?R.drawable.ic_profile:R.drawable.bh_splash);

        }
        alertDialog.setButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

            }
        });
        alertDialog.show();

    }
}