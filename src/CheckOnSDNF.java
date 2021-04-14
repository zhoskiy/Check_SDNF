//////////////////////////////////////////////////////////////////////////////////////
// Лабораторная работа 1 по дисциплине ЛОИС
// Выполнена студентом группы 821703 БГУИР Дмитруком Алексеем Александровичем
// В данном файле реализован алгоритм проверки проверки формулы на СДНФ
// 20.03.2021 v1.2
//

import java.util.ArrayList;
import java.util.Arrays;

public class CheckOnSDNF {

    private static final String grammar = "()/\\!ABCDEFGHIJKLMNOPQRSTUVWXYZ";
    private static final String symbols = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
    private static final String conjunction = "/\\";
    private static final String disjunction = "\\/";
    private static final String brackets = "()";

    public static boolean isSDNF(String formula) {

        if (formula == null || formula.equals("")) {
            System.out.println("null formula");
            return false;
        }

        for (int i = 0; i < formula.length(); i++) {
            if (grammar.indexOf(formula.charAt(i)) == -1) { //некорректные символы
                System.out.println("incorrect symbols");
                return false;
            }
        }

        if (!isCorrectSequenceBrackets(formula)) {
            System.out.println("incorrect sequence brackets");
            return false;
        }

        String[] terms = split(formula, disjunction);  //делим на элементарные конъюнкции
        String[] terms2 = new String[terms.length];


        int k = 0;
        for (String term : terms) {             //отбрасываем скобки
            StringBuilder stringBuilder = new StringBuilder();
            for (int i = 0; i < term.length(); i++) {
                if (brackets.indexOf(term.charAt(i)) == -1)
                    stringBuilder.append(term.charAt(i));
            }
            terms2[k] = stringBuilder.toString();
            k++;
        }
        System.out.println(Arrays.toString(terms2));


        for (String term : terms2)
            for (int i = 0; i < term.length() - 1; i++) {
                if (term.charAt(i) == term.charAt(i + 1)) //если за следующим знаком идет такой же то возращаем false
                    return false;
            }

        String[] multipliers;
        String[] firstMultipliers = new String[0];
        ArrayList<String[]> lastTerms = new ArrayList<>();
        int multiplierCount = 0;

        for (String term : terms2) {
            multipliers = split(term, conjunction); //делим слагаемое на множители
            System.out.println(Arrays.toString(multipliers));

            if (multiplierCount == 0) {
                multiplierCount = multipliers.length;
                firstMultipliers = multipliers;
                lastTerms.add(multipliers);
            } else {
                if (multiplierCount != multipliers.length) { //сравниваем кол-во множителей у слагаемых
                    System.out.println("count multipliers not equals");
                    return false;
                }

                if (equalTerms(lastTerms, multipliers)) {
                    System.out.println("equal terms");
                    return false;
                }

                lastTerms.add(multipliers);

                for (int i = 0; i < multiplierCount; i++) {
                    int count = 0;
                    int start = 0;

                    for (int j = 0; j < multiplierCount; j++) {
                        int lastIndex2 = multipliers[j].length() - 1;
//                        System.out.println(firstMultipliers[i] + " " + multipliers[j].charAt(multipliers[j].length() - 1));
                        while (firstMultipliers[i].indexOf(multipliers[j].charAt(lastIndex2), start) != -1) {
                            count++;
                            start = firstMultipliers[i].indexOf(multipliers[j].charAt(lastIndex2), start) + 1;
                        }
                    }
                    if (count != 1) {
                        System.out.println("atom count != 1");
                        return false;
                    }
                }
            }

            for (int i = 0; i < multipliers.length; i++) {
                int lastIndex1 = multipliers[i].length();
                if (lastIndex1 > 2) {//если кол-во знаков у множителя > 2
                    System.out.println("count >2");
                    return false;
                }
                for (int j = i + 1; j < multipliers.length; j++) {
                    int lastIndex2 = multipliers[j].length();
                    //если в множителе несколько одинаковых атомов, то это не сднф
                    if (multipliers[i].charAt(lastIndex1 - 1) == multipliers[j].charAt(lastIndex2 - 1)) {
                        System.out.println("double symbol");
                        return false;
                    }
                }
            }
        }
        return isCorrectBrackets(terms, multiplierCount);
    }

    private static boolean isCorrectSequenceBrackets(String formula) {
        int counter = 0;
        for (int i = 0; i < formula.length(); i++) { //проверяем на правильную последовательность скобок
            if (formula.charAt(i) == '(')
                counter++;
            else if (formula.charAt(i) == ')')
                counter--;
            if (counter < 0)
                return false;
        }
        if (counter != 0)
            return false;

        if (formula.length() != 1)
            for (int i = 0; i < formula.length(); i++) {
                if (symbols.indexOf(formula.charAt(i)) != -1) {
                    int prev = 1, next = 1;
                    try {
                    if (formula.charAt(i - 1) == '!') {
                        prev+=2;
                        next++;
                    }
                    if (formula.charAt(i - prev) != '(' && formula.charAt(i + next) != ')')
                        return false;
                    } catch (Exception ignored) {
                    }
                }
            }
        return true;
    }

    private static boolean equalTerms(ArrayList<String[]> terms, String[] term2) {
        for (String[] term1 : terms) {
            boolean result = true;
            for (String multiplier : term1) {
                int lastIndex1 = multiplier.length() - 1;
                for (String s : term2) {
                    int lastIndex2 = s.length() - 1;
                    if (multiplier.charAt(lastIndex1) == s.charAt(lastIndex2))
                        result &= multiplier.equals(s);
                }
            }
            if (result)
                return true;
        }
        return false;
    }

    private static String[] split(String formula, String delim) {
        int end;
        int start = 0;
        ArrayList<String> result = new ArrayList<>();

        while (true) {
            if (formula.indexOf(delim, start) == -1) {
                result.add(formula.substring(start));
                break;
            }

            end = formula.indexOf(delim, start);
            result.add(formula.substring(start, end));
            start = end + 2;
        }


        return result.toArray(new String[0]);
    }

    private static boolean isCorrectBrackets(String[] terms, int multiplierCount) {
        int countTerms = terms.length;

        for (int i = 0; i < countTerms; i++) {
            int countOpenBrackets = 0;
            int countCloseBrackets = 0;
            for (int j = 0; j < terms[i].length(); j++) {
                if (terms[i].charAt(j) == '(')
                    countOpenBrackets++;
                if (terms[i].charAt(j) == ')')
                    countCloseBrackets++;
                try {
                    if (terms[i].charAt(j) == '!')
                        if (terms[i].charAt(j - 1) == '(' && terms[i].charAt(j + 2) == ')') {
                            countOpenBrackets--;
                            countCloseBrackets--;
                        } else
                            return false;
                } catch (Exception e) {
                    return false;
                }

            }

            if (i == 0) {
                if (terms.length == 1 && split(terms[i], conjunction).length == 1) {
                    if (countOpenBrackets != 0 || countCloseBrackets != 0)
                        return false;
                } else if (countOpenBrackets != (multiplierCount - 1 + countTerms - 1) ||
                        countCloseBrackets != multiplierCount - 1) {
                    System.out.println("countOpenBrackets: " + countOpenBrackets);
                    System.out.println("countCloseBrackets: " + countCloseBrackets);
                    System.out.println(multiplierCount + " " + countTerms);
                    System.out.println("bracket dont equals counts");
                    return false;
                }
            } else if (countCloseBrackets != (multiplierCount - 1 + 1) ||
                    countOpenBrackets != multiplierCount - 1) {
                System.out.println("bracket dont equals counts");
                return false;
            }
        }
        return true;
    }
}