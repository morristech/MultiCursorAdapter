## MultiCursorAdapter

This is a Android CursorAdapter which can handle multiple Cursors as datasource for one ListView.

#### The MultiCursorAdapter Constructor

The MultiCursorAdapter needs 3 parameters, the Context, the number of Cursors you want to add and ViewBuilders, one for each Cursor.

```java
  final ViewBuilder[] viewBuilders = new ViewBuilder[] { new ViewBuilder0(), new ViewBuilder1() };
  this.mAdapter = new MultiCursorAdapter(getApplicationContext(), 2, viewBuilders);
  this.mList.setAdapter(this.mAdapter);
```

#### The ViewBuilder

The ViewBuilder is the guy who creates the Views. It has 4 methods and all are working the same way you know from a normal [CursorAdapter](http://developer.android.com/reference/android/widget/CursorAdapter.html).

```java
  public static class ViewBuilder0 extends ViewBuilder {


    @Override
    public int getViewTypeCount() {

      return 2;
    }

    @Override
    public int getItemViewType(final int position) {

      return position % 2;
    }

    @Override
    public View newView(final Context context, final Cursor cursor, final ViewGroup parent) {

      final LayoutInflater inflater = LayoutInflater.from(context);

      final int position = cursor.getPosition();
      switch (position % 2) {
        case 0:
          return inflater.inflate(R.layout.table_item_0, parent, false);
        case 1:
          return inflater.inflate(R.layout.table_item_1, parent, false);
      }

      return null;
    }

    @Override
    public void bindView(final View view, final Context context, final Cursor cursor) {

      final TextView textView = (TextView) view.findViewById(R.id.textView);
      textView.setText(cursor.getString(1));
    }
  }
```

#### Swap the Cursor

Android prefers [Loaders](http://developer.android.com/guide/components/loaders.html) for this loading stuff. You can use it also with the MultiCursorAdapter. In the onLoadFinished you have to swap the cursors. But the MultiCursorAdapter needs a second parameter, the position for the cursor. The position defines the order of all cursors.

```java
  @Override
  public void onLoadFinished(final Loader<Cursor> loader, final Cursor cursor) {

    this.mAdapter.swapCursor(cursor, loader.getId());
  }
```

#####  Copyright (C) 2013 Ralph Bergmann | the4thFloor.eu and The Android Open Source Project

Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at
 
  http://www.apache.org/licenses/LICENSE-2.0
 
Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.