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
import org.anddev.andengine.input.touch.TouchEvent;
import org.anddev.andengine.opengl.font.Font;
import org.anddev.andengine.opengl.font.FontFactory;
import org.anddev.andengine.opengl.texture.Texture;
import org.anddev.andengine.opengl.texture.TextureOptions;
import org.anddev.andengine.opengl.texture.region.TextureRegion;
import org.anddev.andengine.opengl.texture.region.TextureRegionFactory;
import org.anddev.andengine.opengl.texture.region.TiledTextureRegion;
import org.anddev.andengine.ui.activity.BaseGameActivity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Handler;
import android.text.InputFilter;
import android.widget.EditText;

public class MainGameActivity extends BaseGameActivity implements IOnMenuItemClickListener{

	private final static int CAMERA_WIDTH = 480;
	private final static int CAMERA_HEIGHT = 800;

	private static final int CAR_LEFT_POSITION = 50;
	private static final int CAR_RIGHT_POSITION = 200;
	private static final int TRUCK_LEFT_POSITION = CAR_LEFT_POSITION + 50;
	private static final int TRUCK_RIGHT_POSITION = CAR_RIGHT_POSITION + 30;
	private static final int CAR_YPOSITION = CAMERA_HEIGHT-300;	
	private static final int MAILBOX_LEFT_POS = CAR_LEFT_POSITION - 30;
	private static final int MAILBOX_RIGHT_POS = CAR_RIGHT_POSITION + 200;
	private static final int MENU_PLAY_AGAIN = 4;
	private static final int MENU_EXIT_TO_MAIN_MENU = 5;
	private Camera mCamera;
	private AnimatedSprite mTruckCrashedSprite;
	private AnimatedSprite mCarCrashedSprite;
	private Sprite mTruckSprite;
	private ChangeableText mStrikeKeeper;
	private ChangeableText mScoreKeeper;
	private Scene mMainScene;
	private Scene mGameScene;
	private String mPlayer;
	private int mScore;
	private int mStrikeCount;
	private Font mScoreFont;
	private TiledSprite mCarTiledSprite;
	private MailBox mMailBoxSprite;
	private Road mRoadTiledSprite;
	private Timer mTruckTimer;
	private int mTruckPosition;
	private boolean mTruckInRight;
	private boolean mCarInRight;
	private TimerTask mTruckTimerTask;	
	private Handler mHandler;
	private EditText mHighScorerName;
	private AlertDialog mSaveHighScoreAlertDialog;
	private MenuScene mGameOverMenuScene;
	private Sprite mGameOverMenuSprite;
	private Font mFont;
	private Controller mController;
	private float mTruckSpeed;
	private float mMailboxSpeed;

	@Override
	public Engine onLoadEngine() {
		this.mCamera = new Camera(0, 0, CAMERA_WIDTH, CAMERA_HEIGHT);
		return new Engine(new EngineOptions(true, ScreenOrientation.PORTRAIT, 
				new RatioResolutionPolicy(CAMERA_WIDTH,CAMERA_HEIGHT), this.mCamera));
	}

