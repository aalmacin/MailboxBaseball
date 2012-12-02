package com.aldrinmike.mailboxbaseball;

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
import org.anddev.andengine.entity.util.FPSLogger;
import org.anddev.andengine.input.touch.TouchEvent;
import org.anddev.andengine.opengl.font.Font;
import org.anddev.andengine.opengl.font.FontFactory;
import org.anddev.andengine.opengl.texture.Texture;
import org.anddev.andengine.opengl.texture.TextureOptions;
import org.anddev.andengine.opengl.texture.region.TextureRegion;
import org.anddev.andengine.opengl.texture.region.TextureRegionFactory;
import org.anddev.andengine.opengl.texture.region.TiledTextureRegion;
import org.anddev.andengine.ui.activity.BaseGameActivity;



import android.content.Context;
import android.graphics.Color;
import android.os.Handler;

public class MainMenuActivity extends BaseGameActivity  implements IOnMenuItemClickListener{

	private final static int CAMERA_WIDTH = 480;
	private final static int CAMERA_HEIGHT = 800;


	private static final int CAR_LEFT_POSITION = 50;
	private static final int CAR_RIGHT_POSITION = 200;
	private static final int TRUCK_LEFT_POSITION = CAR_LEFT_POSITION + 30;
	private static final int TRUCK_RIGHT_POSITION = CAR_RIGHT_POSITION + 30;
	private static final int CAR_YPOSITION = CAMERA_HEIGHT-300;
	
	private static final int MAILBOX_LEFT_POS = CAR_LEFT_POSITION - 30;
	private static final int MAILBOX_RIGHT_POS = CAR_RIGHT_POSITION + 200;
	
	private final static int MENU_START = 0;
	private final static int MENU_HELP = 1;
	private final static int MENU_HIGHSCORES = 2;
	private final static int MENU_EXIT = 3;
	private static final int MENU_PLAY_AGAIN = 4;
	private static final int MENU_EXIT_TO_MAIN_MENU = 5;
	
	private Camera mCamera;
	private Font mFont;
	private Texture mFontTexture;
	private Texture mBackgroundTexture;
	private TextureRegion mBackgroundTextureRegion;
	private Scene mMainScene;
	private MenuScene mStaticMenuScene;
	private MainBG mMainBGSprite;
	private Sprite mHelpScreenSprite;
	private Texture mHelpScreenTexture;
	private TextureRegion mHelpScreenTextureRegion;
	private Texture mBackButtonTexture;
	private TextureRegion mBackButtonTextureRegion;
	private Sprite mBackButtonSprite;
	private Scene mGameScene;
	private Scene mHelpScene;
	private Texture mCarTiledTexture;
	private TiledTextureRegion mCarTiledTextureRegion;
	private TiledSprite mCarTiledSprite;
	private Texture mRoadTiledTexture;
	private TiledTextureRegion mRoadTiledTextureRegion;
	private AnimatedSprite mRoadTiledSprite;
	private Texture mTruckTexture;
	private TextureRegion mTruckTextureRegion;
	private Sprite mTruckSprite;
	private Timer mTruckTimer;
	private Texture mTruckCrashedTexture;
	private TiledTextureRegion mTruckCrashedTextureRegion;
	private AnimatedSprite mTruckCrashedSprite;
	private Texture mCarCrashedTexture;
	private TiledTextureRegion mCarCrashedTextureRegion;
	private AnimatedSprite mCarCrashedSprite;
	private int mTruckPosition;
	public boolean mCarInRight;
	private boolean mTruckInRight;
	public Context mContext;
	private Texture mMailBoxTiledTexture;
	private TiledTextureRegion mMailBoxTiledTextureRegion;
	private MailBox mMailBoxSprite;
	private int mScore;
	private int mStrikeCount;
	private ChangeableText mScoreKeeper;
	private ChangeableText mStrikeKeeper;
	private Texture mScoreFontTexture;
	private Font mScoreFont;
	private MenuScene mGameOverMenuScene;
	private Scene mHighScoreScene;
	private Texture mHighScoreTexture;
	private TextureRegion mHighScoreTextureRegion;
	private Sprite mHighScoreSceneSprite;
	private Handler mHandler;
	private TimerTask mTruckTimerTask;
	
