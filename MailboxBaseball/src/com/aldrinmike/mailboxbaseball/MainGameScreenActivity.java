package com.aldrinmike.mailboxbaseball;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.Locale;
import java.util.Timer;
import java.util.TimerTask;

import javax.microedition.khronos.opengles.GL10;

import org.anddev.andengine.engine.Engine;
import org.anddev.andengine.engine.camera.Camera;
import org.anddev.andengine.engine.handler.IUpdateHandler;
import org.anddev.andengine.engine.options.EngineOptions;
import org.anddev.andengine.engine.options.EngineOptions.ScreenOrientation;
import org.anddev.andengine.engine.options.resolutionpolicy.RatioResolutionPolicy;
import org.anddev.andengine.entity.modifier.MoveXModifier;
import org.anddev.andengine.entity.modifier.MoveYModifier;
import org.anddev.andengine.entity.modifier.RotationModifier;
import org.anddev.andengine.entity.scene.Scene;
import org.anddev.andengine.entity.scene.menu.MenuScene;
import org.anddev.andengine.entity.scene.menu.MenuScene.IOnMenuItemClickListener;
import org.anddev.andengine.entity.scene.menu.item.IMenuItem;
import org.anddev.andengine.entity.scene.menu.item.TextMenuItem;
import org.anddev.andengine.entity.scene.menu.item.decorator.ColorMenuItemDecorator;
import org.anddev.andengine.entity.sprite.AnimatedSprite;
import org.anddev.andengine.entity.sprite.Sprite;
import org.anddev.andengine.entity.sprite.TiledSprite;
import org.anddev.andengine.entity.text.ChangeableText;
import org.anddev.andengine.input.touch.TouchEvent;
import org.anddev.andengine.opengl.font.Font;
import org.anddev.andengine.opengl.font.FontFactory;
import org.anddev.andengine.opengl.texture.Texture;
import org.anddev.andengine.opengl.texture.TextureOptions;
import org.anddev.andengine.opengl.texture.region.TextureRegion;
import org.anddev.andengine.opengl.texture.region.TextureRegionFactory;
import org.anddev.andengine.opengl.texture.region.TiledTextureRegion;
import org.anddev.andengine.ui.activity.BaseGameActivity;
import org.apache.http.util.LangUtils;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Handler;
import android.text.InputFilter;
import android.widget.EditText;

public class MainGameScreenActivity extends BaseGameActivity implements IOnMenuItemClickListener{

	private static final int ROAD_ANIMATION_SPEED = 300;
	// The camera width and height
	private final static int CAMERA_WIDTH = 480;
	private final static int CAMERA_HEIGHT = 800;
	
	// The cheat code used if the user wants infinite life.
	private static final String CHEAT_CODE = "SIAKOL";

	// Car positions.
	private static final int CAR_LEFT_POSITION = 50;
	private static final int CAR_RIGHT_POSITION = 200;	
	private static final int CAR_YPOSITION = CAMERA_HEIGHT-300;	
	
	// Truck positions.
	private static final int TRUCK_LEFT_POSITION = CAR_LEFT_POSITION + 50;
	private static final int TRUCK_RIGHT_POSITION = CAR_RIGHT_POSITION + 40;
	
	// Mailbox positions.
	private static final int MAILBOX_LEFT_POS = CAR_LEFT_POSITION - 30;
	private static final int MAILBOX_RIGHT_POS = CAR_RIGHT_POSITION + 200;
	
	// Menu item ids
	private static final int MENU_PLAY_AGAIN = 0;
	private static final int MENU_EXIT_TO_MAIN_MENU = 1;
	
	// The position of the game over score and the hit text
	private static final float SCORE_AND_HIT_TEXT_X = 150;
	private static final float SCORE_AND_HIT_TEXT_Y = 200;
	
	// The number of mailbox in the screen
	private static final int MAILBOX_COUNT = 4;
	
	// A reference to the game camera.
	private Camera mCamera;

	// Font used by the end score and game over menu.
	private Font mFont;
	
	// Font used when keeping track of scores.
	private Font mScoreFont;
	
	// The font used by the text that tells the user when an item is hit.
	private Font mHitTextFont;
	
	// The parent scene that holds the game and game over scenes
	private Scene mMainScene;
	
