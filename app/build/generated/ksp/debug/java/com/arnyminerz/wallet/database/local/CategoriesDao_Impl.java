package com.arnyminerz.wallet.database.local;

import android.database.Cursor;
import androidx.lifecycle.LiveData;
import androidx.room.EntityDeletionOrUpdateAdapter;
import androidx.room.EntityInsertionAdapter;
import androidx.room.RoomDatabase;
import androidx.room.RoomSQLiteQuery;
import androidx.room.util.CursorUtil;
import androidx.room.util.DBUtil;
import androidx.sqlite.db.SupportSQLiteStatement;
import com.arnyminerz.wallet.database.data.FireflyCategory;
import java.lang.Class;
import java.lang.Exception;
import java.lang.Override;
import java.lang.String;
import java.lang.SuppressWarnings;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.Callable;

@SuppressWarnings({"unchecked", "deprecation"})
public final class CategoriesDao_Impl implements CategoriesDao {
  private final RoomDatabase __db;

  private final EntityInsertionAdapter<FireflyCategory> __insertionAdapterOfFireflyCategory;

  private final EntityDeletionOrUpdateAdapter<FireflyCategory> __deletionAdapterOfFireflyCategory;

  private final EntityDeletionOrUpdateAdapter<FireflyCategory> __updateAdapterOfFireflyCategory;

  public CategoriesDao_Impl(RoomDatabase __db) {
    this.__db = __db;
    this.__insertionAdapterOfFireflyCategory = new EntityInsertionAdapter<FireflyCategory>(__db) {
      @Override
      public String createQuery() {
        return "INSERT OR ABORT INTO `ff_categories` (`id`,`name`) VALUES (?,?)";
      }

      @Override
      public void bind(SupportSQLiteStatement stmt, FireflyCategory value) {
        stmt.bindLong(1, value.getId());
        if (value.getName() == null) {
          stmt.bindNull(2);
        } else {
          stmt.bindString(2, value.getName());
        }
      }
    };
    this.__deletionAdapterOfFireflyCategory = new EntityDeletionOrUpdateAdapter<FireflyCategory>(__db) {
      @Override
      public String createQuery() {
        return "DELETE FROM `ff_categories` WHERE `id` = ?";
      }

      @Override
      public void bind(SupportSQLiteStatement stmt, FireflyCategory value) {
        stmt.bindLong(1, value.getId());
      }
    };
    this.__updateAdapterOfFireflyCategory = new EntityDeletionOrUpdateAdapter<FireflyCategory>(__db) {
      @Override
      public String createQuery() {
        return "UPDATE OR ABORT `ff_categories` SET `id` = ?,`name` = ? WHERE `id` = ?";
      }

      @Override
      public void bind(SupportSQLiteStatement stmt, FireflyCategory value) {
        stmt.bindLong(1, value.getId());
        if (value.getName() == null) {
          stmt.bindNull(2);
        } else {
          stmt.bindString(2, value.getName());
        }
        stmt.bindLong(3, value.getId());
      }
    };
  }

  @Override
  public void addAll(final FireflyCategory... values) {
    __db.assertNotSuspendingTransaction();
    __db.beginTransaction();
    try {
      __insertionAdapterOfFireflyCategory.insert(values);
      __db.setTransactionSuccessful();
    } finally {
      __db.endTransaction();
    }
  }

  @Override
  public void deleteAll(final FireflyCategory... values) {
    __db.assertNotSuspendingTransaction();
    __db.beginTransaction();
    try {
      __deletionAdapterOfFireflyCategory.handleMultiple(values);
      __db.setTransactionSuccessful();
    } finally {
      __db.endTransaction();
    }
  }

  @Override
  public void updateAll(final FireflyCategory... values) {
    __db.assertNotSuspendingTransaction();
    __db.beginTransaction();
    try {
      __updateAdapterOfFireflyCategory.handleMultiple(values);
      __db.setTransactionSuccessful();
    } finally {
      __db.endTransaction();
    }
  }

  @Override
  public List<FireflyCategory> getAll() {
    final String _sql = "SELECT * FROM ff_categories";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 0);
    __db.assertNotSuspendingTransaction();
    final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
    try {
      final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
      final int _cursorIndexOfName = CursorUtil.getColumnIndexOrThrow(_cursor, "name");
      final List<FireflyCategory> _result = new ArrayList<FireflyCategory>(_cursor.getCount());
      while(_cursor.moveToNext()) {
        final FireflyCategory _item;
        final long _tmpId;
        _tmpId = _cursor.getLong(_cursorIndexOfId);
        final String _tmpName;
        if (_cursor.isNull(_cursorIndexOfName)) {
          _tmpName = null;
        } else {
          _tmpName = _cursor.getString(_cursorIndexOfName);
        }
        _item = new FireflyCategory(_tmpId,_tmpName);
        _result.add(_item);
      }
      return _result;
    } finally {
      _cursor.close();
      _statement.release();
    }
  }

  @Override
  public LiveData<List<FireflyCategory>> getAllLive() {
    final String _sql = "SELECT * FROM ff_categories";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 0);
    return __db.getInvalidationTracker().createLiveData(new String[]{"ff_categories"}, false, new Callable<List<FireflyCategory>>() {
      @Override
      public List<FireflyCategory> call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
          final int _cursorIndexOfName = CursorUtil.getColumnIndexOrThrow(_cursor, "name");
          final List<FireflyCategory> _result = new ArrayList<FireflyCategory>(_cursor.getCount());
          while(_cursor.moveToNext()) {
            final FireflyCategory _item;
            final long _tmpId;
            _tmpId = _cursor.getLong(_cursorIndexOfId);
            final String _tmpName;
            if (_cursor.isNull(_cursorIndexOfName)) {
              _tmpName = null;
            } else {
              _tmpName = _cursor.getString(_cursorIndexOfName);
            }
            _item = new FireflyCategory(_tmpId,_tmpName);
            _result.add(_item);
          }
          return _result;
        } finally {
          _cursor.close();
        }
      }

      @Override
      protected void finalize() {
        _statement.release();
      }
    });
  }

  public static List<Class<?>> getRequiredConverters() {
    return Collections.emptyList();
  }
}
