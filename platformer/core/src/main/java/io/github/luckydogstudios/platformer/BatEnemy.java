package io.github.luckydogstudios.platformer;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Disposable;

import static com.badlogic.gdx.math.MathUtils.clamp;
import static com.badlogic.gdx.math.MathUtils.lerp;

public class BatEnemy extends Enemy implements Disposable {

    private Sprite sprite;
    private Player player;
    private float speed;
    private Rectangle hitbox;

    public BatEnemy(int health, int damage, float speed, float x, float y, Player player) {
        super(health, damage);
        this.player = player;
        Texture tex = new Texture("sprites/batenemy.png");
        sprite = new Sprite(tex);
        sprite.setSize(tex.getWidth() / 100f, tex.getHeight()/100f);
        sprite.setPosition(x, y);
        sprite.setOrigin(sprite.getWidth() / 2, sprite.getHeight() / 2);
        this.speed = speed;
        hitbox = new Rectangle(x, y, sprite.getWidth(), sprite.getHeight());

    }

    public void update(float delta) {
        // Get direction vector from enemy to player
        float dx = (player.x - player.width/2) - sprite.getX();
        float dy = (player.y - player.height/2) - sprite.getY();

        Vector2 direction = new Vector2(dx, dy);

        // Normalize to get direction only
        if (!direction.isZero()) {
            direction.nor(); // unit vector (length = 1)

            // Move enemy toward player
            float moveX = direction.x * speed * delta;
            float moveY = direction.y * speed * delta;

            sprite.setPosition(sprite.getX() + moveX, sprite.getY() + moveY);
        }
    }

    public void render(SpriteBatch batch) {
        sprite.draw(batch);
    }

    @Override
    public void dispose() {
        sprite.getTexture().dispose();
    }
}
