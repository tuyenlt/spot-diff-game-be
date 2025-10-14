package imggame.engine;

class DiffBox {
    public int x;
    public int y;
    public int width;
    public int height;
    public boolean found = false;

    public DiffBox(int x, int y, int width, int height) {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
    }
}

public class ImageSet {
    private String originImagePath;
    private String diffImagePath;
    private DiffBox[] diffBoxes;

    public ImageSet() {
        // Default image set
        this.originImagePath = "images/original.jpg";
        this.diffImagePath = "images/differences.jpg";
        this.diffBoxes = new DiffBox[] {
            new DiffBox(50, 50, 30, 30),
            new DiffBox(150, 80, 25, 25),
            new DiffBox(200, 200, 20, 20),
            new DiffBox(300, 150, 40, 40),
            new DiffBox(400, 300, 35, 35)
        };
    }

    public ImageSet(String originImagePath, String diffImagePath, DiffBox[] diffBoxes) {
        this.originImagePath = originImagePath;
        this.diffImagePath = diffImagePath;
        this.diffBoxes = diffBoxes;
    }

    public String getOriginImagePath() {
        return originImagePath;
    }

    public String getDiffImagePath() {
        return diffImagePath;
    }

    public DiffBox[] getDiffBoxes() {
        return diffBoxes;
    }

    public int getTotalDifferences() {
        return diffBoxes.length;
    }

    public boolean checkGuess(int x, int y) {
        for (DiffBox box : diffBoxes) {
            if (!box.found && x >= box.x && x <= box.x + box.width && y >= box.y && y <= box.y + box.height) {
                box.found = true;
                return true;
            }
        }
        return false;
    }
}