	@Override
	public Engine onLoadEngine() {
		this.mCamera = new Camera(0, 0, CAMERA_WIDTH, CAMERA_HEIGHT);
		mCarInRight = true;
		return new Engine(new EngineOptions(true, ScreenOrientation.PORTRAIT, 
				new RatioResolutionPolicy(CAMERA_WIDTH,CAMERA_HEIGHT), this.mCamera));
	}

	@Override
	public void onLoadResources() {
		mContext = this;
		mFontTexture = new Texture(256, 256,TextureOptions.BILINEAR_PREMULTIPLYALPHA);
		FontFactory.setAssetBasePath("font/");
		this.mFont = FontFactory.createFromAsset(this.mFontTexture, this, "kulminoituva.ttf", 48, true, Color.BLUE);
		this.mEngine.getTextureManager().loadTexture(this.mFontTexture);
		this.mEngine.getFontManager().loadFont(this.mFont);

		mScoreFontTexture = new Texture(256, 256,TextureOptions.BILINEAR_PREMULTIPLYALPHA);
		this.mScoreFont = FontFactory.createFromAsset(this.mScoreFontTexture, this, "FLORLRG_.ttf", 20, true, Color.BLUE);
		this.mEngine.getTextureManager().loadTexture(this.mScoreFontTexture);
		this.mEngine.getFontManager().loadFont(this.mScoreFont);
		
		
		this.mBackgroundTexture = new Texture(1024,1024,TextureOptions.BILINEAR_PREMULTIPLYALPHA);
		this.mBackgroundTextureRegion = TextureRegionFactory.createFromAsset(mBackgroundTexture, this, "gfx/background.png", 0, 0);
		this.mEngine.getTextureManager().loadTexture(this.mBackgroundTexture);	
		
		mMainBGSprite = new MainBG(CAMERA_WIDTH-this.mBackgroundTextureRegion.getWidth(),
				CAMERA_HEIGHT-this.mBackgroundTextureRegion.getHeight(),this.mBackgroundTextureRegion);
		

		this.mHelpScreenTexture = new Texture(1024,1024,TextureOptions.BILINEAR_PREMULTIPLYALPHA);
		this.mHelpScreenTextureRegion = TextureRegionFactory.createFromAsset(mHelpScreenTexture, this, "gfx/helpScreen.png", 0, 0);
		this.mEngine.getTextureManager().loadTexture(this.mHelpScreenTexture);

		mHelpScreenSprite = new Sprite(CAMERA_WIDTH-this.mHelpScreenTextureRegion.getWidth(),
				CAMERA_WIDTH-this.mHelpScreenTextureRegion.getWidth(),this.mHelpScreenTextureRegion);
		
		this.mHighScoreTexture = new Texture(1024,1024,TextureOptions.BILINEAR_PREMULTIPLYALPHA);
		this.mHighScoreTextureRegion = TextureRegionFactory.createFromAsset(mHighScoreTexture, this, "gfx/highScore.png", 0, 0);
		this.mEngine.getTextureManager().loadTexture(this.mHighScoreTexture);
		
		mHighScoreSceneSprite = new Sprite(CAMERA_WIDTH-this.mHighScoreTextureRegion.getWidth(),
				CAMERA_WIDTH-this.mHighScoreTextureRegion.getWidth(),this.mHighScoreTextureRegion);

		this.mBackButtonTexture = new Texture(1024,1024,TextureOptions.BILINEAR_PREMULTIPLYALPHA);
		this.mBackButtonTextureRegion = TextureRegionFactory.createFromAsset(mBackButtonTexture, this, "gfx/backButton.png", 0,0);
		this.mEngine.getTextureManager().loadTexture(this.mBackButtonTexture);
		
		mBackButtonSprite = new BackButton(40,CAMERA_HEIGHT-100,this.mBackButtonTextureRegion);		
		
		this.mCarTiledTexture = new Texture(1024, 1024, TextureOptions.BILINEAR_PREMULTIPLYALPHA);
		this.mCarTiledTextureRegion = TextureRegionFactory.createTiledFromAsset(mCarTiledTexture, this, "gfx/carTile.png", 
				0, 0, 3, 1);
		this.mEngine.getTextureManager().loadTexture(this.mCarTiledTexture);
		
		this.mCarTiledSprite = new TiledSprite(CAR_RIGHT_POSITION, CAR_YPOSITION, mCarTiledTextureRegion);
		

		
		this.mMailBoxTiledTexture = new Texture(1024, 1024, TextureOptions.BILINEAR_PREMULTIPLYALPHA);
		this.mMailBoxTiledTextureRegion = TextureRegionFactory.createTiledFromAsset(mMailBoxTiledTexture, this, "gfx/mailbox.png", 
				0, 0, 4, 1);
		this.mEngine.getTextureManager().loadTexture(this.mMailBoxTiledTexture);
		
		this.mMailBoxSprite = new MailBox(mMailBoxTiledTextureRegion);
		

		
		this.mRoadTiledTexture = new Texture(2048, 2048, TextureOptions.BILINEAR_PREMULTIPLYALPHA);
		this.mRoadTiledTextureRegion = TextureRegionFactory.createTiledFromAsset(mRoadTiledTexture, this, "gfx/gameScreen.png", 
				0, 0, 3, 1);
		this.mEngine.getTextureManager().loadTexture(this.mRoadTiledTexture);
		
		this.mRoadTiledSprite = new AnimatedSprite(0, 0, mRoadTiledTextureRegion);
		mRoadTiledSprite.animate(300);
		


		this.mTruckTexture = new Texture(1024,1024,TextureOptions.BILINEAR_PREMULTIPLYALPHA);
		this.mTruckTextureRegion = TextureRegionFactory.createFromAsset(mTruckTexture, this, "gfx/truck.png", 0,0);
		this.mEngine.getTextureManager().loadTexture(this.mTruckTexture);

		this.mTruckCrashedTexture = new Texture(1024,1024,TextureOptions.BILINEAR_PREMULTIPLYALPHA);
		this.mTruckCrashedTextureRegion = TextureRegionFactory.createTiledFromAsset(mTruckCrashedTexture, this, "gfx/truckCrashed.png", 
				0, 0, 2, 1);
		this.mEngine.getTextureManager().loadTexture(this.mTruckCrashedTexture);
		
		this.mTruckCrashedSprite = new AnimatedSprite(0, 0, mTruckCrashedTextureRegion);		
		mTruckCrashedSprite.setPosition(0,-mTruckCrashedSprite.getHeight());

		this.mCarCrashedTexture = new Texture(512,512,TextureOptions.BILINEAR_PREMULTIPLYALPHA);
		this.mCarCrashedTextureRegion = TextureRegionFactory.createTiledFromAsset(mCarCrashedTexture, this, "gfx/carCrashed.png", 
				0, 0, 2, 1);
		this.mEngine.getTextureManager().loadTexture(this.mCarCrashedTexture);
		
		this.mCarCrashedSprite = new AnimatedSprite(0, 0, mCarCrashedTextureRegion);
		mCarCrashedSprite.setPosition(0, -mCarCrashedSprite.getHeight());
		
		mTruckSprite = new Sprite(-CAMERA_WIDTH,-CAMERA_HEIGHT,this.mTruckTextureRegion);		
		
		this.mScoreKeeper = new ChangeableText(5, 5, mScoreFont, "Score: 0     ");
		this.mStrikeKeeper = new ChangeableText(mScoreKeeper.getX(), mScoreKeeper.getY()+mScoreKeeper.getHeight()+15, mScoreFont, "Strikes: 0     ");
	}
	
