import javax.swing.*;
import javax.swing.text.Style;
import javax.swing.text.StyleConstants;
import javax.swing.text.StyledDocument;
import java.awt.*;

public class GuiSystem {
    JFrame frame = new JFrame();
    TypingRace race;
    Typist[] typists;
    String passageText;
    private int realAnswer;
    //JLabel label;
    JLabel winner;
    JTextPane[] typistTracker;
    JProgressBar[] typistProgress;
    Color[] progressBarColours;
    int raceTurns;
    int[] leaderBoardStats;
    double[] highestScore;
    int winCountAndBurnouts[][];
    double history[][];



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
                passageText = "Birb is the biggest bird";
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
        race = new TypingRace(realAnswer);
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
        progressBarColours = new Color[realNumOfSeats]; // creating the colour array for each typist picked
        leaderBoardStats = new int[realNumOfSeats];
        highestScore = new double[realNumOfSeats];
        typists = new Typist[realNumOfSeats];
        winCountAndBurnouts = new int[realNumOfSeats][2];
        history = new double[realNumOfSeats][5];

       // for(int i = 0;i < typists.length;i++){
         //   typistsNames = JOptionPane.showInputDialog("Please enter your name,Typist: ");
           // typists[i].setTypistName(typistsNames);

        //}


        for (int i = 0; i < realNumOfSeats; i++) {
            String name = JOptionPane.showInputDialog("Enter your name, typist: ");
            if(name == null){
                name = "typist "+ (i + 1); // jus incase anyone makes the mistake of pressing cancel or not writing anything
            }
            typists[i] = new Typist(baseSymbols[i], name, 0.67,0);

            String[] typingStyleOptions = {" Touch Typist"," Hunt & Peck", "Phone Thumbs", "Voice-to-Text"};
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
                typists[i].setmisstypeRates(0.5);
                typists[i].setAccuracy(0.85); // speed adjustment
            } else if (boardType == 1) { // issue im having is that idk if i should adjust the typist field for each typist or for the whole race as odk if im allowed to change the typist class, for now ill apple it to the whole race and make ill tweak it later
                typists[i].setmisstypeRates(0.7);
                typists[i].setAccuracy(0.75);
            } else if (boardType == 2) {
                typists[i].setmisstypeRates(0.55);
                typists[i].setAccuracy(0.85);
            } else if (boardType == 3) {
                typists[i].setmisstypeRates(0.90);
                typists[i].setAccuracy(0.90);
            } // changes made to typist class implemented
            else {
                System.out.println("Please pick a valid keyboard type");

            }
            //the typist can choose a symbol for their representation
           // JOptionPane.showOptionDialog(null, "Default or custom Symbol","Customisation screen", JOptionPane.YES_NO_CANCEL_OPTION, JOptionPane.INFORMATION_MESSAGE,null,baseSymbols,);
            String playerSymbolString = JOptionPane.showInputDialog("Please input your custom Symbol: ");
            while (playerSymbolString == null || !playerSymbolString.matches(".")) {
                playerSymbolString = JOptionPane.showInputDialog("This response is unaccepted, please try a new symbol: ");
            }
            char playerSymbol = playerSymbolString.charAt(0);
            typists[i].setSymbol(playerSymbol);

            //Colouring the progress bars
            String colours[] = {"Red", "Green", "Blue", "Yellow", "Orange"};
            int colourSpectrum = JOptionPane.showOptionDialog(null, "what Colour would you like your progess bar", "Customisation screen", JOptionPane.YES_NO_CANCEL_OPTION, JOptionPane.INFORMATION_MESSAGE, null, colours, colours[0]);
            if (colourSpectrum == 0) { // tried using a for loop to optimise but that fell apart so if /else wall it is
                progressBarColours[i] = Color.red;
            } else if (colourSpectrum == 1) {
                progressBarColours[i] = Color.GREEN;
            } else if (colourSpectrum == 2) {
                progressBarColours[i] = Color.BLUE;
            } else if (colourSpectrum == 3) {
                progressBarColours[i] = Color.yellow;
            } else if (colourSpectrum == 4) {
                progressBarColours[i] = Color.ORANGE;
            } else {
                System.out.println("INVALID OPTION");
            }

