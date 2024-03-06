package edu.jsu.mcis.cs408.calculator;

import android.nfc.Tag;
import android.util.Log;

import java.math.BigDecimal;

public class CalculatorModel extends AbstractModel {
    public static final String TAG = "CALCULATOR";

    private enum InputSide {
        LEFTHAND {
            @Override
            public InputSide switchSide() {
                return RIGHTHAND;
            }
        },
        RIGHTHAND {
            @Override
            public InputSide switchSide() {
                return LEFTHAND;
            }
        };

        public abstract InputSide switchSide();
    }

    private CalculatorState state;
    private CalculatorState prevState;
    private InputSide side;

    private StringBuilder sb;

    private String displayText;
    private String btnPressed;
    private String left_input;
    private String operator;
    private String right_input;
    private String output;
    private BigDecimal left;
    private BigDecimal right;
    private BigDecimal out;
    private boolean decimal;
    private boolean error;


    public CalculatorModel() {
        state = CalculatorState.CLEAR;
        prevState = state;
        side = InputSide.LEFTHAND;
        clearCalculator();
    }

    private void stateHandler() {
        CalculatorState currentState = getState();

        switch (currentState) {
            case CLEAR:
                clearCalculator();
                break;
            case ERROR:
                clearCalculator();
                setDisplayText("ERROR");
                error = true;
                break;
            case INPUT:
                appendInput(btnPressed.substring(3));
                break;
            case OPERATION:
                setOperator(btnPressed);
                break;
            case RESULT:
                if (prevState.equals(CalculatorState.RESULT)) {
                    left_input = output;
                } else {
                    right_input = getDisplayText();
                }
                calculate(left_input, right_input);
        }
    }


    private void readBtnPressed() {
        String currentBtn = getBtnPressed();

        switch (currentBtn) {
            case "btn0":
            case "btn1":
            case "btn2":
            case "btn3":
            case "btn4":
            case "btn5":
            case "btn6":
            case "btn7":
            case "btn8":
            case "btn9":
            case "btn.":
                if (error) return;
                setState(CalculatorState.INPUT);
                break;
            case "btnPlus":
            case "btnMinus":
            case "btnMulti":
            case "btnDiv":
                if (error) return;
                setState(CalculatorState.OPERATION);
                break;
            case "btnPercent":
                if (error) return;
                Percent();
                break;
            case "btnSqrt":
                if (error) return;
                Sqrt();
                break;
            case "btnSign":
                if (error) return;
                setSign();
                break;
            case "btnEquals":
                if (error) return;
                setState(CalculatorState.RESULT);
                break;
            case "btnClear":
                setState(CalculatorState.CLEAR);
                break;
        }

    }


    private void appendInput(String input_char) {
        if (getDisplayText().length() > 14) return;
        if (input_char.equals(".")) {
            if (decimal) return;
            decimal = true;
            if (getDisplayText().equals("0")) {
                sb = new StringBuilder("0.");
                setDisplayText(sb.toString());
                return;
            }
        }
        if (input_char.equals("0") && getDisplayText().equals("0")) return;

        switch (prevState) {
            case ERROR:
                return;
            case CLEAR:
            case INPUT:
                sb = (getDisplayText().equals("0")) ? new StringBuilder() : new StringBuilder(getDisplayText());
                sb.append(input_char);
                break;
            case OPERATION:
                sb = new StringBuilder();
                sb.append(input_char);

        }


        setDisplayText(sb.toString());

    }

    private void setSign() {
        BigDecimal signed = new BigDecimal(getDisplayText());
        signed = signed.negate();
        setDisplayText(signed.toEngineeringString());

    }

    private void Sqrt() {
        try {
            BigDecimal radicand = new BigDecimal(getDisplayText());
            radicand = BigDecimal.valueOf(Math.sqrt(radicand.toBigInteger().doubleValue()));
            setDisplayText(radicand.toEngineeringString());
        } catch (Exception e) {
            setState(CalculatorState.ERROR);
        }
    }

    private void Percent() {
        if (!side.equals(InputSide.RIGHTHAND)) return;
        right_input = getDisplayText();

        left = new BigDecimal(left_input);
        right = new BigDecimal(right_input);

        right = left.multiply(right.divide(BigDecimal.valueOf(100)));

        right_input = right.toEngineeringString();
        setDisplayText(right_input);
    }

    private void setOperator(String op) {
        if (prevState.equals(CalculatorState.OPERATION) || prevState.equals(CalculatorState.ERROR))
            return;
        if (prevState.equals(CalculatorState.RESULT)) {
            left_input = output;
            right_input = "";
            side = side.switchSide();
        }

        decimal = false;

        switch (side) {
            case LEFTHAND:
                left_input = getDisplayText();
                side = side.switchSide();
                break;
            case RIGHTHAND:
                right_input = getDisplayText();
                side = side.switchSide();
                break;
        }

        if (!left_input.isEmpty() && !right_input.isEmpty()) {
            calculate(left_input, right_input);
            left_input = output;
            right_input = "";
            side = side.switchSide();
        }


        operator = op.substring(3);
    }

    private void calculate(String lhs, String rhs) {
        left = new BigDecimal(lhs);
        right = new BigDecimal(rhs);

        try {
            switch (operator) {
                case "Plus":
                    out = left.add(right);
                    break;
                case "Minus":
                    out = left.subtract(right);
                    break;
                case "Multi":
                    out = left.multiply(right);
                    break;
                case "Div":
                    out = left.divide(right);
                    break;
            }
        } catch (Exception e) {
            setState(CalculatorState.ERROR);
        }


        if (out.signum() == 0 || out.scale() <= 0 || out.stripTrailingZeros().scale() <= 0)
            out = out.stripTrailingZeros();
        output = out.toEngineeringString();
        setDisplayText(output);
    }

    private void clearCalculator() {
        setDisplayText("0");
        side = InputSide.LEFTHAND;
        left_input = "";
        right_input = "";
        output = "";
        left = null;
        right = null;
        out = null;
        decimal = false;
        error = false;

        Log.i(TAG, "Calculator CLEARED");
    }


    public CalculatorState getState() {
        return state;
    }

    public void setState(CalculatorState newState) {
        prevState = getState();
        this.state = newState;
        stateHandler();
    }

    public String getDisplayText() {
        return displayText;
    }

    public void setDisplayText(String newText) {
        String oldDisplayText = this.displayText;
        this.displayText = newText;

        firePropertyChange(CalculatorController.ELEMENT_DISPLAY_TEXT, oldDisplayText, displayText);

    }


    public String getBtnPressed() {
        return btnPressed;
    }

    public void setBtnPressed(String btnPressed) {
        this.btnPressed = btnPressed;
        readBtnPressed();
    }
}
