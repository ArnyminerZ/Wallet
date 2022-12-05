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
import com.arnyminerz.wallet.database.data.FireflyAccount;
import com.arnyminerz.wallet.database.data.FireflyCurrency;
import com.arnyminerz.wallet.database.data.FireflyGeoRef;
import java.lang.Class;
import java.lang.Double;
import java.lang.Exception;
import java.lang.IllegalStateException;
import java.lang.Long;
import java.lang.Override;
import java.lang.String;
import java.lang.SuppressWarnings;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.concurrent.Callable;

@SuppressWarnings({"unchecked", "deprecation"})
public final class AccountsDao_Impl implements AccountsDao {
  private final RoomDatabase __db;

  private final EntityInsertionAdapter<FireflyAccount> __insertionAdapterOfFireflyAccount;

  private final Converters __converters = new Converters();

  private final EntityDeletionOrUpdateAdapter<FireflyAccount> __deletionAdapterOfFireflyAccount;

  private final EntityDeletionOrUpdateAdapter<FireflyAccount> __updateAdapterOfFireflyAccount;

  public AccountsDao_Impl(RoomDatabase __db) {
    this.__db = __db;
    this.__insertionAdapterOfFireflyAccount = new EntityInsertionAdapter<FireflyAccount>(__db) {
      @Override
      public String createQuery() {
        return "INSERT OR ABORT INTO `ff_accounts` (`id`,`createdAt`,`active`,`order`,`name`,`type`,`role`,`currency`,`balance`,`balanceDate`,`notes`,`monthlyPaymentDate`,`creditCardType`,`accountNumber`,`iban`,`bic`,`virtualBalance`,`openingBalance`,`openingBalanceDate`,`liabilityType`,`liabilityDirection`,`interest`,`interestPeriod`,`currentDebt`,`includeNetWorth`,`geoRef`) VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
      }

      @Override
      public void bind(SupportSQLiteStatement stmt, FireflyAccount value) {
        stmt.bindLong(1, value.getId());
        final Long _tmp = __converters.dateToTimestamp(value.getCreatedAt());
        if (_tmp == null) {
          stmt.bindNull(2);
        } else {
          stmt.bindLong(2, _tmp);
        }
        final int _tmp_1 = value.getActive() ? 1 : 0;
        stmt.bindLong(3, _tmp_1);
        if (value.getOrder() == null) {
          stmt.bindNull(4);
        } else {
          stmt.bindLong(4, value.getOrder());
        }
        if (value.getName() == null) {
          stmt.bindNull(5);
        } else {
          stmt.bindString(5, value.getName());
        }
        if (value.getType() == null) {
          stmt.bindNull(6);
        } else {
          stmt.bindString(6, value.getType());
        }
        if (value.getRole() == null) {
          stmt.bindNull(7);
        } else {
          stmt.bindString(7, value.getRole());
        }
        final String _tmp_2 = __converters.fromCurrency(value.getCurrency());
        if (_tmp_2 == null) {
          stmt.bindNull(8);
        } else {
          stmt.bindString(8, _tmp_2);
        }
        stmt.bindDouble(9, value.getBalance());
        final Long _tmp_3 = __converters.dateToTimestamp(value.getBalanceDate());
        if (_tmp_3 == null) {
          stmt.bindNull(10);
        } else {
          stmt.bindLong(10, _tmp_3);
        }
        if (value.getNotes() == null) {
          stmt.bindNull(11);
        } else {
          stmt.bindString(11, value.getNotes());
        }
        final Long _tmp_4 = __converters.dateToTimestamp(value.getMonthlyPaymentDate());
        if (_tmp_4 == null) {
          stmt.bindNull(12);
        } else {
          stmt.bindLong(12, _tmp_4);
        }
        if (value.getCreditCardType() == null) {
          stmt.bindNull(13);
        } else {
          stmt.bindString(13, value.getCreditCardType());
        }
        if (value.getAccountNumber() == null) {
          stmt.bindNull(14);
        } else {
          stmt.bindString(14, value.getAccountNumber());
        }
        if (value.getIban() == null) {
          stmt.bindNull(15);
        } else {
          stmt.bindString(15, value.getIban());
        }
        if (value.getBic() == null) {
          stmt.bindNull(16);
        } else {
          stmt.bindString(16, value.getBic());
        }
        if (value.getVirtualBalance() == null) {
          stmt.bindNull(17);
        } else {
          stmt.bindDouble(17, value.getVirtualBalance());
        }
        if (value.getOpeningBalance() == null) {
          stmt.bindNull(18);
        } else {
          stmt.bindDouble(18, value.getOpeningBalance());
        }
        final Long _tmp_5 = __converters.dateToTimestamp(value.getOpeningBalanceDate());
        if (_tmp_5 == null) {
          stmt.bindNull(19);
        } else {
          stmt.bindLong(19, _tmp_5);
        }
        if (value.getLiabilityType() == null) {
          stmt.bindNull(20);
        } else {
          stmt.bindString(20, value.getLiabilityType());
        }
        if (value.getLiabilityDirection() == null) {
          stmt.bindNull(21);
        } else {
          stmt.bindString(21, value.getLiabilityDirection());
        }
        if (value.getInterest() == null) {
          stmt.bindNull(22);
        } else {
          stmt.bindDouble(22, value.getInterest());
        }
        if (value.getInterestPeriod() == null) {
          stmt.bindNull(23);
        } else {
          stmt.bindString(23, value.getInterestPeriod());
        }
        if (value.getCurrentDebt() == null) {
          stmt.bindNull(24);
        } else {
          stmt.bindString(24, value.getCurrentDebt());
        }
        final int _tmp_6 = value.getIncludeNetWorth() ? 1 : 0;
        stmt.bindLong(25, _tmp_6);
        final String _tmp_7 = __converters.fromGeoRef(value.getGeoRef());
        if (_tmp_7 == null) {
          stmt.bindNull(26);
        } else {
          stmt.bindString(26, _tmp_7);
        }
      }
    };
    this.__deletionAdapterOfFireflyAccount = new EntityDeletionOrUpdateAdapter<FireflyAccount>(__db) {
      @Override
      public String createQuery() {
        return "DELETE FROM `ff_accounts` WHERE `id` = ?";
      }

      @Override
      public void bind(SupportSQLiteStatement stmt, FireflyAccount value) {
        stmt.bindLong(1, value.getId());
      }
    };
    this.__updateAdapterOfFireflyAccount = new EntityDeletionOrUpdateAdapter<FireflyAccount>(__db) {
      @Override
      public String createQuery() {
        return "UPDATE OR ABORT `ff_accounts` SET `id` = ?,`createdAt` = ?,`active` = ?,`order` = ?,`name` = ?,`type` = ?,`role` = ?,`currency` = ?,`balance` = ?,`balanceDate` = ?,`notes` = ?,`monthlyPaymentDate` = ?,`creditCardType` = ?,`accountNumber` = ?,`iban` = ?,`bic` = ?,`virtualBalance` = ?,`openingBalance` = ?,`openingBalanceDate` = ?,`liabilityType` = ?,`liabilityDirection` = ?,`interest` = ?,`interestPeriod` = ?,`currentDebt` = ?,`includeNetWorth` = ?,`geoRef` = ? WHERE `id` = ?";
      }

      @Override
      public void bind(SupportSQLiteStatement stmt, FireflyAccount value) {
        stmt.bindLong(1, value.getId());
        final Long _tmp = __converters.dateToTimestamp(value.getCreatedAt());
        if (_tmp == null) {
          stmt.bindNull(2);
        } else {
          stmt.bindLong(2, _tmp);
        }
        final int _tmp_1 = value.getActive() ? 1 : 0;
        stmt.bindLong(3, _tmp_1);
        if (value.getOrder() == null) {
          stmt.bindNull(4);
        } else {
          stmt.bindLong(4, value.getOrder());
        }
        if (value.getName() == null) {
          stmt.bindNull(5);
        } else {
          stmt.bindString(5, value.getName());
        }
        if (value.getType() == null) {
          stmt.bindNull(6);
        } else {
          stmt.bindString(6, value.getType());
        }
        if (value.getRole() == null) {
          stmt.bindNull(7);
        } else {
          stmt.bindString(7, value.getRole());
        }
        final String _tmp_2 = __converters.fromCurrency(value.getCurrency());
        if (_tmp_2 == null) {
          stmt.bindNull(8);
        } else {
          stmt.bindString(8, _tmp_2);
        }
        stmt.bindDouble(9, value.getBalance());
        final Long _tmp_3 = __converters.dateToTimestamp(value.getBalanceDate());
        if (_tmp_3 == null) {
          stmt.bindNull(10);
        } else {
          stmt.bindLong(10, _tmp_3);
        }
        if (value.getNotes() == null) {
          stmt.bindNull(11);
        } else {
          stmt.bindString(11, value.getNotes());
        }
        final Long _tmp_4 = __converters.dateToTimestamp(value.getMonthlyPaymentDate());
        if (_tmp_4 == null) {
          stmt.bindNull(12);
        } else {
          stmt.bindLong(12, _tmp_4);
        }
        if (value.getCreditCardType() == null) {
          stmt.bindNull(13);
        } else {
          stmt.bindString(13, value.getCreditCardType());
        }
        if (value.getAccountNumber() == null) {
          stmt.bindNull(14);
        } else {
          stmt.bindString(14, value.getAccountNumber());
        }
        if (value.getIban() == null) {
          stmt.bindNull(15);
        } else {
          stmt.bindString(15, value.getIban());
        }
        if (value.getBic() == null) {
          stmt.bindNull(16);
        } else {
          stmt.bindString(16, value.getBic());
        }
        if (value.getVirtualBalance() == null) {
          stmt.bindNull(17);
        } else {
          stmt.bindDouble(17, value.getVirtualBalance());
        }
        if (value.getOpeningBalance() == null) {
          stmt.bindNull(18);
        } else {
          stmt.bindDouble(18, value.getOpeningBalance());
        }
        final Long _tmp_5 = __converters.dateToTimestamp(value.getOpeningBalanceDate());
        if (_tmp_5 == null) {
          stmt.bindNull(19);
        } else {
          stmt.bindLong(19, _tmp_5);
        }
        if (value.getLiabilityType() == null) {
          stmt.bindNull(20);
        } else {
          stmt.bindString(20, value.getLiabilityType());
        }
        if (value.getLiabilityDirection() == null) {
          stmt.bindNull(21);
        } else {
          stmt.bindString(21, value.getLiabilityDirection());
        }
        if (value.getInterest() == null) {
          stmt.bindNull(22);
        } else {
          stmt.bindDouble(22, value.getInterest());
        }
        if (value.getInterestPeriod() == null) {
          stmt.bindNull(23);
        } else {
          stmt.bindString(23, value.getInterestPeriod());
        }
        if (value.getCurrentDebt() == null) {
          stmt.bindNull(24);
        } else {
          stmt.bindString(24, value.getCurrentDebt());
        }
        final int _tmp_6 = value.getIncludeNetWorth() ? 1 : 0;
        stmt.bindLong(25, _tmp_6);
        final String _tmp_7 = __converters.fromGeoRef(value.getGeoRef());
        if (_tmp_7 == null) {
          stmt.bindNull(26);
        } else {
          stmt.bindString(26, _tmp_7);
        }
        stmt.bindLong(27, value.getId());
      }
    };
  }

