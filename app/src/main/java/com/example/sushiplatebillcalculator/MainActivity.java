package com.example.sushiplatebillcalculator;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import java.text.DecimalFormat;
import java.util.Locale;


public class MainActivity extends AppCompatActivity {

    // Since I made this app for a very specific sushi place, the starting values, and colors of the
    // different plates are based on this place.  As reflected below the plate colors in order from
    // cheapest to most expensive are: Yellow < Red < Green < Blue < Purple.  The prices per plate
    // in order are $2.50, $3.50, $4.50, $5.50, and $6.50.  They are all editable in app, I just am
    // making it easy for myself until I add a way to save colors and prices at some point.

    int numYellowPlates = 0; // The number of plates in the yellow (first) row
    int numRedPlates = 0; // The number of plates in the red (second) row
    int numGreenPlates = 0; // The number of plates in the green (third) row
    int numBluePlates = 0; // The number of plates in the blue (fourth) row
    int numVioletPlates = 0; // The number of plates in the purple (fifth) row

    double yellowPrice = 2.50; // The price of the yellow (first) plate type
    double redPrice = 3.50; // The price of the red (second) plate type
    double greenPrice = 4.50; // The price of the green (third) plate type
    double bluePrice = 5.50; // The price of the blue (fourth) plate type
    double violetPrice = 6.50; // The price of the purple (fifth) plate type

    double totalBill = 0.00; // The total value of the bill, starts at $0.00
    double yellowPlateTotal = 0.00; // The total value of the yellow plates
    double redPlateTotal = 0.00; // The total value of the red plates
    double greenPlateTotal = 0.00; // The total value of the green plates
    double bluePlateTotal = 0.00; // The total value of the blue plates
    double violetPlateTotal = 0.00; // The total value of the purple plates

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

        // ----- Text watchers for the editTexts -----
        // Accessing the editText for the number of yellow plates
        final EditText yNP = findViewById(R.id.yellowNumberPlates);
        // Accessing the editText for the price of yellow plates
        final EditText yP = findViewById(R.id.yellowPrice);
        // Accessing the editText for the number of red plates
        final EditText rNP = findViewById(R.id.redNumberPlates);
        // Accessing the editText for the price of red plates
        final EditText rP = findViewById(R.id.redPrice);
        // Accessing the editText for the number of green plates
        final EditText gNP = findViewById(R.id.greenNumberPlates);
        // Accessing the editText for the price of green plates
        final EditText gP = findViewById(R.id.greenPrice);
        // Accessing the editText for the number of blue plates
        final EditText bNP = findViewById(R.id.blueNumberPlates);
        // Accessing the editText for the price of blue plates
        final EditText bP = findViewById(R.id.bluePrice); 
        // Accessing the editText for the number of violet plates
        final EditText vNP = findViewById(R.id.violetNumberPlates);
        // Accessing the editText for the price of violet plates
        final EditText vP = findViewById(R.id.violetPrice);

