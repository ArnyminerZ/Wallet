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
import com.arnyminerz.wallet.database.data.FireflyCurrency;
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
public final class CurrenciesDao_Impl implements CurrenciesDao {
  private final RoomDatabase __db;

  private final EntityInsertionAdapter<FireflyCurrency> __insertionAdapterOfFireflyCurrency;

  private final EntityDeletionOrUpdateAdapter<FireflyCurrency> __deletionAdapterOfFireflyCurrency;

  private final EntityDeletionOrUpdateAdapter<FireflyCurrency> __updateAdapterOfFireflyCurrency;

  public CurrenciesDao_Impl(RoomDatabase __db) {
    this.__db = __db;
    this.__insertionAdapterOfFireflyCurrency = new EntityInsertionAdapter<FireflyCurrency>(__db) {
      @Override
      public String createQuery() {
        return "INSERT OR ABORT INTO `ff_currencies` (`id`,`code`,`symbol`,`decimalPlaces`,`default`) VALUES (?,?,?,?,?)";
      }

      @Override
      public void bind(SupportSQLiteStatement stmt, FireflyCurrency value) {
        stmt.bindLong(1, value.getId());
        if (value.getCode() == null) {
          stmt.bindNull(2);
        } else {
          stmt.bindString(2, value.getCode());
        }
        if (value.getSymbol() == null) {
          stmt.bindNull(3);
        } else {
          stmt.bindString(3, value.getSymbol());
        }
        stmt.bindLong(4, value.getDecimalPlaces());
        final int _tmp = value.getDefault() ? 1 : 0;
        stmt.bindLong(5, _tmp);
      }
    };
    this.__deletionAdapterOfFireflyCurrency = new EntityDeletionOrUpdateAdapter<FireflyCurrency>(__db) {
      @Override
      public String createQuery() {
        return "DELETE FROM `ff_currencies` WHERE `id` = ?";
      }

      @Override
      public void bind(SupportSQLiteStatement stmt, FireflyCurrency value) {
        stmt.bindLong(1, value.getId());
      }
    };
    this.__updateAdapterOfFireflyCurrency = new EntityDeletionOrUpdateAdapter<FireflyCurrency>(__db) {
      @Override
      public String createQuery() {
        return "UPDATE OR ABORT `ff_currencies` SET `id` = ?,`code` = ?,`symbol` = ?,`decimalPlaces` = ?,`default` = ? WHERE `id` = ?";
      }

      @Override
      public void bind(SupportSQLiteStatement stmt, FireflyCurrency value) {
        stmt.bindLong(1, value.getId());
        if (value.getCode() == null) {
          stmt.bindNull(2);
        } else {
          stmt.bindString(2, value.getCode());
        }
        if (value.getSymbol() == null) {
          stmt.bindNull(3);
        } else {
          stmt.bindString(3, value.getSymbol());
        }
        stmt.bindLong(4, value.getDecimalPlaces());
        final int _tmp = value.getDefault() ? 1 : 0;
        stmt.bindLong(5, _tmp);
        stmt.bindLong(6, value.getId());
      }
    };
  }

  @Override
  public void addAll(final FireflyCurrency... values) {
    __db.assertNotSuspendingTransaction();
    __db.beginTransaction();
    try {
      __insertionAdapterOfFireflyCurrency.insert(values);
      __db.setTransactionSuccessful();
    } finally {
      __db.endTransaction();
    }
  }

  @Override
  public void deleteAll(final FireflyCurrency... values) {
    __db.assertNotSuspendingTransaction();
    __db.beginTransaction();
    try {
      __deletionAdapterOfFireflyCurrency.handleMultiple(values);
      __db.setTransactionSuccessful();
    } finally {
      __db.endTransaction();
    }
  }

  @Override
  public void updateAll(final FireflyCurrency... values) {
    __db.assertNotSuspendingTransaction();
    __db.beginTransaction();
    try {
      __updateAdapterOfFireflyCurrency.handleMultiple(values);
      __db.setTransactionSuccessful();
    } finally {
      __db.endTransaction();
    }
  }

