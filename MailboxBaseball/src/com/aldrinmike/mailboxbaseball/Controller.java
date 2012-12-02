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

//	/**
//	 * Creates the shopping list and all its items then save those data into the
//	 * database. If the mode is on modify, then a new shopping list wont be
//	 * made. Also, if the shopping list being created already has the same name
//	 * as an existing one, then just add the values given in this table to that
//	 * existing table.
//	 * 
//	 * @param shoppingListName
//	 *            The name of the shopping list to be created.
//	 * @param maxItems
//	 *            the maximum number of items the shoppingListTableLayout has.
//	 * @param shoppingListTableLayout
//	 *            The table layout that holds each item value.
//	 * @param create
//	 *            states whether the mode is on create
//	 * @return The result of the creation/modification. True if successful,
//	 *         false if not.
//	 */
//	public boolean createOrModifyShoppingList(String shoppingListName,
//			int maxItems, TableLayout shoppingListTableLayout, boolean create) {
//		// Stores all the values that will be checked from each item in this
//		// table.
//		ArrayList<ArrayList<String>> allValues = new ArrayList<ArrayList<String>>();
//		// Used to count how many rows given are valid. Set the number of
//		// correct rows to none.
//		int numOfCorrectRowsCounter = 0;
//
//		// Iterate through each item in the shoppingListTableLayout and check
//		// whether the ShoppingListItem and quantity given by the user is
//		// acceptable.
//		for (int i = 0; i < maxItems; i++) {
//			// Save the current Item into a variable.
//			ShoppingListItem tempItem = (ShoppingListItem) shoppingListTableLayout
//					.getChildAt(i);
//			// Check if the item's name and quantity are both valid.
//			if (tempItem.getShoppingListItemName() != ""
//					&& tempItem.getQuantity() > 0) {
//				// Create a temporary Array of String
//				ArrayList<String> tempArrayList = new ArrayList<String>();
//				// Add the values given by the user.
//				tempArrayList.add(shoppingListName);
//				tempArrayList.add(tempItem.getShoppingListItemName());
//				tempArrayList.add(Integer.toString(tempItem.getQuantity()));
//				// Add the arrayList to the allValues ArrayList
//				allValues.add(tempArrayList);
//				// Increment the number of correct rows.
//				numOfCorrectRowsCounter++;
//			} // End of Check item name and quantity if.
//		} // End of shoppingListTable Layout For
//
//		// If the correct number of valid rows is greater than one, then add the
//		// items in the database.
//		// Otherwise, skip this process then the method will return false.
//		if (numOfCorrectRowsCounter > 0) {
//			// If this is on create mode and the shoppingListItem is unique,
//			// then create the new table.
//			// If not, then the new rows will be added to an existing table with
//			// the added value.
//			if (create && shoppingListIsUnique(shoppingListName))
//				addPlayerAndScore(shoppingListName);
//			// Create an iterator that iterates through each value in the
//			// allValues ArrayList.
//			Iterator<ArrayList<String>> it = allValues.iterator();
//			while (it.hasNext()) {
//				// Save the current ArrayList from the allValues ArrayList
//				ArrayList<String> tempItem = it.next();
//				// Add a shopping list item to the database with the values
//				// specified by the user.
//				addItem(tempItem.get(0), tempItem.get(1),
//						Integer.parseInt(tempItem.get(2)));
//			} // End of allValues while
//
//			// showAllItems(); // Show all database items in the console.
//			return true; // Return a positive result of the creation.
//		}
//		return false; // Return a negative result of the creation.
//	}

//	/**
//	 * Adds a shopping list item into the database with the values specified by
//	 * the user.
//	 * 
//	 * @param shoppingListName
//	 *            The name of the shopping list that the item is being added on.
//	 * @param name
//	 *            The name of the item being added
//	 * @param quantity
//	 *            The number of items to be bought by the user.
//	 */
//	public void addItem(String shoppingListName, String name, int quantity) {
//		// Get the shopping id by the name of the shopping list.
//		int id = getShoppingIdByValue(shoppingListName);
//		// Create a ContentValues object that is used to insert the items in the
//		// database. A key-value pair is used where the key will be the column
//		// names.
//		ContentValues contentValues = new ContentValues();
//		contentValues.put(ITEM_NAME, name);
//		contentValues.put(ITEM_QUANTITY, quantity);
//		contentValues.put(ITEM_SHOPPING_LIST_ID, id);
//
//		// Open the database.
//		DBAdapter dbHelper = new DBAdapter();
//		// Tell the database what kind of database open will be used then insert
//		// the values in the items table.
//		dbHelper.openToWrite().insert(ITEMS_TABLE, null, contentValues);
//		dbHelper.close(); // Close the Database.
//	} // End of addItem

	/**
	 * Adds a shopping list to the database with the name specified by the user.
	 * 
	 * @param playerName
	 *            The shopping name specified by the user.
	 */
	public void addPlayerAndScore(String playerName,int playerScore) {
		// Open the database.
		DBAdapter dbHelper = new DBAdapter();
		// Create a ContentValues object that is used to insert the items in the
		// database. A key-value pair is used where the key will be the column
		// names.
		ContentValues contentValues = new ContentValues();
		contentValues.put(PLAYER_NAME, playerName);
		contentValues.put(PLAYER_SCORE, playerScore);
		// Tell the database what kind of database open will be used then insert
		// the value in the shopping list table.
		dbHelper.openToWrite().insert(HIGH_SCORE_TABLE, null, contentValues);
		dbHelper.close(); // Close the database
	} // End of addShoppingList

