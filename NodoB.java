package arbolb;

public class NodoB {
    int[] claves;
    NodoB[] hijos;
    int n;
    boolean hoja;

    // Constructor
    NodoB(int grado, boolean hoja) {
        this.n = 0;
        this.hoja = hoja;
        this.claves = new int[2 * grado - 1];
        this.hijos = new NodoB[2 * grado];
    }

    // Función para encontrar la clave en el nodo
    int encontrarClave(int clave) {
        for (int i = 0; i < this.n; i++) {
            if (this.claves[i] == clave) {
                return i;
            }
        }
        return -1;
    }
}