package name.nanek.analogtyper;

public class MoveToCharacterMapper {

	public static final MoveToCharacterMapper LEFT = new MoveToCharacterMapper(new String[] {
			"q", "w", "e", 
			"a",      "s", 
			"z", "x", "c", 
	});

	public static final MoveToCharacterMapper UP = new MoveToCharacterMapper(new String[] {
			"r", "t", "y", 
			"d",      "f", 
			"v", "b", "n", 
	});

	public static final MoveToCharacterMapper RIGHT = new MoveToCharacterMapper(new String[] {
			"u", "i", "o", 
			"g",      "h", 
			"m", ",", ".", 
	});

	public static final MoveToCharacterMapper DOWN = new MoveToCharacterMapper(new String[] {
			"p", "[", "]", 
			"j",      "k", 
			"!", "?", "l", 
	});
	
	public String[] characters;

	public MoveToCharacterMapper(String[] characters) {
		this.characters = characters;
	}
	
	public String getCharacter(Move move) {
		if ( move.left && move.up ) {
			return characters[0];
		}
		if ( move.right && move.up ) {
			return characters[2];
		}
		if ( move.up ) {
			return characters[1];
		}
		

		if ( move.left && move.down ) {
			return characters[5];
		}
		if ( move.right && move.down ) {
			return characters[7];
		}
		if ( move.down ) {
			return characters[6];
		}
		

		if ( move.left ) {
			return characters[3];
		}
		if ( move.right ) {
			return characters[4];
		}

		
		return null;
	}
	
	public static MoveToCharacterMapper getChooser(Move move) {
		if ( move.left ) {
			return LEFT;
		}
		if ( move.up ) {
			return UP;
		}
		if ( move.right ) {
			return RIGHT;
		}
		if ( move.down ) {
			return DOWN;
		}
		
		return null;
	}

}
