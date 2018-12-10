import java.io.*;
import java.time.Duration;
import java.util.concurrent.TimeUnit;

/********************************************************
 * Author: Madox Summermatter (madoxs)                  *
 * Date: Fall 2018                                      *
 * Purpose: Research Under Professor Berger             *
 * Description:                                         *
 *                                                      *
 *      The following class takes three space           *
 *      separated inputs:                               *
 *                                                      *
 *          - writing duration in seconds               *
 *          - number of seconds to sleep between writes *
 *          - output file name                          *
 *                                                      *
 *      It writes 0's to the output file for duration   *
 *      seconds, sleeping for numSeconds between each   *
 *      write.                                          *
 ********************************************************/

public class Writer {


    /* ***************************************************************
     *                                                               *
     *                           Constants                           *
     *                                                               *
     *****************************************************************/


    /**
     * Specifies the file path for the input and output files.
     */
    private static final String FILE_PATH = "/Users/madoxfs/Desktop/";
//    private static final String FILE_PATH = "/home/madoxs/scratchdisk/";


    /**
     * Specifies the {@link String} containing usage information.
     */
    private static final String USAGE_MESSAGE =
            "USAGE:\n   This program takes three input arguments separated" +
                    " by a space:\n" +
                    "\n      [duration (in s)] [sleep interval (in ms)] [output filename]\n" +
                    "\n   The first specifies the write duration in seconds, the second" +
                    "\n   specifies the interval of time in milliseconds to sleep" +
                    "\n   between writes, and the third specifies the output" +
                    "\n   filename.";

    /**
     * Specifies the {@link String} containing timeout information.
     */
    private static final String TIMEOUT_MESSAGE =
            "TIMEOUT: This program times out after 10 seconds of inactivity.";


    /**
     * Specifies the number of seconds before timeout.
     */
    private static final int TIMEOUT_DURATION_SECONDS = 10;


    /**
     * Specifies a {@link Duration} of {@link #TIMEOUT_DURATION_SECONDS}.
     */
    private static final Duration timeout = Duration.ofSeconds(
            TIMEOUT_DURATION_SECONDS
    );

    /* ***************************************************************
     *                                                               *
     *                       Global Variables                        *
     *                                                               *
     *****************************************************************/


    /**
     * Specifies the number of writes.
     */
    private long numWrites;


    /**
     * Specifies the number of milliseconds to sleep
     * between each write.
     */
    private int sleepInterval;


    /**
     * Specifies the name of the output {@link File}.
     */
    private String outputFilename;


    /**
     * Specifies the number of seconds to write.
     */
    private long duration;


    /* ***************************************************************
     *                                                               *
     *         Functions responsible for parsing the input.          *
     *                                                               *
     *****************************************************************/


    /**
     * Processes the input from the command line in order
     * to retrieve the names of the input and output files.
     * @return True if the input processed successfully,
     *          false otherwise.
     */
    boolean processInput (
            String[] fields
    )
    {
        // Check that all expected fields are included.
        if (fields.length != 3) {

            // Print out an error message.
            System.out.println(
                    "ERROR: Did not provide correct number of arguments!"
            );

            // Print out usage message.
            System.out.println(
                    USAGE_MESSAGE
            );

            return false;
        }

        /*
         *  Otherwise, the writing duration is first,
         *  the sleep interval is second,
         *  and the output filename is third.
         */

        // Attempt to parse the writing duration.
        try {

            // Compute the duration in milliseconds.
            duration = Long.parseLong(
                    fields[0]
            ) * 1000;

            // Check whether the writing duration is positive.
            if (duration < 0) {

                System.out.println(
                        "ERROR: First argument is negative!"
                );

                System.out.println(
                        USAGE_MESSAGE
                );

                return false;
            }

        } catch (NumberFormatException exception) {

            System.out.println(
                    "ERROR: First argument is non-integer value!"
            );

            System.out.println(
                    USAGE_MESSAGE
            );

            return false;
        }

        // Attempt to parse the sleep interval.
        try {

            // Parse the sleep interval.
            sleepInterval = Integer.parseInt(
                    fields[1]
            );

            // Check whether the sleep interval is negative.
            if (sleepInterval < 0) {

                System.out.println(
                        "ERROR: Second argument is negative!"
                );

                System.out.println(
                        USAGE_MESSAGE
                );

                return false;
            }

        } catch (NumberFormatException exception) {

            System.out.println(
                    "ERROR: Second argument is non-double value!"
            );

            System.out.println(
                    USAGE_MESSAGE
            );

            return false;
        }

        // Retrieve the output filename.
        outputFilename = FILE_PATH + fields[2];

        // Parsed successfully.
        return true;
    }



