#include <errno.h>
#include <stdio.h>
#include <unistd.h>
#include <sys/shm.h>
#include <sys/ipc.h>
#include <sys/stat.h>
#include <sys/time.h>

#define PERM (S_IRUSR | S_IWUSR | IPC_CREAT)

int main (int argc,char *argv[])
{
    int id;
    char *cptr;
    key_t  mykey;

    if((id=shmget(123,100,PERM))==-1){
        perror("Failed to create shared memory segment");
        return 1;
    }

    if((cptr=(char *)shmat(id,NULL,0))==(void *)-1){
        perror("Failed to attach shared memory segment");
        return 1;
    }

    for(int i=0;i<5;i++){
        printf("I read this from shared memory:%s\n",cptr);
        sleep(5);
    }
    return 0;
}