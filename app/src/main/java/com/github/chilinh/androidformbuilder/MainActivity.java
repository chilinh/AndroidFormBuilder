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

package com.github.chilinh.androidformbuilder;

import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.InputType;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.github.chilinh.android.form.Form;
import com.github.chilinh.android.form.element.ComboBoxElement;
import com.github.chilinh.android.form.element.DatePickerElement;
import com.github.chilinh.android.form.element.EditTextElement;
import com.github.chilinh.android.form.element.SectionElement;
import com.github.chilinh.android.form.element.TextElement;
import com.github.chilinh.android.form.element.TimePickerElement;

import java.util.Calendar;

public class MainActivity extends AppCompatActivity {

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_main);

    Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
    setSupportActionBar(toolbar);

    ListView listView = (ListView) findViewById(R.id.list);

    listView.setAdapter(new ArrayAdapter<>(
      this,
      android.R.layout.simple_list_item_1,
      new String[]{
        "Dialog Form",
        //"Fragment"
      }
    ));
    listView.setOnItemClickListener(
      new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
          switch (position) {
            case 0:
              showDialog();
              break;
            case 1:
              showFragment();
              break;
          }
        }
      }
    );
  }

  private void showFragment() {

  }

  private void showDialog() {
    AlertDialog dialog = new Form.Builder()
      .title("Edit Info")
      .addSection(new SectionElement("Personal"))
      .addElement(new EditTextElement(null, "Name:").placeholder("Your name here").required(true).labelTypeface(Typeface.BOLD))
      .addElement(new ComboBoxElement(null, "Genre", "", "Male", "Female"))
      .addElement(new TextElement(null, "For you:").placeholder("N/A").labelTypeface(Typeface.BOLD))
      .addElement(new EditTextElement(null, null).placeholder("Phone number here").setInputTypeMask(InputType.TYPE_CLASS_PHONE, true).required(true))
      .addSection(new SectionElement("More"))
      .addElement(new DatePickerElement(null, "Birthday: ").date(Calendar.getInstance().getTime()))
      .addElement(new TimePickerElement(null, "Leave: ").time(Calendar.getInstance().getTime()))
      .build(this)
      .buildDialog(this, null, null);

    dialog.show();
  }
}