	@Override
	public void onLoadResources() {

		FontFactory.setAssetBasePath("font/");
		Texture mScoreFontTexture = new Texture(256, 256,TextureOptions.BILINEAR_PREMULTIPLYALPHA);
		mScoreFont = FontFactory.createFromAsset(mScoreFontTexture, this, "FLORLRG_.ttf", 20, true, Color.BLACK);
		mEngine.getTextureManager().loadTexture(mScoreFontTexture);
		mEngine.getFontManager().loadFont(mScoreFont);
		
		Texture mFontTexture = new Texture(256, 256,TextureOptions.BILINEAR_PREMULTIPLYALPHA);
		this.mFont = FontFactory.createFromAsset(mFontTexture, this, "kulminoituva.ttf", 38, true, Color.BLACK);
		this.mEngine.getTextureManager().loadTexture(mFontTexture);
		this.mEngine.getFontManager().loadFont(this.mFont);

		Texture mGameOverMenuTexture = new Texture(1024,1024,TextureOptions.BILINEAR_PREMULTIPLYALPHA);
		TextureRegion mGameOverMenuTextureRegion = TextureRegionFactory.createFromAsset(mGameOverMenuTexture, this, "gfx/gameOverMenu.png", 0, 0);
		mEngine.getTextureManager().loadTexture(mGameOverMenuTexture);	

		mGameOverMenuSprite = new Sprite(CAMERA_WIDTH-mGameOverMenuTextureRegion.getWidth(),
				CAMERA_HEIGHT-mGameOverMenuTextureRegion.getHeight(),mGameOverMenuTextureRegion);
		
		Texture mCarTiledTexture = new Texture(1024, 1024, TextureOptions.BILINEAR_PREMULTIPLYALPHA);
		TiledTextureRegion mCarTiledTextureRegion = TextureRegionFactory.createTiledFromAsset(mCarTiledTexture, this, "gfx/carTile.png", 
				0, 0, 3, 1);
		mEngine.getTextureManager().loadTexture(mCarTiledTexture);
		
		mCarTiledSprite = new TiledSprite(CAR_RIGHT_POSITION, CAR_YPOSITION, mCarTiledTextureRegion);
		
		Texture mMailBoxTiledTexture = new Texture(1024, 1024, TextureOptions.BILINEAR_PREMULTIPLYALPHA);
		TiledTextureRegion mMailBoxTiledTextureRegion = TextureRegionFactory.createTiledFromAsset(mMailBoxTiledTexture, this, "gfx/mailbox.png", 
				0, 0, 4, 1);
		mEngine.getTextureManager().loadTexture(mMailBoxTiledTexture);
		
		mMailBoxSprite = new MailBox(mMailBoxTiledTextureRegion);
		
		Texture mRoadTiledTexture = new Texture(2048, 2048, TextureOptions.BILINEAR_PREMULTIPLYALPHA);
		TiledTextureRegion mRoadTiledTextureRegion = TextureRegionFactory.createTiledFromAsset(mRoadTiledTexture, this, "gfx/gameScreen.png", 
				0, 0, 3, 1);
		mEngine.getTextureManager().loadTexture(mRoadTiledTexture);
		
		mRoadTiledSprite = new Road(0, 0, mRoadTiledTextureRegion);
		mRoadTiledSprite.animate(300);

		Texture mTruckTexture = new Texture(1024,1024,TextureOptions.BILINEAR_PREMULTIPLYALPHA);
		TextureRegion mTruckTextureRegion = TextureRegionFactory.createFromAsset(mTruckTexture, this, "gfx/truck.png", 0,0);
		mEngine.getTextureManager().loadTexture(mTruckTexture);

		Texture mTruckCrashedTexture = new Texture(1024,1024,TextureOptions.BILINEAR_PREMULTIPLYALPHA);
		TiledTextureRegion mTruckCrashedTextureRegion = TextureRegionFactory.createTiledFromAsset(mTruckCrashedTexture, this, "gfx/truckCrashed.png", 
				0, 0, 2, 1);
		mEngine.getTextureManager().loadTexture(mTruckCrashedTexture);
		
		mTruckCrashedSprite = new AnimatedSprite(0, 0, mTruckCrashedTextureRegion);		
		mTruckCrashedSprite.setPosition(0,-mTruckCrashedSprite.getHeight());

		Texture mCarCrashedTexture = new Texture(512,512,TextureOptions.BILINEAR_PREMULTIPLYALPHA);
		TiledTextureRegion mCarCrashedTextureRegion = TextureRegionFactory.createTiledFromAsset(mCarCrashedTexture, this, "gfx/carCrashed.png", 
				0, 0, 2, 1);
		mEngine.getTextureManager().loadTexture(mCarCrashedTexture);
		
		mCarCrashedSprite = new AnimatedSprite(0, 0, mCarCrashedTextureRegion);
		mCarCrashedSprite.setPosition(0, -mCarCrashedSprite.getHeight());
		
		mTruckSprite = new Sprite(-CAMERA_WIDTH,-CAMERA_HEIGHT,mTruckTextureRegion);		
		
		mScoreKeeper = new ChangeableText(5, 5, mScoreFont, "Score: 0     ");
		mStrikeKeeper = new ChangeableText(mScoreKeeper.getX(), mScoreKeeper.getY()+mScoreKeeper.getHeight()+15, mScoreFont, "Strikes: 0     ");
	}

	@Override
	public Scene onLoadScene() {
		this.createGameScene();
		this.mMainScene = new Scene(1);
		mMainScene.getLastChild().attachChild(mRoadTiledSprite);
		mMainScene.setChildScene(mGameScene);	
		
		return this.mMainScene;
	}

	@Override
	public void onLoadComplete() {
	}


	
	@Override
	public void onBackPressed() {
	}
	
	private void dropTheTruck() {
		mTruckSprite.registerEntityModifier(new MoveYModifier(mTruckSpeed, -mTruckSprite.getHeight(), CAMERA_HEIGHT));
	}

	private void stopTheWorld() {
		mRoadTiledSprite.stopAnimation();
		mTruckTimer.cancel();
		mMailBoxSprite.clearEntityModifiers();
		mTruckSprite.clearEntityModifiers();
	}
	
	@Override
	public boolean onMenuItemClicked(MenuScene pMenuScene, IMenuItem pMenuItem,
			float pMenuItemLocalX, float pMenuItemLocalY) {
		switch (pMenuItem.getID()) {
		case MENU_PLAY_AGAIN:
			mGameOverMenuScene.setVisible(false);
			mMainScene.setChildScene(mGameScene);
			resetGame();
			return true;
		case MENU_EXIT_TO_MAIN_MENU:
			Intent myIntent = new Intent(MainGameActivity.this,MainGameScreenActivity.class);
			MainGameActivity.this.startActivity(myIntent);
			mMainScene.detachChildren();
			finish();
			return true;
		default:
			return false;
		}
	}
	
