package edu.jsu.mcis.cs408.calculator;

public class CalculatorController extends AbstractController{

    private CalculatorModel model;
    private MainActivity view;

    public static final String ELEMENT_DISPLAY_TEXT = "DisplayText";
    public static final String ELEMENT_BUTTON_PRESSED = "BtnPressed";

    public void changeDisplayText(String newText) { setModelProperty(ELEMENT_DISPLAY_TEXT, newText);}

    public void sendBtnPress(String newBtnPressed) { setModelProperty(ELEMENT_BUTTON_PRESSED, newBtnPressed);

    }
}
