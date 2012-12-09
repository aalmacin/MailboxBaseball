package com.aldrinmike.mailboxbaseball;

import java.util.ArrayList;

import javax.microedition.khronos.opengles.GL10;

import org.anddev.andengine.engine.Engine;
import org.anddev.andengine.engine.camera.Camera;
import org.anddev.andengine.engine.options.EngineOptions;
import org.anddev.andengine.engine.options.EngineOptions.ScreenOrientation;
import org.anddev.andengine.engine.options.resolutionpolicy.RatioResolutionPolicy;
import org.anddev.andengine.entity.scene.Scene;
import org.anddev.andengine.entity.scene.menu.MenuScene;
import org.anddev.andengine.entity.scene.menu.MenuScene.IOnMenuItemClickListener;
import org.anddev.andengine.entity.scene.menu.item.IMenuItem;
import org.anddev.andengine.entity.scene.menu.item.TextMenuItem;
import org.anddev.andengine.entity.scene.menu.item.decorator.ColorMenuItemDecorator;
import org.anddev.andengine.entity.sprite.Sprite;
import org.anddev.andengine.entity.text.ChangeableText;
import org.anddev.andengine.entity.util.FPSLogger;
import org.anddev.andengine.input.touch.TouchEvent;
import org.anddev.andengine.opengl.font.Font;
import org.anddev.andengine.opengl.font.FontFactory;
import org.anddev.andengine.opengl.texture.Texture;
import org.anddev.andengine.opengl.texture.TextureOptions;
import org.anddev.andengine.opengl.texture.region.TextureRegion;
import org.anddev.andengine.opengl.texture.region.TextureRegionFactory;
import org.anddev.andengine.ui.activity.BaseGameActivity;



import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
/**
 * FileName: ControlScreenActivity.java
 * 
 * @author Aldrin Jerome Almacin
 *         <p>
 *         <b>Date: </b>December 8, 2012
 *         </p>
 *         <p>
 *         <b>Description: </b>ControlScreenActivity is the activity that shows a set of menu items that can be clicked to send the user to different screens.
 *			</p>
 *			<p>
 *         <b>Menu Items:</b>
 *         <ul>
 *         	<li>Start - Starts the game.</li>
 *         	<li>Help - Shows how the game is played.</li>
 *         	<li>High Scores - Shows the top 10 scores.</li>
 *         	<li>Exit - Closes the game.</li>
 *         </ul>
 *         </p>
 * 
 */
public class ControlScreenActivity extends BaseGameActivity  implements IOnMenuItemClickListener{

	// The camera width and height
	private final static int CAMERA_WIDTH = 480;
	private final static int CAMERA_HEIGHT = 800;
	
	// The ids of the menu items
	private final static int MENU_START = 0;
	private final static int MENU_HELP = 1;
	private final static int MENU_HIGHSCORES = 2;
	private final static int MENU_EXIT = 3;
	
	// The margin of the menu items
	private static final int MENU_MARGIN = 60;
	
	// The x position of the back button
	private static final float BACK_BUTTON_X = 20;
	
	// A reference to the game camera.
	private Camera mCamera;
	
	// The font used by the menu items.
	private Font mFont;
	
	// The font used by the top 10 scores.
	private Font mHighScoreFont;
	
	// The scenes that the activity uses.
	private Scene mMainScene;	// Parent of all the other scenes.
	private Scene mHelpScene;	// Shows the sprite with the instructions.
	private HighScoreScene mHighScoreScene;	// Shows the high scores.
	private MenuScene mMainMenuScene;	// Shows the main game menus.	
	
	// All the sprites that are shown in each activity
	private Sprite mBackgroundSprite;	
	private Sprite mHelpScreenSprite;
	private Sprite mHighScoreSceneSprite;
	
	private Sprite mBackButtonSprite; // The back button
	
	public Context mContext; // A reference to this Activity's context
	
	@Override
	public Engine onLoadEngine() {
		this.mCamera = new Camera(0, 0, CAMERA_WIDTH, CAMERA_HEIGHT);
		return new Engine(new EngineOptions(true, ScreenOrientation.PORTRAIT, 
				new RatioResolutionPolicy(CAMERA_WIDTH,CAMERA_HEIGHT), this.mCamera));
	} // End of onLoadEngine

