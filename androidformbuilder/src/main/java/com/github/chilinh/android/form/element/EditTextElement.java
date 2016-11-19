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

import android.content.Context;
import android.graphics.Color;
import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;

/**
 * Created by Linh on 11/3/16.
 *
 * An editable text element for form
 */
public class EditTextElement extends BaseInputElement<EditTextElement> {

  private static final String TAG = "EditTextElement";
  private static final boolean DEBUG = true;

  /**
   * Constructor
   * @param name
   * @param labelText
   */
  public EditTextElement(String name, String labelText) {
    super(name, labelText);
  }

  private CharSequence mInitValue;

  /**
   * First text value
   * @param text
   * @return
   */
  public EditTextElement text(CharSequence text) {
    mInitValue = text;
    if (isViewCreated()) {
      mModel.set(mName, mInitValue);
      internalRefresh(getEditText());
    }
    return this;
  }

  private CharSequence mPlaceholder;

  /**
   * Placeholder when field is empty
   * @param placeholder
   * @return
   */
  public EditTextElement placeholder(CharSequence placeholder) {
    mPlaceholder = placeholder;
    if (isViewCreated()) {
      getEditText().setHint(mPlaceholder);
    }
    return this;
  }

  private int inputType = InputType.TYPE_CLASS_TEXT;

  /**
   * Make field displays in multi lines
   * @param multiLine
   * @return
   */
  public EditTextElement multiLine(boolean multiLine) {
    return setInputTypeMask(InputType.TYPE_TEXT_FLAG_MULTI_LINE, multiLine);
  }

  /**
   * Make text view input type as password
   * @param pass
   * @return
   */
  public EditTextElement password(boolean pass) {
    return setInputTypeMask(InputType.TYPE_TEXT_VARIATION_PASSWORD, pass);
  }

  /**
   * Set custom type mask for edit text element
   * @param mask
   * @param enabled
   * @return
   */
  public EditTextElement setInputTypeMask(int mask, boolean enabled) {
    if (enabled) {
      inputType = inputType | mask;
    } else {
      inputType = inputType & ~mask;
    }
    if (isViewCreated()) {
      getEditText().setInputType(inputType);
    }
    return this;
  }

  //region Implement
  @Override
  protected View createFieldView(Context context) {
    final EditText editText = new EditText(context);
    editText.setSingleLine(!isMultiLine());
    editText.setHint(mPlaceholder);
    editText.setHintTextColor(Color.GRAY);
    editText.setInputType(inputType);
    mModel.set(mName, mInitValue);
    internalRefresh(editText);
    editText.addTextChangedListener(
      new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
        }

        @Override
        public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
        }

        @Override
        public void afterTextChanged(Editable editable) {
          mModel.set(mName, editText.getText().toString());
        }
      }
    );
    return editText;
  }

  @Override
  public void onModelUpdate() {
    internalRefresh(getEditText());
  }
  //endregion

  private boolean isMultiLine() {
    return (inputType | InputType.TYPE_TEXT_FLAG_MULTI_LINE) != 0;
  }

  private boolean isPassword() {
    return (inputType | InputType.TYPE_TEXT_VARIATION_PASSWORD) != 0;
  }

  private EditText getEditText() {
    return (EditText) getFieldView();
  }

  private void internalRefresh(EditText editText) {
    CharSequence valueStr = mModel.get(mName);
    CharSequence currentStr = editText.getText();
    if ((currentStr == null && valueStr != currentStr) || (currentStr != null && !currentStr.equals(valueStr))) {
      //if (TextUtils.isEmpty(mDisplayFormat)) {
        editText.setText(valueStr);
      //} else {
      //  editText.setText(String.format(mDisplayFormat, valueStr));
      //}
    }
  }
}
