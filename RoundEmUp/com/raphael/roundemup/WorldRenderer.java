package com.raphael.roundemup;

import javax.microedition.khronos.opengles.GL10;

import com.raphael.framework.gl.Camera2D;
import com.raphael.framework.gl.PointBatcher;
import com.raphael.framework.gl.ShapeBatcher;
import com.raphael.framework.gl.SpriteBatcher;
import com.raphael.framework.impl.GLGraphics;
import com.raphael.framework.math.Vector2;

public class WorldRenderer {
	static final float FRUSTUM_WIDTH = 480;
	static final float FRUSTUM_HEIGHT = 800;
	GLGraphics glGraphics;
	World world;
	Camera2D cam;
	SpriteBatcher batcher;
	ShapeBatcher shapeBatcher;
	PointBatcher pointBatcher;
	Vector2 view, position;
	Animal animal;
	int length;

	public WorldRenderer(GLGraphics glGraphics, SpriteBatcher batcher,
			World world, ShapeBatcher shapeBatcher, PointBatcher pointBatcher) {
		this.glGraphics = glGraphics;
		this.world = world;
		this.cam = new Camera2D(glGraphics, FRUSTUM_WIDTH, FRUSTUM_HEIGHT);
		this.batcher = batcher;
		this.shapeBatcher = shapeBatcher;
		this.pointBatcher = pointBatcher;
		view = new Vector2(240, 400);
		position = new Vector2();
	}

	public void render() {
		cam.setViewportAndMatrices();
		cam.position = view;
		renderBackground();
		length = world.animals.size();
		renderPath();
		renderAnimals();
	}

	public void renderBackground() {
		batcher.beginBatch(Assets.background);
		batcher.drawSprite(World.WORLD_WIDTH / 2, World.WORLD_HEIGHT / 2,
				World.WORLD_WIDTH, World.WORLD_HEIGHT, Assets.backgroundRegion);
		batcher.endBatch();

	}

	private void renderPath() {
		int counter = 0;
		for (int i = 0; i < length && counter == 0; i++) {
			animal = world.animals.get(i);
			counter += animal.queue.size();
		}
		if (counter == 0)
			return;
		GL10 gl = glGraphics.getGL();
		gl.glDisable(GL10.GL_TEXTURE_2D);
		gl.glPointSize(5);
		// TDO proof against more points than batcher can handle
		pointBatcher.beginBatch();
		for (int i = 0; i < length; i++) {
			animal = world.animals.get(i);
			int pathLength = animal.queue.size();
			if (pathLength > 0) {
				for (int j = 0; j < pathLength; j++) {
					Vector2 vector = animal.queue.get(j);
					pointBatcher.drawPoint(vector.x, vector.y, 1, 0.1f, 0.1f,
							0.8f);
				}
			}
			pointBatcher.drawPoint(animal.destination.x, animal.destination.y,
					0, 0, 1, 1);
		}
		pointBatcher.endBatch();
		gl.glEnable(GL10.GL_TEXTURE_2D);
	}

	private void renderAnimals() {
		if (length == 0)
			return;
		GL10 gl = glGraphics.getGL();
		gl.glEnable(GL10.GL_BLEND);
		gl.glBlendFunc(GL10.GL_SRC_ALPHA, GL10.GL_ONE_MINUS_SRC_ALPHA);
		batcher.beginBatch(Assets.animals);
		for (int i = 0; i < length; i++) {
			animal = world.animals.get(i);
			batcher.drawSprite(animal.position.x, animal.position.y, 48, 32,
					animal.direction, animal.getTexture());
		}
		batcher.endBatch();
		gl.glDisable(GL10.GL_BLEND);
	}

}
