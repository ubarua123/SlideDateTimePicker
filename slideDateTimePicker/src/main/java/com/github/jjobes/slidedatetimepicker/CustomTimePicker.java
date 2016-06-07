package com.github.jjobes.slidedatetimepicker;

import java.lang.reflect.Field;

import android.content.Context;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.util.Log;
import android.widget.NumberPicker;
import android.widget.TimePicker;

/**
 * A subclass of {@link android.widget.TimePicker} that uses
 * reflection to allow for customization of the default blue
 * dividers.
 *
 * @author jjobes
 *
 */
public class CustomTimePicker extends TimePicker
{
    private static final String TAG = "CustomTimePicker";

    public CustomTimePicker(Context context, AttributeSet attrs) {
        super(context, attrs);
        Log.e(TAG, "In constructor");

        changeDivider(Color.RED);
    }

    private void changeDivider(int color) {
        Class<?> idClass = null;
        Class<?> numberPickerClass = null;
        Field selectionDividerField = null;
        Field hourField = null;
        Field minuteField = null;
        Field amPmField = null;
        NumberPicker hourNumberPicker = null;
        NumberPicker minuteNumberPicker = null;
        NumberPicker amPmNumberPicker = null;

        try
        {
            // Create an instance of the id class
            idClass = Class.forName("com.android.internal.R$id");

            // Get the fields that store the resource IDs for the hour, minute and amPm NumberPickers
            hourField = idClass.getField("hour");
            minuteField = idClass.getField("minute");
            amPmField = idClass.getField("amPm");

            // Use the resource IDs to get references to the hour, minute and amPm NumberPickers
            hourNumberPicker = (NumberPicker) findViewById(hourField.getInt(null));
            minuteNumberPicker = (NumberPicker) findViewById(minuteField.getInt(null));
            amPmNumberPicker = (NumberPicker) findViewById(amPmField.getInt(null));

            numberPickerClass = Class.forName("android.widget.NumberPicker");

            // Set the value of the mSelectionDivider field in the hour, minute and amPm NumberPickers
            // to refer to our custom drawables
            selectionDividerField = numberPickerClass.getDeclaredField("mSelectionDivider");
            selectionDividerField.setAccessible(true);
            Drawable divider = getResources().getDrawable(R.drawable.selection_divider);
            divider.setColorFilter(color, PorterDuff.Mode.SRC_IN);
            selectionDividerField.set(hourNumberPicker, divider);
            selectionDividerField.set(minuteNumberPicker, divider);
            selectionDividerField.set(amPmNumberPicker, divider);
        }
        catch (ClassNotFoundException e)
        {
            Log.e(TAG, "ClassNotFoundException in CustomTimePicker", e);
        }
        catch (NoSuchFieldException e)
        {
            Log.e(TAG, "NoSuchFieldException in CustomTimePicker", e);
        }
        catch (IllegalAccessException e)
        {
            Log.e(TAG, "IllegalAccessException in CustomTimePicker", e);
        }
        catch (IllegalArgumentException e)
        {
            Log.e(TAG, "IllegalArgumentException in CustomTimePicker", e);
        }
    }

    public void setColor(int color) {
        changeDivider(color);
        invalidate();
    }
}
