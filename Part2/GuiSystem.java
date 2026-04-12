import javax.swing.*;

public class GuiSystem {
    public static void startRaceGUI(){
        JLabel label = new JLabel();
        label.setText("Test stuff");


        JFrame frame = new JFrame();
        frame.setVisible(true);
        frame.setSize(250, 250);
        frame.setTitle("Typist simulator");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.add(label);
    }
}
