import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.channels.FileChannel;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Comparator;
import java.util.Date;

public class OperationWithFiles {
    //Позволяет выводить информацию о количестве файлов в папке нужно для отображения стаистики в реальном времени
    private int countImage;

    /**
     * (2) Функция будет заниматься переносом распознаных фалов
     */
    public void recognizedAndCopyTwo(Collection<File> files) {
        //Счетчик распознаных файлов
        int countRecognisedFiles = 0;
        //Все распознанные файлы находятся в этом списке
        Collection<Image> images = new ArrayList<>();
        //Не распознанные файлы находятся в этом списке
        Collection<File> doNotRecognized = new ArrayList<>();
        //Распознает QR коды разнанные добавляет в коллекцию images
        int count = files.size(); //Просто счетчик такая кучерявая запись нужна для стрим
        System.out.println("Всего фалов: " + count);
        //Отсортировал по имени полученные файлы
        files.stream().sorted(Comparator.comparing(File::getName)); //Отсортировал по имени полученные файлы
        //Перебирает файлы ищет QRСode и добавляет данные в images

        for (File file : files) {
            try {
                /** Получает QRCode в строку*/
                String qrCode = ReaderQRCode.getQrCodeForLoopWithTryHander(file.toString());
                //Проверяет подходит ли штрих код под формат если да то добавляет в список image проверить в 1С документ КЦ21-001151 от 30 апреля 2021

                if ((qrCode.matches("\\d+\\.\\d\\.\\d+\\.\\d\\.\\d\\.\\d+"))) {
                    String fileName = CreateNameFile();
                    images.add(new Image(qrCode, file, fileName));

                    //Добавляем QRcode в базу данных
                    DataBase.setQRCodeToDataBase(qrCode, fileName);
                    System.out.println((count--) + " " + file.getName() + " " + qrCode);
                    countRecognisedFiles++;
                }

            } catch (Exception e) {
                System.out.println((count--) + " " + file.getName() + " " + "QR Code не распознан, или отстутсвует! ");
            }
        }

        System.out.println("Распознано файлов: "+countRecognisedFiles);

        countImage = images.size();

        //Создает папки и копирует файлы
        images.stream().forEach(image -> {
            try {
                createFolderFromQRCodeAndCopyFile(image.getQrCode(), image.getFile() ,image.getFileName());
            } catch (Exception exception) {
                exception.printStackTrace();
            }
        });
    }


    /**
     * ! (3) КОПИРУЕТ затем удаляет.  Создает папку разбивая QRСode на массисив строк и копирует в конечную паку файл
     */

    private static void createFolderFromQRCodeAndCopyFile(String qrCode, File file , String fileName) throws InterruptedException, IOException {
        String[] strings = qrCode.split("\\.");
        String folderName = "C:/_archive";
        for (int i = 0; i < strings.length; i++) {
            folderName = folderName + "/" + strings[i];
            new File(folderName).mkdir();
        }
        //Копирует файл в созданную папку
        copyFile(file, new File(folderName + "/" + fileName+".jpg"));

        //Удаляет скопированный файл
        file.delete();
    }

    /**
     * ! (4)  отличии от встренного копирование работае крайне надежно и копирует все файлы
     */

    public static void copyFile(File sourceFile, File destFile) throws IOException {
        if (!sourceFile.exists()) {
            return;
        }
        if (!destFile.exists()) {
            destFile.createNewFile();
        }
        FileChannel source = null;
        FileChannel destination = null;
        source = new FileInputStream(sourceFile).getChannel();
        destination = new FileOutputStream(destFile).getChannel();
        if (destination != null && source != null) {
            destination.transferFrom(source, 0, source.size());
        }
        if (source != null) {
            source.close();
        }
        if (destination != null) {
            destination.close();
        }
    }


    public void deleteFiles(Collection<File> files) throws IOException, InterruptedException {
        System.out.println("Удаление нераспознаных файлов");
        int count = 0;
        for (File file : files) {
            file.delete();
        }

    }


    /** (6) Создавать имя файл из текущей даты в формате Год 2012  Месяц 01 Число 31  Часы 12 Минуты 40 Секунды 32 Милисекунды 2312 */
    public String CreateNameFile(){
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMddHHmmssms");
        //SimpleDateFormat milliSecondFormat = new SimpleDateFormat("ms");
        String result = dateFormat.format(new Date());
        if (result.length()<18){
            return result+"0";
        }
        return result;
    }
}
