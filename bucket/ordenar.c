#include "bucket_sort.h"

int main(int arc, char *argv[]) {
    if (arc < 2)
        return -1;
    llenar_numeros();
    crear_servidores(atoi(argv[1]));
    crear_clientes(atoi(argv[1]));
    printf("Arreglo ordenado:\n");

    for (int i = 0; i < TAM; i++)
        printf("%d ", arreglo_final[i]);
    printf("\n");
    return 0;
}