	@Override
	public void onLoadResources() {		
		mContext = this;
		
		// Load the fonts that the activity will need.
		FontFactory.setAssetBasePath("font/");
		
		Texture mFontTexture = new Texture(256, 256,TextureOptions.BILINEAR_PREMULTIPLYALPHA);
		mFont = FontFactory.createFromAsset(mFontTexture, this, "kulminoituva.ttf", 48, true, Color.BLACK);
		mEngine.getTextureManager().loadTexture(mFontTexture);
		mEngine.getFontManager().loadFont(this.mFont);	

		Texture mHighScoreFontTexture = new Texture(256, 256,TextureOptions.BILINEAR_PREMULTIPLYALPHA); 
		mHighScoreFont = FontFactory.createFromAsset(mHighScoreFontTexture, this, "kulminoituva.ttf", 38, true, Color.BLACK);
		mEngine.getTextureManager().loadTexture(mHighScoreFontTexture);
		mEngine.getFontManager().loadFont(this.mHighScoreFont);	
		
		// Load the textures and create the main screen's background sprite.
		Texture mBackgroundTexture = new Texture(1024,1024,TextureOptions.BILINEAR_PREMULTIPLYALPHA);
		TextureRegion mBackgroundTextureRegion = TextureRegionFactory.createFromAsset(mBackgroundTexture, this, "gfx/background.png", 0, 0);
		mEngine.getTextureManager().loadTexture(mBackgroundTexture);			
		mBackgroundSprite = new Sprite(CAMERA_WIDTH-mBackgroundTextureRegion.getWidth(),
				CAMERA_HEIGHT-mBackgroundTextureRegion.getHeight(),mBackgroundTextureRegion);		

		// Load the textures and create the help screen's background sprite.
		Texture mHelpScreenTexture = new Texture(1024,1024,TextureOptions.BILINEAR_PREMULTIPLYALPHA);
		TextureRegion mHelpScreenTextureRegion = TextureRegionFactory.createFromAsset(mHelpScreenTexture, this, "gfx/helpScreen.png", 0, 0);
		mEngine.getTextureManager().loadTexture(mHelpScreenTexture);
		mHelpScreenSprite = new Sprite(CAMERA_WIDTH-mHelpScreenTextureRegion.getWidth(),
				CAMERA_WIDTH-mHelpScreenTextureRegion.getWidth(),mHelpScreenTextureRegion);

		// Load the textures and create the high score screen's background sprite.
		Texture mHighScoreTexture = new Texture(1024,1024,TextureOptions.BILINEAR_PREMULTIPLYALPHA);
		TextureRegion mHighScoreTextureRegion = TextureRegionFactory.createFromAsset(mHighScoreTexture, this, "gfx/highScore.png", 0, 0);
		mEngine.getTextureManager().loadTexture(mHighScoreTexture);		
		mHighScoreSceneSprite = new Sprite(CAMERA_WIDTH-mHighScoreTextureRegion.getWidth(),
				CAMERA_WIDTH-mHighScoreTextureRegion.getWidth(),mHighScoreTextureRegion);

		// Load the textures and create the back button sprite that is used to go back to the main menu. 
		Texture mBackButtonTexture = new Texture(1024,1024,TextureOptions.BILINEAR_PREMULTIPLYALPHA);
		TextureRegion mBackButtonTextureRegion = TextureRegionFactory.createFromAsset(mBackButtonTexture, this, "gfx/backButton.png", 0,0);
		mEngine.getTextureManager().loadTexture(mBackButtonTexture);	
		// When the back button is touched, send the user back to the main manu.
		mBackButtonSprite = new Sprite(BACK_BUTTON_X,CAMERA_HEIGHT-90,mBackButtonTextureRegion){			
			@Override
			public boolean onAreaTouched(TouchEvent pSceneTouchEvent,
					float pTouchAreaLocalX, float pTouchAreaLocalY) {
					mMainScene.setChildScene(mMainMenuScene);
				
				return super
						.onAreaTouched(pSceneTouchEvent, pTouchAreaLocalX, pTouchAreaLocalY);
			} // End of onAreaTouched method
		}; // End of Sprite anonymous inner class
	} // End of onLoadResources method
	
