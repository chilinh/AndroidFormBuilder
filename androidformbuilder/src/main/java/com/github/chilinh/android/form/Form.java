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

package com.github.chilinh.android.form;

import android.content.Context;
import android.content.DialogInterface;
import android.content.res.Resources;
import android.support.v7.app.AlertDialog;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;

import com.github.chilinh.android.form.element.BaseFormElement;
import com.github.chilinh.android.form.element.SectionElement;
import com.github.chilinh.android.form.validator.ElementErrorDisplay;
import com.github.chilinh.android.form.validator.ErrorDisplay;
import com.github.chilinh.android.form.validator.InputError;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Linh on 11/3/16.
 */
public class Form {

  private static final String TAG = "Form";
  private static final boolean DEBUG = true;

  //region Builder
  public static class Builder {
    private ErrorDisplay errorDisplay;

    private CharSequence title;
    private int titleResId = View.NO_ID;

    private CharSequence submitBtn;
    private int submitResId = View.NO_ID;

    private CharSequence cancelBtn;
    private int cancelResId = View.NO_ID;

    public Builder errorDisplay(ErrorDisplay errorDisplay) {
      this.errorDisplay = errorDisplay;
      return this;
    }

    public Builder title(CharSequence title) {
      this.title = title;
      this.titleResId = View.NO_ID;
      return this;
    }

    public Builder title(int titleResId) {
      this.titleResId = titleResId;
      this.title = null;
      return this;
    }

    public Builder submitButton(CharSequence submitBtn) {
      this.submitBtn = submitBtn;
      this.submitResId = View.NO_ID;
      return this;
    }

    public Builder submitButton(int submitResId) {
      this.submitResId = submitResId;
      this.submitBtn = null;
      return this;
    }

    public Builder cancelButton(CharSequence cancelBtn) {
      this.cancelBtn = cancelBtn;
      this.cancelResId = View.NO_ID;
      return this;
    }

    public Builder cancelButton(int cancelResId) {
      this.cancelResId = cancelResId;
      this.cancelBtn = null;
      return this;
    }

    private final Map<String, SectionElement> mSectionMap = new HashMap<>();
    private final List<SectionElement> mSections = new ArrayList<>();

    public Builder addElement(SectionElement section, int position) {
      if (mSectionMap.containsKey(section.getName())) {
        throw new IllegalArgumentException("Section with that name already exists");
      }

      mSectionMap.put(section.getName(), section);
      mSections.add(position, section);
      return this;
    }

    public Builder addSection(SectionElement section) {
      addElement(section, mSections.size());
      return this;
    }

    public Builder addElement(BaseFormElement element) {
      if (mSections.isEmpty()) {
        addSection(new SectionElement());
      }
      SectionElement section = mSections.get(mSections.size() - 1);
      section.addElement(element);
      return this;
    }

    public static CharSequence getCharsequence(Resources res, int resId, CharSequence value) {
      return resId != View.NO_ID ? res.getText(resId) : value;
    }

    public Form build(Context context) {
      Form form = new Form(context.getApplicationContext(), mSectionMap, mSections, errorDisplay);
      Resources res = context.getResources();
      form.mTitle = getCharsequence(res, titleResId, title);
      form.mSubmitBtn = getCharsequence(res, submitResId, submitBtn);
      form.mCancelBtn = getCharsequence(res, cancelResId, cancelBtn);
      return form;
    }
  }

  private Form(Context context, Map<String, SectionElement> sectionMap, List<SectionElement> sections, ErrorDisplay errorDisplay) {
    mContext = context;
    mErrorDisplay = errorDisplay == null ? new ElementErrorDisplay(this) : null;
    mSectionMap = sectionMap;
    mSections = sections;
  }

  private final Context mContext;
  private final Map<String, SectionElement> mSectionMap;
  private final List<SectionElement> mSections;
  private final ErrorDisplay mErrorDisplay;

  private CharSequence mTitle;
  private CharSequence mSubmitBtn;
  private CharSequence mCancelBtn;

