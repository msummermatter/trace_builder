/* Author: Madox Summermatter (madoxs)
 * Date: Fall 2018
 * Purpose: Research Under Professor Berger
 * Description:
 *
 * 	The following class allocates all available
 * 	memory for a specified amount of time before
 * 	freeing it.  This forces writes to go to disk.
 */

#include<stdio.h>
#include<stdlib.h>
#include<unistd.h>
#include<string.h>

int main(int argc, char** argv)
{
	// Check whether the number of arguments is appropriate.
	if (argc != 2) {

		printf("Unexpected number of arguments!\n");
		printf("USAGE: ./balloon.c [run time (in seconds)]");
		return 0;
	}

	// Specifies whether to turn on debug messages.
	const int DEBUG = 0;

	// Specifies the number of seconds to sleep.
	const int NUM_SECONDS_TO_SLEEP = atoi(argv[1]);

	// Specifies the number of bytes in a gigabyte.
	const int GIGABYTE = 1073741824;

	// Specifies the number of gigabytes to allocate.
	const int NUM_GBS = 29;

	// Specifies a large String to write to each block.
	const char* LARGE_STRING = "0000000000000000000000000000000";

	// For saving the pointers to the allocated blocks
	// of memory so that they can be freed later.
	char* ptrs[NUM_GBS];

	// The index at which the loop breaks.
	int indexBrokenAt = 0;

	// Allocate NUM_GBS gigabytes of memory.
	for (int i = 0; i < NUM_GBS; i++) {

		if (DEBUG) {
			printf("\nAllocating a gigabyte of memory\n");
		}

		// Allocate a gigabyte of memory.
		char* ptr = (char*) malloc(GIGABYTE);

		// Check whether we are out of memory.
		if (ptr == 0) {

			// Print an error message.
			printf("\nWARNING: out of memory\n"); 

			// Break out of the loop.
			break;
		}
		
		// Write to block.
		memset(ptr,0,GIGABYTE);

		// Save the pointer in an array so that
		// the memory can be freed later.
		ptrs[i] = ptr;

		// Save the index.
		indexBrokenAt = i;
	}

	// Sleep for a bit.
	sleep(NUM_SECONDS_TO_SLEEP);

	// Free all the memory allocated.
	for (int i = 0; i < indexBrokenAt; i++) {
	
		if (DEBUG) {
			printf("\nFreeing a gigabyte of memory\n");
		}

		// Retrieve the pointer to the block of memory.
		char* ptr = ptrs[i];

		// Free the block of memory.
		free(ptr);
	}

	return 0;
}
