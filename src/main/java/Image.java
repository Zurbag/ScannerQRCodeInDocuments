import java.io.File;

public class Image{
    String qrCode;
    File file;
    String fileName;



    public Image(String qrCode, File file, String fileName) {
        this.qrCode = qrCode;
        this.file = file;
        this.fileName = fileName;
    }

    public String getQrCode() {
        return qrCode;
    }

    public void setQrCode(String qrCode) {
        this.qrCode = qrCode;
    }

    public File getFile() {
        return file;
    }

    public void setFile(File file) {
        this.file = file;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }


}
