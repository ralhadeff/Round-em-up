package com.raphael.ecosystem;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import com.raphael.framework.math.Vector2;

public class World {
	static final int WORLD_WIDTH = 960;
	static final int WORLD_HEIGHT = 1600;
	static final int SHEEPS = 150;
	static final int DOGS = 3;

	Random random = new Random();

	List<Animal> animals;
	CollisionDetector collisionDetector;
	List<Animal[]> collisions;
	Animal[] collision;

	Animal animal;

	public World() {

		animals = new ArrayList<Animal>();
		collisionDetector = new CollisionDetector(100);
		collisions = new ArrayList<Animal[]>();
		collision = new Animal[2];
		for (int i = 0; i < DOGS; i++) {
			animals.add(new Dog(140, 140+30*i));
		}
		for (int i = 0; i < 10; i++) {
			animals.add(new Fence(200, 1200 + 30 * i,90));
		}
		animals.add(new Fence(200,1500,45));
		for (int i = 0; i < 14; i++) {
			animals.add(new Fence(230+ 30 * i, 1500,0 ));
		}
		animals.add(new Fence(650,1500,135));
		for (int i = 0; i < 10; i++) {
			animals.add(new Fence(650, 1200 + 30 * i,90));
		}
		for (int i = 0; i < SHEEPS; i++) {
			animals.add(new Sheep(random.nextInt(WORLD_WIDTH), random
					.nextInt(WORLD_HEIGHT)));
		}
	}

	public void update(float deltaTime) {

		int length = animals.size();
		for (int i = 0; i < length; i++) {
			animal = animals.get(i);
			animal.move(deltaTime);
		}

		// TDO try to understand why the GC gets filled by the collision
		// detector
		collisions = collisionDetector.calculateProximities(animals, 75);
		int collisionsSize = collisions.size();
		for (int i = 0; i < collisionsSize; i++) {
			// TDO handle proximity
			collision = collisions.get(i);
			collision[0].proxy(collision[1]);
			collision[1].proxy(collision[0]);
			if (collisionDetector.contact(collision[0], collision[1])) {
				collision[0].collide(collision[1], 1);
				collision[1].collide(collision[0], 2);
			}
		}

	}

	public void moveAll(Vector2 worldPoint) {

		int length = animals.size();
		for (int i = 1; i < length; i++) {
			animal = animals.get(i);
			if (animal.queue.size() == 0)
				animal.destination.set(worldPoint.x, worldPoint.y);
		}

	}

}
