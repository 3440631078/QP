/*
 * Pixel Dungeon
 * Copyright (C) 2012-2015 Oleg Dolya
 *
 * Shattered Pixel Dungeon
 * Copyright (C) 2014-2021 Evan Debenham
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>
 */

package com.elementalpixel.elementalpixeldungeon.items.armor.curses;


import com.elementalpixel.elementalpixeldungeon.actors.Char;
import com.elementalpixel.elementalpixeldungeon.actors.buffs.Buff;
import com.elementalpixel.elementalpixeldungeon.actors.buffs.Hunger;
import com.elementalpixel.elementalpixeldungeon.actors.hero.Hero;
import com.elementalpixel.elementalpixeldungeon.effects.Speck;
import com.elementalpixel.elementalpixeldungeon.items.armor.Armor;
import com.elementalpixel.elementalpixeldungeon.sprites.CharSprite;
import com.elementalpixel.elementalpixeldungeon.sprites.ItemSprite;
import com.watabou.utils.Random;

public class Metabolism extends Armor.Glyph {

	private static ItemSprite.Glowing BLACK = new ItemSprite.Glowing( 0x000000 );
	
	@Override
	public int proc(Armor armor, Char attacker, Char defender, int damage) {

		if (Random.Int( 6 ) == 0 && defender instanceof Hero) {

			//assumes using up 10% of starving, and healing of 1 hp per 10 turns;
			int healing = Math.min((int) Hunger.STARVING/100, defender.HT - defender.HP);

			if (healing > 0) {
				
				Hunger hunger = Buff.affect(defender, Hunger.class);
				
				if (!hunger.isStarving()) {
					
					hunger.affectHunger( healing * -10 );
					
					defender.HP += healing;
					defender.sprite.emitter().burst( Speck.factory( Speck.HEALING ), 1 );
					defender.sprite.showStatus( CharSprite.POSITIVE, Integer.toString( healing ) );
				}
			}

		}
		
		return damage;
	}

	@Override
	public ItemSprite.Glowing glowing() {
		return BLACK;
	}

	@Override
	public boolean curse() {
		return true;
	}
}