	@Override
	public Scene onLoadScene() {
		this.mEngine.registerUpdateHandler(new FPSLogger());
		this.createStaticMenuScene();
		this.createGameScene();
		this.createHelpScreen();
		this.createHighScoreScreen();
		this.createGameOverMenuScene();
		this.mMainScene = new Scene(1);
		mMainScene.getLastChild().attachChild(mMainBGSprite);
		mMainScene.setChildScene(mStaticMenuScene);	
		mMainScene.registerTouchArea(mBackButtonSprite);	
		return this.mMainScene;
	}

	private void createStaticMenuScene() {		
		this.mStaticMenuScene = new MenuScene(this.mCamera);
		final IMenuItem startMenuItem = new ColorMenuItemDecorator(
				new TextMenuItem(MENU_START, mFont, "Start"), 0.0f, 0.0f, 0.0f, 1.0f, 1.0f, 1.0f);
		startMenuItem.setBlendFunction(GL10.GL_SRC_ALPHA, GL10.GL_ONE_MINUS_SRC_ALPHA);
		this.mStaticMenuScene.addMenuItem(startMenuItem);
		
		final IMenuItem helpMenuItem = new ColorMenuItemDecorator(
				new TextMenuItem(MENU_HELP, mFont, "Help"), 0.0f, 0.0f, 0.0f, 1.0f, 1.0f, 1.0f);
		helpMenuItem.setBlendFunction(GL10.GL_SRC_ALPHA, GL10.GL_ONE_MINUS_SRC_ALPHA);
		this.mStaticMenuScene.addMenuItem(helpMenuItem);
		
		final IMenuItem highScoreMenuItem = new ColorMenuItemDecorator(
				new TextMenuItem(MENU_HIGHSCORES, mFont, "High Scores"), 0.0f, 0.0f, 0.0f, 1.0f, 1.0f, 1.0f);
		highScoreMenuItem.setBlendFunction(GL10.GL_SRC_ALPHA, GL10.GL_ONE_MINUS_SRC_ALPHA);
		this.mStaticMenuScene.addMenuItem(highScoreMenuItem);
		
		final IMenuItem exitMenuItem = new ColorMenuItemDecorator(
				new TextMenuItem(MENU_EXIT, mFont, "Exit"), 0.0f, 0.0f, 0.0f, 1.0f, 1.0f, 1.0f);
		exitMenuItem.setBlendFunction(GL10.GL_SRC_ALPHA, GL10.GL_ONE_MINUS_SRC_ALPHA);
		this.mStaticMenuScene.addMenuItem(exitMenuItem);		
		
		this.mStaticMenuScene.buildAnimations();
		this.mStaticMenuScene.setBackgroundEnabled(false);
		this.mStaticMenuScene.setOnMenuItemClickListener(this);
		this.mStaticMenuScene.setPosition(mStaticMenuScene.getInitialX(), mStaticMenuScene.getInitialY() + 90);
	}

