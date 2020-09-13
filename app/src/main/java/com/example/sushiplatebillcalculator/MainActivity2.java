package com.example.sushiplatebillcalculator;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.text.DecimalFormat;
import java.util.Locale;
import java.util.HashMap;

// I had to rewrite MainActivity to implements some functionality, so instead of messing around and
// commenting out/ breaking code, I started a fresh class file and only transferred what I needed.
public class MainActivity2 extends AppCompatActivity {

    int numRows = 0;  // The number of rows we have, starts at 0
    int maxDefaultRows = 5; // The max number of rows to create on a default start
    double totalBill = 0.00; // The total value of the bill, starts at $0.00
    // HashMap containing all of our data about each sushi row.
    HashMap<Integer, SushiRow> rowInfo = new HashMap(10);

    final static int spinnerOffset = 100;
    final static int numPlatesOffset = 200;
    final static int rowTotalOffset = 300;



    // Allows doubles to be displayed to exactly two decimal points
    public static DecimalFormat money = new DecimalFormat("0.00");


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
    // called, whether at app creation, adding a row button is pushed, or when loading from a preset.
    public void addRow() {

        // Accessing the Linear Layout where all of the rows are added
        LinearLayout rowContainer = findViewById(R.id.plateRowVerticalLayout);

        // Create the LinearLayout that will hold the row
        LinearLayout layout = new LinearLayout(this);
        // Set the ID of the LinearLayout to the current row number
        layout.setId(numRows);
        // Setting the other params of the LinearLayout


        // Populate the HashMap with a new SushiRow for this new row
        rowInfo.put(numRows, new SushiRow());
        // Lastly, increase the numbers of rows by one
        numRows++;
    }
}
