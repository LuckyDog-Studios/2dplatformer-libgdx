package io.github.luckydogstudios.platformer;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.*;
import space.earlygrey.shapedrawer.ShapeDrawer;

public class Platform {
    private Body body;
    private float width, height;

    public Platform(World world, float x, float y, float width, float height) {
        this.width = width;
        this.height = height;

        // Define body as static
        BodyDef bodyDef = new BodyDef();
        bodyDef.type = BodyDef.BodyType.StaticBody;
        bodyDef.position.set(x, y);
        body = world.createBody(bodyDef);

        PolygonShape shape = new PolygonShape();
        shape.setAsBox(width/2f, height/2f);

        // Fixture definition
        FixtureDef fixtureDef = new FixtureDef();
        fixtureDef.shape = shape;
        fixtureDef.friction = 0.8f;

        // Create fixture and dispose of shape
        body.createFixture(fixtureDef);
        shape.dispose();
    }

    public void render(SpriteBatch batch, ShapeDrawer shapeDrawer) {
        Vector2 pos = body.getPosition();
        // Draw the platform centered at its body position
        shapeDrawer.filledRectangle(
            pos.x - width / 2f,
            pos.y - height / 2f,
            width,
            height,
            Color.BROWN
        );
    }
}
