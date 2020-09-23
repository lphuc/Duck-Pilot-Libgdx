package com.phucle.fadu.utils;


import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.scenes.scene2d.ui.Dialog;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.phucle.fadu.game.DupiAssets;

/*
 * Created by Phuc Engineer on 12/1/2018.
 */
public class MyDialog extends Dialog {

    public MyDialog(String title, Skin skin, String windowStyleName) {
        super(title, skin, windowStyleName);
    }

    @Override
    public Dialog button(String text, Object object, TextButton.TextButtonStyle buttonStyle) {
        TextButton button = new TextButton(text, buttonStyle);
        button.setSize(DupiAssets.sizeUnit * 6, DupiAssets.sizeUnit * 3);
        button.getLabel().setFontScale(2);
        button.getStyle().fontColor.add(Color.YELLOW);
        return button(button, object);
    }

    @Override
    public Dialog text(String text, Label.LabelStyle labelStyle) {
        labelStyle.font = DupiAssets.getInstance().getFont32();

        Label label = new Label(text, labelStyle);
        label.setColor(Color.BLACK);
        return text(label);
    }

    @Override
    public float getPrefWidth() {
        return DupiAssets.screenHeight / 3;
    }

    @Override
    public float getPrefHeight() {
        return DupiAssets.screenWidth / 3;
    }

}
