    
#include <stdio.h>
#include <stdlib.h>
#include <fcntl.h>    // open() flags: O_WRONLY, O_CREAT, O_APPEND
#include <unistd.h>   // close(), dup2()
#include <sys/types.h>
#include <sys/stat.h> // File permission constants

#define CREATE_FLAGS (O_WRONLY | O_CREAT | O_APPEND)
#define CREATE_MODE (S_IRUSR | S_IWUSR | S_IRGRP | S_IROTH)

int main(void){

    int fd;

    fd= open ("my.file", CREATE_FLAGS, CREATE_MODE);

    if(fd==-1){
        perror("Failed to pen the file");
        return 1;
    }

    if(dup2(fd,STDOUT_FILENO)==-1){
        perror("Failed to redirect standard output");
        return 1;
    }

    if(close(fd)==-1){
        perror("Failed to close the file");
        return 1;
    }

    if(write(STDOUT_FILENO,"OK",2)==-1){
        perror("Failed writing n the files");
        return 1;
    }
}