  @Override
  public List<FireflyCurrency> getAll() {
    final String _sql = "SELECT * FROM ff_currencies";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 0);
    __db.assertNotSuspendingTransaction();
    final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
    try {
      final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
      final int _cursorIndexOfCode = CursorUtil.getColumnIndexOrThrow(_cursor, "code");
      final int _cursorIndexOfSymbol = CursorUtil.getColumnIndexOrThrow(_cursor, "symbol");
      final int _cursorIndexOfDecimalPlaces = CursorUtil.getColumnIndexOrThrow(_cursor, "decimalPlaces");
      final int _cursorIndexOfDefault = CursorUtil.getColumnIndexOrThrow(_cursor, "default");
      final List<FireflyCurrency> _result = new ArrayList<FireflyCurrency>(_cursor.getCount());
      while(_cursor.moveToNext()) {
        final FireflyCurrency _item;
        final long _tmpId;
        _tmpId = _cursor.getLong(_cursorIndexOfId);
        final String _tmpCode;
        if (_cursor.isNull(_cursorIndexOfCode)) {
          _tmpCode = null;
        } else {
          _tmpCode = _cursor.getString(_cursorIndexOfCode);
        }
        final String _tmpSymbol;
        if (_cursor.isNull(_cursorIndexOfSymbol)) {
          _tmpSymbol = null;
        } else {
          _tmpSymbol = _cursor.getString(_cursorIndexOfSymbol);
        }
        final long _tmpDecimalPlaces;
        _tmpDecimalPlaces = _cursor.getLong(_cursorIndexOfDecimalPlaces);
        final boolean _tmpDefault;
        final int _tmp;
        _tmp = _cursor.getInt(_cursorIndexOfDefault);
        _tmpDefault = _tmp != 0;
        _item = new FireflyCurrency(_tmpId,_tmpCode,_tmpSymbol,_tmpDecimalPlaces,_tmpDefault);
        _result.add(_item);
      }
      return _result;
    } finally {
      _cursor.close();
      _statement.release();
    }
  }

  @Override
  public LiveData<List<FireflyCurrency>> getAllLive() {
    final String _sql = "SELECT * FROM ff_currencies";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 0);
    return __db.getInvalidationTracker().createLiveData(new String[]{"ff_currencies"}, false, new Callable<List<FireflyCurrency>>() {
      @Override
      public List<FireflyCurrency> call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
          final int _cursorIndexOfCode = CursorUtil.getColumnIndexOrThrow(_cursor, "code");
          final int _cursorIndexOfSymbol = CursorUtil.getColumnIndexOrThrow(_cursor, "symbol");
          final int _cursorIndexOfDecimalPlaces = CursorUtil.getColumnIndexOrThrow(_cursor, "decimalPlaces");
          final int _cursorIndexOfDefault = CursorUtil.getColumnIndexOrThrow(_cursor, "default");
          final List<FireflyCurrency> _result = new ArrayList<FireflyCurrency>(_cursor.getCount());
          while(_cursor.moveToNext()) {
            final FireflyCurrency _item;
            final long _tmpId;
            _tmpId = _cursor.getLong(_cursorIndexOfId);
            final String _tmpCode;
            if (_cursor.isNull(_cursorIndexOfCode)) {
              _tmpCode = null;
            } else {
              _tmpCode = _cursor.getString(_cursorIndexOfCode);
            }
            final String _tmpSymbol;
            if (_cursor.isNull(_cursorIndexOfSymbol)) {
              _tmpSymbol = null;
            } else {
              _tmpSymbol = _cursor.getString(_cursorIndexOfSymbol);
            }
            final long _tmpDecimalPlaces;
            _tmpDecimalPlaces = _cursor.getLong(_cursorIndexOfDecimalPlaces);
            final boolean _tmpDefault;
            final int _tmp;
            _tmp = _cursor.getInt(_cursorIndexOfDefault);
            _tmpDefault = _tmp != 0;
            _item = new FireflyCurrency(_tmpId,_tmpCode,_tmpSymbol,_tmpDecimalPlaces,_tmpDefault);
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
