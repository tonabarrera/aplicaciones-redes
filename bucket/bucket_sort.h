#ifndef __BUCKET_SORT__
#define __BUCKET_SORT__

#include <pthread.h>
#include <time.h>

#include "comunicacion_sockets.h"
#include "merge_sort.h"

#define TAM 1000

struct Mensaje{
    int tam;
    int *numeros;
};

void *cliente(void *);
void *servidor(void *);
void llenar_numeros();
void crear_clientes(int);
void crear_servidores(int);
int meter(int *, int);
int numeros[TAM];
int arreglo_final[TAM];

#endif