#include <stdio.h>
#include <stdlib.h>
void main(){

  int a;
  char caracter;
  float promedio;

  a = 2;
  caracter = 'a';
  promedio = 7.7f;
  int *api = &a;
  char *apc = &caracter;
  float *apf = &promedio;

  printf("HolaMundo\n");
   printf("Int a = %d\n", a);
  printf("char = %c\n", caracter);
printf("float promdio =%f\n",promedio);
printf(" dir int: %u, dir char %u, dir float:%u\n",api,apc,apf);
}