//	/**
//	 * Get the shopping list's id from the database by that shopping list's
//	 * value. Each shopping list has a unique value so this is right.
//	 * 
//	 * @param value
//	 *            The value of the shopping list
//	 * @return The id of the shopping list. -1 if the Shopping List doesn't
//	 *         exist yet.
//	 */
//	public int getShoppingIdByValue(String value) {
//		int id = -1;
//		// Open the database.
//		DBAdapter dbHelper = new DBAdapter();
//		// The result of the database query is saved in the tempCursor variable.
//		// The cursor has all the values that is returned by the query and is
//		// used to access each data.
//		Cursor tempCursor = dbHelper.openToRead()
//				.query(SHOPPING_LIST_TABLE, new String[] { PLAYER_ID },
//						PLAYER_NAME + "='" + value + "'", null, null,
//						null, null);
//		// Move the cursor to first and if the position is not the first, then
//		// don't do anything.
//		if (tempCursor.moveToFirst())
//			// While the cursor haven't reached the last item, access the
//			// current cursor and save its value to the id variable.
//			while (!tempCursor.isAfterLast()) {
//				id = Integer.parseInt(tempCursor.getString(tempCursor
//						.getColumnIndex(PLAYER_ID)));
//				tempCursor.moveToNext(); // Move to the next cursor.
//			} // End of !tempCursor.isAfterLast() While
//		tempCursor.close();
//		dbHelper.close(); // Close the database
//		return id; // Return the value taken. -1 if no such Shopping list
//					// exists.
//	} // End getShoppingIdByValue method

//	/**
//	 * Get the shopping list name with the same id as the one passed.
//	 * 
//	 * @param id
//	 *            The shopping list id used to find the shopping list name.
//	 * @return The shopping list name that is identified by the passed id.
//	 */
//	public String getShoppingListNameById(int id) {
//		String value = null; // Set the value to null initially.
//		// Open the database and take the shopping list name from the shopping
//		// list table where the shopping list id is the same as the id passed.
//		DBAdapter dbHelper = new DBAdapter();
//		Cursor tempCursor = dbHelper.openToRead().query(SHOPPING_LIST_TABLE,
//				new String[] { PLAYER_NAME },
//				PLAYER_ID + "='" + id + "'", null, null, null, null);
//		// While the cursor haven't reached the last item, access the current
//		// cursor and save its value to the id variable.
//		if (tempCursor.moveToFirst())
//			while (!tempCursor.isAfterLast()) {
//				value = tempCursor.getString(tempCursor
//						.getColumnIndex(PLAYER_NAME));
//				tempCursor.moveToNext(); // Move to the next cursor.
//			} // End of !tempCursor.isAfterLast() While
//		tempCursor.close();
//		dbHelper.close(); // Close the database
//		return value; // Return the shopping list name that has the same as the
//						// id passed.
//	} // End of getShoppingListNameById method

//	/**
//	 * Checks if the shopping list is unique and no shopping list with the same
//	 * name exists.
//	 * 
//	 * @param name
//	 *            The name of the shopping list to be checked
//	 * @return The result of the check. True if a shopping list with the given
//	 *         value doesn't exist. False if a shopping list with the name
//	 *         passed already exists.
//	 */
//	public boolean shoppingListIsUnique(String name) {
//		return (getShoppingIdByValue(name) < 0) ? true : false;
//	} // End of shoppingListIsUnique method

//	/**
//	 * Delete all the shopping list and shopping list items where shoppingid
//	 * matches the shopping list.
//	 * 
//	 * @param id
//	 *            The id that is used to check which shopping list items and
//	 *            which shopping list with items should be deleted.
//	 */
//	public void deleteAllShoppingListAndItemsByShoppingListId(int id) {
//		// Open the database and delete the items with the given id.
//		DBAdapter dbHelper = new DBAdapter();
//		dbHelper.openToWrite().delete(ITEMS_TABLE,
//				ITEM_SHOPPING_LIST_ID + "= " + id, null);
//		dbHelper.openToWrite().delete(SHOPPING_LIST_TABLE,
//				PLAYER_ID + "=" + id, null);
//		dbHelper.close(); // Close the database.
//
//		// showAllItems(); // Show all database items in the console.
//	} // End of deleteAllShoppingListItemsByShoppingListId Method

