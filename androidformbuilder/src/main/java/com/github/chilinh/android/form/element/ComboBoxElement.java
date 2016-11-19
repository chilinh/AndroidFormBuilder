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
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;

/**
 * Created by Linh on 11/11/16.
 *
 * A combo box element for form
 */
public class ComboBoxElement extends BaseInputElement<ComboBoxElement> {

  private final List<String> mFields;

  /**
   * Constructor
   * @param name
   * @param labelText
   * @param fields
   */
  public ComboBoxElement(String name, CharSequence labelText, String... fields) {
    this(name, labelText, Arrays.asList(fields));
  }

  /**
   * Constructor
   * @param name
   * @param labelText
   * @param fields
   */
  public ComboBoxElement(String name, CharSequence labelText, List<String> fields) {
    super(name, labelText);
    mFields = Objects.requireNonNull(fields);
  }

  private int mInitIndex;

  /**
   * Set first index selection for combo box when this element is selecting
   * @param index
   * @return
   */
  public ComboBoxElement startIndex(int index) {
    this.mInitIndex = index;
    if (isViewCreated()) {
      mModel.set(mName, mInitIndex);
      internalRefresh(getSpinner());
    }
    return this;
  }

  @Override
  protected View createFieldView(Context context) {
    Spinner view = new Spinner(context);
    view.setPrompt(getLabel());
    ArrayAdapter<String> adapter = new ArrayAdapter<>(context, android.R.layout.simple_spinner_item, mFields);
    adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
    view.setAdapter(adapter);
    mModel.set(mName, mInitIndex);
    internalRefresh(view);
    view.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
      @Override
      public void onItemSelected(AdapterView<?> parent, View view, int pos, long id) {
        mModel.set(mName, pos);
      }

      @Override
      public void onNothingSelected(AdapterView<?> parent) {
      }
    });
    return view;
  }

  @Override
  protected void onModelUpdate() {
    internalRefresh(getSpinner());
  }

  private void internalRefresh(Spinner spinner) {
    int value = mModel.get(mName, 0);
    spinner.setSelection(value);
  }

  private Spinner getSpinner() {
    return ((Spinner) getFieldView());
  }
}
