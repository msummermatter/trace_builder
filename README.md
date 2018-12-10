# trace_builder

## Description 
The purpose of this project is to aid in building read/write traces for testing caching systems.    

## How To Use
The program is run using test_script.sh, in which the particular parameters for a trace building session can be modified.  The trace building session consists of some number of write and read operations, during which the blktrace linux command records relevant information about which LBAs are accessed, when, and by what process.  This information is then filtered into a trace, which can then be plotted using plot.r.  These instructions are outlined step-by-step below:


1. Modify the test_script.sh file with desired parameters.
    1. The reader class takes in the following arguments:
        1. duration to read in seconds
        2. number of milliseconds to sleep between reads
        3. input file name to read from
    2. The writer class takes in the following arguments:
        1. duration to write in seconds
        2. number of milliseconds to sleep between writes
        3. output file name to write to
    3. The blkparse command will run for however long the read and write sessions will take, plus a small buffer.
    4. Specify the output trace file name.
2. Run the test script to generate an trace.  The test script will:
    1. Run a background process called blkparse in order to record relevant information about the read/write process.
    2. Run balloon.c, which hogs as much memory as it can so that any read and writes are not retrieved from the cache.
    3. Filters in only the read and write information from the blktrace output.
    4. Transforms the filtered output into a CSV file so that it may be plotted and distributed more easily.
3. Plot the data by modifing the plot.r file with the relevant file name.  The CSV file has the following information:
    1. major,minor,sequence,timestamp,pid,rbws,offset,size
    
## Outstanding Questions
The following is a list of outstanding questions we have:
1. How do we read at a specific block address and offset?
    1. One idea we tried: dd if=/dev/XXX of=/tmp/test seek=0 count=16 bs=512
    2. We could possibly modify the dd source code.
2. In general, what is going on with the plots?
    1. It looks like there's some journaling going on with multiple kworker threads.
    
## Next Steps
The following is a list of next steps:
1. Write a script that reads at a particular LBA, then hashes that block and stores it. 

  

       
