
public class Tile {
	public int value;
	public int alpha;
	
	public Tile(int value) {
		this.value = value;
		alpha = 255;
	}
	
	public Tile clone() {
		return new Tile(value);
	}

	public int getAlpha() {
		return alpha;
	}

	public void setAlpha(int alpha) {
		this.alpha = alpha;
	}
}
