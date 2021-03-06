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

import android.content.res.Resources;
import android.text.TextUtils;

import com.github.chilinh.android.form.R;
import com.github.chilinh.android.form.element.BaseInputElement;

/**
 * Created by Linh on 11/3/16.
 * A required validator for {@link BaseInputElement}
 */
public class RequiredValidator implements InputValidator {
  public static class RequiredError extends InputError {
    public RequiredError(BaseInputElement element) {
      super(element);
    }

    @Override
    public String getMessage(Resources resources) {
      return TextUtils.isEmpty(fieldLabel) ? resources.getString(R.string.required_without_name_error_message) : String.format(resources.getString(R.string.required_error_message), fieldLabel);
    }
  }

  @Override
  public InputError validate(Object value, BaseInputElement element) {
    if (TextUtils.isEmpty((String) value)) {
      return new RequiredError(element);
    }
    return null;
  }
}
