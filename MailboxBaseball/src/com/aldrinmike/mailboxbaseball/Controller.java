package com.aldrinmike.mailboxbaseball;

import java.util.ArrayList;
import java.util.Iterator;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.widget.EditText;
import android.widget.TableLayout;
import android.widget.TextView;

/**
 * FileName: Controller.java
 * 
 * @author Aldrin Jerome Almacin
 *         <p>
 *         <b>Date: </b>November 13, 2012
 *         </p>
 *         <p>
 *         <b>Description: </b>Controller is used to make a connection from the
 *         app to the database in a way that no database call is made from the
 *         Activities. All the database manipulation and query is processed
 *         here. What the Activities do is just call the right methods to do
 *         CRUD operations.
 *         </p>
 * 
 */
public class Controller {
	public static final int DATABASE_VERSION = 1; // The version of the SQLite
													// Database
	public static final String DATABASE_NAME = "Project1"; // The name of the
																// Database

	public static final String HIGH_SCORE_TABLE = "highScore"; // The name
																		// of
																		// the
																		// shopping
																		// list
																		// table
	public static final String PLAYER_ID = "_id"; // The id that
															// identifies each
															// shopping list.
	public static final String PLAYER_NAME = "playername"; // The name of
																// the shopping
																// list.
	public static final String PLAYER_SCORE = "score"; // The name of
	// the shopping
	// list.

	// The SQLite queries that will be used to create the shopping list and
	// items table
	public static final String HIGH_SCORE_TABLE_QUERY = "CREATE TABLE "
			+ HIGH_SCORE_TABLE + " (" + PLAYER_ID
			+ " INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT,"
			+ PLAYER_NAME + " TEXT NOT NULL,"
			+PLAYER_SCORE+" INTEGER NOT NULL);";

	private Context context; // A copy of the Activity context that is passed as

	/**
	 * The constructor of the controller class.
	 * 
	 * @param context
	 *            The Activity context which is used to know which activity is
	 *            being modified.
	 */
	public Controller(Context context) {
		this.context = context; // Save a copy of the passed context
	} // End of Constructor

	/**
	 * Gets all the shopping lists that the app currently have.
	 */
	public ArrayList<ArrayList<String>> getTop10Scores() {
		ArrayList<ArrayList<String>> allLists = new ArrayList<ArrayList<String>>();
		// Open the database and take the values and quantity into a tempCursor.
		DBAdapter dbHelper = new DBAdapter();
		Cursor tempCursor = dbHelper.openToRead().query(HIGH_SCORE_TABLE, new String[] { PLAYER_ID, PLAYER_NAME,PLAYER_SCORE }, 
				null, null, null, null, PLAYER_SCORE+" DESC", "10");
		if (tempCursor.moveToFirst())
			// Go through each item in the cursor and add the values into the
			// allItems variable.
			while (!tempCursor.isAfterLast()) {
				ArrayList<String> tempArrayList = new ArrayList<String>();
				// Take the shopping list name and id.
				tempArrayList.add(tempCursor.getString(tempCursor
						.getColumnIndex(PLAYER_NAME)));
				tempArrayList.add(tempCursor.getString(tempCursor
						.getColumnIndex(PLAYER_SCORE)));
				allLists.add(tempArrayList); // Add the tempArrayList to the
												// allValues ArrayList
				tempCursor.moveToNext(); // Move to the next item
			} // End of !tempCursor.isAfterLast() While
		else
			allLists = null;
		tempCursor.close();
		dbHelper.close();
		return allLists;
	} // End of getAllShoppingLists Method

	public boolean scoreBelongsToTop10(int score) {
		// Open the database and take the values and quantity into a tempCursor.
		DBAdapter dbHelper = new DBAdapter();
		Cursor tempCursor = dbHelper.openToRead().query(HIGH_SCORE_TABLE, new String[] { PLAYER_ID, PLAYER_SCORE }, 
				null, null, null, null, PLAYER_SCORE, "10");
		boolean belongToTop10 = false;
		if (tempCursor.moveToFirst())
			// Go through each item in the cursor and add the values into the
			// allItems variable.
			while (!tempCursor.isAfterLast()) {
				if(Integer.parseInt(tempCursor.getString(tempCursor.getColumnIndex(PLAYER_SCORE))) <= score)
				{
					belongToTop10 = true;
				}
				tempCursor.moveToNext(); // Move to the next item
			} // End of !tempCursor.isAfterLast() While
		else
			belongToTop10 = true;
		tempCursor.close();
		dbHelper.close();		
		return belongToTop10;
	}

