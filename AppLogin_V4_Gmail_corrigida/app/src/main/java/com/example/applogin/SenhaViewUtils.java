package com.example.applogin;

import android.annotation.SuppressLint;
import android.text.InputType;
import android.view.MotionEvent;
import android.widget.EditText;

public final class SenhaViewUtils {
    private SenhaViewUtils() {}

    @SuppressLint("ClickableViewAccessibility")
    public static void configurarOlhoSenha(EditText campoSenha) {
        campoSenha.setOnTouchListener((v, event) -> {
            if (campoSenha.getCompoundDrawablesRelative()[2] == null) return false;

            int largura = campoSenha.getCompoundDrawablesRelative()[2].getBounds().width();
            float limite = campoSenha.getWidth() - campoSenha.getPaddingEnd()
                    - largura - campoSenha.getCompoundDrawablePadding();
            if (event.getX() < limite) return false;

            if (event.getAction() == MotionEvent.ACTION_DOWN) {
                campoSenha.setInputType(InputType.TYPE_CLASS_TEXT
                        | InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
                campoSenha.setSelection(campoSenha.length());
                return true;
            }

            if (event.getAction() == MotionEvent.ACTION_UP
                    || event.getAction() == MotionEvent.ACTION_CANCEL) {
                campoSenha.setInputType(InputType.TYPE_CLASS_TEXT
                        | InputType.TYPE_TEXT_VARIATION_PASSWORD);
                campoSenha.setSelection(campoSenha.length());
                return true;
            }
            return true;
        });
    }
}