            JCheckBox wristSupport = new JCheckBox();
            wristSupport.setText("Wrist Support");
            wristSupport.setFocusable(false);
            JCheckBox energyDrink = new JCheckBox();
            energyDrink.setText("Energy Drink");
            energyDrink.setFocusable(false);
            JCheckBox noiseCancellingHeadphones = new JCheckBox();
            noiseCancellingHeadphones.setText("Noise Cancelling Headphones");
            noiseCancellingHeadphones.setFocusable(false);

            Object[] Accessories = {"Accessories: ", wristSupport, energyDrink, noiseCancellingHeadphones};
            JOptionPane.showConfirmDialog(null, Accessories, "Accessories", JOptionPane.OK_CANCEL_OPTION);

            boolean wristsupport = wristSupport.isSelected();
            boolean energydrink = energyDrink.isSelected();
            boolean noisecancellingheadphones = noiseCancellingHeadphones.isSelected();


            if (wristsupport == true){
                typists[i].setWristSupportMechanic();
            }

            if(noisecancellingheadphones == true){
                typists[i].setmisstypeRates(typists[i].getmisstypeRates() - 0.1);
            }

            if(energydrink == true){
                typists[i].setEnergyDrinkMechanic();
            }

        }


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
            raceTurns +=1; // had to correct my mistake with =+
            for (int i = 0; i < typists.length; i++) {
                typistProgress[i].setValue(typists[i].getProgress());
            }
            typistColourTracking();
            if (finished == true) {
                ((Timer) e.getSource()).stop();
                double raceTime = (raceTurns * 200)/60000.0;
                statisticsDisplayScreen(raceTime);
            }
        });
        mainRace();
        clock.start();
    }

    public void mainRace() {
        frame.getContentPane().removeAll();
        frame.setLayout(new BorderLayout());

        JPanel panel = new JPanel();
        typistTracker = new JTextPane[typists.length];
        for(int j= 0; j<typists.length;j++){
            typistTracker[j] = new JTextPane();
            typistTracker[j].setText(passageText);
            panel.add(typistTracker[j]);

        }

        winner = new JLabel("");
        panel.add(winner);
        frame.add(panel, BorderLayout.NORTH);
        JPanel keyboardPanel = new JPanel();
        keyboardPanel.setBackground(Color.black);
        keyboardPanel.setPreferredSize(new Dimension(250, 83));
        frame.add(keyboardPanel, BorderLayout.SOUTH);


        JPanel raceStuff = new JPanel();
        raceStuff.setLayout(new GridLayout(2, typists.length));
        typistProgress = new JProgressBar[typists.length];
        for (int i = 0; i < typists.length; i++) {
            typistProgress[i] = new JProgressBar(JProgressBar.VERTICAL, 0,realAnswer);
            typistProgress[i].setString(typists[i].getName());
            typistProgress[i].setStringPainted(true);
            typistProgress[i].setForeground(progressBarColours[i]);
            raceStuff.add(typistProgress[i]);
        }
        frame.add(raceStuff, BorderLayout.CENTER);

        frame.revalidate();
        frame.repaint();
    }

    public void typistColourTracking(){
        for(int i =0;i<typists.length;i++) {
            StyledDocument styleSheet = typistTracker[i].getStyledDocument();
            Style defaulted = typistTracker[i].addStyle("Default", null);
            StyleConstants.setForeground(defaulted, Color.BLACK);
            styleSheet.setCharacterAttributes(0, passageText.length(), defaulted, true);

            Style style = typistTracker[i].addStyle("Typist " + i, null);
            StyleConstants.setForeground(style, progressBarColours[i]);
            styleSheet.setCharacterAttributes(0, typists[i].getProgress(), style, true); // this doesn't even look good lmao im gonna redo it

            int progress = Math.min(typists[i].getProgress(), passageText.length());
            styleSheet.setCharacterAttributes(0, progress, style, true);

        }
    }

    public void statisticsDisplayScreen(double raceTime){
        frame.getContentPane().removeAll();
        frame.setLayout(new BorderLayout());
        JLabel winnerLabel = new JLabel("");
        for(int i = 0;i < typists.length;i++) {
            if (typists[i].getProgress() >= realAnswer) {
                winnerLabel.setText("the winner is: " + typists[i].getName());
            }
        }
        frame.add(winnerLabel,BorderLayout.NORTH);


        JPanel stats = new JPanel();
        stats.setLayout(new GridLayout(1, typists.length));
        for(int i = 0;i < typists.length;i++){
            JTextPane statistics = new JTextPane();
            double wordsPerMinute = (typists[i].getProgress()/5.0)/raceTime;
            double changeInAccuracy = typists[i].getAccuracy() - typists[i].getBaseTypedAmount();

            if(wordsPerMinute > highestScore[i]){
                highestScore[i] = wordsPerMinute;
            }

            history[i][raceTurns < 5 ? raceTurns : 4]= wordsPerMinute;
            if(typists[i].getProgress() >= realAnswer){
                winCountAndBurnouts[i][0] += 1;
            }
            else{
                winCountAndBurnouts[i][0] += 0;
            }

            if(typists[i].getProgress() == 0){
                winCountAndBurnouts[i][0] += 1;
            }
            else{
                winCountAndBurnouts[i][0] += 0;
            }
            String badgeName = "";
            if(winCountAndBurnouts[i][0] >= 3){
                badgeName = "Speed demon";
            }

            if(winCountAndBurnouts[i][0] >= 5){
                badgeName = "Iron fingers";
            }

            statistics.setText("Typist name: " + typists[i].getName() + "\nWords per Minute:" + String.format("%.1f",wordsPerMinute) + "\nPersonal Best:" + String.format("%.1f",highestScore[i])
            + " \nBurnout Count: " +typists[i].getBurntOutNumber() +
                    " \nTypist accuracy: " + typists[i].getAccuracy() + " \nAccuracy change: " + String.format("%.1f",changeInAccuracy) + "\nBadges" + badgeName);
            stats.add(statistics);
        }
        frame.add(stats, BorderLayout.CENTER);
        JButton restartButton = new JButton();
        restartButton.setText("Restart typing simulator");
        restartButton.addActionListener(e -> setupScreen());
        //I should really comment my thoughts/logic more so here we go, For the leaderBoard in statisticsDisplayScreen i need to loop through the total player scores for each player throughout the games and then ill add that all up together and display it on a new pannel, ill use if and else statements to see who has the highest score so the leaderboard can be updated. I think thats the plan for now let me see how it goes. Also before i forget the winner gets 3 points second place gets 2 and everyone else gets 1 if they finish.

        for(int i =0;i<typists.length;i++) {
            if (typists[i].getProgress() >= realAnswer) {
                leaderBoardStats[i] += 3; //made a mistake this should be 3 not 1
            } else {
                int setPoint = 0;
                boolean first = true;
                if (first == true) {
                    for (int j = 0; j < typists.length; j++) {
                        if (typists[j].getProgress() < realAnswer && typists[j].getProgress() > setPoint) {
                            setPoint = typists[j].getProgress();
                        }
                    }
                    if (typists[i].getProgress() == setPoint) {
                        leaderBoardStats[i] += 2; // second place
                    } else {
                        leaderBoardStats[i] += 1; //everybody else
                    }
                }
            }
        }

        JPanel lBP = new JPanel();
        lBP.setLayout(new GridLayout(typists.length + 1, 1));
        JLabel tittle = new JLabel("",JLabel.CENTER);
        tittle.setText("LEADERBOARD");
        lBP.add(tittle);

        for(int leaderboardCount = 0;leaderboardCount<typists.length;leaderboardCount++){
            JTextPane content = new JTextPane();
            content.setText(typists[leaderboardCount].getName() + "\nPoints: "+leaderBoardStats[leaderboardCount]);
            lBP.add(content);
        }
        // i need to make sure the highest score/personal bets is found , ill run the program again from a reply button(prolly easiest way to do that is a JButton) then ill compare the personal best which will be set for each typist after the race(first one) and then after that ill compare that with the next races(each typists) personal best and then display the new personal best.

        frame.add(restartButton, BorderLayout.WEST);
        frame.add(lBP, BorderLayout.SOUTH);
        frame.revalidate();
        frame.repaint();
    }
}
//changes for final commit