//	/**
//	 * Delete all the shopping list items where shoppingid matches the shopping
//	 * list.
//	 * 
//	 * @param id
//	 *            The id that is used to check which shopping list items and
//	 *            which shopping list items should be deleted.
//	 */
//	public void deleteAllShoppingListItemsByShoppingId(int shoppingListId) {
//		DBAdapter dbHelper = new DBAdapter();
//		dbHelper.openToWrite().delete(ITEMS_TABLE,
//				"shoppinglistid = " + shoppingListId, null);
//		dbHelper.close();
//	} // End of deleteAllShoppingListItemsByShoppingId Method

//	/**
//	 * Shows all the items the database currently have. This method is used for
//	 * debugging purposes.
//	 */
//	private void showAllItems() {
//		// Open the database and show each and every shopping list.
//		DBAdapter dbHelper = new DBAdapter();
//		// Output each and every row from the shopping list table.
//		Cursor curs = dbHelper.openToRead().query(SHOPPING_LIST_TABLE, null,
//				null, null, null, null, null);
//		System.out
//				.println("----------------- SHOPPING LISTS -----------------");
//		curs.moveToFirst();
//		while (!curs.isAfterLast()) {
//			System.out.println("List id : "
//					+ curs.getString(curs.getColumnIndex(PLAYER_ID)));
//			System.out.println("List name : "
//					+ curs.getString(curs.getColumnIndex(PLAYER_NAME)));
//			curs.moveToNext();
//		}
//		curs.close(); // Close the cursor
//
//		// Output each and every row from the items table.
//		Cursor curs2 = dbHelper.openToRead().query(ITEMS_TABLE, null, null,
//				null, null, null, null);
//		System.out
//				.println("----------------- SHOPPING LISTS ITEMS -----------------");
//		curs2.moveToFirst();
//		while (!curs2.isAfterLast()) {
//			System.out.println("Item id : "
//					+ curs2.getString(curs2.getColumnIndex(ITEM_ID)));
//			System.out.println("Item name : "
//					+ curs2.getString(curs2.getColumnIndex(ITEM_NAME)));
//			System.out.println("Item quantity : "
//					+ curs2.getString(curs2.getColumnIndex(ITEM_QUANTITY)));
//			System.out.println("Item shopping list id : "
//					+ curs2.getString(curs2
//							.getColumnIndex(ITEM_SHOPPING_LIST_ID)));
//			curs2.moveToNext();
//		}
//		curs2.close(); // Close the second cursor
//		dbHelper.close(); // Close the database
//	} // End of showAllItems method

//	/**
//	 * Updates a shopping list name from the shopping list table.
//	 * 
//	 * @param shoppingId
//	 *            The shopping id of the table that needs to be changed.
//	 * @param newName
//	 *            The new name of the shopping list
//	 */
//	public void updateShoppingListName(int shoppingId, String newName) {
//		// Open the database and update the value of the name to the one passed.
//		DBAdapter dbHelper = new DBAdapter();
//		// Create a ContentValues object that is used to update the items in the
//		// database.
//		ContentValues cv = new ContentValues();
//		cv.put(PLAYER_NAME, newName);
//		dbHelper.openToWrite().update(SHOPPING_LIST_TABLE, cv,
//				PLAYER_ID + "=" + shoppingId, null);
//		dbHelper.close(); // Close the database.
//	} // End of updateShoppingListName method

