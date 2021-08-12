import org.apache.commons.io.FileUtils;
import org.opencv.core.Core;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Scanner;

public class Main {
    static String url;
    static String user;
    static String pass;

    public static void main(String[] args)  {
        //Подключается библиотека OpenCV
        System.loadLibrary(Core.NATIVE_LIBRARY_NAME);

        //Получаем пути папок из файла конфиг  Блок получает информацию в каких папках проводить сканирование
        Scanner scanner = null;
        try {
            scanner = new Scanner(new File("./config/folder.txt"));
        } catch (FileNotFoundException e) {
            System.out.println("Файл config.txt не обнаружен");
        }
        //Добавляем пути
        List<String> pathFolders = new ArrayList<>();
        while (scanner.hasNext()) {
            pathFolders.add(scanner.next().trim());
        }
        scanner.close();

        //Класс для операции с файлами
        OperationWithFiles op = new OperationWithFiles();

        //Перебираем список файлов
        pathFolders.forEach(pf -> {
            System.out.println(pf);

            //Получаем списаок файлов из папки
            Collection<File> files = null;
            try {
                files = FileUtils.listFiles(new File(pf), new String[]{"jpg"}, false);
            }catch (Exception ex){
                System.out.println("На сервере не найдена дирректория для сканирования, проверьте пути распознования либо измените файл config.txt");
            }

            //Передаем список файлов на распознование
            try {
                op.recognizedAndCopyTwo(files);
            }catch (Exception ex){
                System.out.println("Нет файлов для распознования");
            }

            //Удалить оставшиеся файлы
            try {
                op.deleteFiles(pf);
            } catch (Exception ex) {
            }
        });
        System.out.println("--------------------");
        System.out.println("Сканирование завершено");

    }



}

