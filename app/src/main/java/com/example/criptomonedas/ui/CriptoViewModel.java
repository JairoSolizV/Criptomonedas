package com.example.criptomonedas.ui;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import com.example.criptomonedas.model.Item;

import java.util.ArrayList;
import java.util.List;

public class CriptoViewModel extends ViewModel {

    private final MutableLiveData<List<Item>> listaCriptos = new MutableLiveData<>(new ArrayList<>());
    private Item itemEditando;
    private int posicionEditando = -1;

    public LiveData<List<Item>> getListaCriptos() {
        return listaCriptos;
    }

    public void agregarCripto(Item item) {
        List<Item> actual = listaCriptos.getValue();
        if (actual != null) {
            actual.add(item);
            listaCriptos.setValue(actual);
        }
    }

    public void actualizarCripto(int index, Item nuevoItem) {
        List<Item> actual = listaCriptos.getValue();
        if (actual != null && index >= 0 && index < actual.size()) {
            actual.set(index, nuevoItem);
            listaCriptos.setValue(actual);
        }
    }

    public void setListaCriptos(List<Item> lista) {
        listaCriptos.setValue(lista);
    }

    public void setItemEditando(Item item, int posicion) {
        this.itemEditando = item;
        this.posicionEditando = posicion;
    }

    public Item getItemEditando() {
        return itemEditando;
    }

    public int getPosicionEditando() {
        return posicionEditando;
    }

    public void limpiarEdicion() {
        itemEditando = null;
        posicionEditando = -1;
    }
}