  @Override
  public void addAll(final FireflyAccount... values) {
    __db.assertNotSuspendingTransaction();
    __db.beginTransaction();
    try {
      __insertionAdapterOfFireflyAccount.insert(values);
      __db.setTransactionSuccessful();
    } finally {
      __db.endTransaction();
    }
  }

  @Override
  public void deleteAll(final FireflyAccount... values) {
    __db.assertNotSuspendingTransaction();
    __db.beginTransaction();
    try {
      __deletionAdapterOfFireflyAccount.handleMultiple(values);
      __db.setTransactionSuccessful();
    } finally {
      __db.endTransaction();
    }
  }

  @Override
  public void updateAll(final FireflyAccount... values) {
    __db.assertNotSuspendingTransaction();
    __db.beginTransaction();
    try {
      __updateAdapterOfFireflyAccount.handleMultiple(values);
      __db.setTransactionSuccessful();
    } finally {
      __db.endTransaction();
    }
  }

  @Override
  public List<FireflyAccount> getAll() {
    final String _sql = "SELECT * FROM ff_accounts";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 0);
    __db.assertNotSuspendingTransaction();
    final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
    try {
      final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
      final int _cursorIndexOfCreatedAt = CursorUtil.getColumnIndexOrThrow(_cursor, "createdAt");
      final int _cursorIndexOfActive = CursorUtil.getColumnIndexOrThrow(_cursor, "active");
      final int _cursorIndexOfOrder = CursorUtil.getColumnIndexOrThrow(_cursor, "order");
      final int _cursorIndexOfName = CursorUtil.getColumnIndexOrThrow(_cursor, "name");
      final int _cursorIndexOfType = CursorUtil.getColumnIndexOrThrow(_cursor, "type");
      final int _cursorIndexOfRole = CursorUtil.getColumnIndexOrThrow(_cursor, "role");
      final int _cursorIndexOfCurrency = CursorUtil.getColumnIndexOrThrow(_cursor, "currency");
      final int _cursorIndexOfBalance = CursorUtil.getColumnIndexOrThrow(_cursor, "balance");
      final int _cursorIndexOfBalanceDate = CursorUtil.getColumnIndexOrThrow(_cursor, "balanceDate");
      final int _cursorIndexOfNotes = CursorUtil.getColumnIndexOrThrow(_cursor, "notes");
      final int _cursorIndexOfMonthlyPaymentDate = CursorUtil.getColumnIndexOrThrow(_cursor, "monthlyPaymentDate");
      final int _cursorIndexOfCreditCardType = CursorUtil.getColumnIndexOrThrow(_cursor, "creditCardType");
      final int _cursorIndexOfAccountNumber = CursorUtil.getColumnIndexOrThrow(_cursor, "accountNumber");
      final int _cursorIndexOfIban = CursorUtil.getColumnIndexOrThrow(_cursor, "iban");
      final int _cursorIndexOfBic = CursorUtil.getColumnIndexOrThrow(_cursor, "bic");
      final int _cursorIndexOfVirtualBalance = CursorUtil.getColumnIndexOrThrow(_cursor, "virtualBalance");
      final int _cursorIndexOfOpeningBalance = CursorUtil.getColumnIndexOrThrow(_cursor, "openingBalance");
      final int _cursorIndexOfOpeningBalanceDate = CursorUtil.getColumnIndexOrThrow(_cursor, "openingBalanceDate");
      final int _cursorIndexOfLiabilityType = CursorUtil.getColumnIndexOrThrow(_cursor, "liabilityType");
      final int _cursorIndexOfLiabilityDirection = CursorUtil.getColumnIndexOrThrow(_cursor, "liabilityDirection");
      final int _cursorIndexOfInterest = CursorUtil.getColumnIndexOrThrow(_cursor, "interest");
      final int _cursorIndexOfInterestPeriod = CursorUtil.getColumnIndexOrThrow(_cursor, "interestPeriod");
      final int _cursorIndexOfCurrentDebt = CursorUtil.getColumnIndexOrThrow(_cursor, "currentDebt");
      final int _cursorIndexOfIncludeNetWorth = CursorUtil.getColumnIndexOrThrow(_cursor, "includeNetWorth");
      final int _cursorIndexOfGeoRef = CursorUtil.getColumnIndexOrThrow(_cursor, "geoRef");
      final List<FireflyAccount> _result = new ArrayList<FireflyAccount>(_cursor.getCount());
      while(_cursor.moveToNext()) {
        final FireflyAccount _item;
        final long _tmpId;
        _tmpId = _cursor.getLong(_cursorIndexOfId);
        final Date _tmpCreatedAt;
        final Long _tmp;
        if (_cursor.isNull(_cursorIndexOfCreatedAt)) {
          _tmp = null;
        } else {
          _tmp = _cursor.getLong(_cursorIndexOfCreatedAt);
        }
        final Date _tmp_1 = __converters.fromTimestamp(_tmp);
        if(_tmp_1 == null) {
          throw new IllegalStateException("Expected non-null java.util.Date, but it was null.");
        } else {
          _tmpCreatedAt = _tmp_1;
        }
        final boolean _tmpActive;
        final int _tmp_2;
        _tmp_2 = _cursor.getInt(_cursorIndexOfActive);
        _tmpActive = _tmp_2 != 0;
        final Long _tmpOrder;
        if (_cursor.isNull(_cursorIndexOfOrder)) {
          _tmpOrder = null;
        } else {
          _tmpOrder = _cursor.getLong(_cursorIndexOfOrder);
        }
        final String _tmpName;
        if (_cursor.isNull(_cursorIndexOfName)) {
          _tmpName = null;
        } else {
          _tmpName = _cursor.getString(_cursorIndexOfName);
        }
        final String _tmpType;
        if (_cursor.isNull(_cursorIndexOfType)) {
          _tmpType = null;
        } else {
          _tmpType = _cursor.getString(_cursorIndexOfType);
        }
        final String _tmpRole;
        if (_cursor.isNull(_cursorIndexOfRole)) {
          _tmpRole = null;
        } else {
          _tmpRole = _cursor.getString(_cursorIndexOfRole);
        }
        final FireflyCurrency _tmpCurrency;
        final String _tmp_3;
        if (_cursor.isNull(_cursorIndexOfCurrency)) {
          _tmp_3 = null;
        } else {
          _tmp_3 = _cursor.getString(_cursorIndexOfCurrency);
        }
        final FireflyCurrency _tmp_4 = __converters.toCurrency(_tmp_3);
        if(_tmp_4 == null) {
          throw new IllegalStateException("Expected non-null com.arnyminerz.wallet.data.object.FireflyCurrency, but it was null.");
        } else {
          _tmpCurrency = _tmp_4;
        }
        final double _tmpBalance;
        _tmpBalance = _cursor.getDouble(_cursorIndexOfBalance);
        final Date _tmpBalanceDate;
        final Long _tmp_5;
        if (_cursor.isNull(_cursorIndexOfBalanceDate)) {
          _tmp_5 = null;
        } else {
          _tmp_5 = _cursor.getLong(_cursorIndexOfBalanceDate);
        }
        final Date _tmp_6 = __converters.fromTimestamp(_tmp_5);
        if(_tmp_6 == null) {
          throw new IllegalStateException("Expected non-null java.util.Date, but it was null.");
        } else {
          _tmpBalanceDate = _tmp_6;
        }
        final String _tmpNotes;
        if (_cursor.isNull(_cursorIndexOfNotes)) {
          _tmpNotes = null;
        } else {
          _tmpNotes = _cursor.getString(_cursorIndexOfNotes);
        }
        final Date _tmpMonthlyPaymentDate;
        final Long _tmp_7;
        if (_cursor.isNull(_cursorIndexOfMonthlyPaymentDate)) {
          _tmp_7 = null;
        } else {
          _tmp_7 = _cursor.getLong(_cursorIndexOfMonthlyPaymentDate);
        }
        _tmpMonthlyPaymentDate = __converters.fromTimestamp(_tmp_7);
        final String _tmpCreditCardType;
        if (_cursor.isNull(_cursorIndexOfCreditCardType)) {
          _tmpCreditCardType = null;
        } else {
          _tmpCreditCardType = _cursor.getString(_cursorIndexOfCreditCardType);
        }
        final String _tmpAccountNumber;
        if (_cursor.isNull(_cursorIndexOfAccountNumber)) {
          _tmpAccountNumber = null;
        } else {
          _tmpAccountNumber = _cursor.getString(_cursorIndexOfAccountNumber);
        }
        final String _tmpIban;
        if (_cursor.isNull(_cursorIndexOfIban)) {
          _tmpIban = null;
        } else {
          _tmpIban = _cursor.getString(_cursorIndexOfIban);
        }
        final String _tmpBic;
        if (_cursor.isNull(_cursorIndexOfBic)) {
          _tmpBic = null;
        } else {
          _tmpBic = _cursor.getString(_cursorIndexOfBic);
        }
        final Double _tmpVirtualBalance;
        if (_cursor.isNull(_cursorIndexOfVirtualBalance)) {
          _tmpVirtualBalance = null;
        } else {
          _tmpVirtualBalance = _cursor.getDouble(_cursorIndexOfVirtualBalance);
        }
        final Double _tmpOpeningBalance;
        if (_cursor.isNull(_cursorIndexOfOpeningBalance)) {
          _tmpOpeningBalance = null;
        } else {
          _tmpOpeningBalance = _cursor.getDouble(_cursorIndexOfOpeningBalance);
        }
        final Date _tmpOpeningBalanceDate;
        final Long _tmp_8;
        if (_cursor.isNull(_cursorIndexOfOpeningBalanceDate)) {
          _tmp_8 = null;
        } else {
          _tmp_8 = _cursor.getLong(_cursorIndexOfOpeningBalanceDate);
        }
        _tmpOpeningBalanceDate = __converters.fromTimestamp(_tmp_8);
        final String _tmpLiabilityType;
        if (_cursor.isNull(_cursorIndexOfLiabilityType)) {
          _tmpLiabilityType = null;
        } else {
          _tmpLiabilityType = _cursor.getString(_cursorIndexOfLiabilityType);
        }
        final String _tmpLiabilityDirection;
        if (_cursor.isNull(_cursorIndexOfLiabilityDirection)) {
          _tmpLiabilityDirection = null;
        } else {
          _tmpLiabilityDirection = _cursor.getString(_cursorIndexOfLiabilityDirection);
        }
        final Double _tmpInterest;
        if (_cursor.isNull(_cursorIndexOfInterest)) {
          _tmpInterest = null;
        } else {
          _tmpInterest = _cursor.getDouble(_cursorIndexOfInterest);
        }
        final String _tmpInterestPeriod;
        if (_cursor.isNull(_cursorIndexOfInterestPeriod)) {
          _tmpInterestPeriod = null;
        } else {
          _tmpInterestPeriod = _cursor.getString(_cursorIndexOfInterestPeriod);
        }
        final String _tmpCurrentDebt;
        if (_cursor.isNull(_cursorIndexOfCurrentDebt)) {
          _tmpCurrentDebt = null;
        } else {
          _tmpCurrentDebt = _cursor.getString(_cursorIndexOfCurrentDebt);
        }
        final boolean _tmpIncludeNetWorth;
        final int _tmp_9;
        _tmp_9 = _cursor.getInt(_cursorIndexOfIncludeNetWorth);
        _tmpIncludeNetWorth = _tmp_9 != 0;
        final FireflyGeoRef _tmpGeoRef;
        final String _tmp_10;
        if (_cursor.isNull(_cursorIndexOfGeoRef)) {
          _tmp_10 = null;
        } else {
          _tmp_10 = _cursor.getString(_cursorIndexOfGeoRef);
        }
        _tmpGeoRef = __converters.toGeoRef(_tmp_10);
        _item = new FireflyAccount(_tmpId,_tmpCreatedAt,_tmpActive,_tmpOrder,_tmpName,_tmpType,_tmpRole,_tmpCurrency,_tmpBalance,_tmpBalanceDate,_tmpNotes,_tmpMonthlyPaymentDate,_tmpCreditCardType,_tmpAccountNumber,_tmpIban,_tmpBic,_tmpVirtualBalance,_tmpOpeningBalance,_tmpOpeningBalanceDate,_tmpLiabilityType,_tmpLiabilityDirection,_tmpInterest,_tmpInterestPeriod,_tmpCurrentDebt,_tmpIncludeNetWorth,_tmpGeoRef);
        _result.add(_item);
      }
      return _result;
    } finally {
      _cursor.close();
      _statement.release();
    }
  }

  @Override
  public LiveData<List<FireflyAccount>> getAllLive() {
    final String _sql = "SELECT * FROM ff_accounts";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 0);
    return __db.getInvalidationTracker().createLiveData(new String[]{"ff_accounts"}, false, new Callable<List<FireflyAccount>>() {
      @Override
      public List<FireflyAccount> call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
          final int _cursorIndexOfCreatedAt = CursorUtil.getColumnIndexOrThrow(_cursor, "createdAt");
          final int _cursorIndexOfActive = CursorUtil.getColumnIndexOrThrow(_cursor, "active");
          final int _cursorIndexOfOrder = CursorUtil.getColumnIndexOrThrow(_cursor, "order");
          final int _cursorIndexOfName = CursorUtil.getColumnIndexOrThrow(_cursor, "name");
          final int _cursorIndexOfType = CursorUtil.getColumnIndexOrThrow(_cursor, "type");
          final int _cursorIndexOfRole = CursorUtil.getColumnIndexOrThrow(_cursor, "role");
          final int _cursorIndexOfCurrency = CursorUtil.getColumnIndexOrThrow(_cursor, "currency");
          final int _cursorIndexOfBalance = CursorUtil.getColumnIndexOrThrow(_cursor, "balance");
          final int _cursorIndexOfBalanceDate = CursorUtil.getColumnIndexOrThrow(_cursor, "balanceDate");
          final int _cursorIndexOfNotes = CursorUtil.getColumnIndexOrThrow(_cursor, "notes");
          final int _cursorIndexOfMonthlyPaymentDate = CursorUtil.getColumnIndexOrThrow(_cursor, "monthlyPaymentDate");
          final int _cursorIndexOfCreditCardType = CursorUtil.getColumnIndexOrThrow(_cursor, "creditCardType");
          final int _cursorIndexOfAccountNumber = CursorUtil.getColumnIndexOrThrow(_cursor, "accountNumber");
          final int _cursorIndexOfIban = CursorUtil.getColumnIndexOrThrow(_cursor, "iban");
          final int _cursorIndexOfBic = CursorUtil.getColumnIndexOrThrow(_cursor, "bic");
          final int _cursorIndexOfVirtualBalance = CursorUtil.getColumnIndexOrThrow(_cursor, "virtualBalance");
          final int _cursorIndexOfOpeningBalance = CursorUtil.getColumnIndexOrThrow(_cursor, "openingBalance");
          final int _cursorIndexOfOpeningBalanceDate = CursorUtil.getColumnIndexOrThrow(_cursor, "openingBalanceDate");
          final int _cursorIndexOfLiabilityType = CursorUtil.getColumnIndexOrThrow(_cursor, "liabilityType");
          final int _cursorIndexOfLiabilityDirection = CursorUtil.getColumnIndexOrThrow(_cursor, "liabilityDirection");
          final int _cursorIndexOfInterest = CursorUtil.getColumnIndexOrThrow(_cursor, "interest");
          final int _cursorIndexOfInterestPeriod = CursorUtil.getColumnIndexOrThrow(_cursor, "interestPeriod");
          final int _cursorIndexOfCurrentDebt = CursorUtil.getColumnIndexOrThrow(_cursor, "currentDebt");
          final int _cursorIndexOfIncludeNetWorth = CursorUtil.getColumnIndexOrThrow(_cursor, "includeNetWorth");
          final int _cursorIndexOfGeoRef = CursorUtil.getColumnIndexOrThrow(_cursor, "geoRef");
          final List<FireflyAccount> _result = new ArrayList<FireflyAccount>(_cursor.getCount());
          while(_cursor.moveToNext()) {
            final FireflyAccount _item;
            final long _tmpId;
            _tmpId = _cursor.getLong(_cursorIndexOfId);
            final Date _tmpCreatedAt;
            final Long _tmp;
            if (_cursor.isNull(_cursorIndexOfCreatedAt)) {
              _tmp = null;
            } else {
              _tmp = _cursor.getLong(_cursorIndexOfCreatedAt);
            }
            final Date _tmp_1 = __converters.fromTimestamp(_tmp);
            if(_tmp_1 == null) {
              throw new IllegalStateException("Expected non-null java.util.Date, but it was null.");
            } else {
              _tmpCreatedAt = _tmp_1;
            }
            final boolean _tmpActive;
            final int _tmp_2;
            _tmp_2 = _cursor.getInt(_cursorIndexOfActive);
            _tmpActive = _tmp_2 != 0;
            final Long _tmpOrder;
            if (_cursor.isNull(_cursorIndexOfOrder)) {
              _tmpOrder = null;
            } else {
              _tmpOrder = _cursor.getLong(_cursorIndexOfOrder);
            }
            final String _tmpName;
            if (_cursor.isNull(_cursorIndexOfName)) {
              _tmpName = null;
            } else {
              _tmpName = _cursor.getString(_cursorIndexOfName);
            }
            final String _tmpType;
            if (_cursor.isNull(_cursorIndexOfType)) {
              _tmpType = null;
            } else {
              _tmpType = _cursor.getString(_cursorIndexOfType);
            }
            final String _tmpRole;
            if (_cursor.isNull(_cursorIndexOfRole)) {
              _tmpRole = null;
            } else {
              _tmpRole = _cursor.getString(_cursorIndexOfRole);
            }
            final FireflyCurrency _tmpCurrency;
            final String _tmp_3;
            if (_cursor.isNull(_cursorIndexOfCurrency)) {
              _tmp_3 = null;
            } else {
              _tmp_3 = _cursor.getString(_cursorIndexOfCurrency);
            }
            final FireflyCurrency _tmp_4 = __converters.toCurrency(_tmp_3);
            if(_tmp_4 == null) {
              throw new IllegalStateException("Expected non-null com.arnyminerz.wallet.data.object.FireflyCurrency, but it was null.");
            } else {
              _tmpCurrency = _tmp_4;
            }
            final double _tmpBalance;
            _tmpBalance = _cursor.getDouble(_cursorIndexOfBalance);
            final Date _tmpBalanceDate;
            final Long _tmp_5;
            if (_cursor.isNull(_cursorIndexOfBalanceDate)) {
              _tmp_5 = null;
            } else {
              _tmp_5 = _cursor.getLong(_cursorIndexOfBalanceDate);
            }
            final Date _tmp_6 = __converters.fromTimestamp(_tmp_5);
            if(_tmp_6 == null) {
              throw new IllegalStateException("Expected non-null java.util.Date, but it was null.");
            } else {
              _tmpBalanceDate = _tmp_6;
            }
            final String _tmpNotes;
            if (_cursor.isNull(_cursorIndexOfNotes)) {
              _tmpNotes = null;
            } else {
              _tmpNotes = _cursor.getString(_cursorIndexOfNotes);
            }
            final Date _tmpMonthlyPaymentDate;
            final Long _tmp_7;
            if (_cursor.isNull(_cursorIndexOfMonthlyPaymentDate)) {
              _tmp_7 = null;
            } else {
              _tmp_7 = _cursor.getLong(_cursorIndexOfMonthlyPaymentDate);
            }
            _tmpMonthlyPaymentDate = __converters.fromTimestamp(_tmp_7);
            final String _tmpCreditCardType;
            if (_cursor.isNull(_cursorIndexOfCreditCardType)) {
              _tmpCreditCardType = null;
            } else {
              _tmpCreditCardType = _cursor.getString(_cursorIndexOfCreditCardType);
            }
            final String _tmpAccountNumber;
            if (_cursor.isNull(_cursorIndexOfAccountNumber)) {
              _tmpAccountNumber = null;
            } else {
              _tmpAccountNumber = _cursor.getString(_cursorIndexOfAccountNumber);
            }
            final String _tmpIban;
            if (_cursor.isNull(_cursorIndexOfIban)) {
              _tmpIban = null;
            } else {
              _tmpIban = _cursor.getString(_cursorIndexOfIban);
            }
            final String _tmpBic;
            if (_cursor.isNull(_cursorIndexOfBic)) {
              _tmpBic = null;
            } else {
              _tmpBic = _cursor.getString(_cursorIndexOfBic);
            }
            final Double _tmpVirtualBalance;
            if (_cursor.isNull(_cursorIndexOfVirtualBalance)) {
              _tmpVirtualBalance = null;
            } else {
              _tmpVirtualBalance = _cursor.getDouble(_cursorIndexOfVirtualBalance);
            }
            final Double _tmpOpeningBalance;
            if (_cursor.isNull(_cursorIndexOfOpeningBalance)) {
              _tmpOpeningBalance = null;
            } else {
              _tmpOpeningBalance = _cursor.getDouble(_cursorIndexOfOpeningBalance);
            }
            final Date _tmpOpeningBalanceDate;
            final Long _tmp_8;
            if (_cursor.isNull(_cursorIndexOfOpeningBalanceDate)) {
              _tmp_8 = null;
            } else {
              _tmp_8 = _cursor.getLong(_cursorIndexOfOpeningBalanceDate);
            }
            _tmpOpeningBalanceDate = __converters.fromTimestamp(_tmp_8);
            final String _tmpLiabilityType;
            if (_cursor.isNull(_cursorIndexOfLiabilityType)) {
              _tmpLiabilityType = null;
            } else {
              _tmpLiabilityType = _cursor.getString(_cursorIndexOfLiabilityType);
            }
            final String _tmpLiabilityDirection;
            if (_cursor.isNull(_cursorIndexOfLiabilityDirection)) {
              _tmpLiabilityDirection = null;
            } else {
              _tmpLiabilityDirection = _cursor.getString(_cursorIndexOfLiabilityDirection);
            }
            final Double _tmpInterest;
            if (_cursor.isNull(_cursorIndexOfInterest)) {
              _tmpInterest = null;
            } else {
              _tmpInterest = _cursor.getDouble(_cursorIndexOfInterest);
            }
            final String _tmpInterestPeriod;
            if (_cursor.isNull(_cursorIndexOfInterestPeriod)) {
              _tmpInterestPeriod = null;
            } else {
              _tmpInterestPeriod = _cursor.getString(_cursorIndexOfInterestPeriod);
            }
            final String _tmpCurrentDebt;
            if (_cursor.isNull(_cursorIndexOfCurrentDebt)) {
              _tmpCurrentDebt = null;
            } else {
              _tmpCurrentDebt = _cursor.getString(_cursorIndexOfCurrentDebt);
            }
            final boolean _tmpIncludeNetWorth;
            final int _tmp_9;
            _tmp_9 = _cursor.getInt(_cursorIndexOfIncludeNetWorth);
            _tmpIncludeNetWorth = _tmp_9 != 0;
            final FireflyGeoRef _tmpGeoRef;
            final String _tmp_10;
            if (_cursor.isNull(_cursorIndexOfGeoRef)) {
              _tmp_10 = null;
            } else {
              _tmp_10 = _cursor.getString(_cursorIndexOfGeoRef);
            }
            _tmpGeoRef = __converters.toGeoRef(_tmp_10);
            _item = new FireflyAccount(_tmpId,_tmpCreatedAt,_tmpActive,_tmpOrder,_tmpName,_tmpType,_tmpRole,_tmpCurrency,_tmpBalance,_tmpBalanceDate,_tmpNotes,_tmpMonthlyPaymentDate,_tmpCreditCardType,_tmpAccountNumber,_tmpIban,_tmpBic,_tmpVirtualBalance,_tmpOpeningBalance,_tmpOpeningBalanceDate,_tmpLiabilityType,_tmpLiabilityDirection,_tmpInterest,_tmpInterestPeriod,_tmpCurrentDebt,_tmpIncludeNetWorth,_tmpGeoRef);
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