	public void savePlayerAndScore(String mPlayer, int mScore) {
		// Open the database and take the values and quantity into a tempCursor.
				DBAdapter dbHelper = new DBAdapter();
				if(mPlayer.length() == 0)
				{
					mPlayer = "Player 1";
				}
				ContentValues contentValues = new ContentValues();
				contentValues.put(PLAYER_NAME, mPlayer);
				contentValues.put(PLAYER_SCORE, mScore);
				dbHelper.openToWrite().insert(HIGH_SCORE_TABLE, null, contentValues );
				dbHelper.close();		
	}

	/**
	 * @author Aldrin Jerome Almacin
	 *         <p>
	 *         <b>Date: </b>November 13, 2012
	 *         </p>
	 *         <p>
	 *         <b>Description: </b>DBAdapter is a class used to open and close
	 *         the database. If openToRead is called, the database will only
	 *         process file reading. While with openToWrite, you can add and
	 *         manipulate data in the database.
	 *         </p>
	 */
	private class DBAdapter {
		// The SQLiteDatabase is saved in the db field.
		private SQLiteDatabase db;

		/**
		 * Empty Constructor
		 */
		public DBAdapter() {
		}

		/**
		 * Closes the database.
		 */
		public void close() {
			db.close();
		} // End of close method

		/**
		 * Opens the readable database.
		 * 
		 * @return The Database that can be used to access data from the
		 *         database.
		 */
		public SQLiteDatabase openToRead() throws android.database.SQLException {
			ShoppingListSQLHelper shoppingListSQLHelper = new ShoppingListSQLHelper(
					context, DATABASE_NAME, null, DATABASE_VERSION);
			// Get the db from the shoppingListSQLHelper
			db = shoppingListSQLHelper.getReadableDatabase();
			return db; // Return the db
		} // End of openToRead Method

		/**
		 * Opens the writable database.
		 * 
		 * @return The Database that can be used to modify data in the database.
		 */
		public SQLiteDatabase openToWrite()
				throws android.database.SQLException {
			ShoppingListSQLHelper shoppingListSQLHelper = new ShoppingListSQLHelper(
					context, DATABASE_NAME, null, DATABASE_VERSION);
			// Get the db from the shoppingListSQLHelper
			db = shoppingListSQLHelper.getWritableDatabase();
			return db; // Return the db
		} // End of openToWrite Method

		/**
		 * <p>
		 * <b>Date: </b>November 13, 2012
		 * </p>
		 * <p>
		 * <b>Description: </b>ShoppingListSQLHelper that extends the
		 * SQLiteOpenHelper class which helps the access to database easier.
		 * Database is also created in this class from the queries above.
		 * </p>
		 * 
		 */
		private class ShoppingListSQLHelper extends SQLiteOpenHelper {
			/**
			 * The constructor of the ShoppingListSQLHelper class
			 * 
			 * @param context
			 *            The context of the Activity using this
			 * @param name
			 *            The name of the Database
			 * @param version
			 *            The database version
			 */
			public ShoppingListSQLHelper(Context context, String name,
					CursorFactory factory, int version) {
				super(context, name, factory, version);
			} // End of Constructor

			/**
			 * Method that gets called when the db is getting initialized.
			 * Creates the table that the app needs.
			 * 
			 * @param db
			 *            The database that is to be created.
			 */
			public void onCreate(SQLiteDatabase db) {
				db.execSQL(HIGH_SCORE_TABLE_QUERY);
			} // End of onCreate method

			public void onUpgrade(SQLiteDatabase db, int oldVersion,
					int newVersion) {
			}
		} // End of ShoppingListSQLHelper Private Class
	} // End of DBAdapter Private Class
} // End of Controller Class