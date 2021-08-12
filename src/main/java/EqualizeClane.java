import org.opencv.core.*;
import org.opencv.imgcodecs.Imgcodecs;
import org.opencv.imgproc.CLAHE;
import org.opencv.imgproc.Imgproc;

import java.awt.image.BufferedImage;
import java.util.ArrayList;

public class EqualizeClane {

    //Метод ничего не возвращает взял за остнову для своего
    public static void showClane(String fileName) {
        Mat img = Imgcodecs.imread(fileName);
        Mat img2 = new Mat();
        Imgproc.cvtColor(img, img2, Imgproc.COLOR_BGR2GRAY);
        Mat img3 = new Mat();
        CLAHE clane = Imgproc.createCLAHE();
        clane.setClipLimit(4);
        clane.apply(img2, img3);

        // Вычисляем и отрисовываем гистограммы
        ArrayList<Mat> images = new ArrayList<Mat>();
        images.add(img2);
        images.add(img3);
        Mat hist = new Mat();
        Mat hist2 = new Mat();
        Imgproc.calcHist(images, new MatOfInt(0), new Mat(),
                hist, new MatOfInt(256), new MatOfFloat(0, 256));
        Imgproc.calcHist(images, new MatOfInt(1), new Mat(),
                hist2, new MatOfInt(256), new MatOfFloat(0, 256));
        Core.normalize(hist, hist, 0, 128, Core.NORM_MINMAX);
        Core.normalize(hist2, hist2, 0, 128, Core.NORM_MINMAX);
        double v = 0;
        int h = 150;
        Mat imgHist = new Mat(h, 256, CvType.CV_8UC3, CvUtils.COLOR_WHITE);
        Mat imgHist2 = new Mat(h, 256, CvType.CV_8UC3, CvUtils.COLOR_WHITE);
        for (int i = 0, j = hist.rows(); i < j; i++) {
            v = Math.round(hist.get(i, 0)[0]);
            if (v != 0) {
                Imgproc.line(imgHist, new Point(i, h - 1),
                        new Point(i, h - 1 - v), CvUtils.COLOR_BLACK);
            }
            v = Math.round(hist2.get(i, 0)[0]);
            if (v != 0) {
                Imgproc.line(imgHist2, new Point(i, h - 1),
                        new Point(i, h - 1 - v), CvUtils.COLOR_BLACK);
            }
        }
        CvUtils.showImage(img2, "Оригинал");
        CvUtils.showImage(imgHist, "Гистограмма до");
        CvUtils.showImage(img3, "CLAHE");
        CvUtils.showImage(imgHist2, "Гистограмма после");
        img.release();
        img2.release();
        img3.release();
        imgHist.release();
        imgHist2.release();
        hist.release();
        hist2.release();

    }

    //Сохранияет  гистограмму в файл, преобразование clipLimit отыечает за качесво данный метод тоже просто переработал;
    public static void imageSave(String fileName, int clipLimit) {
        Mat img = Imgcodecs.imread(fileName);
        Mat img2 = new Mat();
        Imgproc.cvtColor(img, img2, Imgproc.COLOR_BGR2GRAY);
        Mat img3 = new Mat();
        CLAHE clane = Imgproc.createCLAHE();
        clane.setClipLimit(clipLimit);
        clane.apply(img2, img3);
        Imgcodecs.imwrite("QRCode.jpg", img3);

    }

    //Сохранияет  гистограмму в файл, преобразование clipLimit отыечает за качесво;
    public static BufferedImage imageToBuffer(String fileName, int clipLimit) {
        Mat img = Imgcodecs.imread(fileName);
        Mat img2 = new Mat();
        /**
         * Преобразовать изображение в оттенках серого в черно-белое позволяет статический
         * метод threshold() из класса Imgproc. Формат метода:
         * import org.opencv.imgproc.Imgproc;
         * public static double threshold(Mat src, Mat dst, double thresh,
         *  double maxval, int type)
         */
        Imgproc.cvtColor(img, img2, Imgproc.COLOR_BGR2GRAY);
        Mat img3 = new Mat();
        CLAHE clahe = Imgproc.createCLAHE();
        clahe.setClipLimit(clipLimit);
        clahe.apply(img2, img3);
        return  CvUtils.MatToBufferedImage(img3);

    }
}
