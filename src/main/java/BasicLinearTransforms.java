import org.opencv.core.Mat;
import org.opencv.imgcodecs.Imgcodecs;

import java.awt.image.BufferedImage;

/**
 * Данный класс занимается улучшением яркости и контарстоности
 * По умаолчанию сделан на затемнение
 * alpha с отрицатеьным значение сделает экран черным
 */

public class BasicLinearTransforms {
    private static byte saturate(double val) {
        int iVal = (int) Math.round(val);
        iVal = iVal > 255 ? 255 : (iVal < 0 ? 0 : iVal);
        return (byte) iVal;
    }
    public void run(String name) {
        String imagePath = name;
        Mat image = Imgcodecs.imread(imagePath);
        if (image.empty()) {
            System.out.println("Empty image: " + imagePath);
            System.exit(0);
        }
        Mat newImage = Mat.zeros(image.size(), image.type());

        double alpha = 1.0; /*< Контрастность */
        int beta = 0;       /*< Яркость */
            alpha = 1;
            beta = -150;
        byte[] imageData = new byte[(int) (image.total()*image.channels())];
        image.get(0, 0, imageData);
        byte[] newImageData = new byte[(int) (newImage.total()*newImage.channels())];
        for (int y = 0; y < image.rows(); y++) {
            for (int x = 0; x < image.cols(); x++) {
                for (int c = 0; c < image.channels(); c++) {
                    double pixelValue = imageData[(y * image.cols() + x) * image.channels() + c];
                    pixelValue = pixelValue < 0 ? pixelValue + 256 : pixelValue;
                    newImageData[(y * image.cols() + x) * image.channels() + c]
                            = saturate(alpha * pixelValue + beta);
                }
            }
        }
        newImage.put(0, 0, newImageData);
        //Показать изображение
        //HighGui.imshow("New Image", newImage);
        //Сохраняет матрицу в файл
        Imgcodecs.imwrite("QRCode.jpg", newImage);
        //HighGui.waitKey(); //Ругается на этот метод
        //System.exit(0);
    }


    public static BufferedImage changeBrightness(String fileName, int beta) {
        String imagePath = fileName;
        Mat image = Imgcodecs.imread(imagePath);
        Mat newImage = Mat.zeros(image.size(), image.type());

        double alpha = 1.0; /*< Контрастность */

        alpha = 1;
        beta = beta;/*< Яркость  что бы сделать темнее нужно делать отрицательное число*/
        byte[] imageData = new byte[(int) (image.total()*image.channels())];
        image.get(0, 0, imageData);
        byte[] newImageData = new byte[(int) (newImage.total()*newImage.channels())];
        for (int y = 0; y < image.rows(); y++) {
            for (int x = 0; x < image.cols(); x++) {
                for (int c = 0; c < image.channels(); c++) {
                    double pixelValue = imageData[(y * image.cols() + x) * image.channels() + c];
                    pixelValue = pixelValue < 0 ? pixelValue + 256 : pixelValue;
                    newImageData[(y * image.cols() + x) * image.channels() + c]
                            = saturate(alpha * pixelValue + beta);
                }
            }
        }
        newImage.put(0, 0, newImageData);
        //Показать изображение
        //HighGui.imshow("New Image", newImage);
        //Сохраняет матрицу в файл
        //Imgcodecs.imwrite("QRCode.jpg", newImage);

        //HighGui.waitKey(); //Ругается на этот метод
        //System.exit(0);

        return  CvUtils.MatToBufferedImage(newImage);

    }

    public static BufferedImage changeBrightness(BufferedImage bufferedImage, int beta) {

        Mat image = CvUtils.BufferedImageToMat(bufferedImage);
        Mat newImage = Mat.zeros(image.size(), image.type());

        double alpha = 1.0; /*< Контрастность */

        alpha = 1;
        beta = beta;/*< Яркость  что бы сделать темнее нужно делать отрицательное число*/
        byte[] imageData = new byte[(int) (image.total()*image.channels())];
        image.get(0, 0, imageData);
        byte[] newImageData = new byte[(int) (newImage.total()*newImage.channels())];
        for (int y = 0; y < image.rows(); y++) {
            for (int x = 0; x < image.cols(); x++) {
                for (int c = 0; c < image.channels(); c++) {
                    double pixelValue = imageData[(y * image.cols() + x) * image.channels() + c];
                    pixelValue = pixelValue < 0 ? pixelValue + 256 : pixelValue;
                    newImageData[(y * image.cols() + x) * image.channels() + c]
                            = saturate(alpha * pixelValue + beta);
                }
            }
        }
        newImage.put(0, 0, newImageData);
        //Показать изображение
        //HighGui.imshow("New Image", newImage);
        //Сохраняет матрицу в файл
        //Imgcodecs.imwrite("QRCode.jpg", newImage);

        //HighGui.waitKey(); //Ругается на этот метод
        //System.exit(0);

        return  CvUtils.MatToBufferedImage(newImage);

    }

}
