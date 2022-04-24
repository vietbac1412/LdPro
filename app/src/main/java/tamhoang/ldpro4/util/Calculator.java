package tamhoang.ldpro4.util;

import java.util.Arrays;
import java.util.Stack;

/**
 * @author : Pos365
 * @Skype : chukimmuoi
 * @Mobile : +84 167 367 2505
 * @Email : chukimmuoi@gmail.com
 * @Website : https://cafe365.pos365.vn/
 * @Project : CashiersPos365
 * Created by chukimmuoi on 4/10/19.
 */
public class Calculator {

    /**
     * KIỂM TRA KÝ TỰ ĐẦU TIÊN LÀ ÂM HAY DƯƠNG KIỂM TRA LỖI
     * */
    public boolean check_error = false;

    /**
     * CHUẨN HÓA SỐ
     * */
    public String standardizeDouble(double num) {
        int a = (int) num;
        if (a == num)
            return Integer.toString(a);
        else
            return Double.toString(num);
    }

    /**
     * KIỂM TRA KY TỰ C LÀ PI HAY KHÔNG
     * */
    public boolean isCharPi(char c) {
        if (c == 'r')
            return true;
        else
            return false;
    }

    /**
     * KIỂM TRA SỐ NUM LÀ PI HAY KHÔNG
     * */
    public boolean isNumPi(double num) {
        if (num == Math.PI)
            return true;
        else
            return false;
    }

    /** KIỂM TRA KÝ TỰ C CÓ LÀ SỐ KHÔNG - PI CŨNG LÀ MỘT SỐ */
    public boolean isNum(char c) {
        if (Character.isDigit(c) || isCharPi(c))
            return true;
        else
            return false;
    }

    /**
     * CHUYỂN SỐ SANG CHUỖI
     * */
    public String NumToString(double num) {
        if (isNumPi(num))
            return "r";
        else
            return standardizeDouble(num);
    }

    /**
     * CHUYỂN CHUỖI SANG SỐ
     * */
    public double StringToNum(String s) {
        if (isCharPi(s.charAt(0)))
            return Math.PI;
        else
            return Double.parseDouble(s);
    }

    /**
     * KIỂM TRA TOÁN TỬ ~ THAY CHO DẤU ÂM
     * */
    public boolean isOperator(char c) {
        char operator[] = { '+', '-', 'x', '/', '^', '~', 's', 'c', 't', '@',
                '!', '%', ')', '(' };
        Arrays.sort(operator);
        if (Arrays.binarySearch(operator, c) > -1)
            return true;
        else
            return false;
    }

    /**
     * THIẾT LẬP THỨ TỰ ƯU TIÊN
     * */
    public int priority(char c) {
        switch (c) {
            case '+':
            case '-':
                return 1;
            case 'x':
            case '/':
                return 2;
            case '~':
                return 3;
            case '@':
            case '!':
            case '^':
                return 4;
            case 's':
            case 'c':
            case 't':
                return 5;
        }
        return 0;
    }

    /**
     * KIỂM TRA TOÁN TỬ MỘT NGÔI ~ THAY CHO DẤU ÂM
     */
    public boolean isOneMath(char c) {
        char operator[] = { 's', 'c', 't', '@', '(' };
        Arrays.sort(operator);
        if (Arrays.binarySearch(operator, c) > -1)
            return true;
        else
            return false;
    }

    /**
     * CHUẨN HÓA BIỂU THỨC
     * */
    public String standardize(String s) {
        String s1 = "";
        s = s.trim();
        /**
         * CHUẨN HÓA S
         * */
        s = s.replaceAll("\\s+", " ");
        int open = 0, close = 0;
        for (int i = 0; i < s.length(); i++) {
            char c = s.charAt(i);
            if (c == '(')
                open++;
            if (c == ')')
                close++;
        }
        for (int i = 0; i < (open - close); i++)
        /**
         * THÊM CÁC DẤU NGOẶC VÀO CUỐI NẾU THIẾU
         * */
            s += ')';
        for (int i = 0; i < s.length(); i++) {
            if (i > 0 && isOneMath(s.charAt(i))
                    && (s.charAt(i - 1) == ')' || isNum(s.charAt(i - 1))))
            /**
             * CHUYỂN (...)(...) => (...)*(...)
             * */
                s1 = s1 + "x";
            if ((i == 0 || (i > 0 && !isNum(s.charAt(i - 1))))
                    && s.charAt(i) == '-' && isNum(s.charAt(i + 1))) {
                /**
                 * KIỂM TRA SỐ ÂM
                 * */
                s1 = s1 + "~";
            }
            /**
             * CHUYỂN 6r, (...)r => 6*r, (...)*r
             * */
            else if (i > 0
                    && (isNum(s.charAt(i - 1)) || s.charAt(i - 1) == ')')
                    && isCharPi(s.charAt(i)))
                s1 = s1 + "x" + s.charAt(i);
            else
                s1 = s1 + s.charAt(i);
        }
        return s1;
    }

