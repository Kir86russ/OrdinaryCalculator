package com.example.ordinarycalculator;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.widget.TextView;

import net.objecthunter.exp4j.Expression;
import net.objecthunter.exp4j.ExpressionBuilder;

public class MainActivity extends AppCompatActivity {

    private static TextView textInput;
    private static TextView textResult;
    private TextView buttonDel;
    private TextView buttonC;
    private TextView buttonEqual;
    private boolean isSecretTime;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        textInput = findViewById(R.id.textInput);
        textResult = findViewById(R.id.textResult);

        buttonDel = findViewById(R.id.button_del);
        buttonDel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                textInput.setText("");
                textResult.setText("");
            }
        });

        buttonC = findViewById(R.id.button_C);
        buttonC.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String inputLine = textInput.getText().toString();
                if (!inputLine.equals("")) {
                    textInput.setText(inputLine.substring(0, inputLine.length() - 1));
                    textResult.setText("");
                }
            }
        });

        buttonEqual = findViewById(R.id.button_equal);
        buttonEqual.setOnTouchListener(new View.OnTouchListener() {
            long lastDown, duration;


            @Override
            public boolean onTouch(View view, MotionEvent event) {
                isSecretTime = false;

                solveExpression();

                if (event.getAction() == MotionEvent.ACTION_DOWN) {
                    lastDown = System.currentTimeMillis();
                } else if (event.getAction() == MotionEvent.ACTION_UP) {
                    duration = System.currentTimeMillis() - lastDown;
                }

                if (duration >= 4000)
                    isSecretTime = true;

                return true;
            }
        });

        textInput.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                boolean secretPlace = motionEvent.getX() < textInput.getWidth() / 4 && motionEvent.getY() < textInput.getHeight();

                if (isSecretTime && secretPlace) {
                    Intent intent = new Intent(getApplicationContext(), SecretActivity.class);
                    startActivity(intent);
                    isSecretTime = false;
                }
                return true;
            }
        });
    }


    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        textInput.setText(savedInstanceState.getString("textInput"));
        textResult.setText(savedInstanceState.getString("textResult"));
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putString("textInput", textInput.getText().toString());
        outState.putString("textResult", textResult.getText().toString());
    }

    public void appendText(View view) {
        if (!textResult.getText().toString().equals("")) {
            textInput.setText("");
            textResult.setText("");
        }
        TextView textView = (TextView) view;
        textInput.setText(textInput.getText().toString() + textView.getText().toString());
    }

    private static void solveExpression() {
        try {
            Expression expressionBuilder = new ExpressionBuilder(textInput.getText().toString()).build();
            double result = expressionBuilder.evaluate();
            if (result % 1 == 0)
                textResult.setText(String.valueOf(Integer.valueOf((int) result)));
            else
                textResult.setText(String.valueOf(result));
        } catch (Exception e) {
            textInput.setText("");
            textResult.setText("");
        }
    }

}