#include "comunicacion_sockets.h"

// Crea un servidor socket
int iniciar_servidor(const char *PUERTO) {
    struct addrinfo *lista;
    struct addrinfo hints;

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
    if ((rv = getaddrinfo("localhost", PUERTO, &hints, &lista)) != 0) {
        fprintf(stderr, "getaddrinfo: %s\n", gai_strerror(rv));
        exit(1);
    }

    struct addrinfo *p;
    int serverSocket;
    for (p = lista; p != NULL; p = p->ai_next) {
        // Creamos un socket
        if ((serverSocket = socket(p->ai_family, p->ai_socktype, p->ai_protocol)) == -1) {
            perror("servidor: socket");
            continue;
        }
        if (setsockopt(serverSocket, SOL_SOCKET, SO_REUSEADDR, &v, sizeof(int)) == -1)
            desplegar_error("servidor: setsockopt");
        
        // Asignamos el socket a un puerto
        if (bind(serverSocket, p->ai_addr, p->ai_addrlen) == -1) {
            close(serverSocket);
            perror("servidor: bind");
            continue;
        }
        break;
    }
    freeaddrinfo(lista);
    if (p == NULL)  {
        fprintf(stderr, "servidor: bind\n");
        exit(EXIT_FAILURE);
    }

    // Le decimos al sistema que permita conecciones en un puerto
    if (listen(serverSocket, 5) == -1)
        desplegar_error("servidor: listen");

    return serverSocket;
}

// Crea un cliente socket
int iniciar_cliente(const char *PUERTO) {
    struct addrinfo hints, *servinfo, *p;
    // descriptor, mandar datos, bits enviados
    int clienteSocket, rv;
     // Limpiamos llenando de ceros
    memset(&hints, 0, sizeof hints);
    hints.ai_family = AF_INET;    /* Allow IPv4 or IPv6  familia de dir*/
    hints.ai_socktype = SOCK_STREAM; // Usaremos socket de flujo
    hints.ai_protocol = 0; // Puedo especificar TCP pero mejor que lo haga el sistema
    
    // Pila de protocolos para IP
    if ((rv = getaddrinfo("localhost", PUERTO, &hints, &servinfo)) != 0) {
        fprintf(stderr, "getaddrinfo: %s\n", gai_strerror(rv));
        exit(EXIT_FAILURE);
    }

    // Intentamos crear el socket con algun protocolo devuelto en la parte anterior
    // Como IPv4 e IPv6
    for(p = servinfo; p != NULL; p = p->ai_next) {
        if ((clienteSocket = socket(p->ai_family, p->ai_socktype,p->ai_protocol)) == -1) {
            perror("cliente: socket");
            continue;
        }

        if (connect(clienteSocket, p->ai_addr, p->ai_addrlen) == -1) {
            close(clienteSocket);
            perror("cliente: connect");
            continue;
        }

        break;
    }//for

    // Si no me pude conectar
    if (p == NULL) {
        fprintf(stderr, "cliente: error al conectar con el servidor\n");
        exit(EXIT_FAILURE);
    }

    freeaddrinfo(servinfo); // all done with this structure
    return clienteSocket;
}

// Para desplegar errores
void manejador_errores(int error, char *msj) {
    errno = error;
    perror(msj);
    exit(EXIT_FAILURE); 
}

// Para desplegar errores
void desplegar_error(char *msj) {
    perror(msj);
    exit(EXIT_FAILURE);
}