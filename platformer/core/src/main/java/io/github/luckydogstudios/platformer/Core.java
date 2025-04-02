package io.github.luckydogstudios.platformer;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;

/** {@link com.badlogic.gdx.ApplicationListener} implementation shared by all platforms. */
public class Core extends Game {

    static final float VIRTUAL_WIDTH = 3.6f;
    static final float VIRTUAL_HEIGHT = 1.8f;

    public SpriteBatch batch;
    public OrthographicCamera camera;
    public Viewport viewport;

    @Override
    public void create() {
        batch = new SpriteBatch();
        camera = new OrthographicCamera();
        viewport = new FitViewport(Core.VIRTUAL_WIDTH, Core.VIRTUAL_HEIGHT, camera);
        viewport.apply();

        camera.position.set(Core.VIRTUAL_WIDTH / 2f, Core.VIRTUAL_HEIGHT / 2f, 0);

        this.setScreen(new GameScreen(this));
    }

    @Override
    public void render() {
        super.render();
    }

    @Override
    public void dispose() {
        batch.dispose();
    }
}
