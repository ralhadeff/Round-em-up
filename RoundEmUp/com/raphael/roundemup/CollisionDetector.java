package com.raphael.ecosystem;

import java.util.ArrayList;
import java.util.List;

public class CollisionDetector {

	int xTiles, yTiles;
	List<Animal>[][] tileList;
	List<Animal[]> collisions;
	int tileSize, cutoff;

	@SuppressWarnings("unchecked")
	public CollisionDetector(int tileSize) {
		this.tileSize = tileSize;
		xTiles = (int) Math.ceil(World.WORLD_WIDTH / (float) (tileSize));
		yTiles = (int) Math.ceil(World.WORLD_HEIGHT / (float) (tileSize));
		// create the lists for each segment
		tileList = new List[xTiles][yTiles];
		for (int i = 0; i < xTiles; i++) {
			for (int j = 0; j < yTiles; j++) {
				// TDO make sure that the size is not a problem
				tileList[i][j] = new ArrayList<Animal>(100);
			}
		}
		collisions = new ArrayList<Animal[]>();
	}

	public List<Animal[]> calculateProximities(List<Animal> list, int cutoff) {
		this.cutoff = cutoff;
		for (int i = 0; i < xTiles; i++) {
			for (int j = 0; j < yTiles; j++) {
				tileList[i][j].clear();
			}
		}
		collisions.clear();
		// divide all animals into the segments
		int length = list.size();
		for (int i = 0; i < length; i++) {
			Animal animal = list.get(i);
			float x = animal.position.x;
			float y = animal.position.y;
			x /= tileSize;
			y /= tileSize;
			tileList[(int) x][(int) y].add(animal);
		}
		// start the calculations
		for (int i = 0; i < xTiles; i++) {
			for (int j = 0; j < yTiles; j++) {
				// self
				calculateListSelf(tileList[i][j], collisions);
				// surroundings
				if (j > 0 && i < xTiles - 1)
					calculateLists(tileList[i][j], tileList[i + 1][j - 1],
							collisions);
				if (i < xTiles - 1)
					calculateLists(tileList[i][j], tileList[i + 1][j],
							collisions);
				if (i < xTiles - 1 && j < yTiles - 1)
					calculateLists(tileList[i][j], tileList[i + 1][j + 1],
							collisions);
				if (j < yTiles - 1)
					calculateLists(tileList[i][j], tileList[i][j + 1],
							collisions);
			}
		}
		return collisions;
	}

	private void calculateListSelf(List<Animal> list, List<Animal[]> collisions) {
		int length = list.size();
		Animal animalI, animalJ;
		for (int i = 0; i < length; i++) {
			animalI = list.get(i);
			for (int j = 0; j < i; j++) {
				animalJ = list.get(j);
				if (animalI.position.dist(animalJ.position) <= cutoff * cutoff) {
					Animal[] collision = { animalI, animalJ };
					collisions.add(collision);
				}
			}
		}

	}
	
	private void calculateLists(List<Animal> list1, List<Animal> list2,
			List<Animal[]> collisions) {
		int length1 = list1.size();
		int length2 = list2.size();
		Animal animalI, animalJ;
		for (int i = 0; i < length1; i++) {
			animalI = list1.get(i);
			for (int j = 0; j < length2; j++) {
				animalJ = list2.get(j);
				if (animalI.position.dist(animalJ.position) <= cutoff * cutoff) {
					Animal[] collision = { animalI, animalJ };
					collisions.add(collision);
				}
			}
		}

	}

	public boolean contact(Animal animalI, Animal animalJ) {
		if (animalI.position.distSquared(animalJ.position) < 900)
			return true;
		return false;
	}
}
