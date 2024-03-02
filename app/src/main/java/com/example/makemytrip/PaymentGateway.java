package com.example.makemytrip;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;


public class PaymentGateway extends AppCompatActivity {
    DatabaseReference databaseReference;
    FirebaseAuth mAuth;
    TextView price, durationTextView, total, rooms, gst, cvvwhat;
    Button upipay;
    EditText upiIdEditText;
    int totalAmount;
    AlertDialog dialog;

    Button pay2Button ;

    // Initialize validation status
    boolean isCardNumberValid = false;
    boolean isExpiryDateValid = false;
    boolean isCvvValid = false;
    boolean isCardNameValid = false;

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            // Handle the back button
            case android.R.id.home:
                onBackPressed(); // This will call the default back button behavior
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payment_gateway);

        price = findViewById(R.id.price);
        durationTextView = findViewById(R.id.duration);
        total = findViewById(R.id.total);
        rooms = findViewById(R.id.rooms);
        cvvwhat = findViewById(R.id.cvvwhat);
        upipay = findViewById(R.id.pay1);
        gst = findViewById(R.id.gst);
        pay2Button = findViewById(R.id.pay2);
        getSupportActionBar().setTitle("Confirm Booking");
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
            // Set your desired icon for the navigation drawer toggle
        }
        Intent intent = getIntent();
        HotelBookedInfo receivedBookedInfo = (HotelBookedInfo) intent.getSerializableExtra("hotelBookedInfo");
        Hotel hotel = (Hotel) intent.getParcelableExtra("hotel");
        if (receivedBookedInfo != null && hotel != null) {
            // Now you have the object in the receiving activity
            price.setText("₹ " + hotel.getPrice());
            rooms.setText(receivedBookedInfo.getNumberOfRooms() + " Rooms");

            long duration = DateUtils.getDuration(receivedBookedInfo.getStartingDate(), receivedBookedInfo.getEndingDate());
            durationTextView.setText(duration + " days");
            gst.setText("₹ " + (hotel.getPrice() * duration * receivedBookedInfo.getNumberOfRooms() * 0.18));
            totalAmount = (int) (hotel.getPrice() * duration * receivedBookedInfo.getNumberOfRooms() + (hotel.getPrice() * duration * receivedBookedInfo.getNumberOfRooms() * 0.18));
            total.setText("₹ " + totalAmount);
        }

        RadioGroup paymentMethodRadioGroup = findViewById(R.id.paymentMethodRadioGroup);
        final LinearLayout upiLayout = findViewById(R.id.upilayout);
        final LinearLayout cardLayout = findViewById(R.id.cardlayout);

        paymentMethodRadioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                if (checkedId == R.id.upipay) {
                    upiLayout.setVisibility(View.VISIBLE);
                    cardLayout.setVisibility(View.GONE);
                } else if (checkedId == R.id.cards) {
                    upiLayout.setVisibility(View.GONE);
                    cardLayout.setVisibility(View.VISIBLE);
                }
            }
        });
        upiIdEditText = findViewById(R.id.upiid);
        Button pay1Button = findViewById(R.id.pay1);


        upiIdEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                // Not needed for this case
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                // Check if the UPI ID field is not empty
                boolean isUpiIdFilled = charSequence.length() > 3;

                // Enable or disable the "Proceed to Pay" button accordingly
                pay1Button.setEnabled(isUpiIdFilled);
            }

            @Override
            public void afterTextChanged(Editable editable) {
                // Not needed for this case
            }
        });

        upipay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Get the UPI ID from the EditText
                String upiId = upiIdEditText.getText().toString();

                // Check if the UPI ID is valid (contains @)
                if (upiId.contains("@")) {
                    // Show an alert dialog for processing payment
                    showProcessingDialog();

                    // Proceed to DummyUPIPayment activity after a delay (simulating processing time)
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            // Dismiss the alert dialog
                            dismissProcessingDialog();

                            // Proceed to DummyUPIPayment activity
                            Intent intent = new Intent(PaymentGateway.this, DummyUPIPayment.class);
                            intent.putExtra("bookingType", "hotel");
                            intent.putExtra("totalAmount", totalAmount);
                            intent.putExtra("hotelBookedInfo", receivedBookedInfo);
                            intent.putExtra("hotel", hotel);
                            startActivity(intent);
                            finish(); // Finish the current activity
                        }
                    }, 2000); // Adjust the delay time as needed (in milliseconds)
                } else {
                    // Display a toast for an invalid UPI ID
                    Toast.makeText(PaymentGateway.this, "Invalid UPI ID", Toast.LENGTH_SHORT).show();
                }
            }
        });

        EditText cardNumberEditText = findViewById(R.id.cardNumberEditText);
        EditText expiryDateEditText = findViewById(R.id.expiryDateEditText);
        EditText cvvEditText = findViewById(R.id.cvvEditText);
        EditText cardHolderNameEditText = findViewById(R.id.cardNameEditText);