	// The game scene and the game over scene
	private Scene mGameScene;
	private MenuScene mGameOverMenuScene;
	
	// The road in which the car and truck drives in
	private AnimatedSprite mRoadTiledSprite;	
	
	// The list of mailboxes and golden mailbox
	private ArrayList<MailBox> mMailBoxSprites;
	private GoldenMailbox mGoldenMailbox;

	// The car sprites in the game. One that is not destroyed which has the players and
	// one that is smashed up after hitting the truck
	private TiledSprite mCarTiledSprite;
	private AnimatedSprite mCarCrashedSprite;

	// The truck sprites in the game. One that is not destroyed and
	// one that is on fire
	private Sprite mTruckSprite;
	private AnimatedSprite mTruckCrashedSprite;
	
	// The Sprite that shows the box that contains the scores and menu items
	private Sprite mGameOverMenuSprite;
	
	// Timer and timer task used to count how many seconds elapsed since the game started.
	private Timer mTruckTimer;
	private TimerTask mTruckTimerTask;	
	
	// The handler that calls the Runnables that are to be called when needed
	private Handler mHandler;
	
	// The controller that accesses data from the database.
	private Controller mController;

	// The score and strike counts
	private int mScoreCount;
	private int mStrikeCount;
	
	// The position of the truck in the road
	private int mTruckPosition;
	
	// The speed of the truck and the mailbox
	private float mTruckSpeed;
	private float mMailboxSpeed;
	
	// States whether the truck/car is on the right
	private boolean mTruckInRight;
	private boolean mCarInRight;

	// States whether the player cheated
	private boolean mCheated;
	
	// EditText that is in the AlertDialog that is used to get the player's name.
	private EditText mHighScorerNameEditText;

	// ChangeableText that shows the user the current count of strike and score
	private ChangeableText mScoreKeeper;
	private ChangeableText mStrikeKeeper;
	
	// ChangeableText that shows the score when the game is over.
	private ChangeableText mGameOverScoreText;

	// ChangeableText that shows the user that the mail box is hit.
	private ChangeableText mHitText;
	
	// Alert dialog that asks for the player's name after .
	private AlertDialog mSaveHighScoreAlertDialog;

	@Override
	public Engine onLoadEngine() {
		this.mCamera = new Camera(0, 0, CAMERA_WIDTH, CAMERA_HEIGHT);
		return new Engine(new EngineOptions(true, ScreenOrientation.PORTRAIT, 
				new RatioResolutionPolicy(CAMERA_WIDTH,CAMERA_HEIGHT), this.mCamera));
	} // End of onLoadEngine method

