package clueGame;

public class Solution {

	public Card getPerson() {
		return person;
	}

	public Card getRoom() {
		return room;
	}

	public Card getWeapon() {
		return weapon;
	}

	private Card person;
	private Card room;
	private Card weapon;

	public Solution(Card person, Card room, Card weapon) {
		this.person = person;
		this.room = room;
		this.weapon = weapon;
	}

	public boolean equals(Solution o) {
		return this.room.equals(o.room) && this.person.equals(o.person) && this.weapon.equals(o.weapon);
	}

}
