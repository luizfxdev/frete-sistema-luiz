package br.com.nextlog.util;

public final class CpfValidator {

    private CpfValidator() {}

    public static String limpar(String cpf) {
        if (cpf == null) return null;
        return cpf.replaceAll("\\D", "");
    }

    public static boolean isValido(String cpf) {
        String numeros = limpar(cpf);
        if (numeros == null || numeros.length() != 11) return false;
        if (numeros.matches("(\\d)\\1{10}")) return false;

        try {
            int d1 = calcularDigito(numeros, 9, 10);
            int d2 = calcularDigito(numeros, 10, 11);
            return d1 == Character.getNumericValue(numeros.charAt(9))
                && d2 == Character.getNumericValue(numeros.charAt(10));
        } catch (Exception e) {
            return false;
        }
    }

    private static int calcularDigito(String base, int tamanho, int pesoInicial) {
        int soma = 0;
        for (int i = 0; i < tamanho; i++) {
            soma += Character.getNumericValue(base.charAt(i)) * (pesoInicial - i);
        }
        int resto = soma % 11;
        return resto < 2 ? 0 : 11 - resto;
    }
}