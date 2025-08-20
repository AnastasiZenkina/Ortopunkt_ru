package com.ortopunkt.telegram.aiintegration;
import java.util.regex.Pattern;

public class TextSanitizer {

    // Шаблоны потенциально опасных данных
    private static final Pattern FIO_PATTERN = Pattern.compile("\\b[А-ЯЁ][а-яё]+\\s[А-ЯЁ][а-яё]+(\\s[А-ЯЁ][а-яё]+)?\\b");
    private static final Pattern PHONE_PATTERN = Pattern.compile("\\b\\+?\\d{10,13}\\b");
    private static final Pattern DATE_PATTERN = Pattern.compile("\\b\\d{2}[./-]\\d{2}[./-]\\d{4}\\b");
    private static final Pattern ADDRESS_WORDS = Pattern.compile("\\b(ул\\.?|улица|дом|квартира|проживаю|прописан|адрес)\\b", Pattern.CASE_INSENSITIVE);
    private static final Pattern PASSPORT_PATTERN = Pattern.compile("\\b(паспорт|серия|номер|СНИЛС|ИНН)\\b", Pattern.CASE_INSENSITIVE);
    private static final Pattern EMAIL_PATTERN = Pattern.compile("[\\w.-]+@[\\w.-]+\\.[a-zA-Z]{2,6}");

    public static boolean isSafe(String text) {
        if (text == null || text.isBlank()) return true;

        return !(
                FIO_PATTERN.matcher(text).find()
                        || PHONE_PATTERN.matcher(text).find()
                        || DATE_PATTERN.matcher(text).find()
                        || ADDRESS_WORDS.matcher(text).find()
                        || PASSPORT_PATTERN.matcher(text).find()
                        || EMAIL_PATTERN.matcher(text).find()
        );
    }
}
