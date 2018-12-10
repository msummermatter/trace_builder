/********************************************************
 * Author: Madox Summermatter (madoxs)                  *
 * Date: Fall 2018                                      *
 * Purpose: Research Under Professor Berger             *
 * Description:                                         *
 *                                                      *
 *      The following class takes an input of two       *
 *      Strings from the command line representing      *
 *      input and output file names.  The input         *
 *      file name corresponds to a file containing the  *
 *      output of a blkparse operation.  This class     *
 *      parses the input file, filters in the           *
 *      relevant entries, and outputs a CSV file.       *
 ********************************************************/

import java.io.*;

public class BlkparseToCSV {


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
     * Specifies whether to print out debugging messages.
     */
    private static final boolean DEBUG = false;


    /* ***************************************************************
     *                                                               *
     *                       Global Variables                        *
     *                                                               *
     *****************************************************************/


    /**
     * Specifies the name of the input {@link File}.
     */
    private String inputFilename;


    /**
     * Specifies the name of the output {@link File}.
     */
    private String outputFilename;




    /* ***************************************************************
     *                                                               *
     *         Functions responsible for parsing the input.          *
     *                                                               *
     *****************************************************************/


    /**
     * Processes the input from the command line in order
     * to retrieve the names of the input and output files.
     */
    void processInput (
            String[] fields
    )
            throws IOException
    {
//        // Object responsible for reading the input from
//        // the command line.
//        BufferedReader bufferedReader = new BufferedReader(
//                new InputStreamReader(
//                        System.in
//                )
//        );
//
//        // Read the line of input that contains the
//        // names of the input and output files and
//        // trim the leading and trailing spaces.
//        String line = bufferedReader.readLine().trim();

//        // Split the line into the input and output
//        // file names.
//        String[] fields = line.split(" ");

        // Ensure that both an input and output file
        // name is included.
        if (fields.length != 2) {

            // Print out an error message.
            System.out.println(
                    "ERROR: Did not provide an input and output filename" +
                            " to the command line!"
            );

            return;
        }

        // Otherwise, the input filename is first,
        // and the output filename is second.
        inputFilename = FILE_PATH + fields[0];
        outputFilename = FILE_PATH + fields[1];
    }


    /**
     * Parses the output of blkparse into a CSV file.
     *
     * The output of blkparse has the following format, line-by-line:
     *
     *  major,minor cpu sequence timestamp pid action rbws offset + size [process_name]
     *
     * The CSV file has the following format, line-by-line:
     *
     *  major,minor,sequence,timestamp,pid,rbws,offset,size
     * @throws IOException
     */
    void parseToCSV ()
            throws IOException
    {
        try {

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

            // Used for storing the current line of the file.
            String readLine;

            System.out.println(
                    "READING in file: " + inputFile.getAbsolutePath()
            );

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
                    "WRITING out to file: " + outputFile.getAbsolutePath()
            );

            // Continue reading while there are still more lines
            // to read.
            while ((readLine = bufferedReader.readLine()) != null) {

                if (DEBUG) {
                    System.out.println(
                            readLine
                    );
                }

                // Indices used to extract an entry.
                int startIndex = 0;
                int endIndex = 0;

                // Specifies whether an entry as been found.
                boolean foundEntry = false;

                // Specifies which entry in the line has
                // been extracted.
                int entryNumber = 0;

                // Scan the line for entries.
                for (int i = 0; i < readLine.length(); i++) {

                    // Check whether an entry has been found.
                    if (foundEntry == false) {

                        // Any character besides space marks
                        // the beginning of an entry.
                        if (readLine.charAt(i) != ' ') {

                            // Set the starting index for the
                            // entry.
                            startIndex = i;

                            // Set the found entry flag.
                            foundEntry = true;
                        }

                        // Otherwise, an entry has been found.
                    } else {

                        // The space character marks the end
                        // of an entry.
                        if (readLine.charAt(i) == ' ') {

                            // Set the ending index for the
                            // entry.
                            endIndex = i;

                            // Unset the found entry flag.
                            foundEntry = false;

                            // Check whether the entry is
                            // one we want to keep.
                            if (entryNumber == 0 ||
                                    entryNumber == 2 ||
                                    entryNumber == 3 ||
                                    entryNumber == 4 ||
                                    entryNumber == 6 ||
                                    entryNumber == 7) {

                                if (DEBUG) {
                                    System.out.println(
                                            readLine.substring(
                                                    startIndex,
                                                    endIndex
                                            )
                                    );
                                }

                                // Write the entry out to
                                // file, comma delimited.
                                bufferedWriter.write(
                                        readLine.substring(
                                                startIndex,
                                                endIndex
                                        ) + ","
                                );
                            }

                            // Check whether this is the last
                            // entry we want to keep.
                            else if (entryNumber == 9) {

                                if (DEBUG) {
                                    System.out.println(
                                            readLine.substring(
                                                    startIndex,
                                                    endIndex
                                            )
                                    );
                                }

                                // The last entry does not get
                                // a final comma, since nothing
                                // comes after it.
                                bufferedWriter.write(
                                        readLine.substring(
                                                startIndex,
                                                endIndex
                                        )
                                );
                            }

                            // Don't forget to increment the
                            // entry number.
                            entryNumber++;
                        }

//                        else if (i == readLine.length() - 1) {
//
//                            System.out.println(
//                                    readLine.substring(
//                                            startIndex,
//                                            i
//                                    )
//                            );
//                        }

                    }

                }

                // Add a new line to the output file.
                bufferedWriter.newLine();

            }

            // Don't forget to close the buffered reader and writer.
            bufferedReader.close();
            bufferedWriter.close();

        } catch (IOException exception) {

            exception.printStackTrace();
        }
    }


    /**
     * Parse, filter, and convert the input blkparse file
     * into a CSV file.
     */
    public static void main (
            String args[]
    )
            throws IOException
    {
        // Object for converting blkparse output to
        // comma separated variable format.
        BlkparseToCSV blkparseToCSV = new BlkparseToCSV();

        // Retrieve the names of the input and output
        // files.
        blkparseToCSV.processInput(args);

        // Convert the blkparse output to CSV.
        blkparseToCSV.parseToCSV();
    }
}
