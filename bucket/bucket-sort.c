#include <stdio.h>
#include <stdlib.h>
#include <time.h>

struct Nodo {
    struct Nodo *siguiente;
    float valor;
};

void insertar(struct Nodo *inicio, float valor) {
    struct Nodo *aux = inicio;
    while (aux->siguiente != NULL)
        aux = aux->siguiente;

    aux->siguiente = malloc(sizeof(struct Nodo));
    aux->valor = valor;
    aux->siguiente->siguiente = NULL;
}

void recorrer(struct Nodo *lista) {
    struct Nodo *aux = lista;
    while (aux->siguiente != NULL)
        aux = aux->siguiente;
}

void bucket_sort(float *arreglo, int n) {
    struct Nodo *lista = malloc(sizeof(struct Nodo)*n);

    for (int i = 0; i < n; i++){
        int p = arreglo[i]*n;
        insertar(&lista[p], arreglo[i]);
    }

    for (int i = 0; i < n; i++)
        recorrer(&lista[i]);
}

int main() {
    int n = 5;
    float *arreglo = malloc(sizeof(float)*n);
    srand(time(NULL));  
    for (int i = 0; i < n; i++)
        arreglo[i] = (double)rand()/(double)((unsigned)RAND_MAX + 1);
    
    bucket_sort(arreglo, n);
    return 0;
}