	@Override
	public void onLoadResources() {
		// Load the fonts that the activity will need.
		FontFactory.setAssetBasePath("font/");
		
		Texture mFontTexture = new Texture(256, 256,TextureOptions.BILINEAR_PREMULTIPLYALPHA);
		mFont = FontFactory.createFromAsset(mFontTexture, this, "kulminoituva.ttf", 38, true, Color.BLACK);
		mEngine.getTextureManager().loadTexture(mFontTexture);
		mEngine.getFontManager().loadFont(mFont);
		
		Texture mScoreFontTexture = new Texture(256, 256,TextureOptions.BILINEAR_PREMULTIPLYALPHA);
		mScoreFont = FontFactory.createFromAsset(mScoreFontTexture, this, "FLORLRG_.ttf", 20, true, Color.BLACK);
		mEngine.getTextureManager().loadTexture(mScoreFontTexture);
		mEngine.getFontManager().loadFont(mScoreFont);

		Texture mHitTextFontTexture = new Texture(256, 256,TextureOptions.BILINEAR_PREMULTIPLYALPHA);
		mHitTextFont = FontFactory.createFromAsset(mHitTextFontTexture, this, "Magenta_BBT.ttf", 68, true, Color.YELLOW);
		mEngine.getTextureManager().loadTexture(mHitTextFontTexture);
		mEngine.getFontManager().loadFont(mHitTextFont);
		
		// Load the road texture
		Texture mRoadTiledTexture = new Texture(2048, 2048, TextureOptions.BILINEAR_PREMULTIPLYALPHA);
		TiledTextureRegion mRoadTiledTextureRegion = TextureRegionFactory.createTiledFromAsset(mRoadTiledTexture, this, "gfx/gameScreen.png", 
				0, 0, 3, 1);
		mEngine.getTextureManager().loadTexture(mRoadTiledTexture);
		
		// Create an anonymous inner class of AnimatedSprite
		mRoadTiledSprite = new AnimatedSprite(0, 0, mRoadTiledTextureRegion) {	
			// The index in the tile in which the batter will hit.	
			private static final int LEFT_BATTER_HIT = 1;		
			private static final int RIGHT_BATTER_HIT = 2;			
			
			// The index of the image in normal.
			private static final int NORMAL_IMAGE_INDEX = 0;
			
			// The time it takes before the car is back to normal.
			private static final int MILLIS_TO_NORMAL = 100;
			
			@Override
			public boolean onAreaTouched(TouchEvent pSceneTouchEvent,
					float pTouchAreaLocalX, float pTouchAreaLocalY) {
				if(pTouchAreaLocalX <= CAMERA_WIDTH/2)
				{
					// If the user touched on the left part of the screen then
					// Move the car to the left if the car is on the right.
					// If it is already on the left, make the left batter hit the bat.
					if(mCarInRight)
						mCarTiledSprite.setPosition(CAR_LEFT_POSITION, mCarTiledSprite.getY());
					else
						hitBat(LEFT_BATTER_HIT);
					// Car is now not in right so set the state to false.
					mCarInRight = false;
				}
				else
				{
					// If the user touched on the right part of the screen then
					// Move the car to the right if the car is on the left.
					// If it is already on the right, make the right batter hit the bat.
					if(!mCarInRight)
						mCarTiledSprite.setPosition(CAR_RIGHT_POSITION, mCarTiledSprite.getY());
					else
						hitBat(RIGHT_BATTER_HIT);
					// Car is now not in left so set the state to true.
					mCarInRight = true;
				} // End of if-else
				return super
						.onAreaTouched(pSceneTouchEvent, pTouchAreaLocalX, pTouchAreaLocalY);
			} // End of onAreaTouched method
			
			/**
			 * Makes the batter hit the bat then sets the image back to normal after some time.
			 * 
			 * @param tiledImageIndex The tile index of the tiled image to be shown.
			 */
			private void hitBat(int tiledImageIndex) {
				Runnable carBackToNormalRunnable = new Runnable() {
					@Override
					public void run() {
						mCarTiledSprite.setCurrentTileIndex(NORMAL_IMAGE_INDEX);
					} // End of run method
				}; // End of carBackToNormalRunnable anonymous inner class
				
				// Set the current tile to the passed index.
				mCarTiledSprite.setCurrentTileIndex(tiledImageIndex);
				
				// Check each mailbox if any of them got hit.
				for(int i=0;i<MAILBOX_COUNT;i++)
					mMailBoxSprites.get(i).checkGotHit();
				
				// Check if the golden mailbox got hit.
				mGoldenMailbox.checkGotHit();
				
				// Set the text and strike count to the current count.
				mScoreKeeper.setText("Score: "+mScoreCount);
				mStrikeKeeper.setText("Strikes: "+mStrikeCount);
				
				// Put the car back to normal after the delay
				mHandler.postDelayed(carBackToNormalRunnable, MILLIS_TO_NORMAL);
			} // End of hitBat method
		}; // End of AnimatedSprite anonymous inner class
		
		// Animate the road so that it looks like the car is travelling.
		mRoadTiledSprite.animate(ROAD_ANIMATION_SPEED);
		
		// Create the mailboxes and add them to the mailbox arraylist.
		mMailBoxSprites = new ArrayList<MailBox>();
		for(int i = 0; i<MAILBOX_COUNT;i++){			
			Texture mMailBoxTiledTexture = new Texture(1024, 1024, TextureOptions.BILINEAR_PREMULTIPLYALPHA);
			TiledTextureRegion mMailBoxTiledTextureRegion = TextureRegionFactory.createTiledFromAsset(mMailBoxTiledTexture, this, "gfx/mailbox.png", 
					0, 0, 8, 1);
			mEngine.getTextureManager().loadTexture(mMailBoxTiledTexture);
			mMailBoxSprites.add(new MailBox(mMailBoxTiledTextureRegion));
		} // End of for
		
		// Load and create the golden mailbox
		Texture mGoldenMailBoxTiledTexture = new Texture(1024, 1024, TextureOptions.BILINEAR_PREMULTIPLYALPHA);
		TiledTextureRegion mGoldenMailBoxTiledTextureRegion = TextureRegionFactory.createTiledFromAsset(mGoldenMailBoxTiledTexture, this, "gfx/goldenMailbox.png", 
				0, 0, 2, 1);
		mEngine.getTextureManager().loadTexture(mGoldenMailBoxTiledTexture);
		mGoldenMailbox = new GoldenMailbox(mGoldenMailBoxTiledTextureRegion);

		Texture mGameOverMenuTexture = new Texture(1024,1024,TextureOptions.BILINEAR_PREMULTIPLYALPHA);
		TextureRegion mGameOverMenuTextureRegion = TextureRegionFactory.createFromAsset(mGameOverMenuTexture, this, "gfx/gameOverMenu.png", 0, 0);
		mEngine.getTextureManager().loadTexture(mGameOverMenuTexture);	

		mGameOverMenuSprite = new Sprite(CAMERA_WIDTH-mGameOverMenuTextureRegion.getWidth(),
				CAMERA_HEIGHT-mGameOverMenuTextureRegion.getHeight(),mGameOverMenuTextureRegion);
		
		// Load the textures and create the car sprites. Both crashed and normal
		Texture mCarTiledTexture = new Texture(1024, 1024, TextureOptions.BILINEAR_PREMULTIPLYALPHA);
		TiledTextureRegion mCarTiledTextureRegion = TextureRegionFactory.createTiledFromAsset(mCarTiledTexture, this, "gfx/carTile.png", 
				0, 0, 3, 1);
		mEngine.getTextureManager().loadTexture(mCarTiledTexture);		
		mCarTiledSprite = new TiledSprite(CAR_RIGHT_POSITION, CAR_YPOSITION, mCarTiledTextureRegion);

		Texture mCarCrashedTexture = new Texture(512,512,TextureOptions.BILINEAR_PREMULTIPLYALPHA);
		TiledTextureRegion mCarCrashedTextureRegion = TextureRegionFactory.createTiledFromAsset(mCarCrashedTexture, this, "gfx/carCrashed.png", 
				0, 0, 2, 1);
		mEngine.getTextureManager().loadTexture(mCarCrashedTexture);		
		mCarCrashedSprite = new AnimatedSprite(0, 0, mCarCrashedTextureRegion);
		mCarCrashedSprite.setPosition(0, -mCarCrashedSprite.getHeight());

		// Load the textures and create the truck sprites. Both crashed and normal
		Texture mTruckTexture = new Texture(1024,1024,TextureOptions.BILINEAR_PREMULTIPLYALPHA);
		TextureRegion mTruckTextureRegion = TextureRegionFactory.createFromAsset(mTruckTexture, this, "gfx/truck.png", 0,0);
		mEngine.getTextureManager().loadTexture(mTruckTexture);		
		mTruckSprite = new Sprite(-CAMERA_WIDTH,-CAMERA_HEIGHT,mTruckTextureRegion);	

		Texture mTruckCrashedTexture = new Texture(1024,1024,TextureOptions.BILINEAR_PREMULTIPLYALPHA);
		TiledTextureRegion mTruckCrashedTextureRegion = TextureRegionFactory.createTiledFromAsset(mTruckCrashedTexture, this, "gfx/truckCrashed.png", 
				0, 0, 2, 1);
		mEngine.getTextureManager().loadTexture(mTruckCrashedTexture);		
		mTruckCrashedSprite = new AnimatedSprite(0, 0, mTruckCrashedTextureRegion);		
		mTruckCrashedSprite.setPosition(0,-mTruckCrashedSprite.getHeight());	
		
		// Create and initialize all the Changeable texts to their default values.
		mScoreKeeper = new ChangeableText(5, 5, mScoreFont, "Score: 0     ");
		mStrikeKeeper = new ChangeableText(mScoreKeeper.getX(), mScoreKeeper.getY()+mScoreKeeper.getHeight()+15, mScoreFont, "Strikes: 0     ");		
		mGameOverScoreText = new ChangeableText(SCORE_AND_HIT_TEXT_X, SCORE_AND_HIT_TEXT_Y, mFont, "Score: NONENONENONE");		
		mHitText = new ChangeableText(SCORE_AND_HIT_TEXT_X, SCORE_AND_HIT_TEXT_Y, mHitTextFont, "12345678");
	} // End of onLoadResources

