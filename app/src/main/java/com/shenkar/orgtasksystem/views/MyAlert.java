package com.shenkar.orgtasksystem.views;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.content.res.Resources;
import android.os.Bundle;
import android.widget.Toast;

import com.shenkar.orgtasksystem.R;

/**
 * Created by david on 05/06/2015.
 */
public class MyAlert extends DialogFragment {

    private int min;

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        final MainActivity activity = (MainActivity) getActivity();
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle("Set Sync Interval");
        builder.setSingleChoiceItems(R.array.minutes, 0, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                Resources res = getResources();
                String[] minutes = res.getStringArray(R.array.minutes);
                min = Integer.parseInt(minutes[i]);

            }
        });
        builder.setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });
        builder.setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Toast.makeText(getActivity(), min + " was selected ", Toast.LENGTH_SHORT).show();
                activity.setSyncMin(min);
            }
        });

        Dialog dialog = builder.create();
        return dialog;


    }
}
