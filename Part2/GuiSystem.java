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

    public static void main(String[] args){
        GuiSystem gui = new GuiSystem();
        gui.startRaceGUI();

    }
    public void startRaceGUI() {
        JButton startButton = new JButton("START");
        startButton.addActionListener(e -> setupScreen());
        startButton.setBounds(0,83,125,15);

        JPanel startPanel = new JPanel();
        startPanel.setBounds(0,83,250,83);
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
            while ( answer == null || answer.isEmpty()){
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

        Object[] difficultyStuff = {"Tick Difficulty modifiers:",checkBox,caffeineMode,nightShift};
        JOptionPane.showConfirmDialog(null,difficultyStuff,"Modifiers", JOptionPane.OK_CANCEL_OPTION);

        boolean autoCorrect = checkBox.isSelected();
        boolean caffine = caffeineMode.isSelected();
        boolean nightMode = nightShift.isSelected();

        char[] baseSymbols = {'❶', '❷','❸','❹','❺','❻'};
        typists = new Typist[realNumOfSeats];

        for(int i =0;i< realNumOfSeats;i++){
            typists[i] = new Typist(baseSymbols[i], "Typist "+ (i+1),0.67);
        }
        race = new TypingRace(realAnswer);
        for(int i =0;i < typists.length;i++) {
            race.addTypist(typists[i],i + 1);
        }

        if(autoCorrect == true){
            race.autoCorrectMethod();
        }

        for(int i = 0;i < typists.length;i++){
            if(nightMode == true){
                typists[i].setAccuracy(typists[i].getAccuracy()-0.1);
            }
        }

        if(caffine == true){
            race.setCaffeineActivated();
        }
        Timer clock = new Timer(200, e -> {
            boolean finished = race.turn();
            if (finished == true) {
                ((Timer) e.getSource()).stop();
            }
        });
        clock.start();
    }

    public void mainRace(){
        frame.getContentPane().removeAll();
        frame.setLayout(new BorderLayout());

        JPanel panel = new JPanel();
        frame.add(panel);
    }
}