# OS-Scheduler-Simulator
usage: java -cp <path> SchedulerSimulator  [-c] [-f <arg>] [-g <arg>] [-h] [-i
       <arg>] [-l] [-m <arg>] [-o <arg>] [-q <arg>] [-t]
CSC540 Project Simulated Schedulers Help
    -c,--nogui                Run the program in command line mode
    -f,--iformat <arg>        Input file format. [COURSE, CSV]. Default: COURSE
    -g,--oformat <arg>        Output file format. [LATEX, CSV]. Default: CSV
    -h,--help                 Print this help page
    -i,--input <arg>          Input file. Default: input.txt
    -l,--testLatex            Run the program with test data and produce latex
                              output
    -m,--method <arg>         Scheduling algorithm. [FIFO | RR | SJF | SRT].
                              Default: FIFO
    -o,--output <arg>         Output file. Default: - (stdout)
    -q,--quantum <arg>        Round robin quantum. Default: 2
    -t,--testTxt              Run the program with test data and produce txt
                              output
Author: Lingyan Zhou
