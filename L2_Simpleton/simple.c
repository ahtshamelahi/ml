#include <stdio.h>
#include "simple.h"

int main(int argc,char *argv[])  //argc how many,argv is the real files
{
    int memory[100]={0};   //I chose 100 as per the lecture
    int accumulator=0;
    int instructionCounter=0;
    int instructionRegister=0;
    int operationCode=0;
    int operand=0;

    loadProgram(argv[1],memory);  //loading from utils

    //as per the assignment requirements
    printf("Accumulator:%d\n",accumulator);
    printf("Instruction Counter:%d\n",instructionCounter);
    printf("\nInstruction Register:%d\n",instructionRegister);
    printf("Opcode:%d\n",operationCode);
    printf("Operand:%d\n\n",operand);

    dumpMemory(memory);   //the concept of dump was in the book.used to show data on cmd

    while(1)   //infinite loop
    {
        printf("\nExecuting instruction:%d\n",memory[instructionCounter]);
        instructionRegister=memory[instructionCounter];

        operationCode=instructionRegister/100;
        operand=instructionRegister%100;




        instructionCounter++;

        switch(operationCode)
        {
            case READ:
                printf("Enter an integer:");
                scanf("%d",&memory[operand]);  //using & bcz scanf needs to know where to save this
                break;

            case WRITE:
                printf("Result:%d\n",memory[operand]);
                break;

            case LOAD:
                accumulator=memory[operand];
                break;

            case STORE:
                memory[operand]=accumulator;
                break;

            case ADD:
                accumulator=accumulator+memory[operand];
                break;

            case SUBTRACT:
                accumulator=accumulator-memory[operand];
                break;

            case DIVIDE:
                if(memory[operand]==0)
                {    //divide by 0 error
                    printf("Error.Cannot divide by zero\n");
                    return 1;
                }
                accumulator=accumulator/memory[operand];
                break;

            case MULTIPLY:
                accumulator=accumulator*memory[operand];
                break;

            case BRANCH:
                instructionCounter=operand;
                break;

            case BRANCHNEG:
                if(accumulator<0)
                {
                    instructionCounter=operand;
                }
                break;

            case BRANCHZERO:
                if(accumulator==0)
                {
                    instructionCounter=operand;
                }
                break;

            case HALT:
                printf("\nThe Simpletron has completed its task.So Halt\n");
                return 0;

            default:   //if invalid code is there in the file
                printf("Error:Invalid operation code %d\n",operationCode);
                return 1;
        }
        //again bcz one is done before loop for initial values
        printf("----------------------------------------------------------------------------------\n");
        printf("Accumulator:%d\n",accumulator);
        printf("Instruction Counter:%d\n",instructionCounter);
        printf("Instruction Register:%d\n",instructionRegister);
        printf("Opcode:%d\n",operationCode);
        printf("Operand: %d\n\n",operand);

        dumpMemory(memory);
    }
    return 0;
}