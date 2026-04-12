import java.util.concurrent.TimeUnit;
import java.lang.Math;


/**
 * A typing race simulation. Three typists race to complete a passage of text,
 * advancing character by character — or sliding backwards when they mistype.
 *
 * Originally written by Ty Posaurus, who left this project to "focus on his
 * two-finger technique". He assured us the code was "basically done".
 * We have found evidence to the contrary.
 *
 * @author TyPosaurus
 * @version 0.7 (the other 0.3 is left as an exercise for the reader)
 */
public class TypingRace
{
    private int passageLength;
    private Typist seat1Typist;
    private Typist seat2Typist;
    private Typist seat3Typist;


    private static final double MISTYPE_BASE_CHANCE = 0.3;
    private static final int    SLIDE_BACK_AMOUNT   = 2;
    private static final int    BURNOUT_DURATION     = 3;


    public TypingRace(int passageLength)
    {
        this.passageLength = passageLength;
        seat1Typist = null;
        seat2Typist = null;
        seat3Typist = null;
    }

    public static void main(String[] args) {
        TypingRace race = new TypingRace(40);
        race.addTypist(new Typist('①', "TURBOFINGERS", 0.85), 1);
        race.addTypist(new Typist('②', "QWERTY_QUEEN",  0.60), 2);
        race.addTypist(new Typist('③', "HUNT_N_PECK",   0.30), 3);
        race.startRace();
    }


    public void addTypist(Typist theTypist, int seatNumber)
    {
        if (seatNumber == 1)
        {
            seat1Typist = theTypist;
        }
        else if (seatNumber == 2)
        {
            seat2Typist = theTypist;
        }
        else if (seatNumber == 3)
        {
            seat3Typist = theTypist;
        }
        else
        {
            System.out.println("Cannot seat typist at seat " + seatNumber + " — there is no such seat.");
        }
    }


    public void startRace()
    {
        boolean finished = false;


        if (seat1Typist != null) { seat1Typist.resetToStart(); }
        if (seat2Typist != null) { seat2Typist.resetToStart(); }
        if (seat3Typist != null) { seat3Typist.resetToStart(); }


        while (!finished)
        {
            if (seat1Typist != null) { advanceTypist(seat1Typist); }
            if (seat2Typist != null) { advanceTypist(seat2Typist); }
            if (seat3Typist != null) { advanceTypist(seat3Typist); }


            printRace();


            if (seat1Typist != null && raceFinishedBy(seat1Typist)){
                finished = true;
            }
            if (seat2Typist != null && raceFinishedBy(seat2Typist)){
                finished = true;
            }
            if (seat3Typist != null && raceFinishedBy(seat3Typist)) {
                finished = true;
            }


            try {
                TimeUnit.MILLISECONDS.sleep(200);
            } catch (Exception e) {}
        }


        if(seat1Typist != null && (raceFinishedBy(seat1Typist) == true)){
            System.out.println("The winner is " + seat1Typist.getName());
        }
        else if(seat2Typist != null && (raceFinishedBy(seat2Typist)== true)){
            System.out.println("The winner is " + seat2Typist.getName());
        } else if (seat3Typist != null && raceFinishedBy(seat3Typist) == true) {
            System.out.println("The winner is " + seat3Typist.getName());
        }


        // TODO (Task 2a): Print the winner's name here
    }


    private void advanceTypist(Typist theTypist)
    {
        if (theTypist.isBurntOut())
        {
            theTypist.recoverFromBurnout();
            return;
        }


        if (Math.random() < theTypist.getAccuracy())
        {
            theTypist.typeCharacter();
        }


        // Bug fix: was getAccuracy() * MISTYPE_BASE_CHANCE (inverted)
        if (Math.random() < (1 - theTypist.getAccuracy()) * MISTYPE_BASE_CHANCE)
        {
            theTypist.slideBack(SLIDE_BACK_AMOUNT);
        }


        if (Math.random() < 0.05 * theTypist.getAccuracy() * theTypist.getAccuracy())
        {
            theTypist.burnOut(BURNOUT_DURATION);
        }
    }


    private boolean raceFinishedBy(Typist theTypist)
    {
        // Bug fix: was == (could skip past finish line)
        if (theTypist.getProgress() >= passageLength)
        {
            return true;
        }
        else
        {
            return false;
        }
    }


    private void printRace()
    {
        System.out.print('\u000C');


        System.out.println("  TYPING RACE — passage length: " + passageLength + " chars");
        multiplePrint('=', passageLength + 3);
        System.out.println();


        if (seat1Typist != null){
            printSeat(seat1Typist); System.out.println();
        }
        if (seat2Typist != null) {
            printSeat(seat2Typist); System.out.println();
        }
        if (seat3Typist != null) {
            printSeat(seat3Typist); System.out.println();
        }


        multiplePrint('=', passageLength + 3);
        System.out.println();
        System.out.println("  [zz] = burnt out    [<] = just mistyped");
    }


    private void printSeat(Typist theTypist)
    {
        int spacesBefore = theTypist.getProgress();
        int spacesAfter  = passageLength - theTypist.getProgress();


        System.out.print('|');
        multiplePrint(' ', spacesBefore);


        System.out.print(theTypist.getSymbol());
        if (theTypist.isBurntOut())
        {
            System.out.print('~');
            spacesAfter--;
        }


        multiplePrint(' ', spacesAfter);
        System.out.print('|');
        System.out.print(' ');


        if (theTypist.isBurntOut())
        {
            System.out.print(theTypist.getName()
                    + " (Accuracy: " + theTypist.getAccuracy() + ")"
                    + " BURNT OUT (" + theTypist.getBurnoutTurnsRemaining() + " turns)");
        }
        else
        {
            System.out.print(theTypist.getName()
                    + " (Accuracy: " + theTypist.getAccuracy() + ")");
        }
    }


    private void multiplePrint(char aChar, int times)
    {
        int i = 0;
        while (i < times)
        {
            System.out.print(aChar);
            i = i + 1;
        }
    }
}


