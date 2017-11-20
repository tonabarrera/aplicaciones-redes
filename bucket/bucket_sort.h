#ifndef __BUCKET_SORT__
#define __BUCKET_SORT__
#include <stdio.h>
#include <pthread.h>
#include <time.h>
#include <stdlib.h>
#include <string.h>
#include <unistd.h> // close read write
#include <sys/socket.h> // Aqui esta todo lo que dice socket
#include <netdb.h> //getaddrinfo() getnameinfo() freeaddrinfo()

#define TAM 20
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