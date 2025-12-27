package io.github.luckydogstudios.platformer;

import com.badlogic.gdx.math.Vector2;

public class Enemy {

    private int health;
    private int damage;

    public Enemy(int health, int damage) {
        this.health = health;
        this.damage = damage;
    }

    public void takeDamage(int damageTaken) {
        health -= damageTaken;
        if (health <= 0) {
            die();
        }
    }

    public int getHealth() {
        return health;
    }

    public int getDamage() {
        return damage;
    }

    public void die() {

    }

}
