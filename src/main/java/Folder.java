public class Folder {
    private String pathFolder;
    private String number;

    public Folder(String pathFolder, String number) {
        this.pathFolder = pathFolder;
        this.number = number;
    }

    public String getPathFolder() {
        return pathFolder;
    }

    public void setPathFolder(String pathFolder) {
        this.pathFolder = pathFolder;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }
}