	private void createGameOverMenuScene() {		
		this.mGameOverMenuScene = new MenuScene(this.mCamera);
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


	private void resetGame() {	
		mScore = 0;
		mStrikeCount = 0;
		this.mGameOverMenuScene.setVisible(false);
		mCarTiledSprite.clearEntityModifiers();
		mCarCrashedSprite.clearEntityModifiers();
		mTruckSprite.clearEntityModifiers();
		mTruckCrashedSprite.clearEntityModifiers();
		mCarTiledSprite.setInitialPosition();
		mTruckSprite.setInitialPosition();
		mMailBoxSprite.setInitialPosition();	
		mTruckCrashedSprite.setPosition(0,-mTruckCrashedSprite.getHeight());
		mCarCrashedSprite.setPosition(0, -mCarCrashedSprite.getHeight());

		mRoadTiledSprite.animate(300);
		
		mTruckTimer = new Timer();
		mTruckTimerTask = new TimerTask() {			
			private int timeElapsed;
			@Override
			public void run() {
				timeElapsed++;
				if(timeElapsed % 6 == 0)
				{
					boolean carLeft = (Math.random()>0.4f);
					mTruckPosition = (carLeft)?TRUCK_LEFT_POSITION:TRUCK_RIGHT_POSITION;
					mTruckInRight = (carLeft)?false:true;
					mTruckSprite.setPosition(mTruckPosition, -CAMERA_HEIGHT);
					dropTheTruck();
				}
				if(timeElapsed % 4 == 0)
				{
					mMailBoxSprite.setStatesThenDrop();
				}
			}
			
		};
		mTruckTimer.schedule(mTruckTimerTask, (long)0, (long)1000);
	}
	
	private void createGameScene()
	{
		mScore = 0;
		mStrikeCount = 0;
		
		if(mGameScene == null){
			mGameScene = new Scene(1);
			mGameScene.getLastChild().attachChild(mMainBGSprite);
			mGameScene.getLastChild().attachChild(mRoadTiledSprite);
			mGameScene.getLastChild().attachChild(mCarTiledSprite);
			mGameScene.getLastChild().attachChild(mTruckSprite);
			mGameScene.getLastChild().attachChild(mTruckCrashedSprite);
			mGameScene.getLastChild().attachChild(mCarCrashedSprite);
			mGameScene.getLastChild().attachChild(mMailBoxSprite);
			mGameScene.getLastChild().attachChild(mScoreKeeper);
			mGameScene.getLastChild().attachChild(mStrikeKeeper);
			mGameScene.registerTouchArea(mMainBGSprite);
			
			mTruckTimer = new Timer();
			mTruckTimerTask = new TimerTask() {			
				private int timeElapsed;
				@Override
				public void run() {
					timeElapsed++;
					if(timeElapsed % 6 == 0)
					{
						boolean carLeft = (Math.random()>0.4f);
						mTruckPosition = (carLeft)?TRUCK_LEFT_POSITION:TRUCK_RIGHT_POSITION;
						mTruckInRight = (carLeft)?false:true;
						mTruckSprite.setPosition(mTruckPosition, -CAMERA_HEIGHT);
						dropTheTruck();
					}
					if(timeElapsed % 4 == 0)
					{
						mMailBoxSprite.setStatesThenDrop();
					}
				}
				
			};
			mTruckTimer.schedule(mTruckTimerTask, (long)0, (long)1000);
		}
		
		mGameScene.registerUpdateHandler(new IUpdateHandler() {
			
			@Override
			public void reset() {}
			
			@Override
			public void onUpdate(float pSecondsElapsed) {
				Runnable showGameOverScreen = new Runnable() {
					
					@Override
					public void run() {
						mGameScene.setChildScene(mGameOverMenuScene);
						mGameOverMenuScene.setVisible(true);
					}
				};
				if(mStrikeCount == 3)
				{
					stopTheWorld();
					mHandler.post(showGameOverScreen);
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
						mHandler.postDelayed(showGameOverScreen, 3000);
					}
				}
			}
		});
	}
	