	@Override
	public Scene onLoadScene() {
		this.mEngine.registerUpdateHandler(new FPSLogger());
		// Create the screens to be shown to the user.
		createMainMenuScreen();
		createHelpScreen();
		createHighScoreScreen();
		
		// Create the main scene and set the main menu to be the default scene
		// Then register a touch area of the main scene
		mMainScene = new Scene(1);
		mMainScene.setChildScene(mMainMenuScene);	
		mMainScene.registerTouchArea(mBackButtonSprite);			
		return this.mMainScene;
	} // End of onLoadScene method

	/**
	 * Create the Main menu's screen. Put all the menu items in the right position.
	 */
	private void createMainMenuScreen() {		
		mMainMenuScene = new MenuScene(this.mCamera);
		// Add the background sprite.
		mMainMenuScene.getLastChild().attachChild(mBackgroundSprite);
		
		// Create and add the menus.
		final IMenuItem startMenuItem = new ColorMenuItemDecorator(
				new TextMenuItem(MENU_START, mFont, "Start"), 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f);
		startMenuItem.setBlendFunction(GL10.GL_SRC_ALPHA, GL10.GL_ONE_MINUS_SRC_ALPHA);
		mMainMenuScene.addMenuItem(startMenuItem);
		
		final IMenuItem helpMenuItem = new ColorMenuItemDecorator(
				new TextMenuItem(MENU_HELP, mFont, "Help"), 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f);
		helpMenuItem.setBlendFunction(GL10.GL_SRC_ALPHA, GL10.GL_ONE_MINUS_SRC_ALPHA);
		mMainMenuScene.addMenuItem(helpMenuItem);
		
		final IMenuItem highScoreMenuItem = new ColorMenuItemDecorator(
				new TextMenuItem(MENU_HIGHSCORES, mFont, "High Scores"), 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f);
		highScoreMenuItem.setBlendFunction(GL10.GL_SRC_ALPHA, GL10.GL_ONE_MINUS_SRC_ALPHA);
		mMainMenuScene.addMenuItem(highScoreMenuItem);
		
		final IMenuItem exitMenuItem = new ColorMenuItemDecorator(
				new TextMenuItem(MENU_EXIT, mFont, "Exit"), 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f);
		exitMenuItem.setBlendFunction(GL10.GL_SRC_ALPHA, GL10.GL_ONE_MINUS_SRC_ALPHA);
		mMainMenuScene.addMenuItem(exitMenuItem);		
		
		mMainMenuScene.buildAnimations();
		mMainMenuScene.setBackgroundEnabled(false);
		mMainMenuScene.setOnMenuItemClickListener(this);
		
		// Position the menus
		startMenuItem.setPosition(startMenuItem.getX(), (CAMERA_HEIGHT/2)-18);
		helpMenuItem.setPosition(helpMenuItem.getX(), startMenuItem.getY() + MENU_MARGIN);
		highScoreMenuItem.setPosition(highScoreMenuItem.getX(), helpMenuItem.getY() + MENU_MARGIN);
		exitMenuItem.setPosition(exitMenuItem.getX(), highScoreMenuItem.getY() + MENU_MARGIN);
	} // End of createMainMenuScreen method

	/**
	 * Create the help Screen.
	 */
	private void createHelpScreen() {
		mHelpScene = new Scene(1);
		// Add the background and the back button
		mHelpScene.getLastChild().attachChild(mHelpScreenSprite);
		mHelpScene.getLastChild().attachChild(mBackButtonSprite);
	} // End of createHelpScreen method
	
	/**
	 * Create the High score Screen.
	 */
	private void createHighScoreScreen() {
		mHighScoreScene = new HighScoreScene(1);
	} // End of createHighScoreScreen method
	
	@Override
	public void onLoadComplete() {}

	@Override
	public void onBackPressed() {}
	
