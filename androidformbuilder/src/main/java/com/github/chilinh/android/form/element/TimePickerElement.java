/*
 * MIT License
 *
 * Copyright (c) [2016] [linh]
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */

package com.github.chilinh.android.form.element;

import android.app.TimePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.text.InputType;
import android.view.View;
import android.widget.EditText;
import android.widget.TimePicker;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

/**
 * Created by Linh on 11/11/16.
 *
 * A time picker for form
 */
public class TimePickerElement extends BaseInputElement<TimePickerElement> {

  private final SimpleDateFormat mDisplayFormat;
  private final boolean mIs24Hour;

  /**
   * Constructor
   * @param name
   * @param labelText
   */
  public TimePickerElement(String name, CharSequence labelText) {
    this(name, labelText, new SimpleDateFormat("hh:mm a", Locale.getDefault()), false);
  }

  /**
   * Constructor
   * @param name
   * @param labelText
   * @param timeFormat
   * @param is24Hour
   */
  public TimePickerElement(String name, CharSequence labelText, SimpleDateFormat timeFormat, boolean is24Hour) {
    super(name, labelText);
    mDisplayFormat = timeFormat;
    mIs24Hour = is24Hour;
  }

  private Date mInitTime;

  /**
   * Start time for element
   * @param time
   * @return
   */
  public TimePickerElement time(Date time) {
    mInitTime = time;
    if (isViewCreated()) {
      mModel.set(mName, mInitTime);
      internalRefresh(getEditText());
    }
    return this;
  }

  private TimePickerDialog mTimePickerDialog = null;

  private void showTimePickerDialog(Context context) {
    if (mTimePickerDialog == null) {
      Date date = mModel.get(mName);
      if (date == null) {
        date = new Date();
      }

      final TimeZone timeZone = mDisplayFormat.getTimeZone();
      Calendar calendar = Calendar.getInstance(Locale.getDefault());
      calendar.setTimeZone(timeZone);
      calendar.setTime(date);
      mTimePickerDialog = new TimePickerDialog(context, new TimePickerDialog.OnTimeSetListener() {
        @Override
        public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
          Calendar calendar = Calendar.getInstance(Locale.getDefault());
          calendar.setTimeZone(timeZone);
          calendar.set(Calendar.HOUR_OF_DAY, hourOfDay);
          calendar.set(Calendar.MINUTE, minute);
          mModel.set(mName, calendar.getTime());
          internalRefresh(getEditText());
        }
      }, calendar.get(Calendar.HOUR_OF_DAY), calendar.get(Calendar.MINUTE), mIs24Hour);
      mTimePickerDialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
        @Override
        public void onDismiss(DialogInterface dialog) {
          mTimePickerDialog = null;
        }
      });
      mTimePickerDialog.show();
    }
  }

  private void internalRefresh(EditText editText) {
    Date value = mModel.get(mName, mInitTime);
    editText.setText(value != null ? mDisplayFormat.format(value) : "");
  }

  private EditText getEditText() {
    return (EditText) getFieldView();
  }

  @Override
  protected View createFieldView(final Context context) {
    final EditText editText = new EditText(context);
    editText.setSingleLine(true);
    editText.setInputType(InputType.TYPE_CLASS_DATETIME | InputType.TYPE_DATETIME_VARIATION_TIME);
    editText.setKeyListener(null);
    mModel.set(mName, mInitTime);
    internalRefresh(editText);
    editText.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        showTimePickerDialog(context);
      }
    });
    editText.setOnFocusChangeListener(new View.OnFocusChangeListener() {
      @Override
      public void onFocusChange(View v, boolean hasFocus) {
        if (hasFocus) {
          showTimePickerDialog(context);
        }
      }
    });
    return editText;
  }

  @Override
  protected void onModelUpdate() {
    internalRefresh(getEditText());
  }
}
