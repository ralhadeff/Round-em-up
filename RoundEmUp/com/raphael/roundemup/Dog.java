package com.raphael.roundemup;

import com.raphael.framework.gl.TextureRegion;

public class Dog extends Animal {

	public Dog(float x, float y) {
		super(x, y);
		image = 2;
		moveSpeed += 30;
		rotationSpeed += 30;
	}

	@Override
	public void proxy(Animal animal) {
		Visitor.proxy(this, animal);
	}

	@Override
	public void collide(Animal animal, int index) {
		Visitor.collide(this, animal, index);
	}

	@Override
	public void collide(Sheep sheep, int index) {
		vacate(sheep, 3 - index, false);
	}

	@Override
	public void collide(Dog dog, int index) {
		vacate(dog, 3 - index, false);
	}

	@Override
	public void collide(Fence fence, int index) {
		vacate(fence, 1, false);
	}

	@Override
	public TextureRegion getTexture() {
		float time = 15 * stateTime;
		switch ((int) time % 4) {
		case 1:
			return Assets.dog0;
		case 2:
			return Assets.dog1;
		case 3:
			return Assets.dog_1;
		}
		return Assets.dog0;
	}

}
