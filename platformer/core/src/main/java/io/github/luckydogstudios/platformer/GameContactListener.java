package io.github.luckydogstudios.platformer;

import com.badlogic.gdx.physics.box2d.*;

public class GameContactListener implements ContactListener {

    private final Player player;

    public GameContactListener(final Player player) {
        this.player = player;
    }

    @Override
    public void beginContact(Contact contact) {
        Fixture a = contact.getFixtureA();
        Fixture b = contact.getFixtureB();

        // Check if either fixture is the player's ground sensor.
        if ("groundSensor".equals(a.getUserData()) || "groundSensor".equals(b.getUserData())) {
            // Optionally check that the other fixture is indeed a ground/platform object.
            // For now, simply set the flag.
            player.setGrounded(true);
        }
    }

    @Override
    public void endContact(Contact contact) {
        Fixture a = contact.getFixtureA();
        Fixture b = contact.getFixtureB();

        if ("groundSensor".equals(a.getUserData()) || "groundSensor".equals(b.getUserData())) {
            player.setGrounded(false);
        }
    }
    // Unused methods:
    @Override public void preSolve(Contact contact, Manifold oldManifold) {}
    @Override public void postSolve(Contact contact, ContactImpulse impulse) {}
}
