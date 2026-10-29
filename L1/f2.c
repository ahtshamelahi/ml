#include <stdio.h>

void f2B(void);

void f2A(void)
{
    printf("Calling from f2A\n");
    f2B();
}

void f2B(void)
{
    printf("Calling from f2B\n");
}
