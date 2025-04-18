#include <stdlib.h>
#include <stdio.h>

struct A {
    int x;
    int y;
};

int main()
{
    struct A *val = malloc(sizeof(struct A));
    val->x = 4;
    val->y = 6;
    printf("%d\n", val->x);
}