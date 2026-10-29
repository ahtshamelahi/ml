#include <stdio.h>
#include "simple.h"

//for loading data
void loadProgram(const char *filename,int memory[])
{
    FILE *file;
    int instruction;
    int location=0;

    file=fopen(filename,"r");

    while(fscanf(file,"%d",&instruction)==1)   //using 1 bcz 1 means successfull,0 means unsuccessfull
    {
        memory[location]=instruction;
        location++;
    }

    fclose(file);
}


void dumpMemory(int memory[])
{
    int row;
    int column;

    //spaces are added for styling,nothing else
    printf("\n        0    1    2    3    4    5    6    7    8    9\n\n");

    for(row=0;row<10;row++)
    {
        printf("%d | ",row);

        for(column=0;column<10;column++)
        {
            printf("%5d",memory[row*10+column]);
        }

        printf("\n");
    }
}