#include "bucket_sort.h"
// Funcion que manda a ordenar los numeros
int main(int arc, char *argv[]) {
    // Se debe de ingresar el numero de hilos
    if (arc < 2){
        printf("%s\n", "Ingresa el numero de hilos plis");
        return -1;
    }
    printf("%s\n", "NUMEROS DESORDENADOS:");
    // Generacion de numeros
    llenar_numeros();
    // Creamos N servidores que recibiran las cubetas
    crear_servidores(atoi(argv[1]));
    // Creamos N clientes
    crear_clientes(atoi(argv[1]));
    printf("%s\n", "NUMEROS ORDENADOS:");
    // Imprimimos los numeros ordenados
    for (int i = 0; i < TAM; i++)
        printf("%d ", arreglo_final[i]);
    printf("\n");
    return 0;
}