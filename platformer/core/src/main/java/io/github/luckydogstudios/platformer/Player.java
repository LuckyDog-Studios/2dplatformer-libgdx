package io.github.luckydogstudios.platformer;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.*;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Disposable;
import space.earlygrey.shapedrawer.ShapeDrawer;

import java.util.Arrays;

import static com.badlogic.gdx.math.Interpolation.linear;

public class Player implements Disposable {
    public boolean isInAir;   // True when the player is airborne.
    public boolean isMoving;  // True when the player is actively moving (input-driven).

    // Character properties in world units.
    float x, y;
    float width, height;
    int health;
    private Body body;
    private Texture spriteSheet;
    private Animation<TextureRegion>[] animations;
    private float stateTime;

    public  PlayerState getCurrentState() {
        return currentState;
    }

    enum PlayerState {IDLE, ATTACKING, RUNNING, JUMPING}
    private PlayerState currentState;

    // Constructor: creates a dynamic Box2D body with a main collision fixture and a ground sensor.
    public Player(float x, float y, float width, float height, int health, World world) {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
        this.health = health;
        this.currentState = PlayerState.IDLE;


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


        // Load the sprite sheet image.
        spriteSheet = new Texture(Gdx.files.internal("sprites/character animation.png"));

        int frameWidth = 32;
        int frameHeight = 32;

        // Split the sheet into a 2D array of TextureRegions.
        TextureRegion[][] tmpFrames = TextureRegion.split(spriteSheet, frameWidth, frameHeight);

        // Flatten the 2D array into a 1D array (modify based on your sheet layout).
        TextureRegion[] animationFrames = new TextureRegion[tmpFrames.length * tmpFrames[0].length];
        int index = 0;
        for (int i = 0; i < tmpFrames.length; i++) {
            for (int j = 0; j < tmpFrames[i].length; j++) {
                animationFrames[index++] = tmpFrames[i][j];
            }
        }

        // Create an animation with a frame duration (e.g., 0.1 seconds per frame).
        animations = new Animation[2];
        animations[0] = new Animation<>(0.5f, Arrays.copyOfRange(animationFrames, 0, 2));
        animations[0].setPlayMode(Animation.PlayMode.LOOP);

        animations[1] = new Animation<>(0.1f, Arrays.copyOfRange(animationFrames, 3, animationFrames.length));
        animations[0].setPlayMode(Animation.PlayMode.NORMAL);

        stateTime = 0f;
    }

    // Set whether the player is grounded (not in air).
    public void setGrounded(boolean grounded) {
        isInAir = !grounded;
    }

    // Update method: syncs the position with the Box2D body and applies deceleration if not moving.
    public void update(float delta) {
        stateTime += delta;

        Vector2 pos = body.getPosition();
        this.x = pos.x;
        this.y = pos.y;

        float xVel = body.getLinearVelocity().x;
        float yVel = body.getLinearVelocity().y;

        if (!isMoving) {
            xVel = linear.apply(xVel, 0, delta * 4f); // linear interpolate to 0 if not moving
        }
        body.setLinearVelocity(MathUtils.clamp(xVel, -2f, 2f), yVel);


    }

    // Render method: draws the player centered at its current position.
    public void render(SpriteBatch batch) {
        TextureRegion currentFrame = null;
        if (currentState == PlayerState.IDLE) {
            currentFrame = animations[0].getKeyFrame(stateTime, true);
        } else if (currentState == PlayerState.ATTACKING) {
            currentFrame = animations[1].getKeyFrame(stateTime, false);
            if (animations[1].isAnimationFinished(stateTime)) {
                setPlayerState(PlayerState.IDLE);
                stateTime = 0; // Optionally reset stateTime.
            }
        }

        if (body.getLinearVelocity().x < 0f && !currentFrame.isFlipX()) {
            currentFrame.flip(true, false);
        } else if (body.getLinearVelocity().x >= 0f && currentFrame.isFlipX()) {
            currentFrame.flip(true, false);
        }


        batch.draw(currentFrame, x - width / 2f, y - height / 2f, width, height);
    }

    public void setPlayerState(PlayerState state) {
        this.currentState = state;
        stateTime = 0;
    }

    // Makes the player jump by applying an upward impulse.
    public void jump() {
        body.applyLinearImpulse(new Vector2(0, 0.08f), body.getWorldCenter(), true);
    }

    // Moves the player horizontally by applying a force.
    public void move(float moveSpeed) {
        body.applyForceToCenter(new Vector2(moveSpeed, 0), true);
    }

    public void dispose() {
        spriteSheet.dispose();

    }
}