	@Override
	public Scene onLoadScene() {
		createGameScene();
		mMainScene = new Scene(1);
		mMainScene.setChildScene(mGameScene);	
		
		return mMainScene;
	}

	@Override
	public void onLoadComplete() {
	}


	
	@Override
	public void onBackPressed() {}
	
	private void dropTheTruck() {
		mTruckSprite.registerEntityModifier(new MoveYModifier(mTruckSpeed, -mTruckSprite.getHeight(), CAMERA_HEIGHT));
	}

	private void stopTheWorld() {
		mRoadTiledSprite.stopAnimation();
		mTruckTimer.cancel();
		Iterator<MailBox> it = mMailBoxSprites.iterator();
		while(it.hasNext()){
			it.next().clearEntityModifiers();
		}
		mTruckSprite.clearEntityModifiers();
	}
	
	@Override
	public boolean onMenuItemClicked(MenuScene pMenuScene, IMenuItem pMenuItem,
			float pMenuItemLocalX, float pMenuItemLocalY) {
		switch (pMenuItem.getID()) {
		case MENU_PLAY_AGAIN:
			mMainScene.setChildScene(mGameScene);
			resetGame();
			return true;
		case MENU_EXIT_TO_MAIN_MENU:
			Intent myIntent = new Intent(MainGameScreenActivity.this,ControlScreenActivity.class);
			MainGameScreenActivity.this.startActivity(myIntent);
			mMainScene.clearChildScene();
			mMainScene.clearTouchAreas();
			mMainScene.clearUpdateHandlers();
			mMainScene.clearEntityModifiers();
			finish();
			return true;
		default:
			return false;
		}
	}
	
