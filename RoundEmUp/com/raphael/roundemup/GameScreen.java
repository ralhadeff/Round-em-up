package com.raphael.ecosystem;

import java.util.ArrayList;
import java.util.List;

import javax.microedition.khronos.opengles.GL10;

import com.raphael.framework.Game;
import com.raphael.framework.Input.TouchEvent;
import com.raphael.framework.gl.Camera2D;
import com.raphael.framework.gl.PointBatcher;
import com.raphael.framework.gl.ShapeBatcher;
import com.raphael.framework.gl.SpriteBatcher;
import com.raphael.framework.impl.GLScreen;
import com.raphael.framework.math.OverlapTester;
import com.raphael.framework.math.Rectangle;
import com.raphael.framework.math.Vector2;

//known problems:
//when the map is not in the correct aspect ratio there are visual bugs.

//when the speed of an animal is high and close to the collision radius of another animal, it can simply pass through it.

public class GameScreen extends GLScreen {

	enum FingerMode {
		NONE, DRAG, ZOOM, HOLD, ORDER
	}

	FingerMode mode;
	int dragPointer, zoomPointer;
	int x, y;
	float currentZoom;
	boolean paused;
	Camera2D guiCam;
	Vector2 touchPoint, worldPoint, drag, zoom, currentPosition, middle,
			centerOfGui;
	SpriteBatcher batcher;
	ShapeBatcher shapeBatcher;
	PointBatcher pointBatcher;
	World world;
	WorldRenderer renderer;
	Rectangle pauseBounds, miniMapBounds, worldBounds;
	List<Vector2> queue;
	Animal selected;

	public GameScreen(Game game) {
		super(game);
		paused = false;
		guiCam = new Camera2D(glGraphics, 480, 800);
		touchPoint = new Vector2();
		worldPoint = new Vector2();
		drag = new Vector2();
		zoom = new Vector2();
		middle = new Vector2();
		centerOfGui = new Vector2(guiCam.frustumWidth / 2,
				guiCam.frustumHeight / 2);
		currentPosition = new Vector2();
		batcher = new SpriteBatcher(glGraphics, 20000);
		shapeBatcher = new ShapeBatcher(glGraphics, 1000);
		pointBatcher = new PointBatcher(glGraphics, 10000);
		world = new World();
		renderer = new WorldRenderer(glGraphics, batcher, world, shapeBatcher,
				pointBatcher);
		pauseBounds = new Rectangle(0, 0, 64, 64);
		miniMapBounds = new Rectangle(480 - 80, 0, 80, 80);
		worldBounds = new Rectangle(0, 80, 480, 800 - 80);
		mode = FingerMode.NONE;
		queue = new ArrayList<Vector2>();

	}

	@Override
	public void update(float deltaTime) {

		currentPosition.set(renderer.cam.position);
		currentZoom = renderer.cam.zoom;

		if (deltaTime > 0.1f)
			deltaTime = 0.1f;

		List<TouchEvent> touchEvents = game.getInput().getTouchEvents();
		game.getInput().getKeyEvents();
		int len = touchEvents.size();
		for (int i = 0; i < len; i++) {
			TouchEvent event = touchEvents.get(i);
			touchPoint.set(event.x, event.y);
			guiCam.touchToWorld(touchPoint);
			if (OverlapTester.pointInRectangle(worldBounds, touchPoint)) {
				worldPoint.set(event.x, event.y);
				renderer.cam.touchToWorld(worldPoint);
				handleWorldTouch(event);
			}
			switch (event.type) {
			case TouchEvent.TOUCH_DOWN:
				break;
			case TouchEvent.TOUCH_DRAGGED:
				break;
			case TouchEvent.TOUCH_UP:
				if (mode == FingerMode.ZOOM) {
					if (event.pointer == zoomPointer)
						mode = FingerMode.DRAG;
					if (event.pointer == dragPointer) {
						dragPointer = zoomPointer;
						drag.set(zoom);
						mode = FingerMode.DRAG;
					}
				} else if (mode == FingerMode.DRAG) {
					if (event.pointer == dragPointer)
						mode = FingerMode.NONE;
				} else if (mode == FingerMode.ORDER)
					queue.clear();
				if (OverlapTester.pointInRectangle(miniMapBounds, touchPoint)) {
					if (mode == FingerMode.ORDER)
						mode = FingerMode.NONE;
				}
				if (OverlapTester.pointInRectangle(pauseBounds, touchPoint)) {
					paused = !paused;
				}
				break;
			}
		}

		if (!paused)
			world.update(deltaTime);

		fixZoom();
		fixPosition();

	}

