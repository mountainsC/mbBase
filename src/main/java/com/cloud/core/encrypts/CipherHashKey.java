package com.cloud.core.encrypts;

/**
 * @Author lijinghuan
 * @Email:ljh0576123@163.com
 * @CreateTime:2018/3/1
 * @Description:cipher加密散列key
 * @Modifier:
 * @ModifyContent:
 */
public class CipherHashKey {

    private char[] nums = {'0', '1', '2', '3', '4', '5', '6', '7', '8', '9'};

    private char[] chars = {'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'i', 'j',
            'k', 'l', 'm', 'n', 'o', 'p', 'q', 'i', 's', 't', 'u', 'v', 'w', 'x', 'y', 'z',
            'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I', 'J', 'K', 'L', 'M', 'N', 'O', 'P',
            'Q', 'I', 'S', 'T', 'U', 'V', 'W', 'X', 'Y', 'Z'};

    private String[] civ = {"□:", "※", "눈", "№", "☻"};

    public char[] getNums() {
        if (nums == null) {
            nums = new char[]{};
        }
        return nums;
    }

    public void setNums(char[] nums) {
        this.nums = nums;
    }

    public char[] getChars() {
        if (chars == null) {
            chars = new char[]{};
        }
        return chars;
    }

    public void setChars(char[] chars) {
        this.chars = chars;
    }

    public String[] getCiv() {
        return civ;
    }
}