	private void dropTheTruck() {
		mTruckSprite.registerEntityModifier(new MoveYModifier(5, -mTruckSprite.getHeight(), CAMERA_HEIGHT));
	}

	private void stopTheWorld() {
		mRoadTiledSprite.stopAnimation();
		mTruckTimer.cancel();
		mMailBoxSprite.clearEntityModifiers();
		mTruckSprite.clearEntityModifiers();
	}

	private void createHelpScreen() {
		if(mHelpScene == null){
			mHelpScene = new Scene(1);
			mHelpScene.getLastChild().attachChild(mHelpScreenSprite);
			mHelpScene.getLastChild().attachChild(mBackButtonSprite);
		}
	}

	private void createHighScoreScreen() {
		if(mHighScoreScene == null){
			mHighScoreScene = new Scene(1);
			mHighScoreScene.getLastChild().attachChild(mHighScoreSceneSprite);
			mHighScoreScene.getLastChild().attachChild(mBackButtonSprite);
		}
	}
	
	@Override
	public void onLoadComplete() {
	}
	
	@Override
	public boolean onMenuItemClicked(MenuScene pMenuScene, IMenuItem pMenuItem,
			float pMenuItemLocalX, float pMenuItemLocalY) {
		switch (pMenuItem.getID()) {
		case MENU_START:		
			this.mMainScene.setChildScene(mGameScene);
			return true;
		case MENU_HELP:
			this.mMainScene.setChildScene(mHelpScene);
			return true;
		case MENU_HIGHSCORES:
			this.mMainScene.setChildScene(mHighScoreScene);
			return true;
		case MENU_EXIT:
			this.finish();
			return true;
		case MENU_PLAY_AGAIN:
			resetGame();
			return true;
		case MENU_EXIT_TO_MAIN_MENU:
			this.mMainScene.setChildScene(mStaticMenuScene);
			return true;
		default:
			return false;
		}
	}

