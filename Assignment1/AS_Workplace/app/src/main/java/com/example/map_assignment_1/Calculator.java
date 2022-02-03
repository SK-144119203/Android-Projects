package com.example.map_assignment_1;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;

public class Calculator extends AppCompatActivity {

    LinearLayout layout_Advance;
    private TextView textViewScreen;
    Button btnMode;

    ArrayList<String> inputStr = new ArrayList<>();
    static int result = 0;
    static boolean checkFlag = false, equalFlag = false;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        layout_Advance = findViewById(R.id.layout_AdvanceFeature);
        textViewScreen = findViewById(R.id.textViewScreen);
        btnMode = findViewById(R.id.btnCalculatorMode);

        btnMode.setOnClickListener(view -> {
            if(getString(R.string.AdvanceVersionLogic).equals(btnMode.getText().toString()))
            {
                layout_Advance.setVisibility(View.VISIBLE);
                btnMode.setText(getString(R.string.StandardVersionLogic));
            }
            else
            {
                layout_Advance.setVisibility(View.INVISIBLE);
                btnMode.setText(getString(R.string.AdvanceVersionLogic));
            }
        });
    }

    void push(String userString){
        if(userString.equals("clear"))
        {
            inputStr = new ArrayList<>();
            result = 0;
            equalFlag = false;
            checkFlag = false;
        }
        else
        {
            if(equalFlag)
            {
                inputStr = new ArrayList<>();
                result = 0;
                equalFlag = false;
                checkFlag = false;
            }
            inputStr.add(userString);

            if(userString.equals("=") && !equalFlag)
            {
                equalFlag = true;
                int final_Result = calculate();

                if(checkFlag){
                    inputStr.add(String.valueOf(final_Result));
                }
            }
        }
        textViewScreen.setText(inputStr.toString().replace("[", "").replace("]", "").replace(",", ""));
    }

    public int calculate()
    {
        if(inputStr.size()  % 2 == 0)
        {
            int tempNum;
            for(int i = 0; i < inputStr.size()-1; i++)
            {
                if(i % 2 == 0)
                {
                    if(!isNumber(inputStr.get(i).replace("[", "").replace("]", "").replace(",", "")))
                        break;
                }
                else
                {
                    if(!isOperator(inputStr.get(i).replace("[", "").replace("]", "").replace(",", "")))
                        break;
                }

                if(i >=2 && (i % 2) == 0)
                {
                    tempNum = Integer.parseInt(inputStr.get(i).replace("[", "").replace("]", "").replace(",", ""));
                    if(i == 2)
                    {
                        result += Integer.parseInt(inputStr.get(0).replace("[", "").replace("]", "").replace(",", ""));
                    }
                    switch (inputStr.get(i-1).replace("[", "").replace("]", "").replace(",", "")) {
                        case "+":
                            result += tempNum;
                            break;
                        case "-":
                            result -= tempNum;
                            break;
                        case "*":
                            result *= tempNum;
                            break;
                        case "/":
                            result /= tempNum;
                            break;
                        case "%":
                            result %= tempNum;
                            break;
                        case "pow":
                            result = (int) Math.pow(result, tempNum);
                            break;
                        case "Max":
                            result = Math.max(result, tempNum);
                            break;
                        case "Min":
                            result = Math.min(result, tempNum);
                            break;
                    }
                }

                if(i == inputStr.size() - 2)
                    checkFlag = true;
            }
        }
        if(!checkFlag){
            inputStr.add("Not an Operator");
        }
        return result;
    }

    public boolean isOperator(String str)
    {
        boolean isValid = false;

        switch (str)
        {
            case "+":
            case "-":
            case "*":
            case "/":
            case "%":
            case "pow":
            case "Max":
            case "Min":
                isValid = true;
                break;
            default:
                break;
        }
        return  isValid;
    }

    public boolean isNumber(String str)
    {
        boolean isValid = false;

        switch (str)
        {
            case "0":
            case "1":
            case "2":
            case "3":
            case "4":
            case "5":
            case "6":
            case "7":
            case "8":
            case "9":
                isValid = true;
                break;
            default:
                break;
        }
        return  isValid;
    }

    @SuppressLint("NonConstantResourceId")
    public void btnClickEvent(View view){
        switch (view.getId())
        {
            case R.id.btn0:push("0");
                break;
            case R.id.btn1:push("1");
                break;
            case R.id.btn2:push("2");
                break;
            case R.id.btn3:push("3");
                break;
            case R.id.btn4:push("4");
                break;
            case R.id.btn5:push("5");
                break;
            case R.id.btn6:push("6");
                break;
            case R.id.btn7:push("7");
                break;
            case R.id.btn8:push("8");
                break;
            case R.id.btn9:push("9");
                break;
            case R.id.btnClear:push("clear");
                break;
            case R.id.btnPlus:push("+");
                break;
            case R.id.btnMinus:push("-");
                break;
            case R.id.btnMultiply:push("*");
                break;
            case R.id.btnDivide:push("/");
                break;
            case R.id.btnEqual:push("=");
                break;
            case R.id.btnModulus:push("%");
                break;
            case R.id.btnPower:push("pow");
                break;
            case R.id.btnMaximum:push("Max");
                break;
            case R.id.btnMinimum:push("Min");
                break;
        }
    }
}