//	/**
//	 * Fills up each shopping list item row in the AddModify Activity with the
//	 * value from the database.
//	 * 
//	 * @param shoppingListId
//	 *            The shopping list id that identifies which shopping list the
//	 *            items belong to.
//	 * @param shoppingListEditText
//	 *            The EditText that shows the name of the shopping list.
//	 * @param shoppingListTableLayout
//	 *            The TableLayout that has all the ShoppingListItem rows.
//	 */
//	public void fillUpShoppingItems(int shoppingListId,
//			EditText shoppingListEditText, TableLayout shoppingListTableLayout) {
//		// Store all name and quantity values to an ArrayList<ArrayList<String>>
//		// variable.
//		ArrayList<ArrayList<String>> allValues = getAllNameAndQuantityValues(shoppingListId);
//		// Set the shoppingListEditText's text to the name of the shopping list
//		// with the shoppingId passed.
//		shoppingListEditText.setText(getShoppingListNameById(shoppingListId));
//		int i = 0; // used as an index.
//		Iterator<ArrayList<String>> it = allValues.iterator();
//		while (it.hasNext()) {
//			ArrayList<String> currentItem = it.next();
//			ShoppingListItem tempItem = (ShoppingListItem) shoppingListTableLayout
//					.getChildAt(i); // Get each item from the table layout and
//									// save a reference to a variable.
//
//			// Get a reference of the EditText and TextView from the current
//			// row.
//			EditText tempEditText = (EditText) tempItem.getChildAt(0);
//			TextView tempTextView = (TextView) tempItem.getChildAt(2);
//
//			// Set the current row's EditText and TextView value to the value
//			// from allValues
//			tempEditText.setText(currentItem.get(0));
//			tempTextView.setText(currentItem.get(1));
//
//			// Set the quantity field's value of the ShoppingListItem to the
//			// quantity from the row
//			tempItem.setQuantity(Integer.parseInt(currentItem.get(1)));
//			i++; // Increment the index
//		} // End of Iterator While
//	} // End of fillUpShoppingItems method

	/**
	 * Gets all the shopping lists that the app currently have.
	 */
	public ArrayList<ArrayList<String>> getTop10Scores() {
		ArrayList<ArrayList<String>> allLists = new ArrayList<ArrayList<String>>();
		// Open the database and take the values and quantity into a tempCursor.
		DBAdapter dbHelper = new DBAdapter();
		Cursor tempCursor = dbHelper.openToRead().query(HIGH_SCORE_TABLE, new String[] { PLAYER_ID, PLAYER_NAME }, 
				null, null, null, null, PLAYER_SCORE, "10");
		if (tempCursor.moveToFirst())
			// Go through each item in the cursor and add the values into the
			// allItems variable.
			while (!tempCursor.isAfterLast()) {
				ArrayList<String> tempArrayList = new ArrayList<String>();
				// Take the shopping list name and id.
				tempArrayList.add(tempCursor.getString(tempCursor
						.getColumnIndex(PLAYER_ID)));
				tempArrayList.add(tempCursor.getString(tempCursor
						.getColumnIndex(PLAYER_NAME)));
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

//	/**
//	 * Get all the name and quantity values of all the shopping list items with
//	 * the same id as the one passed.
//	 * 
//	 * @param shoppingListId
//	 *            The id of the shopping list.
//	 * @return The name and quantity values of the specified shopping list.
//	 */
//	public ArrayList<ArrayList<String>> getAllNameAndQuantityValues(
//			int shoppingListId) {
//		ArrayList<ArrayList<String>> allValues = new ArrayList<ArrayList<String>>();
//		// Open the database and take the values and quantity into a tempCursor.
//		DBAdapter dbHelper = new DBAdapter();
//		Cursor tempCursor = dbHelper.openToRead().query(ITEMS_TABLE,
//				new String[] { ITEM_NAME, ITEM_QUANTITY },
//				ITEM_SHOPPING_LIST_ID + " = " + shoppingListId, null, null,
//				null, null);
//		// Go through each item in the cursor and add the values into the
//		// allValues variable.
//		if (tempCursor.moveToFirst())
//			while (!tempCursor.isAfterLast()) {
//				ArrayList<String> tempArrayList = new ArrayList<String>();
//				// Take the item name and quantity.
//				tempArrayList.add(tempCursor.getString(tempCursor
//						.getColumnIndex(ITEM_NAME)));
//				tempArrayList.add(tempCursor.getString(tempCursor
//						.getColumnIndex(ITEM_QUANTITY)));
//				allValues.add(tempArrayList); // Add the tempArrayList to the
//												// allValues ArrayList
//				tempCursor.moveToNext(); // Move to the next item
//			}
//		else
//			throw new Error(
//					"There is no shopping list id that matches with the given id.");
//		tempCursor.close(); // Close the temporary cursor.
//		dbHelper.close(); // Close the database
//		return allValues; // Return all the values from the database
//	} // End of getAllNameAndQuantityValues Method

//	/**
//	 * Check if a shopping list exist in the database.
//	 * 
//	 * @return The result of the check. If empty, return false. Otherwise,
//	 *         return true.
//	 */
//	public boolean checkIfAShoppingListExists() {
//		boolean notEmpty = false; // Set the notEmpty to false.
//		// Open the database
//		DBAdapter dbHelper = new DBAdapter();
//		// Do the query to get a result.
//		Cursor tempCursor = dbHelper.openToRead().query(SHOPPING_LIST_TABLE,
//				null, null, null, null, null, null);
//		// move the cursor to first. If the cursor has contents in it, then set
//		// notEmpty to true.
//		if (tempCursor.moveToFirst())
//			notEmpty = true;
//		tempCursor.close(); // Close the cursor.
//		dbHelper.close(); // Close the database.
//		return notEmpty; // Return the result of the check.
//	} // End of checkIfAShoppingListExists Method

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