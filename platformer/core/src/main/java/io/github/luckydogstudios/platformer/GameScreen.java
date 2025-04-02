package io.github.luckydogstudios.platformer;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.*;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.physics.box2d.*;
import space.earlygrey.shapedrawer.ShapeDrawer;
import com.badlogic.gdx.math.Vector2;

public class GameScreen implements Screen {
    final Core core;

    private ShapeDrawer shapeDrawer;
    private Texture pixel;
    private Player player;
    private World world;
    private Platform platform;

    public GameScreen(Core core) {
        this.core = core;

        Pixmap pixmap = new Pixmap(1, 1,Pixmap.Format.RGBA8888);
        pixmap.setColor(Color.WHITE);
        pixmap.fill();
        pixel = new Texture(pixmap);
        pixmap.dispose();

        shapeDrawer = new ShapeDrawer(core.batch, new TextureRegion(pixel));


        // Create a Box2D world with gravity (e.g., gravity pointing downwards)
        world = new World(new Vector2(0, -9.8f), true);
        platform = new Platform(world, Core.VIRTUAL_WIDTH/2f, 0.1f, 3.0f, 0.1f);
        player = new Player(Core.VIRTUAL_WIDTH/2f, Core.VIRTUAL_HEIGHT/2f, 0.16f, 0.26f, 3, world);

        world.setContactListener(new GameContactListener(player));

    }

    @Override
    public void show() {

    }

    @Override
    public void render(float delta) {
        input();
        update_physics();
        draw();
    }


    private void draw() {
        Gdx.gl.glClearColor(0.1f, 0.1f, 0.1f, 1f);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        core.batch.setProjectionMatrix(core.camera.combined);
        core.batch.begin();
        player.render(core.batch, shapeDrawer);
        platform.render(core.batch, shapeDrawer);
        core.batch.end();
    }

    private void update_physics() {
        float delta = Gdx.graphics.getDeltaTime();
        world.step(delta, 6, 3);
        player.update(delta);

        Vector3 targetPos = new Vector3(player.x, player.y, 0);
        Camera camera = core.viewport.getCamera();
        camera.position.lerp(targetPos, 4 * delta);
        camera.update();
    }

    private void input() {
        if(Gdx.input.isKeyPressed(Input.Keys.SPACE) && !player.isInAir) {
            player.jump();
        } else if(Gdx.input.isKeyPressed(Input.Keys.A)) {
            player.move(-1.0f);
            player.isMoving = true;
        } else if(Gdx.input.isKeyPressed(Input.Keys.D)) {
            player.move(1.0f);
            player.isMoving = true;
        } else {
            player.isMoving = false;
        }
    }

    @Override
    public void resize(int width, int height) {
        core.viewport.update(width, height);
    }

    @Override
    public void pause() {

    }

    @Override
    public void resume() {

    }

    @Override
    public void hide() {

    }

    @Override
    public void dispose() {
        pixel.dispose();
    }
}