	@Override
	public boolean onMenuItemClicked(MenuScene pMenuScene, IMenuItem pMenuItem,
			float pMenuItemLocalX, float pMenuItemLocalY) {
		// Check which menu item is selected and perform actions based on that selection.
		switch (pMenuItem.getID()) {
		case MENU_START:
			// Leave and close the current activity and remove all the items in the main scene that may be rebuilt when back
			// To this activity.
			Intent myIntent = new Intent(ControlScreenActivity.this,MainGameScreenActivity.class);
			ControlScreenActivity.this.startActivity(myIntent);
			mMainScene.clearChildScene();
			mMainScene.clearTouchAreas();
			mMainScene.clearUpdateHandlers();
			mMainScene.clearEntityModifiers();
			finish();
			return true;
		case MENU_HELP:
			// Set the childScene to Help
			this.mMainScene.setChildScene(mHelpScene);
			return true;
		case MENU_HIGHSCORES:
			// Update the texts in the High Score and set it as the main's child scene
			mHighScoreScene.updateTexts();
			this.mMainScene.setChildScene(mHighScoreScene);
			return true;
		case MENU_EXIT:
			// Close this activity
			this.finish();
			return true;
		default:
			return false;
		} // End of switch
	} // End of createHighScoreScreen method
	
	/** 
	 * @author Aldrin Jerome Almacin
	 *         <p>
	 *         <b>Date: </b>December 8, 2012
	 *         </p>
	 *         <p>
	 *         <b>Description: </b>HighScoreScene is a scene that inserts the player names and scores into the screen
	 *         via ChangeableText objects. All the data taken were from the database.
	 *         </p>
	 * 
	 */
	private class HighScoreScene extends Scene
	{
		// Empty string used by the changeable texts. In order to cheat the text's max length, we gave an enormous amount of blank spaces.
		private static final String EMPTY_CHANGEABLE_INITIAL = "" +
				"                                                             " +
				"                                                             ";

		// The corresponding positions of the player names and scores.
		private static final int PLAYER_NAME_X = 70;
		private static final int PLAYER_NAME_Y = 200;
		private static final int HIGH_SCORE_X = 300;
		
		// The controller that is used to take data from the database.
		private Controller mController;
		
		// Changeable texts used to display data
		private ChangeableText mPlayerNames;
		private ChangeableText mPlayerScores;
		
		/**
		 * 
		 * @param pLayerCount the number of players
		 */
		public HighScoreScene(int pLayerCount) {
			super(pLayerCount);
			// Create an instance of the objects needed.
			mPlayerNames = new ChangeableText(PLAYER_NAME_X, PLAYER_NAME_Y, mHighScoreFont, EMPTY_CHANGEABLE_INITIAL);
			mPlayerScores = new ChangeableText(HIGH_SCORE_X, PLAYER_NAME_Y, mHighScoreFont,EMPTY_CHANGEABLE_INITIAL);
			mController = new Controller(mContext);
			
			// Add the sprites that needed to be shown in the screen
			this.getLastChild().attachChild(mHighScoreSceneSprite);
			this.getLastChild().attachChild(mBackButtonSprite);
			this.getLastChild().attachChild(mPlayerNames);
			this.getLastChild().attachChild(mPlayerScores);
			
			// Update the texts in the ChangeableTexts
			updateTexts();
		} // End of Constructor

		/**
		 * Updates the texts in the player and score ChangeableText objects.
		 * All the data were taken from the database.
		 */
		private void updateTexts() {
			// Take the top 10 scores in the database and store it in the top 10 players arraylist
			ArrayList<ArrayList<String>> top10Players = mController.getTop10Scores();
			if(top10Players == null)
				// If the top 10 players is empty, then tell the user that there are no scores to display yet.
				mPlayerNames.setText("No scores to \ndisplay yet.");
			else
			{
				// The players and scores String stores all the player and scores.
				String players = "Name\n";
				String scores = "Score\n";
				
				// Each player and score is concatenated in the players and scores String.
				for(int i = 0;i<top10Players.size();i++){
					players += (top10Players.get(i).get(0)+"\n");
					scores += (top10Players.get(i).get(1)+"\n");
				} // End of for
				mPlayerNames.setText(players);
				mPlayerScores.setText(scores);
			} // End of if-else
		} // End of updateTexts method
	} // End of HighScoreScene method	
} // End of ControlScreenActivity class
