package com.codequest23.model.component;

public class SimpleMutableComponent<T> implements MutableComponent<T> {

    private T value;

    public SimpleMutableComponent(T value) {
        this.value = value;
    }

    @Override
    public T value() {
        return this.value;
    }

    @Override
    public void value(T value) {
        this.value = value;
    }
}
