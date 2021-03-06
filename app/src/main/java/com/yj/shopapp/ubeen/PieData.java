package com.yj.shopapp.ubeen;

/**
 * Created by LK on 2018/3/15.
 *
 * @author LK
 */

public class PieData {
    private String text;
    private float value;
    private float Angle;
    private float Percentage;
    private int color;
    private float CurrentStartAngle;
    private float startX;
    private float startY;

    public float getStartX() {
        return startX;
    }

    public void setStartX(float startX) {
        this.startX = startX;
    }

    public float getStartY() {
        return startY;
    }

    public void setStartY(float startY) {
        this.startY = startY;
    }

    public int getColor() {
        return color;
    }

    public void setColor(int color) {
        this.color = color;
    }

    public float getCurrentStartAngle() {
        return CurrentStartAngle;
    }

    public void setCurrentStartAngle(float currentStartAngle) {
        CurrentStartAngle = currentStartAngle;
    }

    public float getPercentage() {
        return Percentage;
    }

    public void setPercentage(float percentage) {
        Percentage = percentage;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public float getValue() {
        return value;
    }

    public void setValue(float value) {
        this.value = value;
    }

    public float getAngle() {
        return Angle;
    }

    public void setAngle(float angle) {
        Angle = angle;
    }

    public PieData() {
    }

    public PieData(String text, float value) {
        this.text = text;
        this.value = value;
    }
}
