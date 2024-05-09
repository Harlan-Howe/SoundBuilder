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
        int f1 = requestIntInRange("Enter the first frequency in Hz, (100-10000)", 100, 10000 );
        int v1 = requestIntInRange("Enter the first amplitude of the wave (0-127). The total of your waves should not exceed 127.", 0,127);
        int f2 = requestIntInRange("Enter the second frequency in Hz, (100-10000)", 100, 10000 );
        int v2 = requestIntInRange("Enter the second amplitude of the wave (0-127). The total of your waves should not exceed 127.", 0,127);
        int f3 = requestIntInRange("Enter the third frequency in Hz, (100-10000)", 100, 10000 );
        int v3 = requestIntInRange("Enter the third amplitude of the wave (0-127). The total of your waves should not exceed 127.", 0,127);

        generateAudioData(f1, v1, f2, v2, f3, v3);
        playSound();
        copySoundDataToClipboard();
    }

    public static int requestIntInRange(String prompt, int min, int max)
    {
        if (reader == null)
            reader = new Scanner(System.in);
        int response;
        while (true)
        {
            System.out.print(prompt);

            try
            {
                response = reader.nextInt();
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

    public static void generateAudioData(int freq1, int volume1, int freq2, int volume2, int freq3, int voluem3)
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

    public static void appendSoundData(double t, double v)
    {
        // add this value to the sound that will be played. We do a little bit of typecasting to make this work.
        // (Don't worry about this bit of detail.)
        buf[0] = (byte)v;
        sourceDL.write(buf,0,1);

        // TODO: append a line consisting of the time, a tab character, the value, and a newline character to the
        //  StringBuilder, "builder".


    }


    public static void playSound()
    {
        System.out.println("Playing your sound.");
        sourceDL.start();
        sourceDL.drain();
        sourceDL.stop();
        sourceDL.close();
        System.out.println("Done playing sound.");
    }
    public static void copySoundDataToClipboard()
    {
        Clipboard clippy = Toolkit.getDefaultToolkit().getSystemClipboard();
        StringSelection ss = new StringSelection(builder.toString());
        clippy.setContents(ss, ss);
        System.out.println("Sound data saved to the clipboard.");
    }
}
