
package eu.the4thfloor.multicursoradapterdemo;

import android.content.Context;
import android.database.Cursor;
import android.os.Bundle;
import android.provider.BaseColumns;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;
import eu.the4thfloor.multicursoradapter.MultiCursorAdapter;
import eu.the4thfloor.multicursoradapter.ViewBuilder;


public class MainActivity extends FragmentActivity implements LoaderManager.LoaderCallbacks<Cursor> {


  private ListView           mList;
  private MultiCursorAdapter mAdapter;


  @Override
  protected void onCreate(final Bundle savedInstanceState) {

    super.onCreate(savedInstanceState);
    setContentView(R.layout.main);

    final ViewBuilder[] viewBuilders = new ViewBuilder[] { new ViewBuilder0(), new ViewBuilder1() };
    this.mAdapter = new MultiCursorAdapter(getApplicationContext(), 2, viewBuilders);

    this.mList = (ListView) findViewById(R.id.list);
    this.mList.setAdapter(this.mAdapter);

    getSupportLoaderManager().initLoader(0, null, this);
    getSupportLoaderManager().initLoader(1, null, this);
  }

  /*
   * LoaderManager.LoaderCallbacks<Cursor>
   */

  @Override
  public Loader<Cursor> onCreateLoader(final int id, final Bundle args) {

    switch (id) {
      case 0:
        return new CursorLoader(getApplicationContext(),
                                Provider.CONTENT_URI_TABLE0,
                                new String[] { BaseColumns._ID, "text" },
                                null,
                                null,
                                "text ASC");
      case 1:
        return new CursorLoader(getApplicationContext(),
                                Provider.CONTENT_URI_TABLE1,
                                new String[] { BaseColumns._ID, "text" },
                                null,
                                null,
                                "text ASC");
    }

    return null;
  }

  @Override
  public void onLoadFinished(final Loader<Cursor> loader, final Cursor cursor) {

    this.mAdapter.swapCursor(cursor, loader.getId());
  }

  @Override
  public void onLoaderReset(final Loader<Cursor> loader) {

    this.mAdapter.swapCursor(null, loader.getId());
  }


  /*
   * ViewBuilders
   */

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

  public static class ViewBuilder1 extends ViewBuilder {


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
          return inflater.inflate(R.layout.table_item_2, parent, false);
        case 1:
          return inflater.inflate(R.layout.table_item_3, parent, false);
      }

      return null;
    }

    @Override
    public void bindView(final View view, final Context context, final Cursor cursor) {

      final TextView textView = (TextView) view.findViewById(R.id.textView);
      textView.setText(cursor.getString(1));
    }
  }
}
