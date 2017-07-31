package io.github.biezhi.wechat.robot;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.decoder.ErrorCorrectionLevel;

import java.util.Hashtable;

public class QRterminal {

    public static String getQr(String text) {
        String s      = "生成二维码失败";
        int    width  = 30;
        int    height = 30;
        // 用于设置QR二维码参数
        Hashtable<EncodeHintType, Object> qrParam = new Hashtable<EncodeHintType, Object>();
        // 设置QR二维码的纠错级别——这里选择最低L级别
        qrParam.put(EncodeHintType.ERROR_CORRECTION, ErrorCorrectionLevel.L);
        qrParam.put(EncodeHintType.CHARACTER_SET, "utf-8");
        try {
            BitMatrix bitMatrix = new MultiFormatWriter().encode(text, BarcodeFormat.QR_CODE, width, height, qrParam);
            s = toAscii(bitMatrix);
        } catch (WriterException e) {
            e.printStackTrace();
        }
        return s;
    }

    public static String toAscii(BitMatrix bitMatrix) {
        StringBuilder sb = new StringBuilder();
        for (int rows = 0; rows < bitMatrix.getHeight(); rows++) {
            for (int cols = 0; cols < bitMatrix.getWidth(); cols++) {
                boolean x = bitMatrix.get(rows, cols);
                if (!x) {
                    // white
                    sb.append("\033[47m  \033[0m");
                } else {
                    sb.append("\033[30m  \033[0;39m");
                }
            }
            sb.append("\n");
        }
        return sb.toString();
    }

    public static void main(String[] args) throws Exception {
        String text = "https://github.com/biezhi";
        System.out.println(getQr(text));
    }

}