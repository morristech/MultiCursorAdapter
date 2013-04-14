
package eu.the4thfloor.multicursoradapterdemo;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.provider.BaseColumns;


public class DBHelper extends SQLiteOpenHelper {


  private static final int    DATABASE_VERSION = 1;
  private static final String DATABASE_NAME    = "de.testberichte.tbapp";

  private static DBHelper     dh;


  public static synchronized DBHelper singleton(final Context context) {

    if (DBHelper.dh == null) {
      DBHelper.dh = new DBHelper(context.getApplicationContext());
    }

    return DBHelper.dh;
  }

  private DBHelper(final Context context) {

    super(context, DBHelper.DATABASE_NAME, null, DBHelper.DATABASE_VERSION);
  }

  @Override
  public void onCreate(final SQLiteDatabase db) {

    createTables(db);
  }

  @Override
  public void onUpgrade(final SQLiteDatabase db, final int oldVersion, final int newVersion) {

    createTables(db);
  }

  private void createTables(final SQLiteDatabase db) {

    db.execSQL("DROP TABLE IF EXISTS table0;");
    db.execSQL("CREATE TABLE table0 (" + BaseColumns._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, text TEXT);");
    for (int i = 0; i < 10; i++) {
      db.execSQL(String.format("INSERT INTO table0 VALUES (%d, \"table 0 row %d\");", i, i));
    }

    db.execSQL("DROP TABLE IF EXISTS table1;");
    db.execSQL("CREATE TABLE table1 (" + BaseColumns._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, text TEXT);");
    for (int i = 0; i < 10; i++) {
      db.execSQL(String.format("INSERT INTO table1 VALUES (%d, \"table 1 row %d\");", i, i));
    }
  }
}
