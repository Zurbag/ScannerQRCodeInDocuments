import com.google.zxing.*;
import com.google.zxing.client.j2se.BufferedImageLuminanceSource;
import com.google.zxing.common.HybridBinarizer;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.FileInputStream;
import java.util.Hashtable;

public class ReaderQRCode {


    //Метод работает с получает данный из имейдж буффера и возвращает строку
    public static String getQRCodeForStrung(BufferedImage bf) throws Exception {

        try {
            BinaryBitmap bitmap = new BinaryBitmap(new HybridBinarizer(
                    new BufferedImageLuminanceSource(bf)));
            Result result = new MultiFormatReader().decode(bitmap);
            return result.getText();
        } catch (Exception e) {
        }
        return "";
    }

    /** (5) Изменение гистограммы работает медленнее обычного метода изменения гистограммы от стандартного рекомендуемого способу отличается этими полями
    decodeHints.put(DecodeHintType.TRY_HARDER, Boolean.TRUE); result = new MultiFormatReader().decode(bitmap, decodeHints);*/

    public static String getQrCodeForLoopWithTryHander(String fileName) throws Exception {
        Hashtable<DecodeHintType, Object> decodeHints = new Hashtable<DecodeHintType, Object>();
        decodeHints.put(DecodeHintType.TRY_HARDER, Boolean.TRUE);
        String text = "";
        String path = fileName;
        BufferedImage bf = ImageIO.read(new FileInputStream(path));
        BinaryBitmap bitmap = new BinaryBitmap(new HybridBinarizer(
                new BufferedImageLuminanceSource(bf)));
        Result result = null;
        try {
            result = new MultiFormatReader().decode(bitmap, decodeHints);
        } catch (NotFoundException e) {

            //Запускается цикл поиска QR кода с изменением Гистограммы
            for (int i = -20; i < 40; i++) {
                text = ReaderQRCode.getQRCodeForStrung(EqualizeClane.imageToBuffer(fileName, i));
                if (text.length() > 0) {
                    return text;
                }
            }
        }
        text = result.getText();
        return text;
    }

}