	private void createGameScene()
	{
		initializeGameVals();
		mHandler = new Handler();
		mHighScorerNameEditText = new EditText(this);
		mHighScorerNameEditText.setHint("0 - 10 characters.");
		mHighScorerNameEditText.setFilters(new InputFilter[] { new InputFilter.LengthFilter(10)});
		
		mHitText.setPosition((CAMERA_WIDTH/2)-(mHitText.getWidth()/2), 100);
		
		mController = new Controller(this);
		createAlertDialogs();
		createGameOverMenuScene();
		
		if(mGameScene == null){
			mGameScene = new Scene(1);
			mGameScene.getLastChild().attachChild(mRoadTiledSprite);
			for(int i=0;i<MAILBOX_COUNT;i++)
				mGameScene.getLastChild().attachChild(mMailBoxSprites.get(i));
			mGameScene.getLastChild().attachChild(mGoldenMailbox);
			mGameScene.getLastChild().attachChild(mCarTiledSprite);
			mGameScene.getLastChild().attachChild(mTruckSprite);
			mGameScene.getLastChild().attachChild(mTruckCrashedSprite);
			mGameScene.getLastChild().attachChild(mCarCrashedSprite);
			mGameScene.getLastChild().attachChild(mScoreKeeper);
			mGameScene.getLastChild().attachChild(mStrikeKeeper);
			mGameScene.getLastChild().attachChild(mHitText);			
			mGameScene.registerTouchArea(mRoadTiledSprite);
			
			buildTimer();
		}
		
		mGameScene.registerUpdateHandler(new IUpdateHandler() {

			@Override
			public void reset() {}
			
			@Override
			public void onUpdate(float pSecondsElapsed) {
				Runnable showScoreRunnable = new Runnable() {

					@Override
					public void run() {
						mGameOverScoreText.setText("Score: "+mScoreCount);
						if(mController.scoreBelongsToTop10(mScoreCount) && mScoreCount > 0){
							mSaveHighScoreAlertDialog.setMessage(getDialogMessage());
							mSaveHighScoreAlertDialog.show();
						}
						else
						{
							mMainScene.setChildScene(mGameOverMenuScene);
						}
					}

					private CharSequence getDialogMessage() {
						return "Congrats, your score made it to the top 10." +
								"\n Score: "+mScoreCount +
								"\n Enter your name:";
					}
				};
				if(mStrikeCount == 3)
				{
					stopTheWorld();
					mHandler.post(showScoreRunnable);
				}
				if(mTruckSprite.collidesWith(mCarTiledSprite) && mTruckSprite.getY() < CAMERA_HEIGHT-10){
					if(mCarInRight == mTruckInRight)
					{
						stopTheWorld();
						mTruckSprite.setPosition(-CAMERA_WIDTH, mTruckSprite.getY());
						mTruckCrashedSprite.setPosition(mTruckPosition, mTruckSprite.getY());
						mTruckCrashedSprite.animate(300);
						mCarTiledSprite.setPosition(0, -mCarTiledSprite.getHeight());
						mCarCrashedSprite.setPosition(mTruckPosition, CAR_YPOSITION);
						mCarCrashedSprite.animate(300);
						
						float xCarOrigin = mCarCrashedSprite.getX();

						float degCounter = 0;
						float xPosCounter = 0;
						while(mTruckCrashedSprite.collidesWith(mCarCrashedSprite))
						{
							if(mTruckPosition == TRUCK_LEFT_POSITION)
							{
								xPosCounter+=0.1f;
								degCounter--;
							}
							else
							{
								xPosCounter-=0.1f;
								degCounter++;
							}
							mCarCrashedSprite.setRotation(degCounter);
							mTruckCrashedSprite.setRotation(degCounter/3);
							mCarCrashedSprite.setPosition(mCarCrashedSprite.getX()+xPosCounter, mCarCrashedSprite.getY());
						}
						mTruckCrashedSprite.registerEntityModifier(new RotationModifier(3, 0, mTruckCrashedSprite.getRotation()+degCounter/3));
						mCarCrashedSprite.registerEntityModifier(new RotationModifier(3, 0, mCarCrashedSprite.getRotation()+degCounter));
						mCarCrashedSprite.registerEntityModifier(new MoveXModifier(3, xCarOrigin, mCarCrashedSprite.getX()+xPosCounter));
						mHandler.postDelayed(showScoreRunnable,1500);
					}
				}
			}
		});
	}
	private void initializeGameVals() {
		mCarInRight = true;
		// If the player did not use a cheat code, reset the score.
		if(!mCheated)
		{
			mScoreCount = 0;
			mScoreKeeper.setText("Score: "+mScoreCount);
		}
		mTruckSpeed = 5;
		mMailboxSpeed = 4;
		mStrikeCount = 0;
		mStrikeKeeper.setText("Strikes: "+mStrikeCount);
		mHitText.setVisible(false);
	}

