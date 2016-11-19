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

package com.github.chilinh.android.form.validator;

import android.content.Context;
import android.util.Log;

import com.github.chilinh.android.form.Form;
import com.github.chilinh.android.form.element.BaseFormElement;
import com.github.chilinh.android.form.element.SectionElement;

import java.util.List;

/**
 * Created by Linh on 11/3/16.
 * Simple implement of {@link ErrorDisplay}
 */
public class ElementErrorDisplay implements ErrorDisplay {

  private static final String TAG = "ElementErrorDisplay";
  private static final boolean DEBUG = false;

  private final Form mForm;

  public ElementErrorDisplay(Form form) {
    mForm = form;
  }

  @Override
  public void show(Context context, List<InputError> errors) {
    for (InputError error : errors) {
      if (DEBUG) {
        Log.d(TAG, "show: " + error.fieldName + " " + error.fieldLabel);
      }
      BaseFormElement element = mForm.getElement(error.fieldName);
      element.setError(error.getMessage(context.getResources()));
    }
  }

  @Override
  public void clear(Context context) {
    for (SectionElement section: mForm.getSections()) {
      section.clearError();
    }
  }
}
