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
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.github.chilinh.android.form.FormModel;

import java.util.UUID;

/**
 * Created by Linh on 11/3/16.
 *
 * Base class for form element
 */
public abstract class BaseFormElement<T extends BaseFormElement> {

  /**
   * Method to construct view for form element
   * @param context
   * @return view of this element
   */
  protected abstract View createView(Context context);

  /**
   * Callback when form model is set
   * @param model data of a form that owns this element
   */
  protected abstract void onSetModel(FormModel model);

  /**
   * Callback like {@link #onSetModel(FormModel)} but it will be called only if view of
   * this element has been created
   */
  protected abstract void onModelUpdate();

  /**
   * Callback when view of this element has been attached to container
   * @param container
   */
  protected abstract void onViewMake(ViewGroup container);

  /**
   * Method will be call when error occur
   * @param message
   */
  public abstract void setError(CharSequence message);

  protected final String mName;
  protected FormModel mModel;
  protected View mView;

  /**
   * Constructor of form element
   * @param name of this element, if null name will be auto generated
   */
  protected BaseFormElement(String name) {
    this.mName = TextUtils.isEmpty(name) ? UUID.randomUUID().toString() : name;
  }

  /**
   * Method to set model to this element
   * @param model
   * @return builder
   */
  public final T setModel(FormModel model) {
    this.mModel = model;
    onSetModel(model);
    if (isViewCreated()) {
      onModelUpdate();
    }
    return (T) this;
  }

  /**
   * @return name of this element
   */
  public final String getName() {
    return mName;
  }

  /**
   * @return true if view has been created
   */
  public final boolean isViewCreated() {
    return mView != null;
  }

  /**
   * @return view of this element
   */
  public final View getView() {
    return mView;
  }

  /**
   * Method to create and attach element's view to container
   * @param container
   */
  public final void makeView(ViewGroup container) {
    if (mView == null) {
      mView = createView(container.getContext());
    }
    container.addView(mView);
    onViewMake(container);
  }

  /**
   * Helper method to set style to text view
   * @param view the text view will be applied
   * @param typeface style to set
   */
  static void applyTypeface(TextView view, int typeface) {
    view.setTypeface(view.getTypeface(), typeface);
  }

  /**
   * Helper method to set text to text view
   * If text is null or empty, text view will be set to GONE
   * @param view the text view will be applied
   * @param text to set
   */
  static void setText(TextView view, CharSequence text) {
    if (TextUtils.isEmpty(text)) {
      view.setVisibility(View.GONE);
    } else {
      view.setText(text);
      view.setVisibility(View.VISIBLE);
    }
  }
}