	private void resetGame() {
		initializeGameVals();
		mCarTiledSprite.clearEntityModifiers();
		mCarCrashedSprite.clearEntityModifiers();
		mTruckSprite.clearEntityModifiers();
		mTruckCrashedSprite.clearEntityModifiers();
		mCarTiledSprite.setInitialPosition();
		mTruckSprite.setInitialPosition();

		Iterator<MailBox> it = mMailBoxSprites.iterator();
		for(int i=0;i<MAILBOX_COUNT;i++)
		{
			MailBox tempMailbox = it.next();
			tempMailbox.setPosition(0,-(tempMailbox.getHeight()+100));
		}
			
		mTruckCrashedSprite.setPosition(0,-(mTruckCrashedSprite.getHeight()+100));
		mCarCrashedSprite.setPosition(0, -(mCarCrashedSprite.getHeight()+100));

		mRoadTiledSprite.animate(300);
		
		buildTimer();
	}

	private void buildTimer() {		
		mTruckTimer = new Timer();
		mTruckTimerTask = new TimerTask() {			
			private int timeElapsed;
			@Override
			public void run() {
				timeElapsed++;

				if(timeElapsed % 60 == 0 && mTruckSpeed >= 0.5f && mMailboxSpeed >= 0.5f)
				{
					mTruckSpeed -= 0.5f;
					mMailboxSpeed -= 0.5f;
				}
				
				if(timeElapsed % 20 == 0){
					if(Math.random()>0.4)
					{
						mGoldenMailbox.setStatesThenDrop(mMailboxSpeed, 0);
					}
				}
				
				if(timeElapsed % (mTruckSpeed+1) == 0)
				{
					boolean carLeft = (Math.random()>0.4f);
					mTruckPosition = (carLeft)?TRUCK_LEFT_POSITION:TRUCK_RIGHT_POSITION;
					mTruckInRight = (carLeft)?false:true;
					mTruckSprite.setPosition(mTruckPosition, -CAMERA_HEIGHT);
					dropTheTruck();
				}
				if(timeElapsed % (mMailboxSpeed+(MAILBOX_COUNT-1)) == 0)
				{
					for(int i=0;i<MAILBOX_COUNT;i++){
						mMailBoxSprites.get(i).setStatesThenDrop(mMailboxSpeed+i,i*500);
					}
				}
			}
			
		};
		mTruckTimer.schedule(mTruckTimerTask, (long)0, (long)1000);
	}


