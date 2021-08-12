import java.io.File;
import java.io.FileNotFoundException;
import java.sql.*;
import java.util.Scanner;
//Для старой версии драйвера
import com.mysql.jdbc.Driver;
//Все доп библиотеки через манифест resources/META-INF/MANIFEST.MF

public class DataBase {
    static String url;
    static String user;
    static String pass;

    public static void setQRCodeToDataBase(String QRcode, String QRCodeNameFile) {

        Scanner scanner = null;
        try {
            scanner = new Scanner(new File("config/access.txt"));
        } catch (FileNotFoundException e) {
            System.out.println("Файл access.txt не обнаружен");
        }
        //Добавляем пути
        while (scanner.hasNext()) {
            url = scanner.nextLine();
            user = scanner.nextLine();
            pass = scanner.nextLine();
        }
        scanner.close();

        //Пришедший кварко разделяем на значения
        String [] qrCodeSplit = QRcode.split("\\.");
        try {
            //Подключаем коннектор используем переменные выше
            //Connection connection = DriverManager.getConnection(url, user, pass);
            //Connection connection = DriverManager.getConnection(url, user, pass);

            Connection connection = null;
            try {
                Class.forName("com.mysql.jdbc.Driver").newInstance();
                connection = DriverManager.getConnection(url ,
                        user,pass);
                //System.out.println("Доступ есть");
            }
            catch(SQLException ex) {
                System.out.println(ex.getMessage());

            }
            catch(Exception ex) {
                System.out.println(ex.getMessage());
            }

            Statement statement = connection.createStatement();
            //Тестовый кусок
            //statement.execute("INSERT into testbase.2 (vid,nomer,forma,s,vr,filename) values (2,32321,1231,23123,13231,231233)");

            //Получаем значение из базы
            String id = "";
            String vid = "";
            String nomer = "";
            String forma = "";
            String s = "";
            String vr = "";
            String filename = "";
            String resultString = "";

            ResultSet resultSet = statement.executeQuery("SELECT * FROM (base."+qrCodeSplit[0]+") " +
                    "WHERE (vid = "+qrCodeSplit[1]+") and " +
                    "(nomer = "+qrCodeSplit[2]+") and " +
                    "(forma = "+qrCodeSplit[3]+") and " +
                    "(s = "+qrCodeSplit[4]+") and " +
                    "(vr = "+qrCodeSplit[5]+");");

            //Перебираем запрос
            while (resultSet.next()) {
                id = resultSet.getString("id");
                vid = resultSet.getString("vid");
                nomer = resultSet.getString("nomer");
                forma = resultSet.getString("forma");
                s = resultSet.getString("s");
                vr = resultSet.getString("vr");
                filename = resultSet.getString("filename");
                resultString = vid + "." + nomer + "." + forma + "." + s + "." + vr + ".";
            }

            //Если данные найдены
            if (nomer.equals(qrCodeSplit[2])){
                statement.execute("UPDATE (base."+qrCodeSplit[0]+") SET filename = "+QRCodeNameFile+" WHERE id = "+id+"");
            }
            //Если данные не найдены
            if (nomer.isEmpty()) {
                statement.execute("INSERT into base."+qrCodeSplit[0]+" (vid,nomer,forma,s,vr,filename) " +
                        "values ("+qrCodeSplit[1]+","+qrCodeSplit[2]+","+qrCodeSplit[3]+","+qrCodeSplit[4]+","+qrCodeSplit[5]+","+QRCodeNameFile+")");
            }

            //Закрываем подключения
            resultSet.close();
            statement.close();
            connection.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


}
