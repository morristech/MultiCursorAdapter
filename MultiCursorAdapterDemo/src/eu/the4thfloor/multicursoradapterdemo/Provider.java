
package eu.the4thfloor.multicursoradapterdemo;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteQueryBuilder;
import android.net.Uri;
import android.provider.BaseColumns;


public class Provider extends ContentProvider {


  public static final String      AUTHORITY               = "eu.the4thfloor.multicursoradapterdemo";

  private static final UriMatcher matcher;

  private static final int        LIST_TABLE0             = 0;
  private static final int        LIST_TABLE1             = 1;

  private static final String     CONTENT_URI_PATH_TABLE0 = "table0";
  private static final String     CONTENT_URI_PATH_TABLE1 = "table1";

  public static final Uri         CONTENT_URI_TABLE0      = Uri.parse("content://" + Provider.AUTHORITY + "/" + Provider.CONTENT_URI_PATH_TABLE0);
  public static final Uri         CONTENT_URI_TABLE1      = Uri.parse("content://" + Provider.AUTHORITY + "/" + Provider.CONTENT_URI_PATH_TABLE1);

  private static final String     CONTENT_TYPE_TABLE0     = "vnd.android.cursor.dir/vnd." + Provider.AUTHORITY + "."
                                                              + Provider.CONTENT_URI_PATH_TABLE0;
  private static final String     CONTENT_TYPE_TABLE1     = "vnd.android.cursor.dir/vnd." + Provider.AUTHORITY + "."
                                                              + Provider.CONTENT_URI_PATH_TABLE1;

  private DBHelper                mOpenHelper;

  static {

    matcher = new UriMatcher(UriMatcher.NO_MATCH);
    Provider.matcher.addURI(Provider.AUTHORITY, Provider.CONTENT_URI_PATH_TABLE0, Provider.LIST_TABLE0);
    Provider.matcher.addURI(Provider.AUTHORITY, Provider.CONTENT_URI_PATH_TABLE1, Provider.LIST_TABLE1);
  }


  @Override
  public boolean onCreate() {

    this.mOpenHelper = DBHelper.singleton(getContext());
    return true;
  }

  @Override
  public Cursor query(final Uri uri, final String[] projection, final String selection, final String[] selectionArgs, final String sortOrder) {

    final SQLiteQueryBuilder qb = new SQLiteQueryBuilder();

    switch (Provider.matcher.match(uri)) {
      case LIST_TABLE0: {
        qb.setTables("table0");
        break;
      }
      case LIST_TABLE1: {
        qb.setTables("table1");
        break;
      }
      default:
        throw new IllegalArgumentException("Unknown URI " + uri);
    }

    final SQLiteDatabase db = this.mOpenHelper.getWritableDatabase();
    final Cursor c = qb.query(db, new String[] { BaseColumns._ID, "text" }, selection, selectionArgs, null, null, null);

    if (c == null) {
      return null;
    }

    c.setNotificationUri(getContext().getContentResolver(), uri);

    return c;
  }

  @Override
  public Uri insert(final Uri uri, final ContentValues values) {

    return null;
  }

  @Override
  public int update(final Uri uri, final ContentValues values, final String selection, final String[] selectionArgs) {

    return 0;
  }

  @Override
  public int delete(final Uri uri, final String selection, final String[] selectionArgs) {

    return 0;
  }

  @Override
  public String getType(final Uri uri) {

    switch (Provider.matcher.match(uri)) {
      case LIST_TABLE0:
        return Provider.CONTENT_TYPE_TABLE0;
      case LIST_TABLE1:
        return Provider.CONTENT_TYPE_TABLE1;
      default:
        throw new IllegalArgumentException("Unknown URI " + uri);
    }
  }
}