	private class BackButton extends Sprite
	{

		public BackButton(float pX, float pY, TextureRegion pTextureRegion) {
			super(pX, pY, pTextureRegion);
		}
		
		
		@Override
		public boolean onAreaTouched(TouchEvent pSceneTouchEvent,
				float pTouchAreaLocalX, float pTouchAreaLocalY) {
				mMainScene.getLastChild().attachChild(mMainBGSprite);
				mMainScene.setChildScene(mStaticMenuScene);
			
			return super
					.onAreaTouched(pSceneTouchEvent, pTouchAreaLocalX, pTouchAreaLocalY);
		}
	}	

	private class MainBG extends Sprite
	{
		public MainBG(float pX, float pY, TextureRegion pTextureRegion) {
			super(pX, pY, pTextureRegion);
			mHandler = new Handler();
		}
		
		
		@Override
		public boolean onAreaTouched(TouchEvent pSceneTouchEvent,
				float pTouchAreaLocalX, float pTouchAreaLocalY) {
			if(pTouchAreaLocalX < CAMERA_WIDTH/2)
			{
				if(mCarInRight)
				{
					mCarTiledSprite.setCurrentTileIndex(0);
					mCarTiledSprite.setPosition(CAR_LEFT_POSITION, mCarTiledSprite.getY());
				}
				else
				{
					mCarTiledSprite.setCurrentTileIndex(1);
					hitBat();					
				}
				mCarInRight = false;
			}
			else
			{
				if(!mCarInRight)
				{
					mCarTiledSprite.setCurrentTileIndex(0);
					mCarTiledSprite.setPosition(CAR_RIGHT_POSITION, mCarTiledSprite.getY());
				}
				else
				{
					mCarTiledSprite.setCurrentTileIndex(2);
					hitBat();
				}
				mCarInRight = true;
			}
			return super
					.onAreaTouched(pSceneTouchEvent, pTouchAreaLocalX, pTouchAreaLocalY);
		}


		private void hitBat() {
			Runnable carBackToNormalRunnable = new Runnable() {
				
				@Override
				public void run() {
					mCarTiledSprite.setCurrentTileIndex(0);
				}
			};
			if(mCarTiledSprite.collidesWith(mMailBoxSprite) && !mMailBoxSprite.isHit())
			{
				if(mMailBoxSprite.isEmpty)
					mScore++;
				else
					mStrikeCount++;
				mMailBoxSprite.setHit(true);
			}
			mScoreKeeper.setText("Score: "+mScore);
			mStrikeKeeper.setText("Strikes: "+mStrikeCount);
			mHandler.postDelayed(carBackToNormalRunnable, 500);
			System.out.println(mScore);
		}
	}	
	
	private class MailBox extends TiledSprite
	{		
		private boolean isEmpty;
		private boolean isHit;

		public MailBox(TiledTextureRegion pTiledTextureRegion) {
			super(-pTiledTextureRegion.getWidth(), -pTiledTextureRegion.getHeight(), pTiledTextureRegion);
		}
		
		public void setStatesThenDrop()
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
			
			drop();
		}
		
		private void drop() {
			this.registerEntityModifier(new MoveYModifier(3, this.getHeight(), CAMERA_HEIGHT));
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
}
