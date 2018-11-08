package br.com.stanzione.gigigotest.util;

import java.text.NumberFormat;
import java.util.Locale;

public class CurrencyUtil {

    private static Locale locale = new Locale("pt", "BR");

    public static String convertToBrazilianCurrency(double amount){
        return NumberFormat.getCurrencyInstance(locale).format(amount);
    }

}
