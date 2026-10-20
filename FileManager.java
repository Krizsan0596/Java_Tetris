import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.LinkedList;

public class FileManager {
    private static final String filename = "scores.sav";

    public void save(LinkedList<Score> scores) {
        try (ObjectOutputStream out = new ObjectOutputStream(
                new FileOutputStream(filename))) {
            out.writeObject(scores);
        } catch (Exception e) {
            System.out.println("Error during file write: " + e.getMessage());
            return;
        }

        System.out.println("Scores saved!");
    }

    public LinkedList<Score> load() {
        try (ObjectInputStream in = new ObjectInputStream(
                new FileInputStream(filename))) {
            Object obj = in.readObject();

            if (!(obj instanceof LinkedList<?> list)) {
                throw new ClassCastException();
            }

            LinkedList<Score> scores = new LinkedList<>();

            for (Object item : list) {
                if (!(item instanceof Score score)) {
                    throw new ClassCastException();
                }

                scores.add(score);
            }

            System.out.println("Scores loaded!");
            return scores;
        } catch (ClassCastException e) {
            System.out.println("Malformed save file.");
            return new LinkedList<>();
        } catch (Exception e) {
            System.out.println("Error during file read: " + e.getMessage());
            return new LinkedList<>();
        }
    }
}
