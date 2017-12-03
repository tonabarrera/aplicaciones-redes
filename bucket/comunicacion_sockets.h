#ifndef __COMUNICACION_SOCKETS__
#define __COMUNICACION_SOCKETS__

#include <unistd.h> // close read write
#include <sys/socket.h> // Aqui esta todo lo que dice socket
#include <netdb.h> //getaddrinfo() getnameinfo() freeaddrinfo()
#include <errno.h>
#include <stdlib.h>
#include <stdio.h>
#include <string.h>

// Definicion de las funciones que se utilizan
int iniciar_servidor(const char *);
int iniciar_cliente(const char *);
void manejador_errores(int, char *);
void desplegar_error(char *msj);
#endif