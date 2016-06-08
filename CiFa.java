import java.io.*;
import java.util.ArrayList;

/**
 * Created by 温 睿诚 on 2016/5/11/0011.
 */
public class CiFa {
    String[] keyword = {"if", "int"};
    ArrayList<String> biaoshi = new ArrayList<>();
    ArrayList<Integer> changshu = new ArrayList<>();
    String[] yunsuan = {"+", "=", "-", ">", "==", "!="};
    String[] spilt = {",", "(", ")", "{", "}", ";"};
    //记录结果的符号表
    //用什么数据结构呢？

    //当前单词
    StringBuilder str = new StringBuilder("");
    //下一个要读的字符
    char now;
    //一个栈
    ArrayList<Character> stack = new ArrayList<>();

    private void put() {
        stack.add(new Character(now));
    }

    private char pop() {
        if (stack.size() > 0) {
            return ((Character) stack.remove(stack.size() - 1)).charValue();
        } else {
            return 0;
        }
    }

    //错误信息
    String errorMsg;
    Reader reader = null;

    public static void main(String[] args) {
        CiFa cifa = new CiFa();
        cifa.fenXi(args[0]);
    }

    private void fenXi(String filename) {
        //读取文件
        File file = new File(filename);
        try {
            reader = new InputStreamReader(new FileInputStream(file));
        } catch (FileNotFoundException e) {
            System.out.println("读取文件字符失败！");
            e.printStackTrace();
        }
        //不断读取字符直到结束

        getChar();
        int result;

        //使用预测分析法
        YuFa yuFa = new YuCeFenXi();
        boolean flag = true;
        while (!(now < 0 || now >= 65535)) {
            //根据返回数值查找或插入，错误则打印并提示。正确则记录到map
            result = read();
            if (result != 6) {
                System.out.println("(" + result + " , \"" + str + "\")");

                if (!yuFa.fenxi(result, str.toString())) {
                    flag = false;
                    System.err.println("语法分析出错！出错单词: " + str.toString());

                }

            } else {
                System.err.println("(" + errorMsg + " , \"" + str + "\")");
            }
            str.delete(0, str.length());
        }
        //结束
        boolean tempResult = false;
        if (yuFa != null)
            tempResult = yuFa.fenxi(6, "#");
        if (tempResult && flag)
            System.out.println("语法分析通过！");


    }

    //判断是否为数字
    private boolean isDigit() {
        if ('0' <= now && now <= '9')
            return true;
        else
            return false;
    }

    //判断是否为字母
    private boolean isLetter() {
        if (('a' <= now && now <= 'z') || ('A' <= now && now <= 'Z'))
            return true;
        else
            return false;
    }

    //赋值下一字符给now,返回true表示读到空格、换行等空白字符
    private boolean getChar() {
        boolean flag = false;
        try {
            now = (char) reader.read();
            while (now == 0 || now == '\t' || now == '\r' || now == '\n' || now == 32) {
                flag = true;
                now = (char) reader.read();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return flag;

    }

    //连接字符到单词
    private void concat() {
        str.append(now);
    }

    private boolean isSpilt() {
        String temp = String.valueOf(now);
        for (String str : spilt) {
            if (str.startsWith(temp)) {
                return true;
            }
        }
        return false;
    }

    private boolean inAL(String str, ArrayList<String> strings) {

        if (strings.contains(str))
            return true;
        return false;
    }

    private boolean inShuzu(String str, String[] strings) {
        for (String str1 : strings) {
            if (str.equals(str1)) {
                return true;
            }
        }
        return false;
    }

    private boolean isYun() {
        String temp = String.valueOf(now);
        for (String str : yunsuan) {
            if (str.startsWith(temp)) {
                return true;
            }
        }
        return false;
    }

    //词法分析器
    private int read() {
        if (isLetter()) {
            //字母开始的，要么关键字，要么标识符，其用空格、tab、回车之类的分隔，
            // 而标识符还可以用分割符号、运算符号分割。暂不判断标识符是否定义
            concat();
            boolean flag;
            flag = getChar();
            while ((isDigit() || isLetter()) && flag == false) {
                concat();
                flag = getChar();
            }
            if (inShuzu(str.toString(), keyword)) {
                return 1;
            } else {
                if (!inAL(toString(), biaoshi)) {
                    biaoshi.add(str.toString());
                }
                return 2;
            }


        } else if (isDigit()) {
            //数字开头的，是常数，以空格、tab等以及分隔符、运算符分割
            concat();
            getChar();
            while (isDigit()) {
                concat();
                getChar();
            }
            return 3;

        } else if (isSpilt()) {
            //分隔符，当出现｛、（时入栈，接收到｝、）时判断是否符合。单个字符，不需要分隔
            if (now == '{' || now == '(') {
                put();
            } else if (now == '}') {
                char temp = pop();
                if (temp != '{') {
                    errorMsg = "没有'{'与'}'匹配";
                    concat();
                    getChar();
                    return 6;
                }
            } else if (now == ')') {
                char temp = pop();
                if (temp != '(') {
                    errorMsg = "没有'('与')'匹配";
                    concat();
                    getChar();
                    return 6;
                }
            }
            concat();
            getChar();
            return 5;
        } else if (isYun()) {
            //运算符，一般为单个符号，例外如下
            concat();
            if (now == '<' || now == '>' || now == '!') {
                getChar();
                if (now == '=') {
                    concat();
                    getChar();
                }
            } else {
                getChar();
            }
            return 4;
        } else {
            //ERROR
            errorMsg = "无法识别\"" + now + "\"";
            concat();
            getChar();
            return 6;
        }
    }

}
