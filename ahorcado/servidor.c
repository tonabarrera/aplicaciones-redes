#include <stdio.h>
#include <stdlib.h>
#include <sys/socket.h> // Aqui esta todo lo que dice socket
#include <netdb.h> //getaddrinfo() getnameinfo() freeaddrinfo()

void error(const char *);

int main(int argc, char const *argv[]) {
    
    return 0;
}

void error(const char *mensaje) {
    perror(mensaje);
    exit(1);
}
