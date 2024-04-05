package arbolb;

import java.util.Scanner;

    class ArbolB {
    private NodoB raiz;
    private int grado;

    // Constructor
    ArbolB(int grado) {
        this.raiz = null;
        this.grado = grado;
    }

    // Función para buscar una clave en el árbol
    boolean buscar(int clave) {
        return buscar(this.raiz, clave);
    }

    private boolean buscar(NodoB nodo, int clave) {
        if (nodo == null) {
            return false;
        }
        int i = 0;
        while (i < nodo.n && clave > nodo.claves[i]) {
            i++;
        }
        if (i < nodo.n && clave == nodo.claves[i]) {
            return true;
        }
        if (nodo.hoja) {
            return false;
        }
        return buscar(nodo.hijos[i], clave);
    }

    // Función para insertar una clave en el árbol
    void insertar(int clave) {
        if (this.raiz == null) {
            this.raiz = new NodoB(this.grado, true);
            this.raiz.claves[0] = clave;
            this.raiz.n = 1;
        } else {
            if (this.raiz.n == 2 * this.grado - 1) {
                NodoB s = new NodoB(this.grado, false);
                s.hijos[0] = this.raiz;
                dividirHijo(s, 0, this.raiz);
                int i = 0;
                if (s.claves[0] < clave) {
                    i++;
                }
                insertarNoLleno(s.hijos[i], clave);
                this.raiz = s;
            } else {
                insertarNoLleno(this.raiz, clave);
            }
        }
    }

    private void insertarNoLleno(NodoB nodo, int clave) {
        int i = nodo.n - 1;
        if (nodo.hoja) {
            while (i >= 0 && nodo.claves[i] > clave) {
                nodo.claves[i + 1] = nodo.claves[i];
                i--;
            }
            nodo.claves[i + 1] = clave;
            nodo.n++;
        } else {
            while (i >= 0 && nodo.claves[i] > clave) {
                i--;
            }
            i++;
            if (nodo.hijos[i].n == 2 * this.grado - 1) {
                dividirHijo(nodo, i, nodo.hijos[i]);
                if (nodo.claves[i] < clave) {
                    i++;
                }
            }
            insertarNoLleno(nodo.hijos[i], clave);
        }
    }

    private void dividirHijo(NodoB padre, int indice, NodoB hijo) {
        NodoB nuevo = new NodoB(this.grado, hijo.hoja);
        nuevo.n = this.grado - 1;
        for (int j = 0; j < this.grado - 1; j++) {
            nuevo.claves[j] = hijo.claves[j + this.grado];
        }
        if (!hijo.hoja) {
            for (int j = 0; j < this.grado; j++) {
                nuevo.hijos[j] = hijo.hijos[j + this.grado];
            }
        }
        hijo.n = this.grado - 1;
        for (int j = padre.n; j >= indice + 1; j--) {
            padre.hijos[j + 1] = padre.hijos[j];
        }
        padre.hijos[indice + 1] = nuevo;
        for (int j = padre.n - 1; j >= indice; j--) {
            padre.claves[j + 1] = padre.claves[j];
        }
        padre.claves[indice] = hijo.claves[this.grado - 1];
        padre.n++;
    }

    // Funcion para eliminar una clave del árbol
    void eliminar(int clave) {
        if (this.raiz == null) {
            System.out.println("El árbol está vacío");
            return;
        }
        eliminar(this.raiz, clave);
        if (this.raiz.n == 0) {
            if (this.raiz.hoja) {
                this.raiz = null;
            } else {
                this.raiz = this.raiz.hijos[0];
            }
        }
    }

    private void eliminar(NodoB nodo, int clave) {
        int indice = nodo.encontrarClave(clave);
        if (indice != -1) {
            if (nodo.hoja) {
                eliminarDeHoja(nodo, indice);
            } else {
                eliminarDeNoHoja(nodo, indice);
            }
        } else {
            int i = 0;
            while (i < nodo.n && clave > nodo.claves[i]) {
                i++;
            }
            if (i < nodo.n) {
                boolean esUltimo = (i == nodo.n - 1);
                if (nodo.hijos[i].n < this.grado) {
                    llenar(nodo, i);
                }
                if (esUltimo && i > nodo.n) {
                    eliminar(nodo.hijos[i - 1], clave);
                } else {
                    eliminar(nodo.hijos[i], clave);
                }
            } else {
                eliminar(nodo.hijos[i], clave);
            }
        }
    }

    private void eliminarDeHoja(NodoB nodo, int indice) {
        for (int i = indice + 1; i < nodo.n; i++) {
            nodo.claves[i - 1] = nodo.claves[i];
        }
        nodo.n--;
    }

    private void eliminarDeNoHoja(NodoB nodo, int indice) {
        int clave = nodo.claves[indice];
        if (nodo.hijos[indice].n >= this.grado) {
            int predecesor = obtenerPredecesor(nodo, indice);
            nodo.claves[indice] = predecesor;
            eliminar(nodo.hijos[indice], predecesor);
        } else if (nodo.hijos[indice + 1].n >= this.grado) {
            int sucesor = obtenerSucesor(nodo, indice);
            nodo.claves[indice] = sucesor;
            eliminar(nodo.hijos[indice + 1], sucesor);
        } else {
            fusionar(nodo, indice);
            eliminar(nodo.hijos[indice], clave);
        }
    }

    private int obtenerPredecesor(NodoB nodo, int indice) {
        NodoB actual = nodo.hijos[indice];
        while (!actual.hoja) {
            actual = actual.hijos[actual.n];
        }
        return actual.claves[actual.n - 1];
    }

    private int obtenerSucesor(NodoB nodo, int indice) {
        NodoB actual = nodo.hijos[indice + 1];
        while (!actual.hoja) {
            actual = actual.hijos[0];
        }
        return actual.claves[0];
    }

    private void fusionar(NodoB nodo, int indice) {
        NodoB hijo = nodo.hijos[indice];
        NodoB hermano = nodo.hijos[indice + 1];
        hijo.claves[this.grado - 1] = nodo.claves[indice];
        for (int i = 0; i < hermano.n; i++) {
            hijo.claves[i + this.grado] = hermano.claves[i];
        }
        if (!hijo.hoja) {
            for (int i = 0; i <= hermano.n; i++) {
                hijo.hijos[i + this.grado] = hermano.hijos[i];
            }
        }
        for (int i = indice + 1; i < nodo.n; i++) {
            nodo.claves[i - 1] = nodo.claves[i];
        }
        for (int i = indice + 2; i <= nodo.n; i++) {
            nodo.hijos[i - 1] = nodo.hijos[i];
        }
        hijo.n += hermano.n + 1;
        nodo.n--;
    }

    // Función para imprimir el árbol
    void imprimir() {
        if (this.raiz != null) {
            imprimir(this.raiz, "");
        } else {
            System.out.println("El arbol está vacio");
        }
    }

    private void imprimir(NodoB nodo, String prefijo) {
        for (int i = 0; i < nodo.n; i++) {
            System.out.print(prefijo + nodo.claves[i] + " ");
        }
        System.out.println();
        if (!nodo.hoja) {
            for (int i = 0; i <= nodo.n; i++) {
                imprimir(nodo.hijos[i], prefijo + "   ");
            }
        }
    }

    private void llenar(NodoB nodo, int indice) {
        if (indice != 0 && nodo.hijos[indice - 1].n >= this.grado) {
            prestarDelAnterior(nodo, indice);
        } else if (indice != nodo.n && nodo.hijos[indice + 1].n >= this.grado) {
            prestarDelSiguiente(nodo, indice);
        } else {
            if (indice != nodo.n) {
                fusionar(nodo, indice);
            } else {
                fusionar(nodo, indice - 1);
            }
        }
    }
    private void prestarDelAnterior(NodoB nodo, int indice) {
    NodoB hijo = nodo.hijos[indice];
    NodoB hermano = nodo.hijos[indice - 1];

    // Mover todas las claves y punteros del hijo al principio
    for (int i = hijo.n - 1; i >= 0; i--) {
        hijo.claves[i + 1] = hijo.claves[i];
    }

    if (!hijo.hoja) {
        for (int i = hijo.n; i >= 0; i--) {
            hijo.hijos[i + 1] = hijo.hijos[i];
        }
    }

    // Establecer la primera clave del nodo como la ultima clave del nodo hermano
    hijo.claves[0] = nodo.claves[indice - 1];

    // Si el nodo hijo no es una hoja, mover el ultimo hijo del nodo hermano al primer hijo del nodo hijo
    if (!hijo.hoja) {
        hijo.hijos[0] = hermano.hijos[hermano.n];
    }

    // Mover la ultima clave del nodo hermano al nodo padre
    nodo.claves[indice - 1] = hermano.claves[hermano.n - 1];

    // Actualizar las cantidades de claves en los nodos
    hijo.n++;
    hermano.n--;
}

private void prestarDelSiguiente(NodoB nodo, int indice) {
    NodoB hijo = nodo.hijos[indice];
    NodoB hermano = nodo.hijos[indice + 1];

    // La primera clave del hermano se mueve al final del hijo
    hijo.claves[hijo.n] = nodo.claves[indice];

    // Si el hijo no es una hoja, el primer hijo del hermano se mueve al final del hijo
    if (!hijo.hoja) {
        hijo.hijos[hijo.n + 1] = hermano.hijos[0];
    }

    // La primera clave del hermano se mueve al nodo padre
    nodo.claves[indice] = hermano.claves[0];

    // Mover las claves y punteros del hermano
    for (int i = 1; i < hermano.n; i++) {
        hermano.claves[i - 1] = hermano.claves[i];
    }

    if (!hermano.hoja) {
        for (int i = 1; i <= hermano.n; i++) {
            hermano.hijos[i - 1] = hermano.hijos[i];
        }
    }

    // Actualizar las cantidades de claves en los nodos
    hijo.n++;
    hermano.n--;
    }

public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        
        System.out.print("Ingrese el grado del arbol B porfavor: ");
        int grado = scanner.nextInt();
        ArbolB arbol = new ArbolB(grado);
        
        while (true) {
            System.out.println("|-------------Menu:-------------|");
            System.out.println("1. Insertar la clave");
            System.out.println("2. Buscar la clave");
            System.out.println("3. Eliminar la clave");
            System.out.println("4. Imprimir el arbol");
            System.out.println("5. Salir del programa");
            System.out.print("Seleccione una opcion (ingrese las claves una por una): ");
            int opcion = scanner.nextInt();
            
            switch (opcion) {
                case 1:
                    System.out.print("Ingrese la clave que desea insertar: ");
                    int claveInsertar = scanner.nextInt();
                    arbol.insertar(claveInsertar);
                    System.out.println("Se ingreso la clave correctamente.");
                    break;
                case 2:
                    System.out.print("Ingrese la clave a buscar: ");
                    int claveBuscar = scanner.nextInt();
                    boolean encontrado = arbol.buscar(claveBuscar);
                    if (encontrado) {
                        System.out.println("La clave " + claveBuscar + " si se encuentra en el arbol.");
                    } else {
                        System.out.println("La clave " + claveBuscar + " no se encuentra en el árbol.");
                    }
                    break;
                case 3:
                    System.out.print("Ingrese la clave a eliminar: ");
                    int claveEliminar = scanner.nextInt();
                    arbol.eliminar(claveEliminar);
                    System.out.println("Clave eliminada correctamente.");
                    break;
                case 4:
                    System.out.println("Arbol B:");
                    arbol.imprimir();
                    break;
                case 5:
                    System.out.println("Programa finalizado correctamente");
                    scanner.close();
                    System.exit(0);
                    break;
                default:
                    System.out.println("Opción invalida. Por favor, seleccione una opcion que sea valida.");
                    break;
            }
        }
    }
}