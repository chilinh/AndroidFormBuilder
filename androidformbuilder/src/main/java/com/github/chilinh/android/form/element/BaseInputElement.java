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
import android.widget.EditText;
import android.widget.TextView;

import com.github.chilinh.android.form.FormModel;
import com.github.chilinh.android.form.R;
import com.github.chilinh.android.form.validator.InputError;
import com.github.chilinh.android.form.validator.InputValidator;
import com.github.chilinh.android.form.validator.RequiredValidator;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Created by Linh on 11/3/16.
 *
 * Base element to input element
 */
public abstract class BaseInputElement<T extends BaseInputElement> extends BaseFormElement<T> {

  private static final String TAG = "BaseInputElement";
  private static final boolean DEBUG = true;

  /**
   * Constructor
   * @param name name of this element
   * @param labelText the text that will be set as label of this element
   */
  public BaseInputElement(String name, CharSequence labelText) {
    super(name);
    this.mLabelText = labelText;
  }

  private static final RequiredValidator REQUIRED_VALIDATOR = new RequiredValidator();
  private final Set<InputValidator> mValidators = new HashSet<>();

  private boolean mLabelVertical;

  /**
   * Method to set element using vertical label
   * @param vertical
   * @return builder
   */
  public T verticalLabel(boolean vertical) {
    mLabelVertical = vertical;
    if (isViewCreated()) {
      applyTitle();
    }
    return (T) this;
  }

  private int mLabelTypeface;

  /**
   * Method to set style (as normal, bold, italic,...) of label
   * @param typeface
   * @return
   */
  public T labelTypeface(int typeface) {
    mLabelTypeface = typeface;
    if (isViewCreated()) {
      applyTitle();
    }
    return (T) this;
  }

  private boolean mUseNativeErrorEditView = true;

  /**
   * Method to make error message using or not using build in error edit view display
   * if this element using edit view as display value field
   * @param useNative
   * @return
   */
  public T useNativeErrorEditView(boolean useNative) {
    this.mUseNativeErrorEditView = useNative;
    return (T) this;
  }

  /**
   * Method to make this element is required when submit
   * @param required
   * @return
   */
  public T required(boolean required) {
    if (!required) {
      mValidators.remove(REQUIRED_VALIDATOR);
    } else if (!isRequired()) {
      mValidators.add(REQUIRED_VALIDATOR);
    }
    return (T) this;
  }

  /**
   * Method to add custom validator for this element
   * @param validator
   * @return
   */
  public T addValidator(InputValidator validator) {
    mValidators.add(validator);
    return (T) this;
  }

  /**
   * Method to add collection of validator for this element
   * @param newValidators
   * @return
   */
  public T addValidatorSet(Collection<InputValidator> newValidators) {
    if (newValidators != null) {
      mValidators.addAll(newValidators);
    }
    return (T) this;
  }

  /**
   * Method to remove custom validator
   * @param validator
   * @return
   */
  public T removeValidator(InputValidator validator) {
    mValidators.remove(validator);
    return (T) this;
  }

  private final CharSequence mLabelText;
  private View mFieldView;
  private TextView mErrorView;
  private TextView mVerticalLabel;
  private TextView mHorizontalLabel;

  /**
   * @return label text
   */
  public CharSequence getLabel() {
    return mLabelText;
  }

  /**
   * @return true if element is required
   */
  public boolean isRequired() {
    return mValidators.contains(REQUIRED_VALIDATOR);
  }

  /**
   * Method to validate element value
   * @return list of error
   */
  public List<InputError> validateInput() {
    List<InputError> errors = new ArrayList<>();
    Object value = mModel.get(mName);
    for (InputValidator validator : mValidators) {
      InputError error = validator.validate(value, this);
      if (error != null) {
        errors.add(error);
      }
    }
    return errors;
  }

  /**
   * Method to create field view for this input element
   * @param context
   * @return
   */
  protected abstract View createFieldView(Context context);

  /**
   * Method to get current field view
   * @return
   */
  public View getFieldView() {
    return mFieldView;
  }

  @Override
  protected final View createView(Context context) {
    View view = LayoutInflater.from(context).inflate(R.layout.form_input_element, null);
    mErrorView = (TextView) view.findViewById(R.id.field_error);

    mVerticalLabel = (TextView) view.findViewById(R.id.field_label_vertical);
    mHorizontalLabel = (TextView) view.findViewById(R.id.field_label_horizontal);
    applyTitle();

    ViewGroup container = (ViewGroup) view.findViewById(R.id.field_container);
    mFieldView = createFieldView(context);
    container.addView(mFieldView);
    return view;
  }

  @Override
  protected void onSetModel(FormModel model) {
  }

  @Override
  protected void onViewMake(ViewGroup container) {
  }

  @Override
  public void setError(CharSequence message) {
    View view = getFieldView();
    if (mUseNativeErrorEditView && (view instanceof EditText)) {
      ((EditText) view).setError(message);
    } else {
      setText(mErrorView, message);
    }
  }

  private void applyTitle() {
    if (mLabelVertical) {
      mHorizontalLabel.setVisibility(View.GONE);
      mVerticalLabel.setVisibility(View.VISIBLE);
      applyTypeface(mVerticalLabel, mLabelTypeface);
      setText(mVerticalLabel, mLabelText);
    } else {
      mVerticalLabel.setVisibility(View.GONE);
      mHorizontalLabel.setVisibility(View.VISIBLE);
      applyTypeface(mHorizontalLabel, mLabelTypeface);
      setText(mHorizontalLabel, mLabelText);
    }
  }
}
