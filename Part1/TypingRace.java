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
    private Typist seat4Typist;
    private Typist seat5Typist;
    private Typist seat6Typist;

    // Accuracy thresholds for mistype and burnout events
    // (Ty tuned these values "by feel". They may need adjustment.)
    private static final double MISTYPE_BASE_CHANCE = 0.3;
    private int SLIDE_BACK_AMOUNT   = 2;
    private int turnCount = 0;
    private boolean caffeineActivated = false;
    private int BURNOUT_DURATION  = 3;

    public void setCaffeineActivated(){
        caffeineActivated = true;
    }
    public void autoCorrectMethod(){
        SLIDE_BACK_AMOUNT = SLIDE_BACK_AMOUNT /2;
    }
    /**
     * Constructor for objects of class TypingRace.
     * Sets up the race with a passage of the given length.
     * Initially there are no typists seated.
     *
     * @param passageLength the number of characters in the passage to type
     */
    public TypingRace(int passageLength)
    {
        this.passageLength = passageLength;
        seat1Typist = null;
        seat2Typist = null;
        seat3Typist = null;
        seat4Typist = null;
        seat5Typist = null;
        seat6Typist = null;
    }

    public static void main(String[] args) {
    }

    /**
     * Seats a typist at the given seat number (1, 2, or 3).
     *
     * @param theTypist  the typist to seat
     * @param seatNumber the seat to place them in (1–3)
     */
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
        else if (seatNumber == 4)
        {
            seat4Typist = theTypist;
        }
        else if (seatNumber == 5)
        {
            seat5Typist = theTypist;
        }
        else if (seatNumber == 6)
        {
            seat6Typist = theTypist;
        }
        else
        {
            System.out.println("Cannot seat typist at seat " + seatNumber + " — there is no such seat.");
        }
    }

    /**
     * Starts the typing race.
     * All typists are reset to the beginning, then the simulation runs
     * turn by turn until one typist completes the full passage.
     *
     * Note from Ty: "I didn't bother printing the winner at the end,
     * you can probably figure that out yourself."
     */
    public void startRace()
    {
        boolean finished = false;
        // Reset all typists to the start of the passage
        // (Ty was in a hurry here)

        if (seat1Typist != null) { seat1Typist.resetToStart(); }
        if (seat2Typist != null) { seat2Typist.resetToStart(); }
        if (seat3Typist != null) { seat3Typist.resetToStart(); }
        if (seat4Typist != null) { seat4Typist.resetToStart(); }
        if (seat5Typist != null) { seat5Typist.resetToStart(); }
        if (seat6Typist != null) { seat6Typist.resetToStart(); }



        while (!finished)
        {
            // Advance each typist by one turn
            if (seat1Typist != null) { advanceTypist(seat1Typist); }
            if (seat2Typist != null) { advanceTypist(seat2Typist); }
            if (seat3Typist != null) { advanceTypist(seat3Typist); }
            if (seat4Typist != null) { advanceTypist(seat4Typist); }
            if (seat5Typist != null) { advanceTypist(seat5Typist); }
            if (seat6Typist != null) { advanceTypist(seat6Typist); }

            turnCount += 1;
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
            if (seat4Typist != null && raceFinishedBy(seat4Typist)) {
                finished = true;
            }
            if (seat5Typist != null && raceFinishedBy(seat5Typist)) {
                finished = true;
            }
            if (seat6Typist != null && raceFinishedBy(seat6Typist)) {
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
        else if (seat4Typist != null && raceFinishedBy(seat4Typist) == true) {
            System.out.println("The winner is " + seat4Typist.getName());
        }
        else if (seat5Typist != null && raceFinishedBy(seat5Typist) == true) {
            System.out.println("The winner is " + seat5Typist.getName());
        }
        else if (seat6Typist != null && raceFinishedBy(seat6Typist) == true) {
            System.out.println("The winner is " + seat6Typist.getName());
        }

    }

    /**
     * Simulates one turn for a typist.
     *
     * If the typist is burnt out, they recover one turn's worth and skip typing.
     * Otherwise:
     *   - They may type a character (advancing progress) based on their accuracy.
     *   - They may mistype (sliding back) — the chance of a mistype should decrease
     *     for more accurate typists.
     *   - They may burn out — more likely for very high-accuracy typists
     *     who are pushing themselves too hard.
     *
     * @param theTypist the typist to advance
     */

    private void advanceTypist(Typist theTypist)
    {
        if (theTypist.isBurntOut())
        {
            // Recovering from burnout — skip this turn
            theTypist.recoverFromBurnout();
            return;
        }

        // Attempt to type a character
        if (Math.random() < theTypist.getAccuracy())
        {
            theTypist.typeCharacter();
        }

        // Mistype check — the probability should reflect the typist's accuracy
        if (Math.random() < (1 - theTypist.getAccuracy()) * MISTYPE_BASE_CHANCE)
        {
            theTypist.slideBack(SLIDE_BACK_AMOUNT);
        }

        // Burnout check — pushing too hard increases burnout risk
        // (probability scales with accuracy squared, capped at ~0.05)
        if (Math.random() < 0.05 * theTypist.getAccuracy() * theTypist.getAccuracy())
        {
            theTypist.burnOut(BURNOUT_DURATION);
        }
    }

    /**
     * Returns true if the given typist has completed the full passage.
     *
     * @param theTypist the typist to check
     * @return true if their progress has reached or passed the passage length
     */
    private boolean raceFinishedBy(Typist theTypist)
    {
        if (theTypist.getProgress() >= passageLength)
        {
            return true;
        }
        else
        {
            return false;
        }
    }

    /**
     * Prints the current state of the race to the terminal.
     * Shows each typist's position along the passage, burnout state,
     * and a WPM estimate based on current progress.
     */
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
        if (seat4Typist != null){
            printSeat(seat4Typist); System.out.println();
        }
        if (seat5Typist != null){
            printSeat(seat5Typist); System.out.println();
        }
        if (seat6Typist != null){
            printSeat(seat6Typist); System.out.println();
        }

        multiplePrint('=', passageLength + 3);
        System.out.println();
        System.out.println("  [zz] = burnt out    [<] = just mistyped");
    }

    /**
     * Prints a single typist's lane.
     *
     * Examples:
     *   |          ⌨           | TURBOFINGERS (Accuracy: 0.85)
     *   |    [zz]              | HUNT_N_PECK  (Accuracy: 0.40) BURNT OUT (2 turns)
     *
     * Note: Ty forgot to show when a typist has just mistyped. That would
     * be a nice improvement — perhaps a [<] marker after their symbol.
     *
     * @param theTypist the typist whose lane to print
     */
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

    /**
     * Prints a character a given number of times.
     *
     * @param aChar the character to print
     * @param times how many times to print it
     */
    private void multiplePrint(char aChar, int times)
    {
        int i = 0;
        while (i < times)
        {
            System.out.print(aChar);
            i = i + 1;
        }
    }

    public boolean turn() {
        // Advance each typist by one turn
        if (seat1Typist != null) { advanceTypist(seat1Typist); }
        if (seat2Typist != null) { advanceTypist(seat2Typist); }
        if (seat3Typist != null) { advanceTypist(seat3Typist); }
        if (seat4Typist != null) { advanceTypist(seat4Typist); }
        if (seat5Typist != null) { advanceTypist(seat5Typist); }
        if (seat6Typist != null) { advanceTypist(seat6Typist); }

        if (seat1Typist != null && raceFinishedBy(seat1Typist)){
            return true;
        }
        if (seat2Typist != null && raceFinishedBy(seat2Typist)){
            return true;
        }
        if (seat3Typist != null && raceFinishedBy(seat3Typist)) {
            return true;
        }
        if (seat4Typist != null && raceFinishedBy(seat4Typist)) {
            return true;
        }
        if (seat5Typist != null && raceFinishedBy(seat5Typist)) {
            return true;
        }
        if (seat6Typist != null && raceFinishedBy(seat6Typist)) {
            return true;
        }
        return false;
    }
}