	private void createAlertDialogs() {
		// Get the AlertDialog Builder class and save it in a variable.
		AlertDialog.Builder saveAlertDialogBuilder = new AlertDialog.Builder(this);
		saveAlertDialogBuilder.setTitle("High Score:");
		saveAlertDialogBuilder.setView(mHighScorerNameEditText);
		saveAlertDialogBuilder.setPositiveButton("Save", saveToDbAlertDialogClickListener);
		saveAlertDialogBuilder.setCancelable(false);
		
		mSaveHighScoreAlertDialog = saveAlertDialogBuilder.create();
	}	
	
	
	
	/**
	 * Anonymous class used by the AlertDialog when the OK button is clicked
	 */
	private AlertDialog.OnClickListener saveToDbAlertDialogClickListener = new AlertDialog.OnClickListener() 
	{

		// When the button is clicked, call the reset method and resume the thread.
		public void onClick(DialogInterface dialog, int which) 
		{
			String mPlayerName = (mHighScorerNameEditText.getText().length() == 0)?"Player 1":mHighScorerNameEditText.getText().toString();
			// If the user did not enter the cheat code, then save the name of the user and show the end screen.
			if(!mPlayerName.toUpperCase().equals(CHEAT_CODE.toUpperCase()))
			{
				mCheated = false;
				mController.savePlayerAndScore(mPlayerName,mScoreCount);
				mMainScene.setChildScene(mGameOverMenuScene);
			}
			else
			{
				// If the user entered the cheat code, then start the game again with the score not resetted
				mCheated = true;
				mMainScene.setChildScene(mGameScene);
				resetGame();
			}
		} // End of onClick method
	}; // End of alertDialogResetOnClickListener anonymous inner class

	private void createGameOverMenuScene() {
		this.mGameOverMenuScene = new MenuScene(this.mCamera);		
		mGameOverMenuScene.getLastChild().attachChild(mGameOverMenuSprite);
		mGameOverMenuScene.getLastChild().attachChild(mGameOverScoreText);
		final IMenuItem playAgainMenuItem = new ColorMenuItemDecorator(
				new TextMenuItem(MENU_PLAY_AGAIN, mFont, "Play again"), 0.5f, 0.5f, 0.5f, 1.0f, 1.0f, 1.0f);
		playAgainMenuItem.setBlendFunction(GL10.GL_SRC_ALPHA, GL10.GL_ONE_MINUS_SRC_ALPHA);
		this.mGameOverMenuScene.addMenuItem(playAgainMenuItem);
	
		final IMenuItem exitToMainMenuItem = new ColorMenuItemDecorator(
				new TextMenuItem(MENU_EXIT_TO_MAIN_MENU, mFont, "Exit"), 0.5f, 0.5f, 0.5f, 1.0f, 1.0f, 1.0f);
		exitToMainMenuItem.setBlendFunction(GL10.GL_SRC_ALPHA, GL10.GL_ONE_MINUS_SRC_ALPHA);
		this.mGameOverMenuScene.addMenuItem(exitToMainMenuItem);	
	
		this.mGameOverMenuScene.buildAnimations();
		this.mGameOverMenuScene.setBackgroundEnabled(false);
		this.mGameOverMenuScene.setOnMenuItemClickListener(this);
		this.mGameOverMenuScene.setPosition(mGameOverMenuScene.getInitialX(), mGameOverMenuScene.getInitialY() + 70);
	}

	private class MailBox extends TiledSprite
	{		
		protected static final float MAILBOX_HIT_AREA_MIN = CAR_YPOSITION+50;
		protected static final float MAILBOX_HIT_AREA_MAX = CAMERA_HEIGHT-100;
		private boolean isEmpty;
		private boolean isHit;
		private Handler mHandler;

