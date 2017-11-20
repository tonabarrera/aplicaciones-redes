#include "bucket_sort.h"
#include "merge_sort.h"

void crear_servidores(int N) {
    pthread_t hilos[N];

    for (int i = 0; i < N; i++) {
        int *j = malloc(sizeof(int));
        *j = i;
        if(pthread_create(&hilos[i], NULL, &servidor, (void *)j)) {
            printf("%s %d\n", "ERROR EN CREATE SERVIDOR: ", i);
            return;
        }
    }
    sleep(1);
}

void crear_clientes(int N) {
    int rango = TAM/N;
    pthread_t hilos[N];
    int *indices;
    int inicio = 0;
    for (int i = 0; i < N; ++i) {
        indices = (int *) malloc(sizeof(int)*3);
        indices[0] = i;
        indices[1] = inicio;
        inicio += rango; 
        indices[2] = inicio;
        if (i == N-1 && inicio < TAM)
            indices[2] = TAM;
        if(pthread_create(&hilos[i], NULL, cliente, (void *)indices)) {
            printf("%s %d\n", "ERROR EN CREATE CLIENTE: ", i);
            return;
        }
    }
    int iniciar = 0;
    for (int i = 0; i < N; i++){
        int *ordenados;
        pthread_join(hilos[i], (void **)&ordenados);
        iniciar = meter(ordenados, iniciar);
    }
}

int meter(int *ordenados, int inicio) {
    int i = 0;
    while (ordenados[i] != -1){
        arreglo_final[inicio++] = ordenados[i++];
    }
    return inicio;
}

void llenar_numeros() {
    srand(time(NULL));
    printf("%s\n", "Arreglos desordenados:");
    for (int i=0; i < TAM; i++) {
        numeros[i] = rand() % TAM;
        printf("%d ", numeros[i]);
    }
    printf("\n");
}

void *servidor(void *indice) {
    int *dato = (int *)indice;
    struct addrinfo *lista;
    struct addrinfo hints;
    struct sockaddr_storage c_addr; // connector's address 

    memset(&hints, 0, sizeof(hints));
    hints.ai_family = AF_INET;    /* IPv4 o IPv6 */
    hints.ai_socktype = SOCK_STREAM;
    hints.ai_flags = AI_PASSIVE; // use my IP
    hints.ai_protocol = 0; /* Any protocol */
    hints.ai_canonname = NULL;
    hints.ai_addr = NULL;
    hints.ai_next = NULL;
    int puerto = 8000 + dato[0];
    char puerto2[10];
    snprintf(puerto2, 10, "%d", puerto);
    int rv;
    int v = 1;
    if ((rv = getaddrinfo("localhost", puerto2, &hints, &lista)) != 0) {
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
    printf("Servidor #%d listo, esperando..\n", dato[0]);
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
            printf("Cliente conectado desde %s:%s\n", hbuf, sbuf);

        int opcion;
        int n = read(clienteSocket, &opcion, sizeof(opcion));
        printf("Bytes leidos en el server #%d %d opcion = %d\n", dato[0], n, opcion);

        if (opcion == 5) {
            int enviados = write(clienteSocket, &opcion, sizeof(opcion));
            printf("Bytes enviados en el server #%d: %d\n", dato[0],enviados);

            close(clienteSocket);
            printf("%s\n", "Se cerro la conexion");
            break;
        }
    }
    close(serverSocket);
    printf("Se cerro el servidor\n");
    return NULL;
}

void *cliente(void *indices) {
    int *datos = (int *)indices;
    //printf("%s%d TOMO LOS NUMEROS %d<n<%d\n", "SOY EL CLIENTE #", datos[0], datos[1], datos[2]);
    int desordenados[TAM];
    int j = 0;
    for (int i = 0; i < TAM; i++ ) {
        if (numeros[i] >= datos[1] && numeros[i] < datos[2]){
            desordenados[j++] = numeros[i];
        }
    }
    //printf("CLIENTE #%d total: %d \n", datos[0], j);
    merge_sort(desordenados, 0, j-1);
    int *ordenados = malloc((j+1)*sizeof(int));
    for (int i = 0; i < j; i++)
        ordenados[i] = desordenados[i];
    ordenados[j] = -1;
    struct addrinfo hints, *servinfo, *p;
    // descriptor, mandar datos, bits enviados
    int cd, rv;
     // Limpiamos llenando de ceros
    memset(&hints, 0, sizeof hints);
    hints.ai_family = AF_INET;    /* Allow IPv4 or IPv6  familia de dir*/
    hints.ai_socktype = SOCK_STREAM; // Usaremos socket de flujo
    hints.ai_protocol = 0; // Puedo especificar TCP pero mejor que lo haga el sistema
    int puerto = 8000 + datos[0];
    char pto[10];
    snprintf(pto, 10, "%d", puerto);
    // Pila de protocolos para IP
    if ((rv = getaddrinfo("localhost", pto, &hints, &servinfo)) != 0) {
        fprintf(stderr, "getaddrinfo: %s\n", gai_strerror(rv));
        return NULL;
    }

    // Intentamos crear el socket con algun protocolo devuelto en la parte anterior
    // Como IPv4 e IPv6
    for(p = servinfo; p != NULL; p = p->ai_next) {
        if ((cd = socket(p->ai_family, p->ai_socktype,p->ai_protocol)) == -1) {
            perror("client: socket");
            continue;
        }

        if (connect(cd, p->ai_addr, p->ai_addrlen) == -1) {
            close(cd);
            perror("client: connect");
            continue;
        }

        break;
    }//for

    // Si no me pude conectar
    if (p == NULL) {
        fprintf(stderr, "client: error al conectar con el servidor\n");
        return NULL;
    }

    freeaddrinfo(servinfo); // all done with this structure
    int envio = 5;
    int enviados = write(cd, &envio, sizeof(envio));
    printf("Bytes enviados en el cliente #%d: %d\n", datos[0], enviados);

    int opcion;
    int leidos = read(cd, &opcion, sizeof(opcion));
    printf("Bytes leidos en el cliente #%d: %d opcion = %d\n", datos[0], leidos, opcion);

    close(cd);
    return (void *) ordenados;
}