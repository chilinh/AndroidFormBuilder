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

/**
 * Created by Linh on 11/11/16.
 */
public interface Callback {

  /**
   * Callback when submit form
   * @param <T>
   */
  interface Submit<T> {
    /**
     * Will call when form need to validate
     * @param owner
     * @param form
     * @return
     */
    boolean validate(T owner, Form form);

    /**
     * When call when submit form
     * @param owner
     * @param form
     */
    void onSubmit(T owner, Form form);
  }

  /**
   * Callback when dismiss form
   * @param <T>
   */
  interface Dismiss<T> {
    /**
     * Will call when form is dismiss or canceled by user
     * @param owner
     * @param form
     */
    void onCancel(T owner, Form form);

    /**
     * Will call when form is dismiss or lost focus
     * @param owner
     * @param form
     */
    void onDismiss(T owner, Form form);
  }

  abstract class SubmitAbs<T> implements Submit<T> {
    @Override
    public boolean validate(T owner, Form form) {
      return true;
    }

    @Override
    public void onSubmit(T owner, Form form) {
    }
  }

  abstract class DismissAbs<T> implements Dismiss<T> {
    @Override
    public void onCancel(T owner, Form form) {
    }

    @Override
    public void onDismiss(T owner, Form form) {
    }
  }
}
