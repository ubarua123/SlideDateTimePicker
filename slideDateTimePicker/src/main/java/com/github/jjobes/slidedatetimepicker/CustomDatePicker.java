package com.github.jjobes.slidedatetimepicker;

import java.lang.reflect.Field;

import android.content.Context;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.util.Log;
import android.widget.DatePicker;
import android.widget.NumberPicker;

/**
 * A subclass of {@link android.widget.DatePicker} that uses
 * reflection to allow for customization of the default blue
 * dividers.
 *
 * @author jjobes
 *
 */
public class CustomDatePicker extends DatePicker
{
    private static final String TAG = "CustomDatePicker";

    public CustomDatePicker(Context context, AttributeSet attrs)
    {
        super(context, attrs);

        changeDivider(Color.RED);
    }

    private void changeDivider(int color) {
        Class<?> idClass = null;
        Class<?> numberPickerClass = null;
        Field selectionDividerField = null;
        Field monthField = null;
        Field dayField = null;
        Field yearField = null;
        NumberPicker monthNumberPicker = null;
        NumberPicker dayNumberPicker = null;
        NumberPicker yearNumberPicker = null;

        try
        {
            // Create an instance of the id class
            idClass = Class.forName("com.android.internal.R$id");

            // Get the fields that store the resource IDs for the month, day and year NumberPickers
            monthField = idClass.getField("month");
            dayField = idClass.getField("day");
            yearField = idClass.getField("year");

            // Use the resource IDs to get references to the month, day and year NumberPickers
            monthNumberPicker = (NumberPicker) findViewById(monthField.getInt(null));
            dayNumberPicker = (NumberPicker) findViewById(dayField.getInt(null));
            yearNumberPicker = (NumberPicker) findViewById(yearField.getInt(null));

            numberPickerClass = Class.forName("android.widget.NumberPicker");

            // Set the value of the mSelectionDivider field in the month, day and year NumberPickers
            // to refer to our custom drawables
            selectionDividerField = numberPickerClass.getDeclaredField("mSelectionDivider");
            selectionDividerField.setAccessible(true);
            Drawable divider = getResources().getDrawable(R.drawable.selection_divider);
            divider.setColorFilter(color, PorterDuff.Mode.SRC_IN);
            selectionDividerField.set(monthNumberPicker, divider);
            selectionDividerField.set(dayNumberPicker, divider);
            selectionDividerField.set(yearNumberPicker, divider);

        }
        catch (ClassNotFoundException e)
        {
            Log.e(TAG, "ClassNotFoundException in CustomDatePicker", e);
        }
        catch (NoSuchFieldException e)
        {
            Log.e(TAG, "NoSuchFieldException in CustomDatePicker", e);
        }
        catch (IllegalAccessException e)
        {
            Log.e(TAG, "IllegalAccessException in CustomDatePicker", e);
        }
        catch (IllegalArgumentException e)
        {
            Log.e(TAG, "IllegalArgumentException in CustomDatePicker", e);
        }
    }

    public void setColor(int color) {
        changeDivider(color);
        invalidate();
    }
}
