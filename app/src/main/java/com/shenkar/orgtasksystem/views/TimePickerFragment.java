package com.shenkar.orgtasksystem.views;


import android.app.Dialog;
import android.app.TimePickerDialog;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.text.format.DateFormat;
import android.widget.TextView;
import android.widget.TimePicker;

import com.shenkar.orgtasksystem.R;

import java.util.Calendar;

/**
 * Created by david on 16/12/2015.
 */
public class TimePickerFragment extends DialogFragment
        implements TimePickerDialog.OnTimeSetListener {

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        // Use the current time as the default values for the picker
        final Calendar c = Calendar.getInstance();
        int hour = c.get(Calendar.HOUR_OF_DAY);
        int minute = c.get(Calendar.MINUTE);

        // Create a new instance of TimePickerDialog and return it
        return new TimePickerDialog(getActivity(), this, hour, minute,
                DateFormat.is24HourFormat(getActivity()));
    }

    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
        // Do something with the time chosen by the user
        TextView textview = (TextView)getActivity().findViewById(R.id.tvTime);
        String hourString;
        if (hourOfDay < 10)
            hourString = "0" + hourOfDay;
        else
            hourString = "" +hourOfDay;

        String minuteSting;
        if (minute < 10)
            minuteSting = "0" + minute;
        else
            minuteSting = "" +minute;
        textview.setText(hourString+":"+minuteSting);
    }
}
