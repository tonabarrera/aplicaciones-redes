#include "bucket_sort.h"

// Creamos N servidores que ordenaran una cubeta
void crear_servidores(int N) {
    pthread_t hilos[N];
    int error;
    for (int i = 0; i < N; i++) {
        int *j = malloc(sizeof(int));
        *j = i;
        error = pthread_create(&hilos[i], NULL, &servidor, (void *)j);
        if(error)
            manejador_errores(error, "CREAR SERVIDORES");   
    }
    sleep(1);
}

// Se crean los clientes y se seleccionan los numeros
// que perteneceran a su cubeta 
void crear_clientes(int N) {
    int rango = TAM/N;
    pthread_t hilos[N];
    int *indices;
    int inicio = 0;
    int error;
    // Creacion de hilos y seleccion de numeros
    for (int i = 0; i < N; ++i) {
        indices = (int *) malloc(sizeof(int)*3);
        indices[0] = i;
        indices[1] = inicio;
        inicio += rango; 
        indices[2] = inicio;
        if (i == N-1 && inicio < TAM)
            indices[2] = TAM;
        error = pthread_create(&hilos[i], NULL, cliente, (void *)indices);
        if(error)
            manejador_errores(error, "CREAR CLIENTE");
    }
    // Se espera a que termine cada hilo en orden para introducir
    // Los elementos en el arrreglo final
    int iniciar = 0;
    for (int i = 0; i < N; i++){
        int *ordenados;
        error = pthread_join(hilos[i], (void **) &ordenados);
        if (error)
            manejador_errores(error, "JOIN CLIENTE");
        iniciar = meter(ordenados, iniciar);
    }
}

// Funcion que mete los cubetas ordenadas en el arreglo final
int meter(int *ordenados, int inicio) {
    int i = 0;
    while (ordenados[i] != -1){
        arreglo_final[inicio++] = ordenados[i++];
    }
    return inicio;
}

// Generacion de numeros aleatorios
void llenar_numeros() {
    srand(time(NULL));
    for (int i=0; i < TAM; i++) {
        numeros[i] = rand() % TAM;
        printf("%d ", numeros[i]);
    }
    printf("\n");
}

// Servidor que se encarga de recibir una cubeta del cliente
// la ordena por merge-sort y la devuelve al cliente
void *servidor(void *indice) {
    int *dato = (int *)indice;
    int puerto = 8000 + dato[0];
    char pto[10];
    snprintf(pto, 10, "%d", puerto);
    int serverSocket = iniciar_servidor(pto);
    
    printf("Servidor #%d listo, esperando..\n", dato[0]);
    int clienteSocket;
    socklen_t ctam;
    char hbuf[NI_MAXHOST], sbuf[NI_MAXSERV];
    struct sockaddr_storage c_addr; // connector's address 
    for (;;) {
        ctam = sizeof(c_addr);
        clienteSocket = accept(serverSocket, (struct sockaddr *)&c_addr, &ctam);
        if (clienteSocket == -1) {
            perror("servidor: accept");
            continue;
        }

        if (getnameinfo((struct sockaddr *)&c_addr, sizeof(c_addr), hbuf, sizeof(hbuf), sbuf, 
            sizeof(sbuf), NI_NUMERICHOST | NI_NUMERICSERV) == 0)
            printf("Cliente conectado desde %s:%s\n", hbuf, sbuf);

        char b[200];
        memset(&b, 0, sizeof(b));
        read(clienteSocket, b, sizeof(b));
        struct Mensaje *msj_desordenados = (struct Mensaje *) b;
        merge_sort(msj_desordenados->numeros, 0, msj_desordenados->tam-1);
        write(clienteSocket, (const char*)msj_desordenados, sizeof(struct Mensaje));

        close(clienteSocket);
        break;
    }
    close(serverSocket);
    return NULL;
}

// El cliente simplemente se conecta al servidor y envia
// su cubeta, espera y la recibe para proceder a mandarla
// al hilo principal
void *cliente(void *indices) {
    int *datos = (int *)indices;
    int desordenados[TAM];
    int j = 0;
    for (int i = 0; i < TAM; i++ ) {
        if (numeros[i] >= datos[1] && numeros[i] < datos[2]){
            desordenados[j++] = numeros[i];
        }
    }
    int puerto = 8000 + datos[0];
    char pto[10];
    snprintf(pto, 10, "%d", puerto);
    int clienteSocket = iniciar_cliente(pto);

    struct Mensaje *msj_desordenados = (struct Mensaje *) malloc(sizeof(struct Mensaje));
    msj_desordenados->tam = j;
    msj_desordenados->numeros = desordenados;
    write(clienteSocket, (const char*)msj_desordenados, sizeof(struct Mensaje));

    char b[200];
    memset(&b, 0, sizeof(b));
    
    read(clienteSocket, b, sizeof(b));
    struct Mensaje *msj_ordenados = (struct Mensaje *) b;

    int *ordenados = malloc((j+1)*sizeof(int));
    for (int i = 0; i < j; i++)
        ordenados[i] = msj_ordenados->numeros[i];
    ordenados[j] = -1;
    close(clienteSocket);
    return (void *) ordenados;
}