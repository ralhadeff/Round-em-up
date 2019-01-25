package com.raphael.roundemup;

import com.raphael.framework.gl.TextureRegion;

public class Fence extends Animal {

	public Fence(float x, float y,int direction) {
		super(x, y);
		image = 3;
		this.direction=direction+super.random.nextInt(20)-10;
	}
	
	@Override
	public void collide(Animal animal, int index) {
		Visitor.collide(this, animal, index);
	}
	
	@Override
	public void move(float deltaTime) {
	}
	
	@Override
	public TextureRegion getTexture(){
		return Assets.fence;
	}
	
}
