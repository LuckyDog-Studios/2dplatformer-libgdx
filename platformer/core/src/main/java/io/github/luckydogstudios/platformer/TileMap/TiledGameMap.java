package io.github.luckydogstudios.platformer.TileMap;

import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapRenderer;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;

public class TiledGameMap extends GameMap{

    private TiledMap tiledMap;
    private OrthogonalTiledMapRenderer renderer;


    public TiledGameMap() {
        tiledMap = new TmxMapLoader().load("maps/platformer.tmx");
        renderer = new OrthogonalTiledMapRenderer(tiledMap);

    }
    @Override
    public void render (OrthographicCamera camera, SpriteBatch batch) {
        renderer.setView(camera);
        renderer.render();

        batch.setProjectionMatrix(camera.combined);
        super.render(camera, batch);
    }

    @Override
    public void update (float delta) {
        super.update(delta);
    }

    @Override
    public void dispose () {
        tiledMap.dispose();
    }

    @Override
    public TileType getTileTypeByCoordinate(int layer, int col, int row) {
        return null;
    }

    @Override
    public int getWidth() {
        return 0;
    }

    @Override
    public int getHeight() {
        return 0;
    }

    @Override
    public int getLayers() {
        return 0;
    }
}
