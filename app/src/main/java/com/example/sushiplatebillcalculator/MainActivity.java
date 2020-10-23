package com.example.sushiplatebillcalculator;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.text.Editable;
import android.text.InputFilter;
import android.text.InputType;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.view.ViewManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;

import java.text.DecimalFormat;
import java.util.Locale;
import java.util.HashMap;

// I had to rewrite MainActivity to implements some functionality, so instead of messing around and
// commenting out/ breaking code, I started a fresh class file and only transferred what I needed.
public class MainActivity extends AppCompatActivity {

    int numRows = 0;  // The number of rows we have, starts at 0
    int maxDefaultRows = 5; // The max number of rows to create on a default start
    double totalBill = 0.00; // The total value of the bill, starts at $0.00

    // String that stores the decimal separator of the phone
    String dS;

    // HashMap containing all of our data about each sushi row.
    HashMap<Integer, SushiRow> rowInfo = new HashMap<>(10);

    final static int maxTotalRows = 10; // Max number of rows allowed to be created

    final static int spinnerOffset = 100; // Offset between row id and spinner view
    final static int priceOffset = 200; // Offset between row id and price view
    final static int numPlatesOffset = 300; // Offset between row id and plate view
    final static int rowTotalOffset = 400; // Offset between row id and row total view

    // Allows doubles to be displayed to exactly two decimal points
    public static DecimalFormat money = new DecimalFormat("0.00");

    // Static values for parts of the row.  Not sure why trying to draw the values from the
    // dimensions.xml file isn't working correctly for me, so I added the values here.
    final static int maxLines = 1; // The max number of lines for the EditTexts
    final static int minEmsPrice = 3; // Minimum width in characters of the price EditText
    final static int minEmsPlates = 2; // Minimum width in characters of the plates EditText
    final static int maxCharsPrice = 5; // Max number of characters allowed in the price
    final static int maxCharsPlates = 3; // Max number of characters allowed for plates
    final static int minButtonWidth = 35; // Min width of the + and - buttons
    final static int minButtonHeight = 38; // Min height of the + and - buttons
    final static float textSize = 18f; // The default text size


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Getting a version of the local decimal format
        DecimalFormat format = (DecimalFormat) DecimalFormat.getInstance();
        // Accessing the specific decimal symbol for this locale
        char decimalSeparator = format.getDecimalFormatSymbols().getDecimalSeparator();
        // Cast the decimal separator char to a string for comparing later
        dS = Character.toString(decimalSeparator);

