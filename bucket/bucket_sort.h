#ifndef __BUCKET_SORT__
#define __BUCKET_SORT__

#include <pthread.h>
#include <time.h>

#include "comunicacion_sockets.h"
#include "merge_sort.h"

// Tamaño del arreglo de numeros aleatorios a ordenar
#define TAM 100
// Mensaje que se envia entre clientes y servidores
// Contiene el arreglo de numeros y su tamaño
struct Mensaje{
    int tam;
    int *numeros;
};
// Definicion de las funciones que se utilizan
void *cliente(void *);
void *servidor(void *);
void llenar_numeros();
void crear_clientes(int);
void crear_servidores(int);
int meter(int *, int);
int numeros[TAM];
int arreglo_final[TAM];

#endif