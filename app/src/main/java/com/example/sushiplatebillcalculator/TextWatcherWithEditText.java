package com.example.sushiplatebillcalculator;

import android.text.TextWatcher;
import android.widget.EditText;

// For the main file, I need to access the EditText that the text watcher is attached to.  Therefore,
// I made my own abstract class to use the text watcher and allow me to pass an instance of the
// EditText into the method afterTextChanged, in order to update the correct variables.
public abstract class TextWatcherWithEditText implements TextWatcher {

    private EditText editText;

    // Constructor takes an EditText, should be the one that the text watcher is attached to
    public TextWatcherWithEditText(EditText editText) {

        this.editText = editText;
    }

    // Returns the EditText view
    public EditText getEditText() {
        return editText;
    }
}
