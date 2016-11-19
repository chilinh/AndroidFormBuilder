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
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.github.chilinh.android.form.FormModel;
import com.github.chilinh.android.form.R;
import com.github.chilinh.android.form.validator.InputError;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Linh on 11/3/16.
 *
 * A form section
 */
public final class SectionElement extends BaseFormElement<SectionElement> {

  //region Constructor
  /**
   * Constructor
   * @param name
   * @param title
   */
  public SectionElement(String name, CharSequence title) {
    super(name);
    this.mTitle = title;
  }

  /**
   * Constructor
   * @param title
   */
  public SectionElement(CharSequence title) {
    this(null, title);
  }

  /**
   * Constructor
   */
  public SectionElement() {
    this(null, null);
  }
  //endregion

  //region Get-Set
  private final CharSequence mTitle;
  private final Map<String, BaseFormElement> mElemMap = new HashMap<>();
  private final List<BaseFormElement> mElements = new ArrayList<>();

  /**
   * Method to add element to this section at specific position
   * @param element
   * @param position
   * @return
   */
  public SectionElement addElement(BaseFormElement element, int position) {
    if (element instanceof SectionElement) {
      throw new IllegalArgumentException("Sub-sections are not supported");
    }

    if (mElemMap.containsKey(element.getName())) {
      throw new IllegalArgumentException("Element with that name already exists");
    }

    element.setModel(mModel);
    mElemMap.put(element.getName(), element);
    mElements.add(position, element);
    return this;
  }

  /**
   * Method to append element to this section
   * @param element
   * @return
   */
  public SectionElement addElement(BaseFormElement element) {
    return addElement(element, mElements.size());
  }

  /**
   * Method to add a list of elements to this section
   * @param values
   * @return
   */
  public SectionElement addElements(Collection<BaseFormElement> values) {
    for (BaseFormElement element : values) {
      addElement(element);
    }
    return this;
  }

  /**
   * Method to remove element form this section by name
   * @param name
   * @return
   */
  public BaseFormElement removeElement(String name) {
    BaseFormElement element = mElemMap.remove(name);
    mElements.remove(element);
    element.setModel(null);
    return element;
  }

  /**
   * Method to remove element form this section
   * @param element
   * @return
   */
  public BaseFormElement removeElement(BaseFormElement element) {
    return removeElement(element.getName());
  }

  /**
   * Method to get element form this section by name
   * @param name
   * @return
   */
  public BaseFormElement getElement(String name) {
    return mElemMap.get(name);
  }

  /**
   * Method to get element form this section by index
   * @param i
   * @return
   */
  public BaseFormElement getElement(int i) {
    return mElements.get(i);
  }

  /**
   * Method to get list elements of this section
   * @return
   */
  public List<BaseFormElement> getElements() {
    return Collections.unmodifiableList(mElements);
  }

  /**
   * Method to get number of element in this section
   * @return
   */
  public int elementCount() {
    return mElements.size();
  }

  /**
   * @return
   */
  public CharSequence getTitle() {
    return mTitle;
  }

  /**
   * Method to validate all elements in this section
   * @param list
   * @return
   */
  public List<InputError> validate(List<InputError> list) {
    list = list == null ? new ArrayList<InputError>() : list;
    for (BaseFormElement element : mElements) {
      if (element instanceof BaseInputElement) {
        BaseInputElement field = (BaseInputElement) element;
        list.addAll(field.validateInput());
      }
    }
    return list;
  }

  /**
   * Method to clear error message from all elements of this section
   */
  public void clearError() {
    for (BaseFormElement elem : mElements) {
      elem.setError(null);
    }
  }
  //endregion

  private TextView mErrorView;

  //region Implementation
  @Override
  protected View createView(Context context) {
    LayoutInflater layoutInflater = LayoutInflater.from(context);
    View view = layoutInflater.inflate(R.layout.form_section, null);
    view.setOnClickListener(null);
    view.setOnLongClickListener(null);
    view.setLongClickable(false);

    mErrorView = (TextView) view.findViewById(R.id.field_error);

    if (!TextUtils.isEmpty(getTitle())) {
      final TextView sectionView = (TextView) view.findViewById(R.id.list_item_section_text);
      sectionView.setText(mTitle);
      sectionView.setVisibility(View.VISIBLE);
    }

    return view;
  }

  @Override
  protected void onSetModel(FormModel model) {
    for (BaseFormElement element : mElements) {
      element.setModel(model);
    }
  }

  @Override
  public void onModelUpdate() {
  }

  @Override
  public void setError(CharSequence message) {
    setText(mErrorView, message);
  }

  @Override
  protected void onViewMake(ViewGroup container) {
    ViewGroup viewGroup = (ViewGroup) mView.findViewById(R.id.field_section);
    viewGroup.removeAllViews();
    for (BaseFormElement element : mElements) {
      element.makeView(viewGroup);
    }
  }
  //endregion
}
