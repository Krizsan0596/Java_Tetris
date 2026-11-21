package hu.krizsan.tetris;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.LinkedList;

import javax.swing.JOptionPane;

public class FileManager {
    private static final String filename = "scores.sav";

    public void save(LinkedList<Score> scores) {
        try (ObjectOutputStream out = new ObjectOutputStream(
                new FileOutputStream(filename))) {
            out.writeObject(scores);
        } catch (Exception e) {
            System.out.println("Error during file write: " + e.getMessage());
            JOptionPane.showMessageDialog(
                    null,
                    "Could not save high scores.\n" + e.getMessage(),
                    "Save Error",
                    JOptionPane.ERROR_MESSAGE);
            return;
        }

        System.out.println("Scores saved!");
    }

    public LinkedList<Score> load() throws ClassCastException, ClassNotFoundException, IOException {
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
        } catch (IOException | ClassNotFoundException | ClassCastException e) {
            if (e instanceof FileNotFoundException && Files.notExists(Path.of(filename))) {
                System.out.println("No saved data found.");
                return new LinkedList<>();
            }

            System.out.println("Error during file read: " + e.getMessage());
            int choice = JOptionPane.showConfirmDialog(
                    null,
                    "Could not load high scores.\n" + e.getMessage()
                            + "\nOverwrite the save with an empty leaderboard?\nExisting scores will be lost.",
                    "Load Error",
                    JOptionPane.YES_NO_OPTION,
                    JOptionPane.WARNING_MESSAGE);
            if (choice == JOptionPane.YES_OPTION) {
                LinkedList<Score> scores = new LinkedList<>();
                save(scores);
                return scores;
            }
            throw e;
        }
    }
}
