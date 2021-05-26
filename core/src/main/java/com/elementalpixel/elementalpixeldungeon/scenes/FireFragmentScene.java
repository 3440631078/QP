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

package com.elementalpixel.elementalpixeldungeon.scenes;

import com.elementalpixel.elementalpixeldungeon.Assets;
import com.elementalpixel.elementalpixeldungeon.Dungeon;
import com.elementalpixel.elementalpixeldungeon.GamesInProgress;
import com.elementalpixel.elementalpixeldungeon.effects.Flare;
import com.elementalpixel.elementalpixeldungeon.effects.Speck;
import com.elementalpixel.elementalpixeldungeon.items.Amulet;
import com.elementalpixel.elementalpixeldungeon.messages.Messages;
import com.elementalpixel.elementalpixeldungeon.ui.RedButton;
import com.elementalpixel.elementalpixeldungeon.ui.RenderedTextBlock;
import com.watabou.noosa.Camera;
import com.watabou.noosa.Game;
import com.watabou.noosa.Image;
import com.watabou.utils.Random;

public class FireFragmentScene extends PixelScene {
	
	private static final int WIDTH			= 120;
	private static final int BTN_HEIGHT		= 18;
	private static final float SMALL_GAP	= 2;
	private static final float LARGE_GAP	= 8;
	
	public static boolean noText = false;
	
	private Image firefragment;
	
	@Override
	public void create() {
		super.create();
		
		RenderedTextBlock text = null;
		if (!noText) {
			text = renderTextBlock( Messages.get(this, "text"), 8 );
			text.maxWidth(WIDTH);
			add( text );
		}
		
		firefragment = new Image( Assets.Sprites.FIRE_FRAGMENT );
		add( firefragment );
		
		RedButton btnExit = new RedButton( Messages.get(this, "exit") ) {
			@Override
			protected void onClick() {
				Dungeon.win( Amulet.class );
				Dungeon.deleteGame( GamesInProgress.curSlot, true );
				Game.switchScene( RankingsScene.class );
			}
		};
		btnExit.setSize( WIDTH, BTN_HEIGHT );
		add( btnExit );
		
		RedButton btnStay = new RedButton( Messages.get(this, "stay") ) {
			@Override
			protected void onClick() {
				onBackPressed();
			}
		};
		btnStay.setSize( WIDTH, BTN_HEIGHT );
		add( btnStay );
		
		float height;
		if (noText) {
			height = firefragment.height + LARGE_GAP + btnExit.height() + SMALL_GAP + btnStay.height();
			
			firefragment.x = (Camera.main.width - firefragment.width) / 2;
			firefragment.y = (Camera.main.height - height) / 2;
			align(firefragment);

			btnExit.setPos( (Camera.main.width - btnExit.width()) / 2, firefragment.y + firefragment.height + LARGE_GAP );
			btnStay.setPos( btnExit.left(), btnExit.bottom() + SMALL_GAP );
			
		} else {
			height = firefragment.height + LARGE_GAP + text.height() + LARGE_GAP + btnExit.height() + SMALL_GAP + btnStay.height();
			
			firefragment.x = (Camera.main.width - firefragment.width) / 2;
			firefragment.y = (Camera.main.height - height) / 2;
			align(firefragment);

			text.setPos((Camera.main.width - text.width()) / 2, firefragment.y + firefragment.height + LARGE_GAP);
			align(text);
			
			btnExit.setPos( (Camera.main.width - btnExit.width()) / 2, text.top() + text.height() + LARGE_GAP );
			btnStay.setPos( btnExit.left(), btnExit.bottom() + SMALL_GAP );
		}

		new Flare( 8, 48 ).color( 0xFFDDBB, true ).show( firefragment, 0 ).angularSpeed = +30;
		
		fadeIn();
	}
	
	@Override
	protected void onBackPressed() {
		InterlevelScene.mode = InterlevelScene.Mode.CONTINUE;
		Game.switchScene( InterlevelScene.class );
	}
	
	private float timer = 0;
	
	@Override
	public void update() {
		super.update();
		
		if ((timer -= Game.elapsed) < 0) {
			timer = Random.Float( 0.5f, 5f );
			
			Speck star = (Speck)recycle( Speck.class );
			star.reset( 0, firefragment.x + 10.5f, firefragment.y + 5.5f, Speck.DISCOVER );
			add( star );
		}
	}
}
