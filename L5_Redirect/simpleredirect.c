#include <errno.h>
#include <stdio.h>
#include <stdlib.h>
#include <fcntl.h>    // open() flags: O_WRONLY, O_CREAT, O_APPEND
#include <unistd.h>   // close(), dup2()
#include <sys/types.h>
#include <sys/stat.h> // File permission constants

int main(void){
    pid_t childpid;
    int fd[2];

    if ((pipe(fd)==-1) || ((childpid=fork())==-1)){
        perror("Failed t setup pipe");
        return 1;
    }

    if(childpid==0){

        if(dup2(fd[1],STDOUT_FILENO)==-1)
            perror("Failed to redirect stdout of ls");
        
        else if((close(fd[0])==-1) || (close(fd[1])==-1))
            perror("Failed to close extra pipe descriptors on ls");

        else{
            execl("/bin/ls","ls","-l",NULL);
            perror("Failed to exec ls");
        }
        return 1;
    }


    if(dup2(fd[0],STDIN_FILENO)==-1)
        perror("Failed to redirect stdin of sort");
        
    else if((close(fd[0])==-1) || (close(fd[1])==-1))
        perror("Failed to close extra pipe descriptors on sort");

    else{
        execl("/bin/wc","wc",NULL);
        perror("Failed to exec wc");
        }

    return 1;

}