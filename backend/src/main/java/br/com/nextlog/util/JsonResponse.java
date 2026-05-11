package br.com.nextlog.util;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Collection;
import java.util.Map;

public final class JsonResponse {

    private static final DateTimeFormatter ISO_DATE_TIME = DateTimeFormatter.ISO_LOCAL_DATE_TIME;
    private static final DateTimeFormatter ISO_DATE = DateTimeFormatter.ISO_LOCAL_DATE;

    private JsonResponse() {}

    public static void enviar(HttpServletResponse resp, int status, Object payload) throws IOException {
        resp.setStatus(status);
        resp.setContentType("application/json;charset=UTF-8");
        try (PrintWriter out = resp.getWriter()) {
            out.write(toJson(payload));
        }
    }

    public static void ok(HttpServletResponse resp, Object payload) throws IOException {
        enviar(resp, HttpServletResponse.SC_OK, payload);
    }

    public static void erro(HttpServletResponse resp, int status, String mensagem) throws IOException {
        StringBuilder sb = new StringBuilder();
        sb.append("{\"erro\":").append(escape(mensagem)).append("}");
        resp.setStatus(status);
        resp.setContentType("application/json;charset=UTF-8");
        try (PrintWriter out = resp.getWriter()) {
            out.write(sb.toString());
        }
    }

    public static String toJson(Object obj) {
        StringBuilder sb = new StringBuilder();
        appendValue(sb, obj);
        return sb.toString();
    }

    private static void appendValue(StringBuilder sb, Object value) {
        if (value == null) { sb.append("null"); return; }
        if (value instanceof Boolean) { sb.append(value); return; }
        if (value instanceof Number) { sb.append(value); return; }
        if (value instanceof BigDecimal) { sb.append(((BigDecimal) value).toPlainString()); return; }
        if (value instanceof LocalDate) { sb.append(escape(((LocalDate) value).format(ISO_DATE))); return; }
        if (value instanceof LocalDateTime) { sb.append(escape(((LocalDateTime) value).format(ISO_DATE_TIME))); return; }
        if (value instanceof Enum) { sb.append(escape(((Enum<?>) value).name())); return; }
        if (value instanceof Map) { appendMap(sb, (Map<?, ?>) value); return; }
        if (value instanceof Collection) { appendCollection(sb, (Collection<?>) value); return; }
        if (value.getClass().isArray()) { appendCollection(sb, java.util.Arrays.asList((Object[]) value)); return; }
        sb.append(escape(value.toString()));
    }

    private static void appendMap(StringBuilder sb, Map<?, ?> map) {
        sb.append('{');
        boolean first = true;
        for (Map.Entry<?, ?> e : map.entrySet()) {
            if (!first) sb.append(',');
            sb.append(escape(String.valueOf(e.getKey()))).append(':');
            appendValue(sb, e.getValue());
            first = false;
        }
        sb.append('}');
    }

    private static void appendCollection(StringBuilder sb, Collection<?> col) {
        sb.append('[');
        boolean first = true;
        for (Object item : col) {
            if (!first) sb.append(',');
            appendValue(sb, item);
            first = false;
        }
        sb.append(']');
    }

    private static String escape(String s) {
        if (s == null) return "null";
        StringBuilder sb = new StringBuilder(s.length() + 2);
        sb.append('"');
        for (int i = 0; i < s.length(); i++) {
            char c = s.charAt(i);
            switch (c) {
                case '"': sb.append("\\\""); break;
                case '\\': sb.append("\\\\"); break;
                case '\n': sb.append("\\n"); break;
                case '\r': sb.append("\\r"); break;
                case '\t': sb.append("\\t"); break;
                default:
                    if (c < 0x20) sb.append(String.format("\\u%04x", (int) c));
                    else sb.append(c);
            }
        }
        sb.append('"');
        return sb.toString();
    }
}