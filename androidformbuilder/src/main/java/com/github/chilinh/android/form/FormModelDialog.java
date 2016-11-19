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

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.Observable;
import java.util.Observer;

/**
 * Created by Linh on 11/10/16.
 */
public final class FormModelDialog implements FormModel {
  private final Observable mObservable = new Observable();
  private final Map<String, Object> mData = new HashMap<>();

  @Override
  public <T> T get(String name, T defaultIfEmpty) {
    if (mData.containsKey(name)) {
      return (T) mData.get(name);
    }
    return defaultIfEmpty;
  }

  @Override
  public <T> T get(String name) {
    return get(name, null);
  }

  @Override
  public FormModel set(String name, Object value) {
    Object old = null;
    if (mData.containsKey(name)) {
      old = mData.get(name);
    }
    if (!Objects.equals(old, value)) {
      mData.put(name, value);
      mObservable.notifyObservers(name);
    }
    return this;
  }

  @Override
  public FormModel addObserver(Observer observer) {
    mObservable.addObserver(observer);
    return this;
  }

  @Override
  public FormModel removeObserver(Observer observer) {
    mObservable.deleteObserver(observer);
    return this;
  }
}
