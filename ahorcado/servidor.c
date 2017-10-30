#include <stdio.h>
#include <time.h>
#include <stdlib.h>
#include <string.h> // memeset
#include <netinet/in.h>
#include <unistd.h> // close read write
#include <sys/socket.h> // Aqui esta todo lo que dice socket
#include <netdb.h> //getaddrinfo() getnameinfo() freeaddrinfo()

#define puerto "7777"

char *palabras[] = {"computadora", "taco", "bizarro",
                    "raton", "natural", "complejo",
                    "direccion", "palabra", "mesa", "noventa"};

int main(int argc, char const *argv[]) {
    struct addrinfo *lista;
    struct addrinfo hints;
    struct sockaddr_storage c_addr; // connector's address 

    srand(time(NULL));
    memset(&hints, 0, sizeof(hints));
    hints.ai_family = AF_INET;    /* IPv4 o IPv6 */
    hints.ai_socktype = SOCK_STREAM;
    hints.ai_flags = AI_PASSIVE; // use my IP
    hints.ai_protocol = 0; /* Any protocol */
    hints.ai_canonname = NULL;
    hints.ai_addr = NULL;
    hints.ai_next = NULL;

    int rv;
    int v = 1;
    if ((rv = getaddrinfo("localhost", puerto, &hints, &lista)) != 0) {
        fprintf(stderr, "getaddrinfo: %s\n", gai_strerror(rv));
        exit(1);
    }
    struct addrinfo *p;
    int serverSocket;
    for (p = lista; p != NULL; p = p->ai_next) {
        // Creamos un socket
        if ((serverSocket = socket(p->ai_family, p->ai_socktype, p->ai_protocol)) == -1) {
            perror("server: socket");
            continue;
        }
        if (setsockopt(serverSocket, SOL_SOCKET, SO_REUSEADDR, &v, sizeof(int)) == -1) {
            perror("setsockopt");
            exit(1);
        }
        // Asignamos el socket a un puerto
        if (bind(serverSocket, p->ai_addr, p->ai_addrlen) == -1) {
            close(serverSocket);
            perror("server: bind");
            continue;
        }
        break;
    }
    freeaddrinfo(lista);
    if (p == NULL)  {
        fprintf(stderr, "servidor: error en bind\n");
        exit(1);
    }
    // Le decimos al sistema que permita conecciones en un puerto
    listen(serverSocket, 5);
    printf("Servidor listo, esperando..\n");
    int clienteSocket;
    socklen_t ctam;
    char hbuf[NI_MAXHOST], sbuf[NI_MAXSERV];

    for (;;) {
        ctam = sizeof(c_addr);
        clienteSocket = accept(serverSocket, (struct sockaddr *)&c_addr, &ctam);
        if (clienteSocket == -1) {
            perror("accept");
            continue;
        }

        if (getnameinfo((struct sockaddr *)&c_addr, sizeof(c_addr), hbuf, sizeof(hbuf), sbuf, 
            sizeof(sbuf), NI_NUMERICHOST | NI_NUMERICSERV) == 0)
            printf("Cliente conectado desde %s:%s\n", hbuf,sbuf);
        char buffer[2];
        int n = read(clienteSocket, &buffer, sizeof(buffer));
        if (n < 0)
            perror("Error de lectura\n");
        else if (n == 0)
            perror("Socket cerrado\n");
        int opcion = atoi(buffer);
        if (opcion == 1) {
            int random = rand() % (sizeof(palabras)/sizeof(palabras[0]));
            write(clienteSocket, palabras[random], strlen(palabras[random]));
        }
        close(clienteSocket);
    }
    close(serverSocket);
    printf("Se cerro el servidor\n");
    return 0;
}