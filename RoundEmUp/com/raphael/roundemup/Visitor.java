package com.raphael.ecosystem;

public class Visitor {

	public static void proxy(Sheep sheep, Animal animal) {
		animal.proxy(sheep);
	}

	public static void proxy(Dog dog, Animal animal) {
		animal.proxy(dog);
	}

	public static void collide(Sheep sheep, Animal animal, int index) {
		animal.collide(sheep, index);
	}

	public static void collide(Dog dog, Animal animal, int index) {
		animal.collide(dog, index);
	}
	
	public static void collide(Fence fence, Animal animal, int index) {
		animal.collide(fence, index);
	}

}
