import javax.swing.*;

public class Main {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            SinhVienManager frame = new SinhVienManager();
            frame.setVisible(true);
        });
    }
}