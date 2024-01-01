package com.example.happyplaces.databases
import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteException
import android.database.sqlite.SQLiteOpenHelper
import com.example.happyplaces.models.HappyPlaceModel

class DatabaseHandler(context: Context):
        SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION ) {

    companion object {
        private const val DATABASE_VERSION = 1
        private const val DATABASE_NAME = "HappyPlaceDatabase"
        private const val TABLE_HAPPY_PLACE = "HappyPlaceTable"
        private const val KEY_ID = "id"
        private const val KEY_TITLE = "title"
        private const val KEY_IMAGE = "image"
        private const val KEY_DESCRIPTION = "description"
        private const val KEY_DATE = "date"
        private const val KEY_LOCATION = "location"
        private const val KEY_LATITUDE = "latitude"
        private const val KEY_LONGITUDE = "longitude"
    }

    override fun onCreate(db: SQLiteDatabase?) {
        //creating table with fields
        val createHappyPlaceTable = "CREATE TABLE $TABLE_HAPPY_PLACE " +
                "($KEY_ID INTEGER PRIMARY KEY, " +
                "$KEY_TITLE TEXT, " +
                "$KEY_IMAGE TEXT, " +
                "$KEY_DESCRIPTION TEXT," +
                "$KEY_DATE TEXT," +
                "$KEY_LOCATION TEXT," +
                "$KEY_LATITUDE TEXT, " +
                "$KEY_LONGITUDE TEXT)"
        db?.execSQL(createHappyPlaceTable)
    }

    override fun onUpgrade(db: SQLiteDatabase?, p1: Int, p2: Int) {
        db!!.execSQL("DROP TABLE IF EXISTS $TABLE_HAPPY_PLACE")
        onCreate(db)
    }

    fun addHappyPlace(happyPlace: HappyPlaceModel): Long {
        val db = this.writableDatabase
        val values = ContentValues().apply {
            //put(KEY_ID, happyPlace.id)
            put(KEY_TITLE, happyPlace.title)
            put(KEY_IMAGE, happyPlace.image)
            put(KEY_DESCRIPTION, happyPlace.description)
            put(KEY_DATE, happyPlace.date)
            put(KEY_LOCATION, happyPlace.location)
            put(KEY_LATITUDE, happyPlace.latitude)
            put(KEY_LONGITUDE, happyPlace.longitude)
        }
        //inserting row
        val result = db.insert(TABLE_HAPPY_PLACE, null, values)
        db.close() //Closing Database Connection
        return result
    }

    //END
    fun getHappyPlacesList(): ArrayList<HappyPlaceModel> {
        val happyPlaceList = ArrayList<HappyPlaceModel>()
        val selectQuery = "SELECT * FROM $TABLE_HAPPY_PLACE"
        val db = this.readableDatabase

        try {
            val cursor: Cursor = db.rawQuery(selectQuery, null)
            if (cursor.moveToFirst()) {
                do {
                        val place = HappyPlaceModel(
                        cursor.getInt(cursor.getColumnIndexOrThrow(KEY_ID)),
                        cursor.getString(cursor.getColumnIndexOrThrow(KEY_TITLE)),
                        cursor.getString(cursor.getColumnIndexOrThrow(KEY_IMAGE)),
                        cursor.getString(cursor.getColumnIndexOrThrow(KEY_DESCRIPTION)),
                        cursor.getString(cursor.getColumnIndexOrThrow(KEY_DATE)),
                        cursor.getString(cursor.getColumnIndexOrThrow(KEY_LOCATION)),
                        cursor.getDouble(cursor.getColumnIndexOrThrow(KEY_LATITUDE)),
                        cursor.getDouble(cursor.getColumnIndexOrThrow(KEY_LONGITUDE)),
                        )
                    happyPlaceList.add(place)
                } while (cursor.moveToNext())
            }
            cursor.close()
        } catch (e: SQLiteException) {
            db.execSQL(selectQuery)
            return ArrayList()
        }
        return happyPlaceList
    }
}