#include "merge_sort.h"

void merge_sort(int *arreglo, int inicio, int fin) {
    if (inicio < fin) {
        int mitad = (inicio + fin) / 2;
        merge_sort(arreglo, inicio, mitad);
        merge_sort(arreglo, mitad+1, fin);
        merge(arreglo, inicio, mitad, fin);
    }
}

void merge(int *arreglo, int inicio, int mitad, int fin) {
    int n = mitad - inicio;
    int n2 = fin - mitad;
    int izq[n];
    int der[n2];
    for (int i=0; i<=n; i++){
        izq[i] = arreglo[inicio+i];
    }
    for (int i=0; i<n2; i++){
        der[i] = arreglo[mitad+i+1];
    }
    int i = n;
    int j = n2-1;
    int k = fin;
    while (i > -1 || j >-1) {
        if (i > -1 && j >-1) {
            if (izq[i] < der[j]) {
                arreglo[k--] = der[j--];
            } else {
                arreglo[k--] = izq[i--];
            }
        } else if (i > -1) {
            arreglo[k--] = izq[i--];
        } else {
            arreglo[k--] = der[j--];
        }
    }
}