package com.example.tk.backActivity;

public class MessageAction {

    public String getFirstLeftPipe(String str) {
        if (str == null || !str.contains("|")) {
            return null; // 如果输入为空或不包含 "|"，返回 null
        }
        int pipeIndex = str.indexOf('|');
        return str.substring(0, pipeIndex);
    }

    public String getCenter(String input) {
        if (input == null || input.indexOf('|') == -1 || input.lastIndexOf('|') == -1) {
            return null; // 如果输入为空或不包含两个 "|"，返回 null
        }

        int firstIndex = input.indexOf('|') + 1;
        int lastIndex = input.lastIndexOf('|');

        if (lastIndex <= firstIndex) {
            return null; // 如果没有找到有效的两个 "|"
        }

        return input.substring(firstIndex, lastIndex);
    }

    public String getContentAfterRightPipe(String originalString) {
        if (originalString == null || originalString.indexOf('|') == -1) {
            return null; // 如果输入为空或不包含 "|"，返回 null
        }

        int pipeIndex = originalString.lastIndexOf('|');
        return originalString.substring(pipeIndex + 1);
    }
}