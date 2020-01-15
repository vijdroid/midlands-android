package com.chris.thomson.midlandsriders.Helpers;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.util.Log;


public class AlertHelper extends DialogFragment {
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        AlertDialog.Builder builder =
                new AlertDialog.Builder(getActivity());

        builder.setMessage("¿?")
                .setTitle("Confirmation")
                .setPositiveButton("Ok", new DialogInterface.OnClickListener()  {
                    public void onClick(DialogInterface dialog, int id) {
                        Log.i("Dialogos", " msj.");
                        dialog.cancel();
                    }
                })
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        Log.i("Dialogos", "Msj.");
                        dialog.cancel();
                    }
                });

        return builder.create();
    }

}
