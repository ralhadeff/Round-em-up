package com.raphael.roundemup;

import com.raphael.framework.gl.TextureRegion;

public class Sheep extends Animal {

	public Sheep(float x, float y) {
		super(x, y);
		image = 1;
	}

	@Override
	public void proxy(Animal animal) {
		Visitor.proxy(this, animal);
	}

	@Override
	public void proxy(Dog dog) {
		flee(dog);
	}

	@Override
	public void collide(Animal animal, int index) {
		Visitor.collide(this, animal, index);
	}

	@Override
	public void collide(Sheep sheep, int index) {
		vacate(sheep, 3 - index,true);
	}

	@Override
	public void collide(Fence fence, int index) {
		vacate(fence, 1,false);
	}
	
	@Override
	public TextureRegion getTexture(){
		float time = 18*stateTime;
		switch((int)time%9){
		case 1:
			return Assets.sheep0;
		case 2:
			return Assets.sheep1;
		case 3:
			return Assets.sheep2;
		case 4:
			return Assets.sheep1;
		case 5:
			return Assets.sheep0;
		case 6:
			return Assets.sheep_1;
		case 7:
			return Assets.sheep_2;
		case 8:
			return Assets.sheep_1;
		}
		return Assets.sheep0;
	}

}
