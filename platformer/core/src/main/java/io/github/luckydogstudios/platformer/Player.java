package io.github.luckydogstudios.platformer;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.*;
import space.earlygrey.shapedrawer.ShapeDrawer;

import static com.badlogic.gdx.math.Interpolation.linear;

public class Player {
    public boolean isInAir;   // True when the player is airborne.
    public boolean isMoving;  // True when the player is actively moving (input-driven).

    // Character properties in world units.
    float x, y;
    float width, height;
    int health;
    private Body body;

    // Constructor: creates a dynamic Box2D body with a main collision fixture and a ground sensor.
    public Player(float x, float y, float width, float height, int health, World world) {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
        this.health = health;

        // Create the dynamic body.
        BodyDef bodyDef = new BodyDef();
        bodyDef.type = BodyDef.BodyType.DynamicBody;
        bodyDef.position.set(x, y);
        body = world.createBody(bodyDef);
        body.setLinearDamping(2.0f);  // Helps with smoothing movement.
        body.setFixedRotation(true);

        // Create the main collision fixture.
        PolygonShape mainShape = new PolygonShape();
        mainShape.setAsBox(width / 2f, height / 2f);
        FixtureDef mainFixtureDef = new FixtureDef();
        mainFixtureDef.shape = mainShape;
        mainFixtureDef.density = 1.0f;
        mainFixtureDef.friction = 0.2f;
        body.createFixture(mainFixtureDef);
        mainShape.dispose();

        // Create a sensor fixture at the bottom for ground detection.
        PolygonShape sensorShape = new PolygonShape();
        sensorShape.setAsBox(width * 0.45f, 0.05f, new Vector2(0, -height / 2f), 0);
        FixtureDef sensorFixtureDef = new FixtureDef();
        sensorFixtureDef.shape = sensorShape;
        sensorFixtureDef.isSensor = true;
        Fixture sensorFixture = body.createFixture(sensorFixtureDef);
        sensorFixture.setUserData("groundSensor");
        sensorShape.dispose();
    }

    // Set whether the player is grounded (not in air).
    public void setGrounded(boolean grounded) {
        isInAir = !grounded;
    }

    // Update method: syncs the position with the Box2D body and applies deceleration if not moving.
    public void update(float delta) {
        Vector2 pos = body.getPosition();
        this.x = pos.x;
        this.y = pos.y;

        float xVel = body.getLinearVelocity().x;
        float yVel = body.getLinearVelocity().y;

        // If not actively moving, decelerate horizontally by interpolating toward 0.
        if (!isMoving) {
            xVel = linear.apply(xVel, 0, delta * 4f);
            body.setLinearVelocity(xVel, yVel);
        }

        // Clamp horizontal velocity to keep it within a defined range.
        body.setLinearVelocity(MathUtils.clamp(xVel, -2f, 2f), yVel);
    }

    // Render method: draws the player as a blue rectangle centered at its current position.
    public void render(SpriteBatch batch, ShapeDrawer shapeDrawer) {
        shapeDrawer.filledRectangle(x - (width / 2f), y - (height / 2f), width, height, Color.BLUE);
        Vector2 sensorPos = body.getWorldPoint(new Vector2(0, -height / 2f));
        float pixelSize = 0.01f;  // Adjust as needed
        shapeDrawer.filledRectangle(
            sensorPos.x - pixelSize / 2f,
            sensorPos.y - pixelSize / 2f,
            pixelSize,
            pixelSize,
            Color.RED
        );
    }

    // Makes the player jump by applying an upward impulse.
    public void jump() {
        body.applyLinearImpulse(new Vector2(0, 0.1f), body.getWorldCenter(), true);
    }

    // Moves the player horizontally by applying a force.
    public void move(float moveSpeed) {
        body.applyForceToCenter(new Vector2(moveSpeed, 0), true);
    }
}
