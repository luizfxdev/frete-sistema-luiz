package br.com.nextlog.util;

public final class CnpjValidator {

    private static final int[] PESOS_1 = {5, 4, 3, 2, 9, 8, 7, 6, 5, 4, 3, 2};
    private static final int[] PESOS_2 = {6, 5, 4, 3, 2, 9, 8, 7, 6, 5, 4, 3, 2};

    private CnpjValidator() {}

    public static String limpar(String cnpj) {
        if (cnpj == null) return null;
        return cnpj.replaceAll("\\D", "");
    }

    public static boolean isValido(String cnpj) {
        String numeros = limpar(cnpj);
        if (numeros == null || numeros.length() != 14) return false;
        if (numeros.matches("(\\d)\\1{13}")) return false;

        try {
            int d1 = calcularDigito(numeros, PESOS_1);
            int d2 = calcularDigito(numeros, PESOS_2);
            return d1 == Character.getNumericValue(numeros.charAt(12))
                && d2 == Character.getNumericValue(numeros.charAt(13));
        } catch (Exception e) {
            return false;
        }
    }

    private static int calcularDigito(String base, int[] pesos) {
        int soma = 0;
        for (int i = 0; i < pesos.length; i++) {
            soma += Character.getNumericValue(base.charAt(i)) * pesos[i];
        }
        int resto = soma % 11;
        return resto < 2 ? 0 : 11 - resto;
    }
}