
import java.io.IOException;
import java.lang.invoke.StringConcatFactory;
import java.util.HashMap;
import java.util.HashSet;
import java.util.InputMismatchException;
import java.util.Scanner;

public class Main {
    public static void main(String[] arguments) throws IOException {
        System.out.println("Введите выражение:");
        Scanner scanner = new Scanner(System.in);
        String input = scanner.nextLine();

        System.out.println(calc(input));
    }

    public static String calc(String input) throws IOException {
        Converter converter = new Converter();
        String answer = new String();
        char[] charInput = input.toCharArray();
        int number1;
        int number2;
        Character currentSign = ' ';
        int isCorrectSign = 0;
        String[] component = input.split(" ");

        if(component.length > 3 || component.length <= 1)
            throw new IOException("Не математическое выражение!");



        for(int i = 0; i < charInput.length; i++){
            if(charInput[i] == '+' || charInput[i] == '-' || charInput[i] == '*' || charInput[i] == '/'){
                currentSign = charInput[i];
                isCorrectSign++;
            }
        }

        if(isCorrectSign != 1){
            throw new IOException("возможны только такие математические операции, как: сложение, вычитание, деление и умножение!");
        }

        if(currentSign == '+'){
            component = input.split(" \\+ ");
        } else if (currentSign == '-') {
            component = input.split(" - ");
        } else if (currentSign == '*') {
            component = input.split(" \\* ");
        } else if (currentSign == '/') {
            component = input.split(" / ");
        }

        if(!converter.isRoman(component[0]) && !converter.isRoman(component[1])){
            number1 = Integer.parseInt(component[0]);
            number2 = Integer.parseInt(component[1]);

            answer = Integer.toString(converter.calcInArabic(number1, number2, currentSign));

        } else if (!converter.isRoman(component[0]) == !converter.isRoman(component[1])) {
            number1 = converter.romanToArabic(component[0]);
            number2 = converter.romanToArabic(component[1]);

            int result = converter.calcInArabic(number1, number2, currentSign);

            if(result > 0)
                return converter.arabicToRoman(result);
            else
                throw new ArithmeticException("Отрицательных чисел нет в римской системе счисления!");


        } else {
            throw new IOException("Вы используете различные системы счисления");
        }

        return answer;
    }

}

class Converter{
    private HashMap<Character, Integer> romanMap = new HashMap<>();

    public Converter(){
        romanMap.put('I', 1);
        romanMap.put('V', 5);
        romanMap.put('X', 10);
        romanMap.put('L', 50);
        romanMap.put('C', 100);
        romanMap.put('D', 500);
        romanMap.put('M', 1000);
    }

    public boolean inArrange(int currentNumber){
        if(currentNumber >= 1 && currentNumber <= 10)
            return true;
        else
            return false;
    }
    public int calcInArabic(int firstNumber, int secondNumber, char currentSign) throws IOException {
        int answer = -1;
        if(inArrange(firstNumber) && inArrange(secondNumber)){
            switch (currentSign) {
                case ('+') -> {
                    answer = firstNumber + secondNumber;
                }
                case ('-') -> {
                    answer = firstNumber - secondNumber;
                }
                case ('*') -> {
                    answer = firstNumber * secondNumber;
                }
                case ('/') -> {
                    try {
                        answer = firstNumber / secondNumber;
                    } catch (ArithmeticException | InputMismatchException e) {
                        System.out.println("Exception : " + e);
                        System.out.println("Результатом операции могут быть только целые числа!");
                    }
                }
                default -> throw new IllegalArgumentException("Неверный знак операции");
            }
        } else {
            throw new IOException("Числа не находятся в диапазоне от 1 до 10!");

        }
        return answer;
    }

    public boolean isRoman(String number){
        if(romanMap.containsKey(number.charAt(0))){
            return true;
        } else {
            return false;
        }
    }

    public int romanToArabic(String roman) {
        int result = 0;
        int prevValue = 0;

        for (int i = roman.length() - 1; i >= 0; i--) {
            int currentValue = romanMap.get(roman.charAt(i));

            if (currentValue < prevValue) {
                result -= currentValue;
            } else {
                result += currentValue;
            }

            prevValue = currentValue;
        }

        return result;
    }

    public String arabicToRoman(int arabic){
        String[] romanSymbols = {"M", "CM", "D", "CD", "C", "XC", "L", "XL", "X", "IX", "V", "IV", "I"};
        int[] arabicValues = {1000, 900, 500, 400, 100, 90, 50, 40, 10, 9, 5, 4, 1};

        StringBuilder roman = new StringBuilder();

        for (int i = 0; i < arabicValues.length; i++) {
            while (arabic >= arabicValues[i]) {
                roman.append(romanSymbols[i]);
                arabic -= arabicValues[i];
            }
        }

        return roman.toString();
    }
}