// Add a TextWatcher for Card Number validation
        cardNumberEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                // Not needed for this case
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                // Validate card number (example: simple check for 16 digits)
                if (charSequence.length() == 16) {
                    // Valid card number
                    // You can add further validation if needed
                    isCardNumberValid = (charSequence.length() == 16);
                    updatePayButtonState();
                } else {
                    cardNumberEditText.setError("Invalid card number (16 digits only)");
                    isCardNumberValid = false;
                    updatePayButtonState();
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {
                // Not needed for this case
            }
        });

        cardHolderNameEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                // Not needed for this case
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                // Validate card holder name (example: simple check for 3 characters)
                if (charSequence.length() >= 3) {
                    // Valid card holder name
                    // You can add further validation if needed
                    isCardNameValid = (charSequence.length() >= 3);
                    updatePayButtonState();
                } else {
                    cardHolderNameEditText.setError("Invalid card holder name (3 characters minimum)");
                    isCardNameValid = false;
                    updatePayButtonState();
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {
                // Not needed for this case
            }
        });

// Add a TextWatcher for Expiry Date validation
        expiryDateEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                // Not needed for this case
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                // Validate expiry date (check for MM/YY format and specific criteria)
                if (charSequence.length() == 5 && charSequence.charAt(2) == '/') {
                    try {
                        String[] parts = charSequence.toString().split("/");
                        int mm = Integer.parseInt(parts[0]);
                        int yy = Integer.parseInt(parts[1]);

                        // Validating MM and YY
                        if (mm >= 1 && mm <= 12 && yy > 23) {
                            isExpiryDateValid = true;
                        } else {
                            expiryDateEditText.setError("Invalid format \nCheck if MM<=12 and YY>=24");

                            isExpiryDateValid = false;
                        }
                    } catch (NumberFormatException | ArrayIndexOutOfBoundsException e) {
                        expiryDateEditText.setError("Invalid expiry date");
                        isExpiryDateValid = false;
                    }
                } else {
                    expiryDateEditText.setError("Invalid expiry date (MM/YY format)");
                    isExpiryDateValid = false;
                }
                updatePayButtonState();
            }

            @Override
            public void afterTextChanged(Editable editable) {
                // Not needed for this case
            }
        });


// Add a TextWatcher for CVV validation
        cvvEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                // Not needed for this case
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                // Validate CVV (example: simple check for 3 digits)
                if (charSequence.length() == 3) {
                    // Valid CVV
                    // You can add further validation if needed
                    isCvvValid = (charSequence.length() == 3);
                    updatePayButtonState();

                } else {
                    cvvEditText.setError("Invalid CVV (3 digits only)");
                    isCvvValid = false;
                    updatePayButtonState();
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {
                // Not needed for this case
            }
        });

        pay2Button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Show an alert dialog for processing payment
                showProcessingDialog();

                // Proceed to DummyCardPayment activity after a delay (simulating processing time)
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        // Dismiss the alert dialog
                        dismissProcessingDialog();

                        // Proceed to DummyCardPayment activity
                        Intent intent = new Intent(PaymentGateway.this, cardpayment.class);
                        intent.putExtra("bookingType", "hotel");
                        intent.putExtra("totalAmount", totalAmount);
                        intent.putExtra("hotelBookedInfo", receivedBookedInfo);
                        intent.putExtra("hotel", hotel);
                        startActivity(intent);
                        finish(); // Finish the current activity
                    }
                }, 2000); // Adjust the delay time as needed (in milliseconds)
            }
        });
        cvvwhat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder builder = new AlertDialog.Builder(PaymentGateway.this);

                // Inflate the custom layout
                LayoutInflater inflater = getLayoutInflater();
                View customLayout = inflater.inflate(R.layout.cvv_layout, null);
                customLayout.findViewById(R.id.gotit).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        // Dismiss the dialog
                        dialog.dismiss();
                    }
                });
                // Customize the layout as needed (e.g., set a custom message)

                // Set the custom layout to the dialog
                builder.setView(customLayout);

                // Create and show the dialog
                builder.setCancelable(false);
                dialog = builder.create();
                dialog.show();
            }
        });







    }

    // Function to show the processing dialog
    private void showProcessingDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        // Inflate the custom layout
        LayoutInflater inflater = getLayoutInflater();
        View customLayout = inflater.inflate(R.layout.processing_alert, null);

        // Customize the layout as needed (e.g., set a custom message)

        // Set the custom layout to the dialog
        builder.setView(customLayout);

        // Create and show the dialog
        builder.setCancelable(false);
        dialog = builder.create();
        dialog.show();
    }

    // Function to dismiss the processing dialog
    private void dismissProcessingDialog() {
        if (dialog != null && dialog.isShowing()) {
            dialog.dismiss();
        }
    }
    private void updatePayButtonState() {
        pay2Button.setEnabled(isCardNumberValid && isExpiryDateValid && isCvvValid);
    }

}