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

import android.app.DatePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.text.InputType;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

/**
 * Created by Linh on 11/11/16.
 *
 * A date picker for form
 */
public class DatePickerElement extends BaseInputElement<DatePickerElement> {

  private final SimpleDateFormat mDisplayFormat;

  /**
   * Constructor
   * @param name
   * @param labelText
   */
  public DatePickerElement(String name, CharSequence labelText) {
    this(name, labelText, new SimpleDateFormat("MMM d, yyyy", Locale.getDefault()));
  }

  /**
   * Constructor
   * @param name
   * @param labelText
   * @param dateFormat
   */
  public DatePickerElement(String name, CharSequence labelText, SimpleDateFormat dateFormat) {
    super(name, labelText);
    mDisplayFormat = dateFormat;
  }

  private Date mInitDate;

  /**
   * First date value
   * @param date
   * @return
   */
  public DatePickerElement date(Date date) {
    mInitDate = date;
    if (isViewCreated()) {
      mModel.set(mName, mInitDate);
      internalRefresh(getEditText());
    }
    return this;
  }

  private DatePickerDialog mDatePickerDialog = null;

  private void showDatePickerDialog(final Context context) {
    if (mDatePickerDialog == null) {
      Date date = mModel.get(mName);
      if (date == null) {
        date = new Date();
      }

      final TimeZone timeZone = mDisplayFormat.getTimeZone();
      Calendar calendar = Calendar.getInstance(Locale.getDefault());
      calendar.setTimeZone(timeZone);
      calendar.setTime(date);

      mDatePickerDialog = new DatePickerDialog(context, new DatePickerDialog.OnDateSetListener() {
        @Override
        public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
          Calendar calendar = Calendar.getInstance(Locale.getDefault());
          calendar.setTimeZone(timeZone);
          calendar.set(year, monthOfYear, dayOfMonth);
          mModel.set(mName, calendar.getTime());
          internalRefresh(getEditText());

        }
      }, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH));

      mDatePickerDialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
        @Override
        public void onDismiss(DialogInterface dialog) {
          mDatePickerDialog = null;
        }
      });
      mDatePickerDialog.show();
    }
  }

  private void internalRefresh(EditText editText) {
    Date value = mModel.get(mName);
    editText.setText(value != null ? mDisplayFormat.format(value) : "");
  }

  private EditText getEditText() {
    return (EditText) getFieldView();
  }

  @Override
  protected View createFieldView(final Context context) {
    final EditText editText = new EditText(context);

    editText.setSingleLine(true);
    editText.setInputType(InputType.TYPE_CLASS_DATETIME | InputType.TYPE_DATETIME_VARIATION_DATE);
    editText.setKeyListener(null);
    mModel.set(mName, mInitDate);
    internalRefresh(editText);
    editText.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        showDatePickerDialog(context);
      }
    });
    editText.setOnFocusChangeListener(new View.OnFocusChangeListener() {
      @Override
      public void onFocusChange(View v, boolean hasFocus) {
        if (hasFocus) {
          showDatePickerDialog(context);
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
