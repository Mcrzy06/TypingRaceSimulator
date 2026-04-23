import javax.swing.*;
import java.awt.*;

public class GuiSystem {
    JFrame frame = new JFrame();
    TypingRace race;
    Typist[] typists;
    String passageText;
    private int realAnswer;
    JLabel label;
    JProgressBar[] typistProgress;


    public GuiSystem() {
        frame.setSize(250, 250);
        frame.setVisible(true);
        frame.setLayout(null);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setTitle("Typist simulator game");

    }

    public static void main(String[] args) {
        GuiSystem gui = new GuiSystem();
        gui.startRaceGUI();

    }

    public void startRaceGUI() {
        JButton startButton = new JButton("START");
        startButton.addActionListener(e -> setupScreen());
        startButton.setBounds(0, 83, 125, 15);

        JPanel startPanel = new JPanel();
        startPanel.setBounds(0, 83, 250, 83);
        frame.add(startPanel);
        startPanel.add(startButton);
    }

    public void setupScreen() {
        JLabel header = new JLabel("Typing game");
        frame.add(header);
        header.setBounds(0, 83, 125, 15);


        String[] responses = {"Short", "Medium", "Long", "Custom"};
        int input = JOptionPane.showOptionDialog(null, "What length would you like", "Configuration screen", JOptionPane.YES_NO_CANCEL_OPTION, JOptionPane.INFORMATION_MESSAGE, null, responses, responses[0]);
        String answer;

        if (input == 3) {
            answer = JOptionPane.showInputDialog("Enter your Custom passage: ");
            while (answer == null || answer.isEmpty()) {
                answer = JOptionPane.showInputDialog("Enter your Custom passage: ");
            }
            passageText = answer;
            realAnswer = answer.length();
        } else {
            answer = responses[input];
            if (answer.equals("Short")) {
                realAnswer = 20;
                passageText = "Birb is the largest bird";
            } else if (answer.equals("Medium")) {
                realAnswer = 40;
                passageText = "The lion doesn't care about the opinons of sheep";
            } else if (answer.equals("Long")) {
                realAnswer = 60;
                passageText = "The pickle pepper piper went to find peppers in the market stall grilled";
            } else {
                realAnswer = Integer.parseInt(answer);
            }
        }
        // testing System.out.println(answer);
        String[] seats = {"2", "3", "4", "5", "6"};
        int numOfSeats = JOptionPane.showOptionDialog(null, "How many seats do you need", "Configuration screen", JOptionPane.YES_NO_CANCEL_OPTION, JOptionPane.INFORMATION_MESSAGE, null, seats, seats[0]);

        int realNumOfSeats = numOfSeats + 2;
        JCheckBox checkBox = new JCheckBox();
        checkBox.setText("Autocorrect On");
        checkBox.setFocusable(false);
        JCheckBox caffeineMode = new JCheckBox();
        caffeineMode.setText("Caffeine Mode");
        caffeineMode.setFocusable(false);
        JCheckBox nightShift = new JCheckBox();
        nightShift.setText("Night mode");
        nightShift.setFocusable(false);
        JButton submitButton = new JButton();
        submitButton.setText("Submit");
        //submitButton.addActionListener(e -> )

        Object[] difficultyStuff = {"Tick Difficulty modifiers:", checkBox, caffeineMode, nightShift};
        JOptionPane.showConfirmDialog(null, difficultyStuff, "Modifiers", JOptionPane.OK_CANCEL_OPTION);

        boolean autoCorrect = checkBox.isSelected();
        boolean caffine = caffeineMode.isSelected();
        boolean nightMode = nightShift.isSelected();

        char[] baseSymbols = {'❶', '❷', '❸', '❹', '❺', '❻' }; // base symbols for each typist will be able to change later
        typists = new Typist[realNumOfSeats];

        for (int i = 0; i < realNumOfSeats; i++) {
            typists[i] = new Typist(baseSymbols[i], "Typist " + (i + 1), 0.67);

            String[] typingStyleOptions = {" Touch Typist, Hunt & Peck, Phone Thumbs", "Voice-to-Text"};
            int typingStyle = JOptionPane.showOptionDialog(null, "please pick a typing style", "Customisation", JOptionPane.YES_NO_CANCEL_OPTION, JOptionPane.INFORMATION_MESSAGE, null, typingStyleOptions, typingStyleOptions[0]);
            if (typingStyle == 0) {
                typists[i].setAccuracy(0.95);
            } else if (typingStyle == 1) {
                typists[i].setAccuracy(0.69);
            } else if (typingStyle == 2) {
                typists[i].setAccuracy(0.50);

            } else if (typingStyle == 3) {
                typists[i].setAccuracy(0.75);
            } else {
                System.out.println("No typing style selected , default of 0.67 applied");
            }
            // i need to add a burnout change too *Important
            String[] keyboardType = {"Mechanical", "Membrane", "Touch screen", "Stenography"};
            int boardType = JOptionPane.showOptionDialog(null, "Please pick a keyboard style", "Customisation screen", JOptionPane.YES_NO_CANCEL_OPTION, JOptionPane.INFORMATION_MESSAGE, null, keyboardType, keyboardType[0]);
            if (boardType == 0) {
                race.setMISTYPE_BASE_CHANCE(0.5);
                typists[i].setAccuracy(0.75); // speed adjustment
            } else if (boardType == 1) { // issue im having is that idk if i should adjust the typist field for each typist or for the whole race as odk if im allowed to change the typist class, for now ill apple it to the whole race and make ill tweak it later
                race.setMISTYPE_BASE_CHANCE(0.7);
                typists[i].setAccuracy(0.65);
            } else if (boardType == 2) {
                race.setMISTYPE_BASE_CHANCE(0.55);
                typists[i].setAccuracy(0.85);
            } else if (boardType == 3) {
                race.setMISTYPE_BASE_CHANCE(0.90);
                typists[i].setAccuracy(0.55);
            }
            else {
                System.out.println("Please enter a valid keyboard type");

            }
        }

        race = new TypingRace(realAnswer);
        for (int i = 0; i < typists.length; i++) {
            race.addTypist(typists[i], i + 1);
        }

        if (autoCorrect == true) {
            race.autoCorrectMethod();
        }

        for (int i = 0; i < typists.length; i++) {
            if (nightMode == true) {
                typists[i].setAccuracy(typists[i].getAccuracy() - 0.1);
            }
        }

        if (caffine == true) {
            race.setCaffeineActivated();
        }
        Timer clock = new Timer(200, e -> {
            boolean finished = race.turn();
            for (int i = 0; i < typists.length; i++) {
                typistProgress[i].setValue(typists[i].getProgress());
            }
            if (finished == true) {
                ((Timer) e.getSource()).stop();
                for (int i = 0; i < typists.length; i++) {
                    if (typists[i].getProgress() >= realAnswer) {
                        label.setText("the winner is: " + typists[i].getName());
                        frame.repaint();
                        break;
                    }
                }
            }
        });
        mainRace();
        clock.start();
    }

    public void mainRace() {
        frame.getContentPane().removeAll();
        frame.setLayout(new BorderLayout());

        JPanel panel = new JPanel();
        label = new JLabel(passageText);
        panel.add(label);
        frame.add(panel, BorderLayout.NORTH);
        JPanel keyboardPanel = new JPanel();
        keyboardPanel.setBackground(Color.black);
        keyboardPanel.setPreferredSize(new Dimension(250, 83));
        frame.add(keyboardPanel, BorderLayout.SOUTH);


        JPanel raceStuff = new JPanel();
        raceStuff.setLayout(new GridLayout(1, typists.length));
        typistProgress = new JProgressBar[typists.length];
        for (int i = 0; i < typists.length; i++) {
            typistProgress[i] = new JProgressBar(JProgressBar.VERTICAL, realAnswer);
            typistProgress[i].setString(typists[i].getName());
            typistProgress[i].setStringPainted(true);
            raceStuff.add(typistProgress[i]);
        }
        frame.add(raceStuff, BorderLayout.CENTER);

        frame.revalidate();
        frame.repaint();
    }
}
