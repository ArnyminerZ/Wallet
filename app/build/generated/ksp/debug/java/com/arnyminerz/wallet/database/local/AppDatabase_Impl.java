package com.arnyminerz.wallet.database.local;

import androidx.annotation.NonNull;
import androidx.room.DatabaseConfiguration;
import androidx.room.InvalidationTracker;
import androidx.room.RoomOpenHelper;
import androidx.room.migration.AutoMigrationSpec;
import androidx.room.migration.Migration;
import androidx.room.util.DBUtil;
import androidx.room.util.TableInfo;
import androidx.sqlite.db.SupportSQLiteDatabase;
import androidx.sqlite.db.SupportSQLiteOpenHelper;

import java.lang.Class;
import java.lang.Override;
import java.lang.String;
import java.lang.SuppressWarnings;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

@SuppressWarnings({"unchecked", "deprecation"})
public final class AppDatabase_Impl extends AppDatabase {
  private volatile AccountsDao _accountsDao;

  private volatile CurrenciesDao _currenciesDao;

  private volatile CategoriesDao _categoriesDao;

  @Override
  protected SupportSQLiteOpenHelper createOpenHelper(DatabaseConfiguration configuration) {
    final SupportSQLiteOpenHelper.Callback _openCallback = new RoomOpenHelper(configuration, new RoomOpenHelper.Delegate(1) {
      @Override
      public void createAllTables(SupportSQLiteDatabase _db) {
        _db.execSQL("CREATE TABLE IF NOT EXISTS `ff_accounts` (`id` INTEGER NOT NULL, `createdAt` INTEGER NOT NULL, `active` INTEGER NOT NULL, `order` INTEGER, `name` TEXT NOT NULL, `type` TEXT NOT NULL, `role` TEXT, `currency` TEXT NOT NULL, `balance` REAL NOT NULL, `balanceDate` INTEGER NOT NULL, `notes` TEXT, `monthlyPaymentDate` INTEGER, `creditCardType` TEXT, `accountNumber` TEXT, `iban` TEXT, `bic` TEXT, `virtualBalance` REAL, `openingBalance` REAL, `openingBalanceDate` INTEGER, `liabilityType` TEXT, `liabilityDirection` TEXT, `interest` REAL, `interestPeriod` TEXT, `currentDebt` TEXT, `includeNetWorth` INTEGER NOT NULL, `geoRef` TEXT, PRIMARY KEY(`id`))");
        _db.execSQL("CREATE TABLE IF NOT EXISTS `ff_currencies` (`id` INTEGER NOT NULL, `code` TEXT NOT NULL, `symbol` TEXT NOT NULL, `decimalPlaces` INTEGER NOT NULL, `default` INTEGER NOT NULL, PRIMARY KEY(`id`))");
        _db.execSQL("CREATE TABLE IF NOT EXISTS `ff_categories` (`id` INTEGER NOT NULL, `name` TEXT NOT NULL, PRIMARY KEY(`id`))");
        _db.execSQL("CREATE TABLE IF NOT EXISTS room_master_table (id INTEGER PRIMARY KEY,identity_hash TEXT)");
        _db.execSQL("INSERT OR REPLACE INTO room_master_table (id,identity_hash) VALUES(42, '2a69fc18d6aebd93d03e8b5d7c756b39')");
      }

      @Override
      public void dropAllTables(SupportSQLiteDatabase _db) {
        _db.execSQL("DROP TABLE IF EXISTS `ff_accounts`");
        _db.execSQL("DROP TABLE IF EXISTS `ff_currencies`");
        _db.execSQL("DROP TABLE IF EXISTS `ff_categories`");
        if (mCallbacks != null) {
          for (int _i = 0, _size = mCallbacks.size(); _i < _size; _i++) {
            mCallbacks.get(_i).onDestructiveMigration(_db);
          }
        }
      }

      @Override
      protected void onCreate(SupportSQLiteDatabase _db) {
        if (mCallbacks != null) {
          for (int _i = 0, _size = mCallbacks.size(); _i < _size; _i++) {
            mCallbacks.get(_i).onCreate(_db);
          }
        }
      }

      @Override
      public void onOpen(SupportSQLiteDatabase _db) {
        mDatabase = _db;
        internalInitInvalidationTracker(_db);
        if (mCallbacks != null) {
          for (int _i = 0, _size = mCallbacks.size(); _i < _size; _i++) {
            mCallbacks.get(_i).onOpen(_db);
          }
        }
      }

      @Override
      public void onPreMigrate(SupportSQLiteDatabase _db) {
        DBUtil.dropFtsSyncTriggers(_db);
      }

      @Override
      public void onPostMigrate(SupportSQLiteDatabase _db) {
      }

      @Override
      protected RoomOpenHelper.ValidationResult onValidateSchema(SupportSQLiteDatabase _db) {
        final HashMap<String, TableInfo.Column> _columnsFfAccounts = new HashMap<String, TableInfo.Column>(26);
        _columnsFfAccounts.put("id", new TableInfo.Column("id", "INTEGER", true, 1, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsFfAccounts.put("createdAt", new TableInfo.Column("createdAt", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsFfAccounts.put("active", new TableInfo.Column("active", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsFfAccounts.put("order", new TableInfo.Column("order", "INTEGER", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsFfAccounts.put("name", new TableInfo.Column("name", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsFfAccounts.put("type", new TableInfo.Column("type", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsFfAccounts.put("role", new TableInfo.Column("role", "TEXT", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsFfAccounts.put("currency", new TableInfo.Column("currency", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsFfAccounts.put("balance", new TableInfo.Column("balance", "REAL", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsFfAccounts.put("balanceDate", new TableInfo.Column("balanceDate", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsFfAccounts.put("notes", new TableInfo.Column("notes", "TEXT", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsFfAccounts.put("monthlyPaymentDate", new TableInfo.Column("monthlyPaymentDate", "INTEGER", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsFfAccounts.put("creditCardType", new TableInfo.Column("creditCardType", "TEXT", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsFfAccounts.put("accountNumber", new TableInfo.Column("accountNumber", "TEXT", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsFfAccounts.put("iban", new TableInfo.Column("iban", "TEXT", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsFfAccounts.put("bic", new TableInfo.Column("bic", "TEXT", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsFfAccounts.put("virtualBalance", new TableInfo.Column("virtualBalance", "REAL", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsFfAccounts.put("openingBalance", new TableInfo.Column("openingBalance", "REAL", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsFfAccounts.put("openingBalanceDate", new TableInfo.Column("openingBalanceDate", "INTEGER", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsFfAccounts.put("liabilityType", new TableInfo.Column("liabilityType", "TEXT", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsFfAccounts.put("liabilityDirection", new TableInfo.Column("liabilityDirection", "TEXT", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsFfAccounts.put("interest", new TableInfo.Column("interest", "REAL", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsFfAccounts.put("interestPeriod", new TableInfo.Column("interestPeriod", "TEXT", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsFfAccounts.put("currentDebt", new TableInfo.Column("currentDebt", "TEXT", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsFfAccounts.put("includeNetWorth", new TableInfo.Column("includeNetWorth", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsFfAccounts.put("geoRef", new TableInfo.Column("geoRef", "TEXT", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        final HashSet<TableInfo.ForeignKey> _foreignKeysFfAccounts = new HashSet<TableInfo.ForeignKey>(0);
        final HashSet<TableInfo.Index> _indicesFfAccounts = new HashSet<TableInfo.Index>(0);
        final TableInfo _infoFfAccounts = new TableInfo("ff_accounts", _columnsFfAccounts, _foreignKeysFfAccounts, _indicesFfAccounts);
        final TableInfo _existingFfAccounts = TableInfo.read(_db, "ff_accounts");
        if (! _infoFfAccounts.equals(_existingFfAccounts)) {
          return new RoomOpenHelper.ValidationResult(false, "ff_accounts(com.arnyminerz.wallet.data.object.FireflyAccount).\n"
                  + " Expected:\n" + _infoFfAccounts + "\n"
                  + " Found:\n" + _existingFfAccounts);
        }
        final HashMap<String, TableInfo.Column> _columnsFfCurrencies = new HashMap<String, TableInfo.Column>(5);
        _columnsFfCurrencies.put("id", new TableInfo.Column("id", "INTEGER", true, 1, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsFfCurrencies.put("code", new TableInfo.Column("code", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsFfCurrencies.put("symbol", new TableInfo.Column("symbol", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsFfCurrencies.put("decimalPlaces", new TableInfo.Column("decimalPlaces", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsFfCurrencies.put("default", new TableInfo.Column("default", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        final HashSet<TableInfo.ForeignKey> _foreignKeysFfCurrencies = new HashSet<TableInfo.ForeignKey>(0);
        final HashSet<TableInfo.Index> _indicesFfCurrencies = new HashSet<TableInfo.Index>(0);
        final TableInfo _infoFfCurrencies = new TableInfo("ff_currencies", _columnsFfCurrencies, _foreignKeysFfCurrencies, _indicesFfCurrencies);
        final TableInfo _existingFfCurrencies = TableInfo.read(_db, "ff_currencies");
        if (! _infoFfCurrencies.equals(_existingFfCurrencies)) {
          return new RoomOpenHelper.ValidationResult(false, "ff_currencies(com.arnyminerz.wallet.data.object.FireflyCurrency).\n"
                  + " Expected:\n" + _infoFfCurrencies + "\n"
                  + " Found:\n" + _existingFfCurrencies);
        }
        final HashMap<String, TableInfo.Column> _columnsFfCategories = new HashMap<String, TableInfo.Column>(2);
        _columnsFfCategories.put("id", new TableInfo.Column("id", "INTEGER", true, 1, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsFfCategories.put("name", new TableInfo.Column("name", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        final HashSet<TableInfo.ForeignKey> _foreignKeysFfCategories = new HashSet<TableInfo.ForeignKey>(0);
        final HashSet<TableInfo.Index> _indicesFfCategories = new HashSet<TableInfo.Index>(0);
        final TableInfo _infoFfCategories = new TableInfo("ff_categories", _columnsFfCategories, _foreignKeysFfCategories, _indicesFfCategories);
        final TableInfo _existingFfCategories = TableInfo.read(_db, "ff_categories");
        if (! _infoFfCategories.equals(_existingFfCategories)) {
          return new RoomOpenHelper.ValidationResult(false, "ff_categories(com.arnyminerz.wallet.data.object.FireflyCategory).\n"
                  + " Expected:\n" + _infoFfCategories + "\n"
                  + " Found:\n" + _existingFfCategories);
        }
        return new RoomOpenHelper.ValidationResult(true, null);
      }
    }, "2a69fc18d6aebd93d03e8b5d7c756b39", "298ae51a0f3aa8617f0179953a90231a");
    final SupportSQLiteOpenHelper.Configuration _sqliteConfig = SupportSQLiteOpenHelper.Configuration.builder(configuration.context)
        .name(configuration.name)
        .callback(_openCallback)
        .build();
    final SupportSQLiteOpenHelper _helper = configuration.sqliteOpenHelperFactory.create(_sqliteConfig);
    return _helper;
  }

  @Override
  protected InvalidationTracker createInvalidationTracker() {
    final HashMap<String, String> _shadowTablesMap = new HashMap<String, String>(0);
    HashMap<String, Set<String>> _viewTables = new HashMap<String, Set<String>>(0);
    return new InvalidationTracker(this, _shadowTablesMap, _viewTables, "ff_accounts","ff_currencies","ff_categories");
  }

  @Override
  public void clearAllTables() {
    super.assertNotMainThread();
    final SupportSQLiteDatabase _db = super.getOpenHelper().getWritableDatabase();
    try {
      super.beginTransaction();
      _db.execSQL("DELETE FROM `ff_accounts`");
      _db.execSQL("DELETE FROM `ff_currencies`");
      _db.execSQL("DELETE FROM `ff_categories`");
      super.setTransactionSuccessful();
    } finally {
      super.endTransaction();
      _db.query("PRAGMA wal_checkpoint(FULL)").close();
      if (!_db.inTransaction()) {
        _db.execSQL("VACUUM");
      }
    }
  }

  @Override
  protected Map<Class<?>, List<Class<?>>> getRequiredTypeConverters() {
    final HashMap<Class<?>, List<Class<?>>> _typeConvertersMap = new HashMap<Class<?>, List<Class<?>>>();
    _typeConvertersMap.put(AccountsDao.class, AccountsDao_Impl.getRequiredConverters());
    _typeConvertersMap.put(CurrenciesDao.class, CurrenciesDao_Impl.getRequiredConverters());
    _typeConvertersMap.put(CategoriesDao.class, CategoriesDao_Impl.getRequiredConverters());
    return _typeConvertersMap;
  }

  @Override
  public Set<Class<? extends AutoMigrationSpec>> getRequiredAutoMigrationSpecs() {
    final HashSet<Class<? extends AutoMigrationSpec>> _autoMigrationSpecsSet = new HashSet<Class<? extends AutoMigrationSpec>>();
    return _autoMigrationSpecsSet;
  }

  @Override
  public List<Migration> getAutoMigrations(
      @NonNull Map<Class<? extends AutoMigrationSpec>, AutoMigrationSpec> autoMigrationSpecsMap) {
    return Arrays.asList();
  }

  @Override
  public AccountsDao accountsDao() {
    if (_accountsDao != null) {
      return _accountsDao;
    } else {
      synchronized(this) {
        if(_accountsDao == null) {
          _accountsDao = new AccountsDao_Impl(this);
        }
        return _accountsDao;
      }
    }
  }

  @Override
  public CurrenciesDao currenciesDao() {
    if (_currenciesDao != null) {
      return _currenciesDao;
    } else {
      synchronized(this) {
        if(_currenciesDao == null) {
          _currenciesDao = new CurrenciesDao_Impl(this);
        }
        return _currenciesDao;
      }
    }
  }

  @Override
  public CategoriesDao categoriesDao() {
    if (_categoriesDao != null) {
      return _categoriesDao;
    } else {
      synchronized(this) {
        if(_categoriesDao == null) {
          _categoriesDao = new CategoriesDao_Impl(this);
        }
        return _categoriesDao;
      }
    }
  }
}
