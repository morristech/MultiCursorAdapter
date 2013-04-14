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
import android.view.View;
import android.view.ViewGroup;


public abstract class ViewBuilder {


  /**
   * <p>
   * Returns the number of types of Views that will be created by getView(int, View, ViewGroup). Each type represents a set of views that can be
   * converted in getView(int, View, ViewGroup). If the ViewBuilder always returns the same type of View for all items, this method should return 1.
   * </p>
   * <p>
   * This method will only be called when when the ViewBuilder is set on the the MultiCursorAdapter.
   * </p>
   * 
   * @return The number of types of Views that will be created by this adapter
   */
  public abstract int getViewTypeCount();

  /**
   * Get the type of View that will be created by getView(int, View, ViewGroup) for the specified item.
   * 
   * @param position
   *          The position of the item within the adapter's data set whose view type we want.
   * @return An integer representing the type of View. Two views should share the same type if one can be converted to the other in getView(int, View,
   *         ViewGroup). Note: Integers must be in the range 0 to getViewTypeCount() - 1. IGNORE_ITEM_VIEW_TYPE can also be returned.
   */
  public abstract int getItemViewType(final int position);

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
