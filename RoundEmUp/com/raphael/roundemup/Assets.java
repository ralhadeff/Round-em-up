package com.raphael.roundemup;

import com.raphael.framework.gl.Texture;
import com.raphael.framework.gl.TextureRegion;
import com.raphael.framework.impl.GLGame;

public class Assets {
	public static Texture background;
	public static TextureRegion backgroundRegion;

	public static Texture items;
	public static TextureRegion ready;
	public static TextureRegion pause;
	public static TextureRegion castle;
	
	public static Texture animals;
	public static TextureRegion sheep0;
	public static TextureRegion sheep1;
	public static TextureRegion sheep2;
	public static TextureRegion sheep_1;
	public static TextureRegion sheep_2;	
	public static TextureRegion dog0;
	public static TextureRegion dog1;
	public static TextureRegion dog_1;

	public static TextureRegion fence;

	public static void load(GLGame game) {
		
		background = new Texture(game, "background.png");
		backgroundRegion = new TextureRegion(background, 0, 0, 320, 480);
		
		animals = new Texture(game, "animals.png");
		sheep0 = new TextureRegion(animals, 0, 0, 144, 96);
		sheep1 = new TextureRegion(animals, 144, 96, 144, 96);
		sheep2 = new TextureRegion(animals, 144, 0, 144, 96);
		sheep_1 = new TextureRegion(animals, 288, 96, 144, 96);
		sheep_2 = new TextureRegion(animals, 288, 0, 144, 96);
		dog0 = new TextureRegion(animals, 0, 96, 144, 96);
		dog1 = new TextureRegion(animals, 144, 288, 144, 96);
		dog_1 = new TextureRegion(animals, 288, 288, 144, 96);
		fence = new TextureRegion(animals, 0, 192, 144, 96);


		items = new Texture(game, "items.png");
		ready = new TextureRegion(items, 320, 224, 192, 32);
		pause = new TextureRegion(items, 64, 64, 64, 64);
		castle = new TextureRegion(items, 128, 64, 64, 64);

	}

	public static void reload() {
		background.reload();
		items.reload();
		animals.reload();
	}

}
