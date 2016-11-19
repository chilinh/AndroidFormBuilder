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
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.github.chilinh.android.form.FormModel;
import com.github.chilinh.android.form.R;

/**
 * Created by Linh on 11/11/16.
 *
 * A simple text element to display label-content
 */
public class TextElement extends BaseFormElement<TextElement> {

  public TextElement(String name, CharSequence labelText) {
    super(name);
    mLabelText = labelText;
  }

  private CharSequence mValue;

  /**
   * Display value
   * @param value
   * @return
   */
  public TextElement value(CharSequence value) {
    mValue = value;
    if (isViewCreated()) {
      mValueView.setText(mValue);
    }
    return this;
  }

  private CharSequence mPlaceholder;

  /**
   * Placeholder when value is empty
   * @param placeholder
   * @return
   */
  public TextElement placeholder(CharSequence placeholder) {
    mPlaceholder = placeholder;
    if (isViewCreated()) {
      mValueView.setHint(mPlaceholder);
    }
    return this;
  }

  private int mLabelTypeface;

  /**
   * Set type face for label
   * @param typeface
   * @return
   */
  public TextElement labelTypeface(int typeface) {
    mLabelTypeface = typeface;
    if (isViewCreated()) {
      TextView label = (TextView) getView().findViewById(R.id.field_label);
      applyTypeface(label, mLabelTypeface);
    }
    return this;
  }

  private int mValueTypeface;

  /**
   * Set typeface for value
   * @param typeface
   * @return
   */
  public TextElement valueTypeface(int typeface) {
    mValueTypeface = typeface;
    if (isViewCreated()) {
      applyTypeface(mValueView, mValueTypeface);
    }
    return this;
  }

  private final CharSequence mLabelText;
  private TextView mValueView;

  private void internalRefresh(TextView view) {
    CharSequence value = mModel.get(mName, mValue);
    view.setText(value);
  }

  @Override
  protected View createView(Context context) {
    View view = LayoutInflater.from(context).inflate(R.layout.form_value_element, null);

    TextView label = (TextView) view.findViewById(R.id.field_label);
    applyTypeface(label, mLabelTypeface);
    setText(label, mLabelText);

    mValueView = (TextView) view.findViewById(R.id.field_value);
    mValueView.setHint(mPlaceholder);
    applyTypeface(mValueView, mValueTypeface);
    internalRefresh(mValueView);
    return view;
  }

  @Override
  protected void onSetModel(FormModel model) {
  }

  @Override
  protected void onModelUpdate() {
    internalRefresh(mValueView);
  }

  @Override
  protected void onViewMake(ViewGroup container) {
  }

  @Override
  public void setError(CharSequence message) {
  }
}