		public MailBox(TiledTextureRegion pTiledTextureRegion) {
			super(-pTiledTextureRegion.getWidth(), -pTiledTextureRegion.getHeight(), pTiledTextureRegion);
			this.mHandler = new Handler();
		}
		
		public void checkGotHit() {
			if(mCarTiledSprite.collidesWith(this) && !this.isHit() && 
					(this.getY() > MAILBOX_HIT_AREA_MIN) && (this.getY() < MAILBOX_HIT_AREA_MAX))
			{
				if(this.isEmpty)
				{
					mScoreCount++;
					this.setCurrentTileIndex((this.getX()==MAILBOX_LEFT_POS)?6:7);
					mHitText.setText("SWEEEET!");
				}
				else
				{
					mStrikeCount++;
					this.setCurrentTileIndex((this.getX()==MAILBOX_LEFT_POS)?4:5);
					mHitText.setText("STRIKE!!");
				}
				this.setHit(true);
				mHitText.setVisible(true);
				mHandler.postDelayed(hideTheHitText, 1000);
			}
		}
		protected Runnable hideTheHitText = new Runnable() {
			
			@Override
			public void run() {
				mHitText.setVisible(false);
			}
		};
		public void setStatesThenDrop(float mailBoxSpeed,float yPos)
		{
			boolean isOnLeft = (Math.random()>0.4)?true:false;
			setHit(false);
			setEmpty((Math.random()>0.4)?true:false);
			this.setPosition((isOnLeft)?MAILBOX_LEFT_POS:MAILBOX_RIGHT_POS, -this.getHeight());
			if(isOnLeft && isEmpty())
				this.setCurrentTileIndex(1);
			else if(isOnLeft && !isEmpty())
				this.setCurrentTileIndex(0);
			else if(!isOnLeft && !isEmpty())
				this.setCurrentTileIndex(2);
			else if(!isOnLeft && isEmpty())
				this.setCurrentTileIndex(3);
			
			drop(mailBoxSpeed,yPos);
		}
		
		protected void drop(float mailBoxSpeed,float yPos) {
			this.registerEntityModifier(new MoveYModifier(mailBoxSpeed, -(this.getHeight()+yPos), CAMERA_HEIGHT));
		}

		public boolean isEmpty() {
			return isEmpty;
		}

		public void setEmpty(boolean isEmpty) {
			this.isEmpty = isEmpty;
		}

		public boolean isHit() {
			return isHit;
		}

		public void setHit(boolean isHit) {
			this.isHit = isHit;
		}
		
	}
	
	private class GoldenMailbox extends MailBox
	{

		public GoldenMailbox(TiledTextureRegion pTiledTextureRegion) {
			super(pTiledTextureRegion);}

		@Override
		public void checkGotHit() {
			if(mCarTiledSprite.collidesWith(this) && !this.isHit() && 
					(this.getY() > MAILBOX_HIT_AREA_MIN) && (this.getY() < MAILBOX_HIT_AREA_MAX))
			{
				mHitText.setText("YEAA!!!!");
				mScoreCount += 10;
				this.clearEntityModifiers();
				startHomeRunAnimation();
				mHitText.setVisible(true);
				mHandler.postDelayed(hideTheHitText, 1000);
			}
		}
		
		private void startHomeRunAnimation() {
			this.registerEntityModifier(new MoveXModifier(2, this.getX(), (getX()==MAILBOX_LEFT_POS)?-this.getWidth():CAMERA_WIDTH));
			this.registerEntityModifier(new MoveYModifier(2, this.getY(), -this.getHeight()));
			this.registerEntityModifier(new RotationModifier(4, this.getRotation(), 360));
		}

		@Override
		public void setStatesThenDrop(float mailBoxSpeed, float yPos) {
			boolean isOnLeft = (!mTruckInRight)?true:false;
			this.setPosition((isOnLeft)?MAILBOX_LEFT_POS:MAILBOX_RIGHT_POS, -this.getHeight());
			if(isOnLeft)
				this.setCurrentTileIndex(0);
			else
				this.setCurrentTileIndex(1);
			
			drop(mailBoxSpeed,yPos);
		}
		
	}
}