        // Text Watcher for the yellowNumberPlates editText
        yNP.addTextChangedListener(new TextWatcher() {

            // Don't need the first two methods, so they are left blank
            @Override public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
            @Override public void onTextChanged(CharSequence s, int start, int before, int count) {}

            // After the number of yellow plates is changed we need to update the numYellowPlates
            // variable, and then recalculate the total from yellow plates
            @Override
            public void afterTextChanged(Editable s) {

                // Check if the user input is currently empty
                if (yNP.getText().toString().isEmpty()) {
                    // Set the number of plates to 0, but don't recalculate yet
                    numYellowPlates = 0;
                }
                // If it isn't empty calculate the new yellow plate cost
                else {
                    // Update numYellowPlates with the user input in the editText
                    numYellowPlates = Integer.parseInt(yNP.getText().toString());
                    // Calculate the new yellow plate cost
                    calcAndDisplayYellow();
                }
            }
        });
        // Text Watcher for the price of yellow plates
        yP.addTextChangedListener(new TextWatcher() {

            // Don't need the first two methods, so they are left blank
            @Override public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
            @Override public void onTextChanged(CharSequence s, int start, int before, int count) {}

            // After the price of yellow plates is changed we need to update the yellowPrice
            // variable and recalculate the totals
            @Override
            public void afterTextChanged(Editable s) {

                // Check if the user input is currently empty
                if (yP.getText().toString().isEmpty()) {
                    // If it is empty, set the price to 0, but don't calculate
                    yellowPrice = 0;
                }
                // If the string is just a decimal separator, wait for more user input
                else if (yP.getText().toString().equals(dS)) {}
                // If it isn't empty or just a point, calculate the new yellow plate cost
                else {
                    // Update yellowPrice with the user input in the editText
                    yellowPrice = Double.parseDouble(yP.getText().toString());
                    // Calculate the new yellow plate cost
                    calcAndDisplayYellow();
                }
            }
        });
        // Text Watcher for the redNumberPlates editText
        rNP.addTextChangedListener(new TextWatcher() {

            // Don't need the first two methods, so they are left blank
            @Override public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
            @Override public void onTextChanged(CharSequence s, int start, int before, int count) {}

            // After the number of red plates is changed we need to update the numRedPlates
            // variable, and then recalculate the total from red plates
            @Override
            public void afterTextChanged(Editable s) {

                // Check if the user input is currently empty
                if (rNP.getText().toString().isEmpty()) {
                    // Set the number of plates to 0, but don't recalculate yet
                    numRedPlates = 0;
                }
                // If it isn't empty calculate the new red plate cost
                else {
                    // Update numRedPlates with the user input in the editText
                    numRedPlates = Integer.parseInt(rNP.getText().toString());
                    // Calculate the new red plate cost
                    calcAndDisplayRed();
                }
            }
        });
        // Text Watcher for the price of red plates
        rP.addTextChangedListener(new TextWatcher() {

            // Don't need the first two methods, so they are left blank
            @Override public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
            @Override public void onTextChanged(CharSequence s, int start, int before, int count) {}

            // After the price of red plates is changed we need to update the redPrice
            // variable and recalculate the totals
            @Override
            public void afterTextChanged(Editable s) {

                // Check if the user input is currently empty
                if (rP.getText().toString().isEmpty()) {
                    // If it is empty, set the price to 0, but don't calculate
                    redPrice = 0;
                }
                // If the string is just a decimal separator, wait for more user input
                else if (rP.getText().toString().equals(dS)) {}
                // If it isn't empty or just a point, calculate the new red plate cost
                else {
                    // Update redPrice with the user input in the editText
                    redPrice = Double.parseDouble(rP.getText().toString());
                    // Calculate the new red plate cost
                    calcAndDisplayRed();
                }
            }
        });
        // Text Watcher for the greenNumberPlates editText
        gNP.addTextChangedListener(new TextWatcher() {

            // Don't need the first two methods, so they are left blank
            @Override public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
            @Override public void onTextChanged(CharSequence s, int start, int before, int count) {}

            // After the number of green plates is changed we need to update the numGreenPlates
            // variable, and then recalculate the total from green plates
            @Override
            public void afterTextChanged(Editable s) {

                // Check if the user input is currently empty
                if (gNP.getText().toString().isEmpty()) {
                    // Set the number of plates to 0, but don't recalculate yet
                    numGreenPlates = 0;
                }
                // If it isn't empty calculate the new green plate cost
                else {
                    // Update numGreenPlates with the user input in the editText
                    numGreenPlates = Integer.parseInt(gNP.getText().toString());
                    // Calculate the new green plate cost
                    calcAndDisplayGreen();
                }
            }
        });
        // Text Watcher for the price of green plates
        gP.addTextChangedListener(new TextWatcher() {

            // Don't need the first two methods, so they are left blank
            @Override public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
            @Override public void onTextChanged(CharSequence s, int start, int before, int count) {}

            // After the price of green plates is changed we need to update the greenPrice
            // variable and recalculate the totals
            @Override
            public void afterTextChanged(Editable s) {

                // Check if the user input is currently empty
                if (gP.getText().toString().isEmpty()) {
                    // If it is empty, set the price to 0, but don't calculate
                    greenPrice = 0;
                }
                // If the string is just a decimal separator, wait for more user input
                else if (gP.getText().toString().equals(dS)) {}
                // If it isn't empty or just a point, calculate the new green plate cost
                else {
                    // Update greenPrice with the user input in the editText
                    greenPrice = Double.parseDouble(gP.getText().toString());
                    // Calculate the new green plate cost
                    calcAndDisplayGreen();
                }
            }
        });
        // Text Watcher for the blueNumberPlates editText
        bNP.addTextChangedListener(new TextWatcher() {

            // Don't need the first two methods, so they are left blank
            @Override public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
            @Override public void onTextChanged(CharSequence s, int start, int before, int count) {}

            // After the number of blue plates is changed we need to update the numBluePlates
            // variable, and then recalculate the total from blue plates
            @Override
            public void afterTextChanged(Editable s) {

                // Check if the user input is currently empty
                if (bNP.getText().toString().isEmpty()) {
                    // Set the number of plates to 0, but don't recalculate yet
                    numBluePlates = 0;
                }
                // If it isn't empty calculate the new blue plate cost
                else {
                    // Update numBluePlates with the user input in the editText
                    numBluePlates = Integer.parseInt(bNP.getText().toString());
                    // Calculate the new blue plate cost
                    calcAndDisplayBlue();
                }
            }
        });
        // Text Watcher for the price of blue plates
        bP.addTextChangedListener(new TextWatcher() {

            // Don't need the first two methods, so they are left blank
            @Override public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
            @Override public void onTextChanged(CharSequence s, int start, int before, int count) {}

            // After the price of blue plates is changed we need to update the bluePrice
            // variable and recalculate the totals
            @Override
            public void afterTextChanged(Editable s) {

                // Check if the user input is currently empty
                if (bP.getText().toString().isEmpty()) {
                    // If it is empty, set the price to 0, but don't calculate
                    bluePrice = 0;
                }
                // If the string is just a decimal separator, wait for more user input
                else if (bP.getText().toString().equals(dS)) {}
                // If it isn't empty or just a point, calculate the new blue plate cost
                else {
                    // Update bluePrice with the user input in the editText
                    bluePrice = Double.parseDouble(bP.getText().toString());
                    // Calculate the new blue plate cost
                    calcAndDisplayBlue();
                }
            }
        });
        // Text Watcher for the violetNumberPlates editText
        vNP.addTextChangedListener(new TextWatcher() {

            // Don't need the first two methods, so they are left blank
            @Override public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
            @Override public void onTextChanged(CharSequence s, int start, int before, int count) {}

            // After the number of violet plates is changed we need to update the numVioletPlates
            // variable, and then recalculate the total from violet plates
            @Override
            public void afterTextChanged(Editable s) {

                // Check if the user input is currently empty
                if (vNP.getText().toString().isEmpty()) {
                    // Set the number of plates to 0, but don't recalculate yet
                    numVioletPlates = 0;
                }
                // If it isn't empty calculate the new violet plate cost
                else {
                    // Update numVioletPlates with the user input in the editText
                    numVioletPlates = Integer.parseInt(vNP.getText().toString());
                    // Calculate the new violet plate cost
                    calcAndDisplayViolet();
                }
            }
        });
        // Text Watcher for the price of violet plates
        vP.addTextChangedListener(new TextWatcher() {

            // Don't need the first two methods, so they are left blank
            @Override public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
            @Override public void onTextChanged(CharSequence s, int start, int before, int count) {}

            // After the price of violet plates is changed we need to update the violetPrice
            // variable and recalculate the totals
            @Override
            public void afterTextChanged(Editable s) {

                // Check if the user input is currently empty
                if (vP.getText().toString().isEmpty()) {
                    // If it is empty, set the price to 0, but don't calculate
                    violetPrice = 0;
                }
                // If the string is just a decimal separator, wait for more user input
                else if (vP.getText().toString().equals(dS)) {}
                // If it isn't empty or just a point, calculate the new violet plate cost
                else {
                    // Update violetPrice with the user input in the editText
                    violetPrice = Double.parseDouble(vP.getText().toString());
                    // Calculate the new green plate cost
                    calcAndDisplayViolet();
                }
            }
        });
    }

    // Calculates the new total bill and then updates the textView
    public void calcAndDisplayTotal() {

        // Calculating the total bill
        totalBill = yellowPlateTotal + redPlateTotal + greenPlateTotal
                + bluePlateTotal + violetPlateTotal;
        // Accessing the textView that holds the total bill value
        TextView tv = findViewById(R.id.totalBillCost);
        // Setting the textView to the new total bill
        tv.setText(money.format(totalBill));
    }

    // Calculates the new cost of the yellow plates and updates the textView
    public void calcAndDisplayYellow() {

        // Calculates the cost of the yellow plates
        yellowPlateTotal = yellowPrice * numYellowPlates;
        // Accessing the textView that holds the yellow plate cost
        TextView tv = findViewById(R.id.yellowPlateTotal);
        // Setting the textView to the new yellow plate cost
        tv.setText(money.format(yellowPlateTotal));
        // Updating the new total, since we changed one of its' components
        calcAndDisplayTotal();
    }

    // Increase the value of numYellowPlates by one, and update the editText when the button is
    // pushed.  Also, updates the yellowPlateTotal and the totalBill.
    public void yellowPlusButton(View view) {

        // Increase the value of numYellowPlates by 1
        numYellowPlates += 1;
        // Access the edit text that holds the number of yellow plates
        EditText et = findViewById(R.id.yellowNumberPlates);
        // Update the value of the editText since it has changed
        et.setText(String.format(Locale.getDefault(),"%d", +numYellowPlates));
        // Calculate the new total for the yellow plates and the total bill
        calcAndDisplayYellow();
    }

    // Decrease the value of numYellowPlates by one, and update the editText when the button is
    // pushed.  Also, updates the yellowPlateTotal and the totalBill.
    public void yellowMinusButton(View view) {

        // We don't want negative plates, so check if numYellowPlates is 0 or less
        if (numYellowPlates <= 0) {

            // Set the number to 0 if it somehow got below 0
            numYellowPlates = 0;
        }
        else {
            // Decrease the value of numYellowPlates by 1
            numYellowPlates -= 1;
        }
        // Access the edit text that holds the number of yellow plates
        EditText et = findViewById(R.id.yellowNumberPlates);
        // Update the value of the editText since it has changed
        et.setText(String.format(Locale.getDefault(),"%d", +numYellowPlates));
        // Calculate the new total for the yellow plates and the total bill
        calcAndDisplayYellow();
    }

    // Calculates the new cost of the red plates and updates the textView
    public void calcAndDisplayRed() {

        // Calculates the cost of the red plates
        redPlateTotal = redPrice * numRedPlates;
        // Accessing the textView that holds the red plate cost
        TextView tv = findViewById(R.id.redPlateTotal);
        // Setting the textView to the new red plate cost
        tv.setText(money.format(redPlateTotal));
        // Updating the new total, since we changed one of its' components
        calcAndDisplayTotal();
    }

    // Increase the value of numRedPlates by one, and update the editText when the button is
    // pushed.  Also, updates the redPlateTotal and the totalBill.
    public void redPlusButton(View view) {

        // Increase the value of numRedPlates by 1
        numRedPlates += 1;
        // Access the edit text that holds the number of red plates
        EditText et = findViewById(R.id.redNumberPlates);
        // Update the value of the editText since it has changed
        et.setText(String.format(Locale.getDefault(),"%d", +numRedPlates));
        // Calculate the new total for the red plates and the total bill
        calcAndDisplayRed();
    }

    // Decrease the value of numRedPlates by one, and update the editText when the button is
    // pushed.  Also, updates the redPlateTotal and the totalBill.
    public void redMinusButton(View view) {

        // We don't want negative plates, so check if numRedPlates is 0 or less
        if (numRedPlates <= 0) {

            // Set the number to 0 if it somehow got below 0
            numRedPlates = 0;
        }
        else {
            // Decrease the value of numRedPlates by 1
            numRedPlates -= 1;
        }
        // Access the edit text that holds the number of red plates
        EditText et = findViewById(R.id.redNumberPlates);
        // Update the value of the editText since it has changed
        et.setText(String.format(Locale.getDefault(),"%d", +numRedPlates));
        // Calculate the new total for the red plates and the total bill
        calcAndDisplayRed();
    }

    // Calculates the new cost of the green plates and updates the textView
    public void calcAndDisplayGreen() {

        // Calculates the cost of the green plates
        greenPlateTotal = greenPrice * numGreenPlates;
        // Accessing the textView that holds the green plate cost
        TextView tv = findViewById(R.id.greenPlateTotal);
        // Setting the textView to the new green plate cost
        tv.setText(money.format(greenPlateTotal));
        // Updating the new total, since we changed one of its' components
        calcAndDisplayTotal();
    }

    // Increase the value of numGreenPlates by one, and update the editText when the button is
    // pushed.  Also, updates the greenPlateTotal and the totalBill.
    public void greenPlusButton(View view) {

        // Increase the value of numGreenPlates by 1
        numGreenPlates += 1;
        // Access the edit text that holds the number of green plates
        EditText et = findViewById(R.id.greenNumberPlates);
        // Update the value of the editText since it has changed
        et.setText(String.format(Locale.getDefault(),"%d", +numGreenPlates));
        // Calculate the new total for the green plates and the total bill
        calcAndDisplayGreen();
    }

    // Decrease the value of greenGreenPlates by one, and update the editText when the button is
    // pushed.  Also, updates the greenPlateTotal and the totalBill.
    public void greenMinusButton(View view) {

        // We don't want negative plates, so check if numGreenPlates is 0 or less
        if (numGreenPlates <= 0) {

            // Set the number to 0 if it somehow got below 0
            numGreenPlates = 0;
        }
        else {
            // Decrease the value of numGreenPlates by 1
            numGreenPlates -= 1;
        }
        // Access the edit text that holds the number of green plates
        EditText et = findViewById(R.id.greenNumberPlates);
        // Update the value of the editText since it has changed
        et.setText(String.format(Locale.getDefault(),"%d", +numGreenPlates));
        // Calculate the new total for the green plates and the total bill
        calcAndDisplayGreen();
    }

    // Calculates the new cost of the blue plates and updates the textView
    public void calcAndDisplayBlue() {

        // Calculates the cost of the blue plates
        bluePlateTotal = bluePrice * numBluePlates;
        // Accessing the textView that holds the blue plate cost
        TextView tv = findViewById(R.id.bluePlateTotal);
        // Setting the textView to the new blue plate cost
        tv.setText(money.format(bluePlateTotal));
        // Updating the new total, since we changed one of its' components
        calcAndDisplayTotal();
    }

    // Increase the value of numBluePlates by one, and update the editText when the button is
    // pushed.  Also, updates the bluePlateTotal and the totalBill.
    public void bluePlusButton(View view) {

        // Increase the value of numBluePlates by 1
        numBluePlates += 1;
        // Access the edit text that holds the number of blue plates
        EditText et = findViewById(R.id.blueNumberPlates);
        // Update the value of the editText since it has changed
        et.setText(String.format(Locale.getDefault(),"%d", +numBluePlates));
        // Calculate the new total for the blue plates and the total bill
        calcAndDisplayBlue();
    }

    // Decrease the value of numBluePlates by one, and update the editText when the button is
    // pushed.  Also, updates the bluePlateTotal and the totalBill.
    public void blueMinusButton(View view) {

        // We don't want negative plates, so check if numBluePlates is 0 or less
        if (numBluePlates <= 0) {

            // Set the number to 0 if it somehow got below 0
            numBluePlates = 0;
        }
        else {
            // Decrease the value of numBluePlates by 1
            numBluePlates -= 1;
        }
        // Access the edit text that holds the number of blue plates
        EditText et = findViewById(R.id.blueNumberPlates);
        // Update the value of the editText since it has changed
        et.setText(String.format(Locale.getDefault(),"%d", +numBluePlates));
        // Calculate the new total for the blue plates and the total bill
        calcAndDisplayBlue();
    }

    // Calculates the new cost of the violet plates and updates the textView
    public void calcAndDisplayViolet() {

        // Calculates the cost of the violet plates
        violetPlateTotal = violetPrice * numVioletPlates;
        // Accessing the textView that holds the violet plate cost
        TextView tv = findViewById(R.id.violetPlateTotal);
        // Setting the textView to the new violet plate cost
        tv.setText(money.format(violetPlateTotal));
        // Updating the new total, since we changed one of its' components
        calcAndDisplayTotal();
    }

    // Increase the value of numVioletPlates by one, and update the editText when the button is
    // pushed.  Also, updates the violetPlateTotal and the totalBill.
    public void violetPlusButton(View view) {

        // Increase the value of numVioletPlates by 1
        numVioletPlates += 1;
        // Access the edit text that holds the number of violet plates
        EditText et = findViewById(R.id.violetNumberPlates);
        // Update the value of the editText since it has changed
        et.setText(String.format(Locale.getDefault(),"%d", +numVioletPlates));
        // Calculate the new total for the violet plates and the total bill
        calcAndDisplayViolet();
    }

    // Decrease the value of numVioletPlates by one, and update the editText when the button is
    // pushed.  Also, updates the violetPlateTotal and the totalBill.
    public void violetMinusButton(View view) {

        // We don't want negative plates, so check if numVioletPlates is 0 or less
        if (numVioletPlates <= 0) {

            // Set the number to 0 if it somehow got below 0
            numVioletPlates = 0;
        }
        else {
            // Decrease the value of numVioletPlates by 1
            numVioletPlates -= 1;
        }
        // Access the edit text that holds the number of violet plates
        EditText et = findViewById(R.id.violetNumberPlates);
        // Update the value of the editText since it has changed
        et.setText(String.format(Locale.getDefault(),"%d", +numVioletPlates));
        // Calculate the new total for the red plates and the total bill
        calcAndDisplayViolet();
    }
}
