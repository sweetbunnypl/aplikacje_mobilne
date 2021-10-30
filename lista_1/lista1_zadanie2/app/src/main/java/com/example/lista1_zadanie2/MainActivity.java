package com.example.lista1_zadanie2;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.DialogFragment;

import android.app.AlertDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    private String oldNumber = "";
    private String newNumber = "";
    private String number = "";
    private String currentOperator = "";
    private boolean operatorBool = false;
    private boolean operatorPossibility = false;
    private boolean zeroDiv = false;
    private TextView showView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        showView = findViewById(R.id.current_view);

        if (savedInstanceState != null)
            showView.setText(savedInstanceState.getString("output_state"));

    }

    @Override
    public void onRestoreInstanceState(Bundle savedInstanceState){
        super.onRestoreInstanceState(savedInstanceState);

        if (savedInstanceState != null) {
            oldNumber = savedInstanceState.getString("old_number");
            newNumber = savedInstanceState.getString("new_number");
            number = savedInstanceState.getString("_number");
            currentOperator = savedInstanceState.getString("current_operator");
            operatorBool = savedInstanceState.getBoolean("operator_bool");
            operatorPossibility = savedInstanceState.getBoolean("operator_possibility");
            showView.setText(savedInstanceState.getString("output_state"));
        }
    }

    public void Number(View view) {
        switch (view.getId()) {
            case R.id.button_1:
                number += "1";
                break;
            case R.id.button_2:
                number += "2";
                break;
            case R.id.button_3:
                number += "3";
                break;
            case R.id.button_4:
                number += "4";
                break;
            case R.id.button_5:
                number += "5";
                break;
            case R.id.button_6:
                number += "6";
                break;
            case R.id.button_7:
                number += "7";
                break;
            case R.id.button_8:
                number += "8";
                break;
            case R.id.button_9:
                number += "9";
                break;
            case R.id.button_0:
                number += "0";
                break;
        }
        if(!operatorBool) {
            showView.setText(number);
            operatorPossibility = true;
        }
        else {
            showView.setText(oldNumber+currentOperator+number);
            operatorPossibility = false;
        }
    }

    public void Operator(View view) {
        if(operatorPossibility) {
            if(!operatorBool) {
                oldNumber = number;
                number = "";
            }
            operatorBool = true;
            switch (view.getId()) {
                case R.id.button_plus:
                    currentOperator = "+";
                    break;
                case R.id.button_minus:
                    currentOperator = "-";
                    break;
                case R.id.button_mul:
                    currentOperator = "*";
                    break;
                case R.id.button_div:
                    currentOperator = "/";
                    break;
            }
            showView.setText(oldNumber+currentOperator);

        }
    }

    public void Equal(View view) {
        if (operatorBool && !operatorPossibility) {
            newNumber = number;
            double result = Double.parseDouble(oldNumber);
            switch (currentOperator) {
                case "+":
                    result = Double.parseDouble(oldNumber) + Double.parseDouble(newNumber);
                    break;
                case "-":
                    result = Double.parseDouble(oldNumber) - Double.parseDouble(newNumber);
                    break;
                case "/":
                    if (Double.parseDouble(newNumber) != 0.0) result = Double.parseDouble(oldNumber) / Double.parseDouble(newNumber);
                    else {
                        Toast toast = Toast.makeText(getApplicationContext(), "Nie dziel przez zero.", Toast.LENGTH_SHORT);
                        toast.show();
                        zeroDiv = true;
                    }
                    break;
                case "*":
                    result = Double.parseDouble(oldNumber) * Double.parseDouble(newNumber);
                    break;
            }
            if(!zeroDiv) {
                Clear(view);
                showView.setText(result+"");
            }
            else {
                number = "";
                newNumber = "";
                currentOperator = "/";
                operatorPossibility = false;
                operatorBool = true;
                zeroDiv = false;
                showView.setText(oldNumber+currentOperator);
            }
        }
    }

    public void Clear(View view) {
        showView.setText("");
        operatorBool = false;
        number = "";
        oldNumber = "";
        newNumber = "";
        currentOperator = "";
        operatorPossibility = false;
    }

    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putString("output_state", showView.getText().toString());
        outState.putString("old_number", oldNumber);
        outState.putString("new_number", newNumber);
        outState.putString("_number", number);
        outState.putString("current_operator", currentOperator);
        outState.putBoolean("operator_bool", operatorBool);
        outState.putBoolean("operator_possibility", operatorPossibility);
    }
}