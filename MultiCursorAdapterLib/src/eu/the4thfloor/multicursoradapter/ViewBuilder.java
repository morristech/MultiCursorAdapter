
package eu.the4thfloor.multicursoradapter;

import android.content.Context;
import android.database.Cursor;
import android.view.View;
import android.view.ViewGroup;


public abstract class ViewBuilder {


  /**
   * Makes a new view to hold the data pointed to by cursor.
   * 
   * @param context
   *          Interface to application's global information
   * @param cursor
   *          The cursor from which to get the data. The cursor is already
   *          moved to the correct position.
   * @param parent
   *          The parent to which the new view is attached to
   * @return the newly created view.
   */
  public abstract View newView(Context context, Cursor cursor, ViewGroup parent);

  /**
   * Bind an existing view to the data pointed to by cursor
   * 
   * @param view
   *          Existing view, returned earlier by newView
   * @param context
   *          Interface to application's global information
   * @param cursor
   *          The cursor from which to get the data. The cursor is already
   *          moved to the correct position.
   */
  public abstract void bindView(View view, Context context, Cursor cursor);
}
