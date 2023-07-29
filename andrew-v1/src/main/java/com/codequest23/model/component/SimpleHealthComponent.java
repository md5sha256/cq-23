package com.codequest23.model.component;

public class SimpleHealthComponent implements HealthComponent {

    private int hp;

    public SimpleHealthComponent(int hp) {
        this.hp = hp;
    }

    @Override
    public int health() {
        return this.hp;
    }

    @Override
    public int dealDamage(int damage) {
        return this.hp = Math.max(0, this.hp - damage);
    }

    @Override
    public void health(int health) {
        this.hp = Math.max(0, health);
    }
}
