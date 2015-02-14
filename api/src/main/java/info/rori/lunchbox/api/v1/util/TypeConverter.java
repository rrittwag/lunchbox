package info.rori.lunchbox.api.v1.util;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

/**
 * Hilfsklasse für die Konvertierung zwischen Datentypen.
 */
public class TypeConverter {
    private TypeConverter() {
    }

    public static LocalDate toDate(String str) {
        if (str == null)
            return null;
        try {
            return LocalDate.parse(str, DateTimeFormatter.ISO_DATE);
        } catch (DateTimeParseException exc) {
            return null;
        }
    }

}
