#include<stdlib.h>
#include <stdio.h>
int main()
{
    int *x = (int *)malloc(sizeof(int));
    *x = 4;
    printf("%d", *x);
}