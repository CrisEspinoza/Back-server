package com.example.Entities;

import java.util.ArrayList;

public class Nodo {

    private String id;
    private int x;
    private int y;
    private int size;
    private String image;

    public Nodo(String id, int x, int y, float razon, String image) {
        this.id = id;
        this.x = x;
        this.y = y;
        this.size = genRadio(razon);
        this.image = image;
    }

    public int genRadio (float value ) {

        if(value < 0.1)
            return 5;
        else if(value < 0.2)
            return 10;
        else if(value < 0.3)
            return 15;
        else if(value < 0.4)
            return 20;
        else if(value < 0.5)
            return 25;
        else if(value  < 0.6)
            return 30;
        else if(value < 0.7)
            return 35;
        else if(value < 0.8)
            return 40;
        else if(value < 0.9)
            return 45;
        else if(value <= 1)
            return 50;

        return 0;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public int getX() {
        return x;
    }

    public void setX(int x) {
        this.x = x;
    }

    public int getY() {
        return y;
    }

    public void setY(int y) {
        this.y = y;
    }

    public int getSize() {
        return size;
    }

    public void setSize(int size) {
        this.size = size;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public static int buscarNodo(ArrayList<Nodo> nodos,Nodo usuario){
        int i=0;
        for (Nodo buscado: nodos) {
            if(buscado.getId().equals(usuario.getId())){
                return i;
            }
            i++;

        }
        return -1;
    }
}
