package com.example.sushiplatebillcalculator;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Build;
import android.os.Bundle;
import android.text.Editable;
import android.text.InputFilter;
import android.text.InputType;
import android.text.TextWatcher;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
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
        final String dS = Character.toString(decimalSeparator);

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

    // Method called that adds a new row to the app.  Anytime a row is constructed, this method is
    // called, whether at app creation, adding a row button is pushed, or when loading from a preset.asd
    public void addRow() {

        // Populate the HashMap with a new SushiRow for this new row
        rowInfo.put(numRows, new SushiRow());

        // Accessing the Linear Layout where all of the rows are added
        LinearLayout rowContainer = findViewById(R.id.plateRowVerticalLayout);

        // Layout params for all the Views being added
        LayoutParams params = new LinearLayout.LayoutParams(
                LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);

        // Create the LinearLayout that will hold the row
        LinearLayout layout = new LinearLayout(this);
        // Set the ID of the LinearLayout to the current row number
        layout.setId(numRows);
        // Setting the layout params of the LinearLayout
        layout.setLayoutParams(params);
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
                // .replace isn't supported until v24, so we need to remove the old value instead
                rowInfo.remove(spinnerRowId);
                // And now we put the updated SushiRow in, mapped to the row id
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
        // Add the button to the LinearLayout of the row
        layout.addView(minusButton);

        // Create the EditText that will hold the number of plates of the row
        EditText plateEdit = new EditText(this);
        // Set the id of the plate EditText
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






        // Lastly, increase the numbers of rows by one
        numRows++;
    }
}
