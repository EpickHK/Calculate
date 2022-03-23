package com.begginer;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import static com.begginer.Main.LexemeType.*;

public class Main<Public> {

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        System.out.print("Здравствуйте! Введите свой пример: ");
        String primer = scanner.nextLine();

        List<Lexeme> Lexemes = lexAnalyze(primer);
        LexemeBuffer lexemeBuffer = new LexemeBuffer(Lexemes);
        LexemeBuffer lexemeBuffer1 = new LexemeBuffer(Lexemes);
        int value = expr(lexemeBuffer);
        switch (Type(lexemeBuffer1)) {
            case "Arab": System.out.println(primer + " = " + value);
            break;
            case "Rim": System.out.println(primer + " = " + convertNumToRoman(value));
        }

    }

    public enum LexemeType {
        Plus, Minus, MUL, DIV,
        Arab_Number,
        Rim_Number,
        EOF;
    }

    //вспомогательный класс для каждого символа
    public static class Lexeme {
        LexemeType type;
        String value;

        public Lexeme(LexemeType type, String value) {
            this.type = type;
            this.value = value;
        }

        public Lexeme(LexemeType type, Character value) {
            this.type = type;
            this.value = value.toString();
        }

        public String toString() {
            return "Lexeme{" +
                    "type=" + type +
                    ", value='" + value + '\'' + '}';
        }
    }

    public static class LexemeBuffer {
        private int pos;

        public List<Lexeme> lexemes;

        public LexemeBuffer(List<Lexeme> Lexemes) {
            this.lexemes = Lexemes;
        }

        public Lexeme next() {
            return lexemes.get(pos++);
        }

        public void back() {
            pos--;
        }

        public int getPos() {
            return pos;
        }
    }

    private static String convertNumToRoman (int Arab) {
        String[] roman = {"O", "I", "II", "III", "IV", "V", "VI", "VII", "VIII", "IX", "X", "XI", "XII", "XIII", "XIV", "XV", "XVI", "XVII", "XVIII", "XIX", "XX",
                "XXI", "XXII", "XXIII", "XXIV", "XXV", "XXVI", "XXVII", "XXVIII", "XXIX", "XXX", "XXXI", "XXXII", "XXXIII", "XXXIV", "XXXV", "XXXVI", "XXXVII", "XXXVIII", "XXXIX", "XL",
                "XLI", "XLII", "XLIII", "XLIV", "XLV", "XLVI", "XLVII", "XLVIII", "XLIX", "L", "LI", "LII", "LIII", "LIV", "LV", "LVI", "LVII", "LVIII", "LIX", "LX",
                "LXI", "LXII", "LXIII", "LXIV", "LXV", "LXVI", "LXVII", "LXVIII", "LXIX", "LXX",
                "LXXI", "LXXII", "LXXIII", "LXXIV", "LXXV", "LXXVI", "LXXVII", "LXXVIII", "LXXIX", "LXXX",
                "LXXXI", "LXXXII", "LXXXIII", "LXXXIV", "LXXXV", "LXXXVI", "LXXXVII", "LXXXVIII", "LXXXIX", "XC",
                "XCI", "XCII", "XCIII", "XCIV", "XCV", "XCVI", "XCVII", "XCVIII", "XCIX", "C"
        };
        final String s = roman[Arab];
        if (Arab<=0)
            throw new RuntimeException("В римской системе счисления нет числа "+ Arab);
        else return s;
    }

    //анализатор
    public static List<Lexeme> lexAnalyze(String primer) {
        ArrayList<Lexeme> Lexemes = new ArrayList<>();
        int pos = 0;
        int err1=0;
        int err2=0;
        while (pos < primer.length()) {
            char sym = primer.charAt(pos);
            switch (sym) {
                case '*':
                    Lexemes.add(new Lexeme(LexemeType.MUL, sym));
                    pos++;
                    for (int i = primer.indexOf(sym) + 1; i < primer.length(); i++) {
                        int sym1 = primer.charAt(i);
                        if (sym1 == '*' || sym1 == '+' || sym1 == '-' || sym1 == '/') {
                            throw new RuntimeException("Калькулятор не может выполнить второе действие");
                        }
                    }
                    break;
                case '/':
                    Lexemes.add(new Lexeme(LexemeType.DIV, sym));
                    pos++;
                    for (int i = primer.indexOf(sym) + 1; i < primer.length(); i++) {
                        int sym1 = primer.charAt(i);
                        if (sym1 == '*' || sym1 == '+' || sym1 == '-' || sym1 == '/') {
                            throw new RuntimeException("Калькулятор не может выполнить второе действие");
                        }
                    }
                    break;
                case '+':
                    Lexemes.add(new Lexeme(Plus, sym));
                    pos++;
                    for (int i = primer.indexOf(sym) + 1; i < primer.length(); i++) {
                        int sym1 = primer.charAt(i);
                        if (sym1 == '*' || sym1 == '+' || sym1 == '-' || sym1 == '/') {
                            throw new RuntimeException("Калькулятор не может выполнить второе действие");
                        }
                    }
                    break;
                case '-':
                    Lexemes.add(new Lexeme(LexemeType.Minus, sym));
                    pos++;
                    for (int i = primer.indexOf(sym) + 1; i < primer.length(); i++) {
                        int sym1 = primer.charAt(i);
                        if (sym1 == '*' || sym1 == '+' || sym1 == '-' || sym1 == '/') {
                            throw new RuntimeException("Калькулятор не может выполнить второе действие");
                        }
                    }
                    break;
                default:
                    if (sym <= '9' && sym >= '0') {
                        StringBuilder sb = new StringBuilder();
                        err1++;
                        do {
                            sb.append(sym);
                            pos++;
                            if (pos >= primer.length())
                                break;
                            sym = primer.charAt(pos);
                        } while (sym <= '9' && sym >= '0');
                        Lexemes.add(new Lexeme(LexemeType.Arab_Number, sb.toString()));
                    } else if (sym <= 'X' && sym >= 'I') {
                        StringBuilder sb = new StringBuilder();
                        err2++;
                        do {
                            sb.append(sym);
                            pos++;
                            if (pos >= primer.length())
                                break;
                            sym = primer.charAt(pos);
                        } while (sym <= 'X' && sym >= 'I');
                        Lexemes.add(new Lexeme(LexemeType.Rim_Number, sb.toString()));
                    }else {
                        if (sym != ' ') {
                            throw new RuntimeException("Неверный символ: " + sym);
                        }
                        pos++;
                    }
            }
        }
         if(err1>0 && err2>0) {
             throw new RuntimeException("Разные системы счисления");
         }else{Lexemes.add(new Lexeme(LexemeType.EOF, ""));
        return Lexemes;}
    }

    public static String Type(LexemeBuffer Lexemes) {
        String type=" ";
        while (true) {
            Lexeme lexeme = Lexemes.next();
            switch (lexeme.type) {
                case Arab_Number:
                    type = "Arab";
                    break;
                case Rim_Number:
                    type = "Rim";
                default:
                    return type;
            }
        }
    }

    public static int expr(LexemeBuffer Lexemes) {
        Lexeme lexeme = Lexemes.next();
        if (lexeme.type == LexemeType.EOF) {
            return 0;
        } else {
            Lexemes.back();
            return plusminus(Lexemes);
        }
    }

    public static int plusminus(LexemeBuffer Lexemes) {
        int value = multdiv(Lexemes);
        while (true) {
            Lexeme lexeme = Lexemes.next();
            switch (lexeme.type) {
                case Plus:
                    value += multdiv(Lexemes);
                   break;
                case Minus:
                    value -= multdiv(Lexemes);
                default:
                Lexemes.back();
                return value;}
        }

    }

    public static int multdiv(LexemeBuffer Lexemes) {
        int value = factor(Lexemes);
            while (true) {
                Lexeme lexeme = Lexemes.next();
                switch (lexeme.type) {
                    case MUL:
                        value *= factor(Lexemes);
                        break;
                    case DIV:
                        value /= factor(Lexemes);
                    default:
                        Lexemes.back();
                        return value;
                }
            }
    }
    public static int factor(LexemeBuffer Lexemes) {
        Lexeme lexeme = Lexemes.next();
        switch (lexeme.type) {
            case Arab_Number:
                if (Integer.parseInt(lexeme.value) > 10)
                    throw new RuntimeException("Число " +Integer.parseInt(lexeme.value)+" больше 10");
                else
                    return Integer.parseInt(lexeme.value);
            case Rim_Number:
                switch (lexeme.value){
                    case "I": return 1;
                    case "II": return 2;
                    case "III": return 3;
                    case "IV": return 4;
                    case "V": return 5;
                    case "VI": return 6;
                    case "VII": return 7;
                    case "VIII": return 8;
                    case "IX": return 9;
                    case "X": return 10;
                    default:
                        throw new RuntimeException("Задача не удовлетворяет условиям");
                }
            default:
                throw new RuntimeException("Ошибка синтаксиса");
        }
    }
}






