import java.util.Stack;

/**
 * Created by 温 睿诚 on 2016/6/1/0001.
 * 预测分析法
 */
public class YuCeFenXi implements YuFa {
    Stack<String> stack;
    String[] t = {"int"};
    String[] o = {"+", "-"};
    String[] k = {"if"};
    String[] c = {">", "==", "!="};
    String[] spilt = {",", "(", ")", "{", "}", ";"};

    private static String[][] map = new String[15][14];
    //横排 S A B C N D E F G H  I  J  K  L
    //对应 1 2 3 4 5 6 7 8 9 10 11 12 13 14

    //竖排 t b z ( o ) , ; = k  {  }  #
    //对应 1 2 3 4 5 6 7 8 9 10 11 12 13
    YuCeFenXi() {
        //null即出错，空字符串即空字
        map[1][1] = "tb(A){N}S";
        map[1][13] = "#";

        map[2][1] = "B";
        map[2][6] = "";


        map[3][1] = "tbC";

        map[4][6] = "";
        map[4][7] = ",tbC";

        map[5][1] = "tbD;N";
        map[5][2] = "bI;N";
        map[5][10] = "k(E){N}N";
        map[5][12] = "";

        map[6][7] = ",bD";
        map[6][8] = "";

        map[7][2] = "bJ";
        map[7][3] = "F";

        map[8][3] = "zG";

        map[9][5] = "oHcH";
        map[9][6] = "";

        map[10][2] = "b";
        map[10][3] = "z";

        map[11][4] = "(A)";
        map[11][9] = "=K";

        map[12][4] = "(A)";
        map[12][5] = "oHcH";
        map[12][6] = "";

        map[13][2] = "HL";
        map[13][3] = "HL";

        map[14][5] = "oHL";
        map[14][8] = "";

        stack = new Stack<>();
        stack.push("#");
        stack.push("S");
    }

    //判断是否非终结符
    private boolean isN(String str) {
        if (str.length() != 1)
            return false;
        char temp = str.charAt(0);
        if ((temp >= 'A' && temp <= 'L') || (temp == 'S') || temp == 'N')
            return true;

        return false;
    }

    //把非终结符转换为表中对应数字
    private int n2I(String str) {
        char temp = str.charAt(0);
        switch (temp) {
            case 'S':
                return 1;
            case 'A':
                return 2;
            case 'B':
                return 3;
            case 'C':
                return 4;
            case 'N':
                return 5;
            default:
                return temp - 'A' + 3;
        }

    }

    //把终结符转换为表中对应数字
    private int t2I(String str) {
        char temp = str.charAt(0);
        switch (temp) {
            case 't':
                return 1;
            case 'b':
                return 2;
            case 'z':
                return 3;
            case '(':
                return 4;
            case 'o':
                return 5;
            case ')':
                return 6;
            case ',':
                return 7;
            case ';':
                return 8;
            case '=':
                return 9;
            case 'k':
                return 10;
            case '{':
                return 11;
            case '}':
                return 12;
            case '#':
                return 13;
            default:
                return -1;
        }

    }

    private boolean inArray(String str, String[] array) {
        for (String temp : array) {
            if (str.equals(temp))
                return true;
        }
        return false;
    }

    public static boolean isNumeric(String str) {
        for (int i = 0; i < str.length(); i++) {
            System.out.println(str.charAt(i));
            if (!Character.isDigit(str.charAt(i))) {
                return false;
            }
        }
        return true;
    }

    //若word是常规终结符如{ , ;则返回原值，若是int这种，则返回t
    public String word2T(int type, String word) {
        switch (type) {
            case 1:
                if (inArray(word, t))
                    return "t";
                else if (inArray(word, k))
                    return "k";
                else
                    return null;
            case 2:
                return "b";
            case 3:
                return "z";
            case 4:
                if (inArray(word, o))
                    return "o";
                else if (inArray(word, c))
                    return "c";
                else if (word.equals("="))
                    return word;
                else
                    return null;
            case 5:
                return word;
            case 6:
                return word;
            default:
                return null;

        }
    }

    @Override
    public boolean fenxi(int type, String str) {
        String str1 = stack.peek();//str1是栈顶符号
        //str是当前输入符号
        if (!isN(str1)) {
            //栈顶符号是终结符
            if (str1.equals("#") && str.equals(str1)) {
//                System.out.println("语法分析通过");
                return true;
            } else if (str1.equals(word2T(type, str))) {
                stack.pop();
                return true;
            } else {
                return false;
            }
        } else {
            //栈顶符号是非终结符
            String chanshengshi = map[n2I(str1)][t2I(word2T(type, str))];
            if (chanshengshi == null)
                return false;
            else {
                stack.pop();
                for (int i = chanshengshi.length() - 1; i >= 0; i--) {
                    stack.push(String.valueOf(chanshengshi.charAt(i)));
                }
                //这里写语义分析

                return fenxi(type, str);
            }

        }
    }
}
