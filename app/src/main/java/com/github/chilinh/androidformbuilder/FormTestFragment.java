/*
 * MIT License
 *
 * Copyright (c) [2017] [linh]
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
package com.github.chilinh.androidformbuilder;

import android.graphics.Typeface;
import android.support.v4.app.Fragment;
import android.text.InputType;

import com.github.chilinh.android.form.Form;
import com.github.chilinh.android.form.FormFragment;
import com.github.chilinh.android.form.element.ComboBoxElement;
import com.github.chilinh.android.form.element.DatePickerElement;
import com.github.chilinh.android.form.element.EditTextElement;
import com.github.chilinh.android.form.element.SectionElement;
import com.github.chilinh.android.form.element.TextElement;
import com.github.chilinh.android.form.element.TimePickerElement;

import java.util.Calendar;

/**
 * Created by Linh on 1/17/17.
 */
public class FormTestFragment extends FormFragment {

  public static final String TAG = "FormTestFragment";

  @Override
  protected Form createForm() {
    return new Form.Builder()
      .addSection(new SectionElement("Personal"))
      .addElement(new EditTextElement(null, "Name:").placeholder("Your name here").required(true).labelTypeface(Typeface.BOLD))
      .addElement(new ComboBoxElement(null, "Genre", "", "Male", "Female"))
      .addElement(new TextElement(null, "For you:").placeholder("N/A").labelTypeface(Typeface.BOLD))
      .addElement(new EditTextElement(null, null).placeholder("Phone number here").setInputTypeMask(InputType.TYPE_CLASS_PHONE, true).required(true))
      .addSection(new SectionElement("More"))
      .addElement(new DatePickerElement(null, "Birthday: ").date(Calendar.getInstance().getTime()))
      .addElement(new TimePickerElement(null, "Leave: ").time(Calendar.getInstance().getTime()))
      .build(getContext());
  }

  @Override
  public boolean validate(Fragment owner, Form form) {
    return true;
  }

  @Override
  public void onSubmit(Fragment owner, Form form) {
    getFragmentManager().popBackStack();
  }
}
