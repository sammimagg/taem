package `is`.hi.hbv601g.taem.Storage

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.provider.BaseColumns

class db {
    object SessionUserContract {
        object SessionUserEntry : BaseColumns {
            const val TABLE_NAME = "SessionUser"
            const val COLUMN_NAME_USERNAME = "username"
            const val COLUMN_NAME_AUTH_TOKEN = "auth_token"
            const val COLUMN_NAME_ACCOUNT_TYPE = "account_type"
        }

        private const val SQL_CREATE_ENTRIES =
            "CREATE TABLE ${SessionUserEntry.TABLE_NAME} (" +
                    "${BaseColumns._ID} INTEGER PRIMARY KEY," +
                    "${SessionUserEntry.COLUMN_NAME_USERNAME} TEXT," +
                    "${SessionUserEntry.COLUMN_NAME_AUTH_TOKEN} TEXT," +
                    "${SessionUserEntry.COLUMN_NAME_ACCOUNT_TYPE} TEXT)"

        private const val SQL_BOBBY_TABLES =
            "DROP TABLE IF EXISTS ${SessionUserEntry.TABLE_NAME}"

        class DBHelper (context : Context) : SQLiteOpenHelper (context, DATABASE_NAME,
            null, DATABASE_VERSION) {
            override fun onCreate(db: SQLiteDatabase) {
                db.execSQL(SQL_CREATE_ENTRIES)
            }

            override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
                db.execSQL(SQL_BOBBY_TABLES)
            }

            override fun onDowngrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
                onUpgrade(db, oldVersion, newVersion)
            }

            /*
            * ATH: Þegar SCHEMA er breytt þarf að
            *      Incrementa DATABASE_VERSION
            */
            companion object {
                const val DATABASE_VERSION = 1
                const val DATABASE_NAME = "SessionUser.db"
            }
        }
    }
}

/*
                "${BaseColumns._ID} INTEGER PRIMARY KEY," +
                "${FeedEntry.COLUMN_NAME_TITLE} TEXT," +
                "${FeedEntry.COLUMN_NAME_SUBTITLE} TEXT)"

 */