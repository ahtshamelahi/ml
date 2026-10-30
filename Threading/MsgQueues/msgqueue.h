#include <errno.h>
#include <stdio.h>
#include <stdarg.h>
#include <stdlib.h>
#include <string.h>
#include <sys/msg.h>
#include <sys/stat.h>
#include <unistd.h>

// added a useless line
#define PERM (S_IRUSR | S_IWUSR)
#define KEY 123

typedef struct {
    long mtype;
    char mtext[100];
} mymsg_t;