	private void createGameScene()
	{
		initializeGameVals();
		mHighScorerName = new EditText(this);
		mHighScorerName.setHint("0 - 10 characters.");
		mHighScorerName.setFilters(new InputFilter[] { new InputFilter.LengthFilter(10)});
		
		mController = new Controller(this);
		createAlertDialogs();
		createGameOverMenuScene();
		
		if(mGameScene == null){
			mGameScene = new Scene(1);
			mGameScene.getLastChild().attachChild(mRoadTiledSprite);
			mGameScene.getLastChild().attachChild(mCarTiledSprite);
			mGameScene.getLastChild().attachChild(mTruckSprite);
			mGameScene.getLastChild().attachChild(mTruckCrashedSprite);
			mGameScene.getLastChild().attachChild(mCarCrashedSprite);
			mGameScene.getLastChild().attachChild(mMailBoxSprite);
			mGameScene.getLastChild().attachChild(mScoreKeeper);
			mGameScene.getLastChild().attachChild(mStrikeKeeper);
			mGameScene.registerTouchArea(mRoadTiledSprite);
			
			buildTimer();
		}
		
		mGameScene.registerUpdateHandler(new IUpdateHandler() {

			@Override
			public void reset() {}
			
			@Override
			public void onUpdate(float pSecondsElapsed) {
				Runnable showHighScoreAddScreen = new Runnable() {

					@Override
					public void run() {
						mScore -= mStrikeCount;
						if(mController.scoreBelongsToTop10(mScore) && mScore > 0){
							mSaveHighScoreAlertDialog.setMessage(getDialogMessage());
							mSaveHighScoreAlertDialog.show();
						}
						else
						{
							mMainScene.setChildScene(mGameOverMenuScene);
							mGameOverMenuScene.setVisible(true);
						}
					}

					private CharSequence getDialogMessage() {
						return "Congrats, your score made it to the top 10." +
								"\n Score: "+mScore +
								"\n Enter your name:";
					}
				};
				if(mStrikeCount == 3)
				{
					stopTheWorld();
					mHandler.post(showHighScoreAddScreen);
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
						mHandler.postDelayed(showHighScoreAddScreen,1500);
					}
				}
			}
		});
	}
	private void initializeGameVals() {
		mCarInRight = true;
		mScore = 0;
		mStrikeCount = 0;
		mTruckSpeed = 5;
		mMailboxSpeed = 4;
		mScoreKeeper.setText("Score: "+mScore);
		mStrikeKeeper.setText("Strikes: "+mStrikeCount);
	}

	private void resetGame() {
		initializeGameVals();
		mCarTiledSprite.clearEntityModifiers();
		mCarCrashedSprite.clearEntityModifiers();
		mTruckSprite.clearEntityModifiers();
		mTruckCrashedSprite.clearEntityModifiers();
		mCarTiledSprite.setInitialPosition();
		mTruckSprite.setInitialPosition();
		
		mMailBoxSprite.setPosition(0,-(mMailBoxSprite.getHeight()+100));	
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
				
				if(timeElapsed % (mTruckSpeed+1) == 0)
				{
					boolean carLeft = (Math.random()>0.4f);
					mTruckPosition = (carLeft)?TRUCK_LEFT_POSITION:TRUCK_RIGHT_POSITION;
					mTruckInRight = (carLeft)?false:true;
					mTruckSprite.setPosition(mTruckPosition, -CAMERA_HEIGHT);
					dropTheTruck();
				}
				if(timeElapsed % (mMailboxSpeed+1) == 0)
				{
					mMailBoxSprite.setStatesThenDrop();
				}
			}
			
		};
		mTruckTimer.schedule(mTruckTimerTask, (long)0, (long)1000);
	}


	private void createAlertDialogs() {
		// Get the AlertDialog Builder class and save it in a variable.
		AlertDialog.Builder saveAlertDialogBuilder = new AlertDialog.Builder(this);
		saveAlertDialogBuilder.setTitle("High Score:");
		saveAlertDialogBuilder.setView(mHighScorerName);
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
			mPlayer = (mHighScorerName.getText().length() == 0)?"Player 1":mHighScorerName.getText().toString();
			mController.savePlayerAndScore(mPlayer,mScore);
			mMainScene.setChildScene(mGameOverMenuScene);
			mGameOverMenuScene.setVisible(true);
		} // End of onClick method
	}; // End of alertDialogResetOnClickListener anonymous inner class

	private void createGameOverMenuScene() {
		this.mGameOverMenuScene = new MenuScene(this.mCamera);
		mGameOverMenuScene.getLastChild().attachChild(mGameOverMenuSprite);
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
		mGameOverMenuScene.setVisible(false);
	}
	private class Road extends AnimatedSprite
	{
		public Road(float pX, float pY, TiledTextureRegion pTiledTextureRegion) {
			super(pX, pY, pTiledTextureRegion);
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
			this.registerEntityModifier(new MoveYModifier(mMailboxSpeed, -this.getHeight(), CAMERA_HEIGHT));
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
