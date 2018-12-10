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
 *          - reading duration in seconds               *
 *          - number of seconds to sleep between reads  *
 *          - input file name                           *
 *                                                      *
 *      It reads the input file numReads times,         *
 *      sleeping for numSeconds between each read.      *
 ********************************************************/

public class Reader {


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
                    "\n      [duration (in s)] [sleep interval (in ms)] [input filename]\n" +
                    "\n   The first specifies the read duration in seconds, the second" +
                    "\n   specifies the interval of time in milliseconds to sleep" +
                    "\n   between read, and the third specifies the output" +
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


    /**
     * Specifies whether to print out debugging messages.
     */
    private static final boolean DEBUG = true;


    /* ***************************************************************
     *                                                               *
     *                       Global Variables                        *
     *                                                               *
     *****************************************************************/


    /**
     * Specifies the number of reads.
     */
    private int numReads;


    /**
     * Specifies the number of milliseconds to sleep
     * between each read.
     */
    private int sleepInterval;


    /**
     * Specifies the name of the input {@link File}.
     */
    private String inputFilename;


    /**
     * Specifies the number of seconds to read.
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
         *  Otherwise, the reading duration is first,
         *  the sleep interval is second,
         *  and the output filename is third.
         */

        // Attempt to parse the reading duration.
        try {

            // Compute the duration in milliseconds.
            duration = Long.parseLong(
                    fields[0]
            ) * 1000;

            // Check whether the reading duration is positive.
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

        // Retrieve the input filename.
        inputFilename = FILE_PATH + fields[2];

        // Parsed successfully.
        return true;
    }



    /* ***************************************************************
     *                                                               *
     *         Functions responsible for writing out to file.        *
     *                                                               *
     *****************************************************************/


    /**
     * Writes {@link #numReads} 0's to {@link #inputFilename}.
     */
    void readFile ()
            throws IOException, InterruptedException
    {
        // Retrieve the input file.
        File inputFile = new File(
                inputFilename
        );

        // Used for reading the contents of the input file.
        BufferedReader bufferedReader = new BufferedReader(
                new FileReader(
                        inputFile
                )
        );

        System.out.println(
                String.format(
                        "READING file [%s] with [%d]ms of sleep between reads for [%d] seconds",
                        inputFile.getAbsolutePath(),
                        sleepInterval,
                        duration / 1000
                )
        );

        // Keep track of number of reads.
        int readCount = 0;

        // Used for storing the current line of the file.
        String readLine;

        // Time stamp the start of the reading process.
        long start = System.currentTimeMillis();

        // Continue reading while there are still more lines
        // to read.
        while (((readLine = bufferedReader.readLine()) != null)
                && ((System.currentTimeMillis() - start) < duration)) {

            if (DEBUG) {

                System.out.println(
                        readLine
                );
            }

            // Keep track of the number of reads.
            readCount += 1;

            // Sleep for the specified number of milliseconds.
            TimeUnit.MILLISECONDS.sleep(sleepInterval);
        }

        // Time stamp the end of the reading process.
        long end = System.currentTimeMillis();

        // Output the read time.
        System.out.println(
                String.format(
                        "READ [%d] times for [%.2f] seconds:",
                        readCount,
                        (end - start) / 1000.0
                )
        );

        // Don't forget to close the buffered reader.
        bufferedReader.close();
    }



    /**
     * Reads the file {@link #inputFilename} for {@link #duration}
     * seconds with {@link #sleepInterval} milliseconds between each read.
     */
    public static void main (
            String args[]
    )
            throws IOException, InterruptedException
    {
        // Object for reading a file.
        Reader reader = new Reader();

        // Try to process the input.
        if (reader.processInput(args)) {
            System.out.println(
                    String.format(
                            "read duration: [%d]s\n" +
                                    "sleep interval [%d]ms\n" +
                                    "filename: [%s]",
                            reader.duration / 1000,
                            reader.sleepInterval,
                            reader.inputFilename
                    )
            );

            // Read the file according to the given specifications.
            reader.readFile();
        }
    }
}
