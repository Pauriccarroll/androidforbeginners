package com.example.android.paurictest;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;

import java.text.NumberFormat;

/**
 * This app displays an order form to order lessons.
 */
public class MainActivity extends AppCompatActivity {

    int quantity = 2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    /**
     * This method is called when the plus button is clicked.
     */
    public void increment(View view) {
        if (quantity == 100) {
            return;
        }
        quantity = quantity + 1;
        displayQuantity(quantity);
    }

    /**
     * This method is called when the minus button is clicked.
     */
    public void decrement(View view) {
        if (quantity == 0) {
            return;
        }
        quantity = quantity - 1;
        displayQuantity(quantity);
    }

    /**
     * This method is called when the order button is clicked.
     */
    public void submitOrder(View view) {
        // Get user's name
        EditText nameField = (EditText) findViewById(R.id.name_field);
        Editable nameEditable = nameField.getText();
        String name = nameEditable.toString();

        // Figure out if the user wants surf board equipment
        CheckBox surfBoardCheckBox = (CheckBox) findViewById(R.id.surf_board_checkbox);
        boolean hasSurfBoard = surfBoardCheckBox.isChecked();

        // Figure out if the user wants surf board equipment
        CheckBox wetsuitCheckBox = (CheckBox) findViewById(R.id.wetsuit_checkbox);
        boolean hasWetsuit = wetsuitCheckBox.isChecked();

        // Calculate the price
        int price = calculatePrice(hasSurfBoard, hasWetsuit);

        // Display the order summary on the screen
        String message = createOrderSummary(name, price, hasSurfBoard, hasWetsuit);

        // Use an intent to launch an email app.
        // Send the order summary in the email body.
        Intent intent = new Intent(Intent.ACTION_SENDTO);
        intent.setData(Uri.parse("mailto:")); // only email apps should handle this
        intent.putExtra(Intent.EXTRA_SUBJECT,
                getString(R.string.order_summary_email_subject, name));
        intent.putExtra(Intent.EXTRA_TEXT, message);

        if (intent.resolveActivity(getPackageManager()) != null) {
            startActivity(intent);
        }
    }

    /**
     * Calculates the price of the order.
     *
     * @param addSurfBoard is whether or not we should include surf board equipment in the price
     * @param addWetsuit   is whether or not we should include surf board equipment in the price
     * @return total price
     */
    private int calculatePrice(boolean addSurfBoard, boolean addWetsuit) {
        // First calculate the price of one cup of lessons
        int basePrice = 25;

        // If the user wants surf board, add $1 per cup
        if (addSurfBoard) {
            basePrice = basePrice + 5;
        }

        // If the user wants wetsuit, add $2 per cup
        if (addWetsuit) {
            basePrice = basePrice + 5;
        }

        // Calculate the total order price by multiplying by the quantity
        return quantity * basePrice;
    }

    /**
     * Create summary of the order.
     *
     * @param name         on the order
     * @param price        of the order
     * @param addSurfBoard is whether or not to add surf board to the lessons
     * @param addWetsuit   is whether or not to add wetsuit to the lessons
     * @return text summary
     */
    private String createOrderSummary(String name, int price, boolean addSurfBoard,
                                      boolean addWetsuit) {
        String priceMessage = getString(R.string.order_summary_name, name);
        priceMessage += "\n" + getString(R.string.order_summary_surf_board, addSurfBoard);
        priceMessage += "\n" + getString(R.string.order_summary_wetsuit, addWetsuit);
        priceMessage += "\n" + getString(R.string.order_summary_quantity, quantity);
        priceMessage += "\n" + getString(R.string.order_summary_price,
                NumberFormat.getCurrencyInstance().format(price));
        priceMessage += "\n" + getString(R.string.thank_you);
        return priceMessage;
    }

    /**
     * This method displays the given quantity value on the screen.
     */
    private void displayQuantity(int numberOfLessons) {
        TextView quantityTextView = (TextView) findViewById(
                R.id.quantity_text_view);
        quantityTextView.setText("" + numberOfLessons);
    }
}