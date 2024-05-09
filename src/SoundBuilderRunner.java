import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.SourceDataLine;
import java.awt.*;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.StringSelection;
import java.util.InputMismatchException;
import java.util.Scanner;

public class SoundBuilderRunner
{
    public static final float rate = 44100; // number of samples per second.
    public static StringBuilder builder; // used to generate a very long string holding the data.

    public static AudioFormat audioF; // information about the way this sound data is organized.
    public static byte[] buf;  // a short-term holder of a _little_ bit of sound data.
    public static SourceDataLine sourceDL; // the longer-term holder of a bunch of sound data.
    public static Scanner reader = null;

    public static void main(String[] args)
    {
        setupAudio();
        double f1 = requestDoubleInRange("Enter the first frequency in Hz, (100-10000)", 100, 10000 );
        double v1 = requestDoubleInRange("Enter the first amplitude of the wave (0-127). The total of your waves should not exceed 127.", 0,127);
        double f2 = requestDoubleInRange("Enter the second frequency in Hz, (100-10000)", 100, 10000 );
        double v2 = requestDoubleInRange("Enter the second amplitude of the wave (0-127). The total of your waves should not exceed 127.", 0,127);
        double f3 = requestDoubleInRange("Enter the third frequency in Hz, (100-10000)", 100, 10000 );
        double v3 = requestDoubleInRange("Enter the third amplitude of the wave (0-127). The total of your waves should not exceed 127.", 0,127);

        generateAudioData(f1, v1, f2, v2, f3, v3);
        playSound();
        copySoundDataToClipboard();
    }

    /**
     * requests a number from the user and will keep asking until it gets a number in the given range, inclusive.
     * @param prompt - what to say to ask the user for the number
     * @param min - the smallest acceptable number
     * @param max - the largest acceptable number
     * @return
     */
    public static double requestDoubleInRange(String prompt, double min, double max)
    {
        if (reader == null)
            reader = new Scanner(System.in);
        double response;
        while (true)
        {
            System.out.print(prompt);

            try
            {
                response = reader.nextDouble();
            } catch (InputMismatchException imExcept)
            {
                System.out.println("That wasn't a number. Please try again.");
                continue;
            }
            if (response >= min && response <= max)
                return response;
            System.out.println(STR."Your response, \{response} isn't in the range[\{min}, \{max}]. Please try again.");
        }
    }

    /**
     * initializes the stuff you need to make the sound play.
     */
    public static void setupAudio()
    {
        audioF = new AudioFormat(rate, 8, 1, true, false);
        buf = new byte[1];
        try
        {
            sourceDL = AudioSystem.getSourceDataLine(audioF);
            sourceDL.open(audioF);
        } catch (LineUnavailableException luException)
        {
            System.out.println("Problem geting source data line. (Probably not student's fault.)");
            System.out.println(luException.getMessage());
        }
        builder = new StringBuilder();
        System.out.println("Set up and ready to receive generated sound data.");
    }

    /**
     * generates a second worth of sound, based on the sum of three sine waves.
     * @param freq1 - the frequency of the first sine wave
     * @param volume1 - the amplitude of the first sine wave
     * @param freq2 - the frequency of the second sine wave
     * @param volume2 -the amplitude of the second sine wave
     * @param freq3 - the frequency of the third sine wave
     * @param voluem3 - the amplitude of the third sine wave
     */
    public static void generateAudioData(double freq1, double volume1, double freq2, double volume2, double freq3, double voluem3)
    {
        //TODO: here is where you will loop to generate one datum of the generated sound (i.e. one point on the
        // sinusoidal wave) at a time, for a total of one second's worth. Note that the variable "rate" was declared
        // at the top of this file, indicates how many data points you need per second.
        double time = 0; // what time (in seconds) does the current value represent?
        double value = 0; // a number in range [-127 to +127] that is the height of the graph at the current time.

        // write your loop here:

            // calculate the current time for this iteration of the loop.

            // calculate the value at this time. You'll likely want some form of Math.sin(t * f * 2 * Math.PI)

            appendSoundData(time, value);
        // end your loop.

    }

    /**
     * adds a single datum to the sound to play and to the "builder" StringBuilder
     * @param t - the time for this datum
     * @param v - the "height" of the graph at this time.
     */
    public static void appendSoundData(double t, double v)
    {
        // add this value to the sound that will be played. We do a little bit of typecasting to make this work.
        // (Don't worry about this bit of detail.)
        buf[0] = (byte)v;
        sourceDL.write(buf,0,1);

        // TODO: append a line consisting of the time, a tab character, the value, and a newline character to the
        //  StringBuilder, "builder".


    }

    /**
     * plays the sound that you just generated.
     */
    public static void playSound()
    {
        System.out.println("Playing your sound.");
        sourceDL.start();
        sourceDL.drain();
        sourceDL.stop();
        sourceDL.close();
        System.out.println("Done playing sound.");
    }

    /**
     * takes the StringBuilder "builder" and copies all of its content into the Clipboard so it can be pasted into
     * another program.
     */
    public static void copySoundDataToClipboard()
    {
        Clipboard clippy = Toolkit.getDefaultToolkit().getSystemClipboard();
        StringSelection ss = new StringSelection(builder.toString());
        clippy.setContents(ss, ss);
        System.out.println("Sound data saved to the clipboard.");
    }
}
