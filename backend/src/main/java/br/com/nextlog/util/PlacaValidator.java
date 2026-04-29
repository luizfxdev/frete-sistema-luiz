package br.com.nextlog.util;

import java.util.regex.Pattern;

public final class PlacaValidator {

    private static final Pattern ANTIGA   = Pattern.compile("^[A-Z]{3}[0-9]{4}$");
    private static final Pattern MERCOSUL = Pattern.compile("^[A-Z]{3}[0-9][A-Z][0-9]{2}$");

    private PlacaValidator() {}

    public static String normalizar(String placa) {
        if (placa == null) return null;
        return placa.replaceAll("[^A-Za-z0-9]", "").toUpperCase();
    }

    public static boolean isValida(String placa) {
        String norm = normalizar(placa);
        if (norm == null) return false;
        return ANTIGA.matcher(norm).matches() || MERCOSUL.matcher(norm).matches();
    }
}