        // Creates maxDefaultRows number of rows in the app in the plateRowVerticalLayout view
        for (int i = 0; i < maxDefaultRows; i++) {
            addRow();
        }
    }

    // Calculates the new total bill and updates the textView
    public void calcAndDisplayTotal() {

        // We recalculate each time because the row totals change first and then we update the
        // total, so reset the total here.
        totalBill = 0;

        // Loop over the number of rows we have currently to calculate the total
        for (int i = 0; i < numRows; i++) {
            // Add the current row total to the total bill
            totalBill = totalBill + rowInfo.get(i).getRowTotal();
        }

        // Accessing the textView that holds the total bill value
        TextView tv = findViewById(R.id.totalBillCost);
        // Setting the textView to the new total bill
        tv.setText(money.format(totalBill));
    }

    // Updates the TextView with the current total for the row, based on the given rowID
    public void displayRowTotal(int rowID) {

        // Get the row total that was calculated from the appropriate SushiRow
        double rowTotal = rowInfo.get(rowID).getRowTotal();
        // Access the TextView that holds the total for this row
        TextView tv = findViewById(rowID + rowTotalOffset);
        // Set the text of the TextView to the new row total
        tv.setText(money.format(rowTotal));
        // Update the total bill since a row total was updated
        calcAndDisplayTotal();
    }

    // Takes the row number and to new value to be displayed.  Finds the correct EditText and
    // updates the value of its' text
    public void displayNumPlates(int rowId) {

        // Get the SushiRow for this row
        SushiRow thisRow = rowInfo.get(rowId);
        // Find the EditText we are looking for using the rowId
        EditText et = findViewById(rowId + numPlatesOffset);
        // Set the text of the EditText to the new value
        et.setText(String.format(Locale.getDefault(), "%d", +thisRow.getNumPlates()));
        // Update the row total since we updated the number of plates
        displayRowTotal(rowId);
    }

    // Then onClick method for the remove row button.  Makes sure we don't remove the last row.
    // Disables the remove row button when we get down to 1 row
    public void removeRowButton(View v) {

        // If we have the max number of rows to start, re-enable the add row button
        if (numRows == maxTotalRows) {findViewById(R.id.addRow).setEnabled(true);}

        // If we have more than one row, delete the lowest row
        if (numRows > 1) {

            // Decrease numRows by 1
            numRows--;
            // Get the LinearLayout for the last row
            LinearLayout row = findViewById(numRows);
            // Delete the row by getting its' parent and removing it
            ((ViewManager) row.getParent()).removeView(row);
            // Also delete the entry in the HashMap for this row
            rowInfo.remove(numRows);
            // Calculate and display the new total since a row was removed
            calcAndDisplayTotal();

            // If deleting this row gets us down to one row, disable this button
            if (numRows == 1) {v.setEnabled(false);}
        }
    }

    // Method called that adds a new row to the app.  Builds the LinearLayout that holds the row,
    // all of its' widgets, and the logic for the buttons, and listeners.  Anytime a row is
    // constructed, this method is called, whether at app creation, the adding a row button is
    // pushed, or when loading from a preset.
    public void addRow() {

        // Populate the HashMap with a new SushiRow for this new row
        rowInfo.put(numRows, new SushiRow());

        // Accessing the Linear Layout where all of the rows are added
        LinearLayout rowContainer = findViewById(R.id.plateRowVerticalLayout);

        // Layout params for the LinearLayout that holds the row
        LayoutParams rowParams = new LinearLayout.LayoutParams(
                LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
        // Layout params for all the Views being added
        LayoutParams params = new LinearLayout.LayoutParams(
                LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);

        // Create the LinearLayout that will hold the row
        LinearLayout layout = new LinearLayout(this);
        // Set the ID of the LinearLayout to the current row number
        layout.setId(numRows);
        // Setting the layout params of the LinearLayout
        layout.setLayoutParams(rowParams);
        // Setting the orientation of the LinearLayout
        layout.setOrientation(LinearLayout.HORIZONTAL);
        // Setting padding, only at the start and end, of the LinearLayout
        layout.setPaddingRelative(0,
                getResources().getDimensionPixelOffset(R.dimen.row_top_padding),0,
                getResources().getDimensionPixelOffset(R.dimen.row_bottom_padding));
        // Add this row's linear layout to the vertical layout
        rowContainer.addView(layout);

        // Create the Spinner at the start of the row
        Spinner colorSpinner = new Spinner(this);
        // Set the id using the offset so we can find it later
        colorSpinner.setId(numRows + spinnerOffset);
        // Set the layout parameters of the Spinner
        colorSpinner.setLayoutParams(params);
        // Set the list used to populate the spinner choices, from resources
        ArrayAdapter<CharSequence> spinnerAdapter = ArrayAdapter.createFromResource(
                this, R.array.color_spinner, android.R.layout.simple_spinner_item);
        // Setting the type of dropdown menu to the simple Android option
        spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        // Attaching the spinner adapter to the actual spinner item View
        colorSpinner.setAdapter(spinnerAdapter);
        // Add an listener to the spinner to track what item is selected
        colorSpinner.setOnItemSelectedListener(new OnItemSelectedListener() {
            // When a selection is made in the color spinner, we first want to change the border
            // of the whole row to indicate the color that has been selected.  Also, we store the
            // position of the selected color in the HashMap so it can be retrieved if the user
            // wants to save the current selections as a preset.
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                // Find the id of the row that the spinner is in so we can  grab it by id to change
                // its' color and also, store the selected item in the correct row of the HashMap.
                // The row id is it id of the parent of the AdapterView.
                int spinnerRowId = ((View)parent.getParent()).getId();
                // Access the LinearLayout that this spinner is in
                LinearLayout spinnerLayout = (LinearLayout) findViewById(spinnerRowId);
                // Grab the array of spinner colors from color.xml
                int[] spinnerColors = getResources().getIntArray(R.array.color_spinner_color_list);
                // TODO work on having color selection change the stroke around the row

                // Get the current SushiRow stored at that position
                SushiRow spinnerRow = rowInfo.get(spinnerRowId);
                // Update the SushiRow with the position of the selected item
                spinnerRow.setColorSelected(position);
                // Replace the old SushiRow with the new one for this row
                rowInfo.put(spinnerRowId, spinnerRow);
            }

            // When nothing is selected, nothing needs to happen, so it doesn't need any code
            @Override
            public void onNothingSelected(AdapterView<?> parent) {}
        });
        // Add the Spinner to the LinearLayout for this row
        layout.addView(colorSpinner);

        // Create the EditText that will hold the price of the row
        EditText priceEdit = new EditText(this);
        // Set the id using the offset so we can find it later
        priceEdit.setId(numRows + priceOffset);
        // Set the layout parameters of the EditText
        priceEdit.setLayoutParams(params);
        // Set the input type to a decimal number
        priceEdit.setInputType(InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_FLAG_DECIMAL);
        // Set the textSize of the EditText
        priceEdit.setTextSize(textSize);
        // Set the max length of the EditText using input filters
        priceEdit.setFilters(new InputFilter[] {new InputFilter.LengthFilter(maxCharsPrice)});
        // Set the min width of the EditText in terms of size of the characters in the font
        priceEdit.setMinEms(minEmsPrice);
        // Set the max number of lines for the EditText
        priceEdit.setMaxLines(maxLines);
        // Add the EditText to the LinearLayout for this row
        layout.addView(priceEdit);

        // Create the text view that will hold the "X" symbol
        TextView xView = new TextView(this);
        // Set the layout parameters of the TextView
        xView.setLayoutParams(params);
        // Set the padding for the TextView
        xView.setPaddingRelative(getResources().getDimensionPixelOffset(R.dimen.X_start_padding),
                0, getResources().getDimensionPixelOffset(R.dimen.X_end_padding), 0);
        // Set the text of the TextView to "X"
        xView.setText(getResources().getString(R.string.multiply));
        // Set the text size of the TextView
        xView.setTextSize(textSize);
        // Set the gravity of the TextView
        xView.setGravity(Gravity.CENTER_VERTICAL);
        // Add the TextView to the LinearLayout for the row
        layout.addView(xView);

        // Create the minus button for the row
        Button minusButton = new Button(this);
        // Set the layout params of the button
        minusButton.setLayoutParams(params);
        // Set the text of the button to "-"
        minusButton.setText(getResources().getString(R.string.minus_button));
        // Set the background drawable of the button
        minusButton.setBackground(getResources().getDrawable(R.drawable.button_half_pill_left));
        // Set the min width of the button, we need to override both minWidth and MinimumWidth here
        minusButton.setMinimumWidth((int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,
                minButtonWidth, getResources().getDisplayMetrics()));
        minusButton.setMinWidth((int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,
                minButtonWidth, getResources().getDisplayMetrics()));
        // Set the min height of the button, we need to override both minHeight and minimumHeight
        minusButton.setMinHeight((int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,
                minButtonHeight, getResources().getDisplayMetrics()));
        minusButton.setMinimumHeight((int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,
                minButtonHeight, getResources().getDisplayMetrics()));
        // Set the button to not be focusable to avoid unwanted soft keyboard popups
        minusButton.setFocusable(false);
        // Add the button to the LinearLayout of the row
        layout.addView(minusButton);

        // Create the EditText that will hold the number of plates of the row
        EditText plateEdit = new EditText(this);
        // Set the id of the plate EditText so it can be found later
        plateEdit.setId(numRows + numPlatesOffset);
        // Set the Layout Parameters of the EditText
        plateEdit.setLayoutParams(params);
        // Set the input type to a decimal number
        plateEdit.setInputType(InputType.TYPE_CLASS_NUMBER);
        // Set the textSize of the EditText
        plateEdit.setTextSize(textSize);
        // Set the max length of the EditText using input filters
        plateEdit.setFilters(new InputFilter[] {new InputFilter.LengthFilter(maxCharsPlates)});
        // Set the min width of the EditText in terms of size of the characters in the font
        plateEdit.setMinEms(minEmsPlates);
        // Set the max number of lines for the EditText
        plateEdit.setMaxLines(maxLines);
        // Add the EditText to the LinearLayout for this row
        layout.addView(plateEdit);

        // Create the plus button for the row
        Button plusButton = new Button(this);
        // Set the layout params of the button
        plusButton.setLayoutParams(params);
        // Set the text of the button to "-"
        plusButton.setText(getResources().getString(R.string.plus_button));
        // Set the background drawable of the button
        plusButton.setBackground(getResources().getDrawable(R.drawable.button_half_pill_right));
        // Set the min width of the button, we need to override both minWidth and MinimumWidth here
        plusButton.setMinimumWidth((int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,
                minButtonWidth, getResources().getDisplayMetrics()));
        plusButton.setMinWidth((int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,
                minButtonWidth, getResources().getDisplayMetrics()));
        // Set the min height of the button, we need to override both minHeight and minimumHeight
        plusButton.setMinHeight((int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,
                minButtonHeight, getResources().getDisplayMetrics()));
        plusButton.setMinimumHeight((int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,
                minButtonHeight, getResources().getDisplayMetrics()));
        // Set the button to not be focusable to avoid unwanted soft keyboard popups
        plusButton.setFocusable(false);
        // Add the button to the LinearLayout of the row
        layout.addView(plusButton);

        // Create the TextView that holds the "=" symbol
        TextView equalView = new TextView(this);
        // Set the layout params of the TextView
        equalView.setLayoutParams(params);
        // Set the padding of the TextView
        equalView.setPaddingRelative(
                getResources().getDimensionPixelOffset(R.dimen.equals_start_padding),
                0, getResources().getDimensionPixelOffset(R.dimen.equals_end_padding), 0);
        // Set the text size of the view
        equalView.setTextSize(textSize);
        // Set the gravity of the text view
        equalView.setGravity(Gravity.CENTER_VERTICAL);
        // Set the text of the view
        equalView.setText(getResources().getString(R.string.equals));
        // Add the view to the layout of the row
        layout.addView(equalView);

        // Create the TextView to hold to row total
        TextView rowTotal = new TextView(this);
        // Set the id of the TextView so it can be found later
        rowTotal.setId(numRows + rowTotalOffset);
        // Set the layout params of the view
        rowTotal.setLayoutParams(params);
        // Set the text size of the view
        rowTotal.setTextSize(textSize);
        // Set the gravity of the view
        rowTotal.setGravity(Gravity.CENTER_VERTICAL);
        // Set the initial text of the view
        rowTotal.setText(getResources().getString(R.string.start_bill));
        // Add the view to the layout of the row
        layout.addView(rowTotal);

        // Adding a Text Watcher to the price EditText
        priceEdit.addTextChangedListener(new TextWatcherWithEditText(priceEdit) {

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {}

            // After text is edited we need to update the proper filed in the SushiRow for this row
            // of the app, push the new row total, and calculate and display the new total bill
            @Override
            public void afterTextChanged(Editable s) {

                // Determine the ID of this row
                int rowId = getEditText().getId() - priceOffset;
                // Grab the SushiRow for this row
                SushiRow thisRow = rowInfo.get(rowId);
                // Get the text from the EditText
                String priceString = getEditText().getText().toString();

                // If the entered text is empty, or just the decimal separator,
                // set the value of the price to 0
                if (priceString.isEmpty() || priceString.equals(dS)) {

                    // Set the price value to 0
                    thisRow.setPrice(0);
                }
                // Otherwise if the EditText is not empty and does not contain just the decimal
                // separator, update the price to the entered value
                else {

                    // Parse it into a double and set the price for this row
                    thisRow.setPrice(Double.parseDouble(priceString));
                }

                // Replace the SushiRow in the HashMap with the updated one
                rowInfo.put(rowId, thisRow);
                // Display the new row total for this row
                displayRowTotal(rowId);
            }
        });

        // Adding a Text Watcher to the plates EditText
        plateEdit.addTextChangedListener(new TextWatcherWithEditText(plateEdit) {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {}

            // After the number of plates is changed we need update the total for the row and then
            // the total bill
            @Override
            public void afterTextChanged(Editable s) {

                // Determine the ID of this row
                int rowId = getEditText().getId() - numPlatesOffset;
                // Grab the SushiRow for this row
                SushiRow thisRow = rowInfo.get(rowId);
                // Get the text from the EditText
                String plateString = getEditText().getText().toString();

                // If the entered text is empty, treat it as a 0, and set the appropriate value
                // in the SushiRow for this row
                if (plateString.isEmpty()) {

                    // Set the number of plates to 0
                    thisRow.setNumPlates(0);
                }
                // Otherwise update the number of plates in the SushiRow with the current inputted
                // value of the EditText
                else {

                    // Set the number of plates to the value of the input
                    thisRow.setNumPlates(Integer.parseInt(plateString));
                }

                // Replace the SushiRow in the HashMap with the updated one
                rowInfo.put(rowId, thisRow);
                // Display the new row total for this row
                displayRowTotal(rowId);
            }
        });

        // Creating the onClick method for the minus button
        minusButton.setOnClickListener(new View.OnClickListener() {

            // When the minus button is clicked, we want to decrease the corresponding number of
            // plates by one, or stop at 0 if we are already there.
            public void onClick(View v) {

                // Grab the Id of the row we are working with
                int rowId = ((View) v.getParent()).getId();
                // Get the SushiRow for this row
                SushiRow thisRow = rowInfo.get(rowId);
                // Get the number of plates in the row currently
                int numPlates = thisRow.getNumPlates();

                // Check if the number of plates is greater than 0
                if (numPlates > 0) {

                    // Decrease the to value of plates by 1
                    numPlates--;
                    // Update the SushiRow
                    thisRow.setNumPlates(numPlates);

                }
                // If the value is somehow negative we set it to 0
                else if (numPlates < 0) {

                    // Set the number of plates in the SushiRow to 0
                    thisRow.setNumPlates(0);

                }
                // If it is 0 we do nothing, so no else statement is required

                // Put the updated SushiRow in the HashMap
                rowInfo.put(rowId, thisRow);
                // Update the plates EditText for this row, and the related row total
                displayNumPlates(rowId);
            }
        });

        // Creating the onClick method for the plus button
        plusButton.setOnClickListener(new View.OnClickListener() {

            // When the plus button is pressed, increase the corresponding num of plates by 1
            @Override
            public void onClick(View v) {

                // Grab the id of the row we are working with
                int rowId = ((View) v.getParent()).getId();
                // Get the SushiRow for this row
                SushiRow thisRow = rowInfo.get(rowId);
                // Get the current number of plates in the row
                int numPlates = thisRow.getNumPlates();
                // Increase the number of plates
                numPlates++;
                // Set the new number of plates in the SushiRow
                thisRow.setNumPlates(numPlates);
                // Put the updated row into the HashMap
                rowInfo.put(rowId, thisRow);
                // Update the plates EditText for this row, and the related row total
                displayNumPlates(rowId);
            }
        });

        // Lastly, increase the numbers of rows by one
        numRows++;
    }

    // The onClick method for the add row button.  Makes sure that we are under the max number
    // of rows.  Disables the add row button when the max number of rows is reached.
    public void addRowButton(View v) {

        // If we have one row to start, re-enable the remove row button
        if (numRows == 1) {findViewById(R.id.removeRow).setEnabled(true);}

        // If we are under the max number of rows, add a new row
        if (numRows < maxTotalRows) {
            // add the new row
            addRow();

            // If adding the new row reaches the max number of rows, disable the add row button
            if (numRows == maxTotalRows) {
                // Disable the button
                v.setEnabled(false);
            }
        }
    }
}
