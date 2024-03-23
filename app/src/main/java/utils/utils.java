package utils;

import android.util.Patterns;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Locale;

public class utils {
    public static boolean isValidEmail(String email) {
        return Patterns.EMAIL_ADDRESS.matcher(email).matches();
    }

    public static boolean isValidDate(String date) {
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
        sdf.setLenient(false);
        try {
            sdf.parse(date);
            return true;
        } catch (ParseException e) {
            return false;
        }
    }

    public static boolean isValidPhoneNumber(String phoneNumber) {
        //Kiểm tra số điện thoại có từ 9 - 12 chữ số và không chứa ký tự đặc biệt
        return phoneNumber.matches("\\d{9,12}");
    }

    public static boolean isValidIntNumer(int number) {
        return number > 0 && number < 10;
    }

    public static boolean isValidFullName(String fullName) {
        //Tách họ tên thành các từ
        String[] words = fullName.split("\\s+");
        if (words.length < 2) {
            return false; //Tên phải có 2 từ trở lên
        }
        //Kiểm tra mỗi từ trong họ tên
        for (String word : words) {
            //Kiểm tra ký tự đầu tiên của từ
            if (!Character.isUpperCase(word.charAt(0))) {
                return false;
            }

            //Kiểm tra các ký tự còn lại của từ có viết thường hay không
            for (int i = 1; i < word.length(); i++) {
                if (!Character.isLetter(word.charAt(i)) && !Character.isWhitespace(word.charAt(i))) {
                    return false;
                }
            }
        }
        return true;
    }
}
