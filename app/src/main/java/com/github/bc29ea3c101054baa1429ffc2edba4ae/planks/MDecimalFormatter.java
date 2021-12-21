package com.github.bc29ea3c101054baa1429ffc2edba4ae.planks;

import com.github.mikephil.charting.formatter.ValueFormatter;

import java.text.DecimalFormat;

public class MDecimalFormatter extends ValueFormatter {

    private DecimalFormat mFormat;

    public MDecimalFormatter() {
        mFormat = new DecimalFormat("#");
    }

    @Override
    public String getFormattedValue(float value) {
        return mFormat.format(value);
    }
}