package io.github.md5sha256.codequest23.model.component;

public class SimpleHealthComponent implements HealthComponent {

    private int health;

    public SimpleHealthComponent(int health) {
        this.health = health;
    }

    @Override
    public int health() {
        return this.health;
    }

    @Override
    public int dealDamage(int damage) {
        this.health = Math.max(0, this.health - damage);
        return this.health;
    }

    @Override
    public void health(int health) {
        this.health = Math.max(0, this.health);
    }
}
