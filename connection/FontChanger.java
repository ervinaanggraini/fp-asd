import javax.swing.*;
import java.awt.*;

public class FontChanger {
    public static final Font FONT_STATUS = new Font("OCR A Extended", Font.PLAIN, 14);

    public static void main(String[] args) {
        // Mengubah font default untuk seluruh komponen UI
        UIManager.put("Label.font", FONT_STATUS);
        UIManager.put("Button.font", FONT_STATUS);
        UIManager.put("TextField.font", FONT_STATUS);
        UIManager.put("TextArea.font", FONT_STATUS);
        UIManager.put("ComboBox.font", FONT_STATUS);
        UIManager.put("Menu.font", FONT_STATUS);
        UIManager.put("MenuItem.font", FONT_STATUS);
        UIManager.put("CheckBox.font", FONT_STATUS);

        // Membuat frame untuk melihat hasil font yang diterapkan
        JFrame frame = new JFrame("Contoh Penggunaan Font OCR A Extended");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        // Menambahkan beberapa komponen dengan font OCR A Extended
        JLabel label = new JLabel("Selamat datang di aplikasi OCR A Extended!");
        JButton button = new JButton("Klik saya!");
        JTextField textField = new JTextField("Masukkan teks di sini");

        frame.setLayout(new FlowLayout());
        frame.add(label);
        frame.add(button);
        frame.add(textField);

        frame.setSize(400, 200);
        frame.setVisible(true);
    }
}
