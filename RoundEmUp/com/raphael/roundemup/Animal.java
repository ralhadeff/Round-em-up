package com.raphael.roundemup;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import com.raphael.framework.gl.TextureRegion;
import com.raphael.framework.math.Vector2;

public class Animal {

	Vector2 position, destination, aux;
	int panic, vision, touchRadius, shortRadius, longRadius, image;
	float moveSpeed, rotationSpeed, stateTime, direction;
	Random random = new Random();
	List<Vector2> queue;

	public Animal(float x, float y) {
		this.position = new Vector2(x, y);
		this.destination = new Vector2(position);
		direction = random.nextInt(360);
		panic = 0;
		vision = 90;
		touchRadius = 30;
		shortRadius = 75;
		longRadius = 225;
		moveSpeed = 50 + random.nextInt(50);
		rotationSpeed = 100 + random.nextInt(100);
		stateTime = 0;
		queue = new ArrayList<Vector2>();
		aux = new Vector2();
	}

	public void move(float deltaTime) {
		stateTime += deltaTime;
		rotate(deltaTime);
		advance(deltaTime);
		correctLocation();
	}

	private void rotate(float deltaTime) {

		if ((int) position.distSquared(destination) == 0)
			return;

		int desiredDir = (int) aux.set(destination).sub(position).angle();
		float stride = rotationSpeed * deltaTime;
		// TDO improve with a helper angle
		if ((desiredDir - direction) > stride
				&& (desiredDir - direction) <= 180) {
			direction += stride;
		} else if ((desiredDir - direction) > 180
				&& (desiredDir - direction) <= (360 - stride)) {
			direction -= stride;
		} else if ((direction - desiredDir) > stride
				&& (direction - desiredDir) <= 180) {
			direction -= stride;
		} else if ((direction - desiredDir) > 180
				&& (direction - desiredDir) <= (360 - stride)) {
			direction += stride;
		} else {
			direction = desiredDir;
		}

		if (direction < 0)
			direction += 360;
	}

	public void advance(float deltaTime) {

		if (destination.distSquared(position) < 1) {
			if (queue.size() > 0) {
				destination.set(queue.get(0));
				queue.remove(0);
			} else
				stateTime = 0;

		}

		float stride = moveSpeed * deltaTime;
		if (!Support
				.angleIsWithin(
						(int) direction,
						(int) aux.set(destination).sub(position).angle(),
						Math.max(
								Math.min(
										vision,
										(int) (destination.dist(position) / (2 * moveSpeed / rotationSpeed))),
								1)))
			return;
		if (position.distSquared(destination) > stride * stride) {
			aux.set(stride, 0);
			aux.rotate(direction);
			position.add(aux);
		} else
			position.set(destination);

	}

	public void vacate(Animal animal, float ratio, boolean turn) {
		aux.set(position).sub(animal.position).angle();
		float distance = aux.len();
		distance = (30) - distance;
		aux.nor().mul(distance / ratio);
		position.add(aux);
		if (turn) {
			float otherDestDir = aux.set(animal.destination)
					.sub(animal.position).angle();
			float otherDestDist = aux.len();
			float destDir = aux.set(destination).sub(position).angle();
			float destDist = aux.len();
			destDir = Support.rotateAngle(destDir, otherDestDir, 1);
			if (!Support.angleIsWithin(destDir, otherDestDir, 0)) {
				aux.rotate(destDir - aux.angle()).mul(
						0.8f + 0.2f * (otherDestDist / destDist));
				destination.set(aux.add(position));
			}
		}
		correctLocation();
	}

	public void flee(Animal animal) {
		aux.set(position).sub(animal.position);
		destination.set(aux.add(position));
	}

	private void correctLocation() {
		if (position.x >= World.WORLD_WIDTH)
			position.x = World.WORLD_WIDTH - 1;
		if (position.x <= 0)
			position.x = 1;
		if (position.y >= World.WORLD_HEIGHT)
			position.y = World.WORLD_HEIGHT - 1;
		if (position.y <= 150)
			position.y = 151;
		if (destination.x >= World.WORLD_WIDTH)
			destination.x = World.WORLD_WIDTH - 1;
		if (destination.x <= 0)
			destination.x = 1;
		if (destination.y >= World.WORLD_HEIGHT)
			destination.y = World.WORLD_HEIGHT - 1;
		if (destination.y <= 150)
			destination.y = 151;

	}

	public void proxy(Animal animal) {
	}

	public void proxy(Sheep sheep) {
	}

	public void proxy(Dog dog) {
	}

	public void collide(Animal animal, int index) {
	}

	public void collide(Sheep sheep, int index) {
	}

	public void collide(Dog dog, int index) {
	}

	public void collide(Fence fence, int index) {
	}

	public TextureRegion getTexture() {
		return null;
	}

}
