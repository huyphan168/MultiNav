package org.javacs.example;

class ConstructorRefs {
    ConstructorRefs(String stringConstructor) {
    }
    ConstructorRefs(int intConstructor) {
    }
    static void main() {
        new ConstructorRefs("1");
        new ConstructorRefs(1);
    }
}