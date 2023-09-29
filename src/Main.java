import java.util.Arrays;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
public class Main {
    public static void main(String[] args) throws CalcException {
        System.out.println("Калькулятор готов к работе. Введите выражение согласно условию. Для завершения введите 'q': ");
        Scanner s = new Scanner(System.in);
        String input = s.nextLine();
        while (! input.equals("q")  ) {
            System.out.println("Порешали: " + calc(input));
            input = s.nextLine();
        }
    }
    public static String calc(String input) throws CalcException {
        String arithOpers = "\\s*([+-/*])\\s*";
        String arabNums = "(10|[1-9])";
        String patternArab = "\\G(" + arabNums + arithOpers + arabNums + ")\\Z";
        final Pattern patternObjArab = Pattern.compile(patternArab, Pattern.DOTALL);
        final Matcher matcherObjArab = patternObjArab.matcher(input);

        String romNums = "(X|IX|VIII|VII|VI|V|IV|III|II|I)";
        String patternRom = "(\\G"  + romNums + arithOpers + romNums + ")\\Z";
        final Pattern patternObjRom = Pattern.compile(patternRom, Pattern.DOTALL);
        final Matcher matcherObjRom = patternObjRom.matcher(input);

        if (matcherObjArab.find()) { return solverArab(matcherObjArab); }
        else if (matcherObjRom.find()) { return solverRom(matcherObjRom);}
        else { throw new CalcException("Ошибка ввода!");}

    }
    static String solverArab(Matcher input) {
        int result = reshalo(Integer.parseInt(input.group(2)) , input.group(3),  Integer.parseInt(input.group(4)));
        return Integer.toString(result);
    }
    static String solverRom(Matcher input) throws CalcException{
        String[] roms = {"", "I", "II", "III", "IV", "V", "VI", "VII", "VIII", "IX", "X"};
        int result = reshalo(Arrays.asList(roms).indexOf(input.group(2)) , input.group(3),  Arrays.asList(roms).indexOf(input.group(4)));
        if (result>0){return  convert1_100ToRoms(result, roms);}
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
    static String convert1_100ToRoms(int inInt, String[] roms ){
        StringBuilder resRom = new StringBuilder();
        if (inInt<100) {
            if (inInt>=50) {
                resRom = new StringBuilder("L");
                inInt -=50;
            }
            if (inInt>=10){
                int decNum = inInt / 10;
                int i;
                inInt %=10;
                for (i=0;i<decNum;i++){
                    resRom.append("X");}
            }
            resRom.append(roms[inInt]);
        }
        else {
            resRom = new StringBuilder("C");}
        return resRom.toString();
    }

}