    /**
     * XỬ LÝ BIỂU THỨC NHẬP VÀO THÀNH CÁC PHẦN TỬ
     * */
    public String[] processString(String sMath) {
        String s1 = "", elementMath[] = null;
        sMath = standardize(sMath);
        Calculator ITP = new Calculator();
        for (int i = 0; i < sMath.length(); i++) {
            char c = sMath.charAt(i);
            /**
             * KIỂM TRA LỖI NẾU CÓ DẠNG r
             */
            if (i < sMath.length() - 1 && isCharPi(c)
                    && !ITP.isOperator(sMath.charAt(i + 1))) {
                check_error = true;
                return null;
            } else if (!ITP.isOperator(c))
                s1 = s1 + c;
            else
                s1 = s1 + " " + c + " ";
        }
        s1 = s1.trim();
        /**
         * CHUẨN HÓA S1
         * */
        s1 = s1.replaceAll("\\s+", " ");
        /**
         * TÁCH S1 THÀNH CÁC PHẦN TỬ
         * */
        elementMath = s1.split(" ");
        return elementMath;
    }

    /**
     * CHUYỂN BIỂU THỨC TRUNG TỐ SANG HẬU TỐ
     */
    public String[] postfix(String[] elementMath) {
        Calculator ITP = new Calculator();
        String s1 = "", E[];
        Stack<String> S = new Stack<String>();
        /**
         * DUYỆT CÁC PHẦN TỬ
         * */
        for (int i = 0; i < elementMath.length; i++) {
            /**
             * C LÀ KÝ TỰ ĐẦU TIÊN CỦA MỖI PHẦN TỬ
             * */
            char c = elementMath[i].charAt(0);

            /**
             * NẾU C KHÔNG LÀ TOÁN TỬ THÌ XUẤT elementMath VÀO s1
             * */
            if (!ITP.isOperator(c))
                s1 = s1 + elementMath[i] + " ";
            else {
                /**
                 * NẾU C LÀ TOÁN TỬ C LÀ "(" -> ĐẨY PHẦN TỬ VÀO STACK
                 */

                if (c == '(')
                    S.push(elementMath[i]);
                else {
                    /**
                     * C LÀ ")" DUYỆT LẠI CÁC PHẦN TỬ TRONG STACK
                     * */
                    if (c == ')') {
                        char c1;
                        do {
                            /**
                             * C1 LÀ KÝ TỰ ĐẦU TIÊN CỦA PHẦN TỬ VÀ C1 != "("
                             * */
                            c1 = S.peek().charAt(0);
                            if (c1 != '(')
                                s1 = s1 + S.peek() + " ";
                            S.pop();
                        } while (c1 != '(');
                    } else {
                        /**
                         * STACK KHÔNG ROONGXVAF CÁC PHẦN TỬ TRONG STACK CÓ ĐỘ
                         * ƯU TIÊN >= PHẦN TỬ HIÊN TẠI
                         * */
                        while (!S.isEmpty()
                                && ITP.priority(S.peek().charAt(0)) >= ITP
                                .priority(c))
                            s1 = s1 + S.pop() + " ";
                        /**
                         * ĐƯA PHẦN TỬ HIÊN TẠI VÀO STACK
                         * */
                        S.push(elementMath[i]);
                    }
                }
            }
        }
        while (!S.isEmpty())
        /**
         * NẾU STACK CÒN PHẨN TỬ THÌ ĐẨY HẾT VÀO S1
         * */
            s1 = s1 + S.pop() + " ";
        /**
         * TÁCH S1 THÀNH CÁC PHẦN TỬ
         * */
        E = s1.split(" ");
        return E;
    }

    /**
     * THỰC HIỆN TÍNH GIÁ TRỊ BIỂU THỨC
     * */
    public String valueMath(String[] elementMath) {
        Stack<Double> S = new Stack<Double>();
        Calculator ITP = new Calculator();
        double num = 0.0;
        for (int i = 0; i < elementMath.length; i++) {
            char c = elementMath[i].charAt(0);
            if (isCharPi(c))
            /**
             * NẾU LÀ PI
             * */
                S.push(Math.PI);
            else {
                if (!ITP.isOperator(c))
                /**
                 * SỐ
                 * */
                    S.push(Double.parseDouble(elementMath[i]));
                else {
                    /**
                     * TOÁN TỬ
                     * */
                    double num1 = S.pop();
                    switch (c) {
                        case '~':
                            num = -num1;
                            break;
                        case 's':
                            num = Math.sin(num1);
                            break;
                        case 'c':
                            num = Math.cos(num1);
                            break;
                        case 't':
                            num = Math.tan(num1);
                            break;
                        case '%':
                            num = num1 / 100;
                            break;
                        case '@': {
                            if (num1 >= 0) {
                                num = Math.sqrt(num1);
                                break;
                            } else
                                check_error = true;
                        }
                        case '!': {
                            if (num1 >= 0 && (int) num1 == num1) {
                                num = 1;
                                for (int j = 1; j <= (int) num1; j++)
                                    num = num * j;
                            } else
                                check_error = true;
                        }
                        default:
                            break;
                    }
                    if (!S.empty()) {
                        double num2 = S.peek();
                        switch (c) {
                            // -----------------------
                            case '+':
                                num = num2 + num1;
                                S.pop();
                                break;
                            case '-':
                                num = num2 - num1;
                                S.pop();
                                break;
                            case 'x':
                                num = num2 * num1;
                                S.pop();
                                break;
                            case '/': {
                                if (num1 != 0)
                                    num = num2 / num1;
                                else
                                    check_error = true;
                                S.pop();
                                break;
                            }
                            case '^':
                                num = Math.pow(num2, num1);
                                S.pop();
                                break;
                        }
                    }
                    S.push(num);
                }
            }
        }
        return NumToString(S.pop());
    }
}
