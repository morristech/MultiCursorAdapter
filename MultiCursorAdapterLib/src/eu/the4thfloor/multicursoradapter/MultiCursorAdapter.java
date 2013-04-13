
package eu.the4thfloor.multicursoradapter;

import android.content.Context;
import android.database.Cursor;
import android.provider.BaseColumns;
import android.util.SparseArray;
import android.util.SparseIntArray;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;


public class MultiCursorAdapter extends BaseAdapter {


  private final Context                  mContext;
  private final SparseArray<Cursor>      mCursors;
  private final SparseArray<ViewBuilder> mViewBuilders;
  private final SparseIntArray           mCounts;
  private final SparseIntArray           mRowIDColumns;
  private int                            mCountSum;


  public MultiCursorAdapter(final Context context, final int initialCapacity) {

    this.mContext = context;
    this.mCursors = new SparseArray<Cursor>(initialCapacity);
    this.mViewBuilders = new SparseArray<ViewBuilder>(initialCapacity);
    this.mCounts = new SparseIntArray(initialCapacity);
    this.mRowIDColumns = new SparseIntArray(initialCapacity);
  }

  @Override
  public final int getCount() {

    return this.mCountSum;
  }

  @Override
  public final Object getItem(final int position) {

    final int index = getIndexToPosition(position);
    final int realPosition = getRealPosition(position);

    final Cursor cursor = this.mCursors.get(index);
    if (cursor != null && cursor.moveToPosition(realPosition)) {
      return cursor;
    }

    return null;
  }

  @Override
  public final long getItemId(final int position) {

    final int index = getIndexToPosition(position);
    final int realPosition = getRealPosition(position);

    final Cursor cursor = this.mCursors.get(index);
    if (cursor != null && cursor.moveToPosition(realPosition)) {
      return cursor.getLong(this.mRowIDColumns.get(index));
    }

    return 0;
  }

  @Override
  public final boolean hasStableIds() {

    return true;
  }

  /**
   * @see android.widget.ListAdapter#getView(int, View, ViewGroup)
   */
  @Override
  public final View getView(final int position, final View convertView, final ViewGroup parent) {

    final int index = getIndexToPosition(position);
    final int realPosition = getRealPosition(position);

    final Cursor cursor = this.mCursors.get(index);
    final ViewBuilder viewBuilder = this.mViewBuilders.get(index);

    if (cursor == null || !cursor.moveToPosition(realPosition)) {
      throw new IllegalStateException(String.format("couldn't move cursor at index %d to position %d", index, position));
    }

    if (viewBuilder == null) {
      throw new IllegalStateException(String.format("missing ViewBuilder for cursor at index %d", index));
    }

    View v = null;
    if (convertView == null) {
      v = viewBuilder.newView(this.mContext, cursor, parent);
    } else {
      v = convertView;
    }
    viewBuilder.bindView(v, this.mContext, cursor);

    return v;
  }

  /**
   * Swap in a new Cursor, returning the old Cursor. Unlike {@link #changeCursor(Cursor)}, the returned old Cursor is <em>not</em> closed.
   * 
   * @param newCursor
   *          The new cursor to be used.
   * @param position
   *          The position of the new cursor.
   * @param viewBuilder
   *          The view builder who builds the views.
   * @return Returns the previously set Cursor, or null if there wasa not one.
   *         If the given new Cursor is the same instance is the previously set
   *         Cursor, null is also returned.
   */
  public final Cursor swapCursor(final Cursor newCursor, final ViewBuilder viewBuilder, final int position) {

    final Cursor oldCursor = this.mCursors.get(position);

    if (newCursor == oldCursor) {
      return null;
    }

    setCursor(newCursor, viewBuilder, position);
    notifyDataSetChanged();

    return oldCursor;
  }

  private void setCursor(final Cursor newCursor, final ViewBuilder viewBuilder, final int position) {

    final int oldCount = this.mCounts.get(position);
    this.mCountSum -= oldCount;

    if (newCursor != null) {

      final int newCount = newCursor.getCount();

      this.mCountSum += newCount;

      this.mCursors.put(position, newCursor);
      this.mViewBuilders.put(position, viewBuilder);
      this.mCounts.put(position, newCount);
      this.mRowIDColumns.put(position, newCursor.getColumnIndexOrThrow(BaseColumns._ID));

    } else {

      this.mCursors.put(position, null);
      this.mViewBuilders.put(position, null);
      this.mCounts.put(position, 0);
      this.mRowIDColumns.put(position, 0);
    }
  }

  private int getIndexToPosition(final int position) {

    int index = 0;
    int sum = 0;

    final int size = this.mCounts.size();
    for (int i = 0; i < size; i++) {

      sum += this.mCounts.get(i);
      if (sum > position) {
        index = i;
        break;
      }
    }

    return index;
  }

  private int getRealPosition(final int position) {

    int real = position;

    final int size = this.mCounts.size();
    for (int i = 0; i < size; i++) {

      final int count = this.mCounts.get(i);

      if (real < count) {
        break;
      }

      real -= count;
    }

    return real;
  }

}
