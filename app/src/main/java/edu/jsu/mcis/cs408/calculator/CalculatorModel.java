package edu.jsu.mcis.cs408.calculator;

import java.math.BigDecimal;

public class CalculatorModel {
    private CalculatorState state;

    private BigDecimal lhs;
    private BigDecimal rhs;
    private BigDecimal result;


    public CalculatorModel() {
        state = CalculatorState.CLEAR;

        lhs = BigDecimal.ZERO;
        rhs = BigDecimal.ZERO;
        result = BigDecimal.ZERO;
    }

    public CalculatorState getState() {
        return state;
    }

    public void setState(CalculatorState state) {
        this.state = state;
    }

    public BigDecimal getLhs() {
        return lhs;
    }

    public void setLhs(BigDecimal lhs) {
        this.lhs = lhs;
    }

    public BigDecimal getRhs() {
        return rhs;
    }

    public void setRhs(BigDecimal rhs) {
        this.rhs = rhs;
    }

    public BigDecimal getResult() {
        return result;
    }

    public void setResult(BigDecimal result) {
        this.result = result;
    }
}