	private void handleWorldTouch(TouchEvent event) {

		switch (event.type) {
		case TouchEvent.TOUCH_DOWN:
			if (mode == FingerMode.NONE) {
				dragPointer = event.pointer;
				drag.set(touchPoint);
				mode = FingerMode.DRAG;
			} else if (mode == FingerMode.DRAG) {
				zoomPointer = event.pointer;
				zoom.set(touchPoint);
				mode = FingerMode.ZOOM;
			} else if (mode==FingerMode.ZOOM){
				world.moveAll(worldPoint);
			} else if (mode == FingerMode.ORDER) {
				dragPointer = event.pointer;
				queue.add(worldPoint.cpy());
			}
			break;
		case TouchEvent.TOUCH_DRAGGED:
			if (mode == FingerMode.DRAG && event.pointer == dragPointer) {
				currentPosition.add(drag.sub(touchPoint).mul(currentZoom));
				drag.set(touchPoint);
			} else if (mode == FingerMode.ZOOM) {
				if (event.pointer == dragPointer) {
					float newZoom = (zoom.dist(drag)) / (touchPoint.dist(zoom));
					currentZoom *= newZoom;
					drag.set(touchPoint);
				}
				if (event.pointer == zoomPointer) {
					float newZoom = (drag.dist(zoom)) / (touchPoint.dist(drag));
					currentZoom *= newZoom;
					zoom.set(touchPoint);
				}
			} else if (mode == FingerMode.ORDER && event.pointer == dragPointer)
				queue.add(worldPoint.cpy());
			break;
		case TouchEvent.TOUCH_UP:
			if (mode == FingerMode.ZOOM) {
				if (event.pointer == zoomPointer)
					mode = FingerMode.DRAG;
				if (event.pointer == dragPointer) {
					dragPointer = zoomPointer;
					drag.set(zoom);
					mode = FingerMode.DRAG;
				}
			} else if (mode == FingerMode.DRAG) {
				if (event.pointer == dragPointer)
					if (touchAnimal(worldPoint))
						mode = FingerMode.ORDER;
					else
						mode = FingerMode.NONE;
			} else if (mode == FingerMode.ORDER && event.pointer == dragPointer) {
				sortQueue();
			}
			break;
		}
	}

	private boolean touchAnimal(Vector2 worldPoint) {
		int length = world.animals.size();
		for (int i = 0; i < length; i++) {
			Animal animal = world.animals.get(i);
			if (animal.position.distSquared(worldPoint) < 800 * renderer.cam.zoom) {
				selected = animal;
				return true;
			}
		}
		return false;
	}

	private void sortQueue() {
		int length = queue.size();
		if (length == 0)
			return;
		int binSize = 10;
		// using drag Vector to conserve memory
		selected.destination.set(queue.get(0));
		if (length > binSize) {
			for (int i = 0; i <= length - binSize; i++) {
				drag.set(queue.get(i));
				for (int j = 1; j < binSize; j++) {
					drag.add(queue.get(i + j));
				}
				drag.mul(1f / binSize);
				selected.queue.add(drag.cpy());
			}
		}
		selected.queue.add(queue.get(length - 1));
		queue.clear();
		selected = null;
		mode = FingerMode.NONE;
	}

	private void fixPosition() {

		if (currentPosition.x < guiCam.frustumWidth * renderer.cam.zoom / 2)
			currentPosition.x = guiCam.frustumWidth * renderer.cam.zoom / 2;
		if (currentPosition.x > World.WORLD_WIDTH - guiCam.frustumWidth
				* renderer.cam.zoom / 2)
			currentPosition.x = World.WORLD_WIDTH - guiCam.frustumWidth
					* renderer.cam.zoom / 2;
		if (currentPosition.y < guiCam.frustumHeight * renderer.cam.zoom / 2)
			currentPosition.y = guiCam.frustumHeight * renderer.cam.zoom / 2;
		if (currentPosition.y > World.WORLD_HEIGHT - guiCam.frustumHeight
				* renderer.cam.zoom / 2)
			currentPosition.y = World.WORLD_HEIGHT - guiCam.frustumHeight
					* renderer.cam.zoom / 2;
		renderer.cam.position.set(currentPosition);

	}

	private void fixZoom() {
		if (renderer.cam.zoom == currentZoom)
			return;
		if (currentZoom < 0.1)
			currentZoom = 0.1f;
		else if (currentZoom > World.WORLD_WIDTH / renderer.cam.frustumWidth)
			currentZoom = World.WORLD_WIDTH / renderer.cam.frustumWidth;
		middle.set(drag).add(zoom).mul(renderer.cam.zoom / 2);
		middle.mul(1 - currentZoom / renderer.cam.zoom);
		middle.sub(centerOfGui.mul(renderer.cam.zoom - currentZoom));
		centerOfGui.set(guiCam.frustumWidth / 2,
				guiCam.frustumHeight / 2);
		renderer.cam.zoom = currentZoom;
		currentPosition.add(middle);
	}

	@Override
	public void present(float deltaTime) {
		GL10 gl = glGraphics.getGL();
		gl.glClear(GL10.GL_COLOR_BUFFER_BIT);
		gl.glEnable(GL10.GL_TEXTURE_2D);

		renderer.render();

		guiCam.setViewportAndMatrices();
		gl.glEnable(GL10.GL_BLEND);
		gl.glBlendFunc(GL10.GL_SRC_ALPHA, GL10.GL_ONE_MINUS_SRC_ALPHA);
		batcher.beginBatch(Assets.items);
		batcher.drawSprite(32, 32, 64, 64, Assets.pause);
		batcher.drawSprite(440, 40, 80, 64, Assets.ready);
		batcher.endBatch();
		gl.glDisable(GL10.GL_TEXTURE_2D);
		if (paused || mode == FingerMode.ORDER) {
			shapeBatcher.beginBatch();
			gl.glLineWidth(12);
			if (paused)
				shapeBatcher.drawEmptyRectangle(240, 400, 480, 800, 1, 0, 0,
						0.5f);
			if (mode == FingerMode.ORDER)
				shapeBatcher.drawEmptyRectangle(440, 40, 80, 64, 1, 1, 0, 0.5f);
			shapeBatcher.endBatch();
		}
		gl.glDisable(GL10.GL_BLEND);

	}

	@Override
	public void pause() {
		paused = true;
	}

	@Override
	public void resume() {
	}

	@Override
	public void dispose() {
	}
}
