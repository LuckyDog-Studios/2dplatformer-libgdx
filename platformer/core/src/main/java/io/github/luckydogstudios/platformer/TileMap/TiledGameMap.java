package io.github.luckydogstudios.platformer.TileMap;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.maps.MapLayer;
import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.maps.tiled.*;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.*;
import io.github.luckydogstudios.platformer.Core;

import java.util.HashMap;

public class TiledGameMap extends GameMap{

    private TiledMap tiledMap;
    private OrthogonalTiledMapRenderer renderer;
    private HashMap<TiledMapTileLayer.Cell, Body> bodies;
    private Core core;
    public static final float PPM = 100f;


    public TiledGameMap(Core core) {

        this.core = core;
        bodies = new HashMap<>();
        tiledMap = new TmxMapLoader().load("maps/platformer.tmx");
        renderer = new OrthogonalTiledMapRenderer(tiledMap);

        // add collisions to tiles
        TiledMapTileLayer layer = (TiledMapTileLayer) tiledMap.getLayers().get(0);
        for (int x = 0; x < layer.getWidth(); x++) {
            for (int y = 0; y < layer.getHeight(); y++) {
                TiledMapTileLayer.Cell cell = layer.getCell(x,y);
                if (cell == null) continue;

                TiledMapTile tile = cell.getTile();
                if (tile == null) continue;

                int tileId = tile.getId();
                System.out.print(tileId + " ");
                TileType type = TileType.getTileTypeById(tileId);
                System.out.println(type.isCollidable() + " " + type);
                if (type != null && type.isCollidable()) {
                    float worldX = (x) * TileType.TILE_SIZE;
                    float worldY = (y - 3) * TileType.TILE_SIZE;

                    // Create body definition
                    BodyDef bodyDef = new BodyDef();
                    bodyDef.type = BodyDef.BodyType.StaticBody;
                    bodyDef.position.set(
                        (worldX + TileType.TILE_SIZE / 2f) / 100,
                        (worldY + TileType.TILE_SIZE / 2f) / 100 //ppm is 100
                    );
                    bodyDef.fixedRotation = true;
                    // make fixutre definition
                    PolygonShape shape = new PolygonShape();
                    shape.setAsBox(TileType.TILE_SIZE / 2f / 100, TileType.TILE_SIZE / 2f / 100);
                    FixtureDef fixtureDef = new FixtureDef();
                    fixtureDef.shape = shape;
                    fixtureDef.friction = 0.8f;
                    // create body
                    Body body = core.world.createBody(bodyDef);
                    body.createFixture(fixtureDef);
                    shape.dispose();

                    bodies.put(cell, body);
                }
            }
        }
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
        for (Body body : bodies.values()) {
            core.world.destroyBody(body);
        }
    }

    @Override
    public TileType getTileTypeByCoordinate(int layer, int col, int row) {
        return null;
    }

    @Override
    public int getWidth() {
        return ((TiledMapTileLayer) tiledMap.getLayers().get(0)).getWidth();
    }

    @Override
    public int getHeight() {
        return ((TiledMapTileLayer) tiledMap.getLayers().get(0)).getHeight();
    }

    @Override
    public int getLayers() {
        return tiledMap.getLayers().size();
    }
}
