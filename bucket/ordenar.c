#include "bucket_sort.h"

int main(int arc, char *argv[]) {
    if (arc < 2){
        printf("%s\n", "Ingresa el numero de hilos plis");
        return -1;
    }
    printf("%s\n", "NUMEROS DESORDENADOS:");
    llenar_numeros();
    crear_servidores(atoi(argv[1]));
    crear_clientes(atoi(argv[1]));
    printf("%s\n", "NUMEROS ORDENADOS:");
    
    for (int i = 0; i < TAM; i++)
        printf("%d ", arreglo_final[i]);
    printf("\n");
    return 0;
}