    /* ***************************************************************
     *                                                               *
     *         Functions responsible for writing out to file.        *
     *                                                               *
     *****************************************************************/



    /**
     * Writes {@link #numWrites} 0's to {@link #outputFilename}.
     */
    void writeZeroes ()
            throws IOException, InterruptedException
    {

        // Create the output file.
        File outputFile = new File(
                outputFilename
        );

        // Used to facilitate writing out to file.
        FileOutputStream fileOutputStream = new FileOutputStream(
                outputFile
        );

        // Used for writing to output file.
        BufferedWriter bufferedWriter = new BufferedWriter(
                new OutputStreamWriter(
                        fileOutputStream
                )
        );

        System.out.println(
                String.format(
                        "WRITING zeros out to file [%s] with [%d]ms of sleep between writes for [%d] seconds",
                        outputFile.getAbsolutePath(),
                        sleepInterval,
                        duration / 1000
                )
        );

        // Time stamp the start of the writing process.
        long start = System.currentTimeMillis();

        // Continue to write to file while the duration
        // not been exceeded.
        for (long timeElapsed = (System.currentTimeMillis() - start);
             timeElapsed < duration;
             timeElapsed = (System.currentTimeMillis() - start)) {

            // Write 10 bytes to file.
            bufferedWriter.write("0");
            bufferedWriter.newLine();

            // Sleep for the specified number of milliseconds.
            TimeUnit.MILLISECONDS.sleep(sleepInterval);

            // Keep track of the number of writes.
            numWrites += 1;
        }

        // Don't forget to close the buffered writer.
        bufferedWriter.close();

        // Print the number of times written and the size
        // of the file.
        printNumberOfBytesWritten(
                outputFile.length()
        );
    }


    /**
     * Prints out the number of bytes written with
     * appropriate units (i.e. KB, MB, GB).
     * Note that each write writes one byte.
     */
    void printNumberOfBytesWritten (
            long lengthOfFile
    )
    {
        // Save the number of bytes written.
        String numberBytesWritten = "";

        if (lengthOfFile < 1000) {

            numberBytesWritten = lengthOfFile + "";
        }

        else if (lengthOfFile < 1000000) {

            numberBytesWritten = String.format(
                    "%.2f KB",
                    lengthOfFile / 1000.0
            );
        }

        else if (lengthOfFile < 1000000000) {

            numberBytesWritten = String.format(
                    "%.2f MB",
                    lengthOfFile / 1000000.0
            );
        }

        else {

            numberBytesWritten = String.format(
                    "%.2f GB",
                    lengthOfFile / 1000000000.0
            );
        }

        System.out.println(
                "WROTE: " + numWrites + " times for total of " + numberBytesWritten
        );
    }


    /**
     * Writes 0's to the file {@link #outputFilename} with
     * {@link #sleepInterval} milliseconds between each write for
     * {@link #duration} seconds.
     */
    public static void main (
            String args[]
    )
            throws IOException, InterruptedException
    {
        // Object for writing 0's out to a file.
        Writer writer = new Writer();

        if (writer.processInput(args)) {
            System.out.println(
                String.format(
                        "write duration: [%d]s\n" +
                                "sleep interval: [%d]ms\n" +
                                "filename: [%s]",
                        writer.duration / 1000,
                        writer.sleepInterval,
                        writer.outputFilename
                )
            );

            writer.writeZeroes();
        }
    }
}
