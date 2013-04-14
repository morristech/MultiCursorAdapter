/*
 * Copyright (C) 2013 Ralph Bergmann | the4thFloor.eu and The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package eu.the4thfloor.multicursoradapter;

import android.content.Context;
import android.database.Cursor;
import android.provider.BaseColumns;
import android.util.SparseArray;
import android.util.SparseIntArray;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;


public final class MultiCursorAdapter extends BaseAdapter {


  private final Context                  mContext;
  private final int                      mInitialCapacity;
  private final SparseArray<Cursor>      mCursors;
  private final SparseArray<ViewBuilder> mViewBuilders;
  private final SparseIntArray           mCounts;
  private int                            mCountSum;
  private final SparseIntArray           mViewTypeCounts;
  private int                            mViewTypeCountSum;
  private final SparseIntArray           mRowIDColumns;


  public MultiCursorAdapter(final Context context, final int initialCapacity, final ViewBuilder[] viewBuilders) {

    this.mContext = context;
    this.mInitialCapacity = initialCapacity;
    this.mCursors = new SparseArray<Cursor>(initialCapacity);
    this.mViewBuilders = new SparseArray<ViewBuilder>(initialCapacity);
    this.mCounts = new SparseIntArray(initialCapacity);
    this.mViewTypeCounts = new SparseIntArray(initialCapacity);
    this.mRowIDColumns = new SparseIntArray(initialCapacity);

    for (int i = 0; i < initialCapacity; i++) {

      final ViewBuilder viewBuilder = viewBuilders[i];
      final int viewTypeCount = viewBuilder.getViewTypeCount();

      this.mViewBuilders.put(i, viewBuilder);
      this.mViewTypeCounts.put(i, viewTypeCount);
      this.mViewTypeCountSum += viewTypeCount;
    }
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
  public final int getViewTypeCount() {

    return this.mViewTypeCountSum;
  }

  @Override
  public final int getItemViewType(final int position) {

    final int index = getIndexToPosition(position);
    final int realPosition = getRealPosition(position);

    final ViewBuilder viewBuilder = this.mViewBuilders.get(index);
    if (viewBuilder == null) {
      throw new IllegalStateException(String.format("missing ViewBuilder for cursor at index %d", index));
    }

    return viewBuilder.getItemViewType(realPosition);
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

    if (cursor == null) {
      throw new IllegalStateException(String.format("missing cursor at index %d", index));
    }

    if (!cursor.moveToPosition(realPosition)) {
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
   * @return Returns the previously set Cursor, or null if there wasa not one.
   *         If the given new Cursor is the same instance is the previously set
   *         Cursor, null is also returned.
   */
  public final Cursor swapCursor(final Cursor newCursor, final int position) {

    final Cursor oldCursor = this.mCursors.get(position);

    if (newCursor == oldCursor) {
      return null;
    }

    setCursor(newCursor, position);
    notifyDataSetChanged();

    return oldCursor;
  }

  private void setCursor(final Cursor newCursor, final int position) {

    final int oldCount = this.mCounts.get(position);
    this.mCountSum -= oldCount;

    if (newCursor != null) {

      final int newCount = newCursor.getCount();

      this.mCountSum += newCount;

      this.mCursors.put(position, newCursor);
      this.mCounts.put(position, newCount);
      this.mRowIDColumns.put(position, newCursor.getColumnIndexOrThrow(BaseColumns._ID));

    } else {

      this.mCursors.put(position, null);
      this.mCounts.put(position, 0);
      this.mRowIDColumns.put(position, 0);
    }
  }

  private int getIndexToPosition(final int position) {

    int index = 0;
    int sum = 0;

    for (int i = 0; i < this.mInitialCapacity; i++) {

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

    for (int i = 0; i < this.mInitialCapacity; i++) {

      final int count = this.mCounts.get(i);

      if (real < count) {
        break;
      }

      real -= count;
    }

    return real;
  }
}
