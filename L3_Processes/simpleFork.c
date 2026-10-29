#include <stdio.h>

int main(void){

    printf("I am a process %ld and my parent is %ld \n",(long)getpid(),(long)getppid())


}