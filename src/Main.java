import java.util.List;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
public class Main {
    public static void main(String[] args) throws CalcException {
        System.out.println(
                "Калькулятор готов к работе. Введите выражение согласно условию. Для завершения введите 'q': ");
        Scanner s = new Scanner(System.in);
        String input = s.nextLine();
        while (! input.equals("q")  ) {
            System.out.println("Порешали: " + calc(input));
            input = s.nextLine();
        }
    }
    public static String calc(String input) throws CalcException {
        String arithOpers = "\\s*([+-/*])\\s*";
        // Prepare patterns for Arabic notation recognition
        String arabicPattern = "(10|[1-9])";
        arabicPattern = "\\G(" + arabicPattern + arithOpers + arabicPattern + ")\\Z";
        final Pattern arabicObjPattern = Pattern.compile(arabicPattern, Pattern.DOTALL);
        final Matcher arabicObjMatcher = arabicObjPattern.matcher(input);
        // Prepare patterns for Roman notation recognition
        List<String> romanNumerals = List.of( "", "I", "II", "III", "IV", "V", "VI", "VII", "VIII", "IX", "X");
        String romanPattern = "(" + String.join("|", romanNumerals).substring(1) + ")";// Generate regex pattern by romanNumerals list, cut first "|"
        romanPattern = "\\G("  + romanPattern + arithOpers + romanPattern + ")\\Z";
        final Pattern romanObjPattern = Pattern.compile(romanPattern, Pattern.DOTALL);
        final Matcher romanObjMatcher = romanObjPattern.matcher(input);

        // recognize notation and solve
        if (arabicObjMatcher.find()) { return arabicSolver(arabicObjMatcher); }
        else if (romanObjMatcher.find()) { return romanSolver(romanObjMatcher, romanNumerals);}
        else { throw new CalcException("Ошибка ввода!");}

    }
    static String arabicSolver(Matcher input) {
        int result = reshalo(Integer.parseInt(input.group(2)) , input.group(3),  Integer.parseInt(input.group(4)));
        return Integer.toString(result);
    }
    static String romanSolver(Matcher input, List<String> romanNumerals) throws CalcException{
        int result = reshalo(romanNumerals.indexOf(input.group(2)) ,
                input.group(3),  romanNumerals.indexOf(input.group(4)));
        if (result>0){return  convert1_100ToRomans(result, romanNumerals);}
        else {throw new CalcException("В римской записи нет 0 и отрицательных чисел!"); }
    }
    static int reshalo(int first, String operand, int second){
        int result = 0;
        switch (operand) {
            case ("+") -> result = first + second;
            case ("-") -> result = first - second;
            case ("*") -> result = first * second;
            case ("/") -> result = first / second;
        }
        return result;
    }
    static String convert1_100ToRomans(int inInt, List<String> romanNumerals ){
        StringBuilder resRom = new StringBuilder();
        if (inInt==100)  { resRom = new StringBuilder("C");
            inInt -=100;}
        else if (89 < inInt ) {resRom = new StringBuilder("XC");
            inInt -=90; }
        else if (49 < inInt ) {resRom = new StringBuilder("L");
            inInt -=50; }
        else if (39 < inInt)  {resRom = new StringBuilder("XL");
            inInt -=40; }

        if (inInt>=10){
            int decNum = inInt / 10;
            int i;
            inInt %=10;
            for (i=0;i<decNum;i++){
                resRom.append("X");}
        }

        resRom.append(romanNumerals.get(inInt));

        return resRom.toString();
    }

}
