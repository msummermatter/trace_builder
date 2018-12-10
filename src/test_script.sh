#!/bin/bash
writeForSecs=300
readForSecs=20
numSeqs=1
runForSecs=45 #($writeForSecs+$readForSecs)*$numSeqs+10
./balloon $runForSecs &
p1=$!
echo $p1
(blktrace -w $runForSecs -d /dev/nvme0n1p1 -o - | blkparse -i - > blkparse.txt) &
p2=$!
echo $p2
#java Writer $writeForSecs 0 writer_output.txt
#java Reader $readForSecs 0 writer_output.txt
#java Writer $writeForSecs 0 writer_output.txt
#java Reader $readForSecs 0 writer_output.txt

#dd if=/dev/nvme0n1p1 of=/tmp/ddtest.txt seek=0 count=16 bs=512
#dd if=/dev/nvme0n101 of=/tmp/ddtest.txt skip=0 count=16 bs=512
wait $p2
grep -e " Q " -e "java" blkparse.txt > clean.txt
java BlkparseToCSV clean.txt r20x2.txt
killall balloon