  public List<SectionElement> getSections() {
    return Collections.unmodifiableList(mSections);
  }

  public SectionElement getSection(String name) {
    return mSectionMap.get(name);
  }

  public SectionElement getLastSection() {
    return mSections.isEmpty() ? null : mSections.get(mSections.size() - 1);
  }

  public BaseFormElement getElement(String name) {
    for (SectionElement section : mSections) {
      BaseFormElement element = section.getElement(name);
      if (element != null) {
        return element;
      }
    }
    return null;
  }
  //endregion

  //region Validation
  private List<InputError> getErrors() {
    List<InputError> errors = new ArrayList<>();
    for (SectionElement section : mSections) {
      section.validate(errors);
    }
    return errors;
  }

  public void clearValidateError(Context context) {
    mLastestErrors = null;
    mErrorDisplay.clear(context);
  }

  private List<InputError> mLastestErrors;

  public boolean validate(Context context) {
    clearValidateError(context);
    mLastestErrors = getErrors();
    if (mLastestErrors != null && !mLastestErrors.isEmpty()) {
      mErrorDisplay.show(context, mLastestErrors);
      return false;
    }
    return true;
  }

  public List<InputError> getLastErrorList() {
    return mLastestErrors;
  }
  //endregion

  private void makeFormView(ViewGroup containerView) {
    containerView.removeAllViews();

    for (SectionElement section : getSections()) {
      section.setModel(mModel);
      section.makeView(containerView);
    }
  }

  private FormModel mModel;

  public FormModel getModel() {
    return mModel;
  }

  private void applyWindow(Window window) {
    window.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE | WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
  }

  //public Fragment buildFragment(Context context, FragmentManager fm) {
  //  return null;
  //}

  public AlertDialog buildDialog(Context context, final Callback.Submit<DialogInterface> submitCb, final Callback.Dismiss<DialogInterface> dismissCb) {
    mModel = new FormModelDialog();

    if (TextUtils.isEmpty(mSubmitBtn)) {
      mSubmitBtn = "Summit";
    }

    TextView title = new TextView(context);
    title.setText(mTitle);
    title.setPadding(10, 10, 10, 15);
    title.setGravity(Gravity.CENTER);
    title.setTextSize(22);

    View view = LayoutInflater.from(context).inflate(R.layout.base_form, null, false);
    ViewGroup group = (ViewGroup) view.findViewById(R.id.form_elements_container);
    makeFormView(group);
    AlertDialog.Builder builder = new AlertDialog.Builder(context)
      .setCustomTitle(title)
      .setView(view)
      .setPositiveButton(mSubmitBtn, null)
      .setNegativeButton(mCancelBtn, null)
      ;

    final AlertDialog alertDialog = builder.create();
    applyWindow(alertDialog.getWindow());

    alertDialog.setOnShowListener(
      new DialogInterface.OnShowListener() {
        @Override
        public void onShow(final DialogInterface dialog) {
          Button positive = alertDialog.getButton(AlertDialog.BUTTON_POSITIVE);
          if (positive != null) {
            positive.setOnClickListener(
              new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                  if (validate(mContext)) {
                    if (submitCb != null) {
                      if (submitCb.validate(dialog, Form.this)) {
                        submitCb.onSubmit(dialog, Form.this);
                      }
                      return;
                    }

                    dialog.dismiss();
                  }
                }
              }
            );
          }
        }
      }
    );

    alertDialog.setOnDismissListener(
      new DialogInterface.OnDismissListener() {
        @Override
        public void onDismiss(DialogInterface dialog) {
          if (dismissCb != null) {
            dismissCb.onDismiss(dialog, Form.this);
          }
        }
      }
    );

    alertDialog.setOnCancelListener(
      new DialogInterface.OnCancelListener() {
        @Override
        public void onCancel(DialogInterface dialog) {
          if (dismissCb != null) {
            dismissCb.onCancel(dialog, Form.this);
          }
        }
      }
    );
    return alertDialog;
  }
}
