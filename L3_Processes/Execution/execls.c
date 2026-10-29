#include <stdio.h>
#include <stdlib.h>
#include <unistd.h>
#include <sys/wait.h>

int main(void){
    pid_t childpid;

    childpid=fork();

    if(childpid==-1){
        perror("Failed to fork");
        return 1;
    }

    else if(childpid==0){
        execl("/usr/bin/ls","ls",NULL);
        perror("Child failed to execute ls");
        return 1;
    }

    if(childpid!=wait(NULL)){
        perror("Parent failed to wait due to signal or error");
        return 1;
    }

    printf("\nI am so sad that my child died\n");
    return 0;

}

//Can be used to run without making a akefile
// yasim@WIN-F9V7B5QU3GS:~/OS/L3_Processes/Execution$ gcc execls.c -o run
// yasim@WIN-F9V7B5QU3GS:~/OS/L3_Processes/Execution$ ./run


// execl.exe: execl.o
//     gcc -o execl.exe execl.o

// execl.o: execl.c
//     gcc -c execl.o execl.c