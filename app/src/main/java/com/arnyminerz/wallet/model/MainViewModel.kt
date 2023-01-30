package com.arnyminerz.wallet.model

import android.accounts.Account
import android.app.Application
import android.graphics.Bitmap
import android.net.Uri
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import com.arnyminerz.wallet.account.AccountHelper
import com.arnyminerz.wallet.database.data.FireflyTransaction
import com.arnyminerz.wallet.database.local.AppDatabase
import com.arnyminerz.wallet.database.remote.api
import com.arnyminerz.wallet.model.provider.AccountsProvider
import com.arnyminerz.wallet.pkpass.Parser
import com.arnyminerz.wallet.pkpass.data.Pass
import com.arnyminerz.wallet.pkpass.data.PassAspect
import com.arnyminerz.wallet.pkpass.data.boarding.BoardingData
import com.arnyminerz.wallet.pkpass.data.boarding.TransitType
import com.arnyminerz.wallet.utils.*
import org.json.JSONException
import timber.log.Timber
import java.io.File
import java.io.FileInputStream
import java.util.*

class MainViewModel(application: Application) : AndroidViewModel(application), AccountsProvider {
    /**
     * Stores the [Parser] instance that gets loaded by [loadPkPass].
     * @author Arnau Mora
     * @since 20220530
     */
    private lateinit var parser: Parser

    var pass by mutableStateOf<Pass?>(null)

    /**
     * A reference to the [AccountHelper] instance for interacting with accounts.
     * @author Arnau Mora
     * @since 20221128
     */
    private val ah = AccountHelper.getInstance(application)

    private val database = AppDatabase.getInstance(application)

    private val accountsDao = database.accountsDao()
    private val currenciesDao = database.currenciesDao()
    private val categoriesDao = database.categoriesDao()

    override val selectedAccount: MutableState<Int> = mutableStateOf(0)

    override val accounts: LiveData<Array<out Account>> = ah.accountsLive

    val storedAccounts = accountsDao.getAllLive()

    val storedCurrencies = currenciesDao.getAllLive()

    val categories = categoriesDao.getAllLive()

    /**
     * Loads a pkpass from the response uri from a file picker.
     * @author Arnau Mora
     * @since 20220530
     * @param uri The uri to load. If null no load will be performed.
     * @throws SecurityException When the hash of a file does not match the manifest.
     */
    @Throws(
        SecurityException::class,
    )
    fun loadPkPass(uri: Uri?) {
        if (uri == null)
            return

        val context = getApplication<Application>()
            .applicationContext
        context
            .contentResolver
            .openFileDescriptor(uri, "r")
            ?.use { descriptor ->
                Timber.i("Loading pkpass from $uri...")
                parser = Parser(context, FileInputStream(descriptor.fileDescriptor))

                @Suppress("ThrowableNotThrown")
                val manifestValid = parser.validManifest
                    .drop(SecurityException("The pkpass file is corrupt: Hashes do not match."))
                Timber.i("Loaded pkpass file. Valid: $manifestValid")

                Timber.i("Loading barcode...")
                val data = parser.readPass()
                val boardingPass = data.getJSONObject("boardingPass")
                pass = Pass(
                    data.getInt("formatVersion"),
                    data.getString("passTypeIdentifier"),
                    data.getString("serialNumber"),
                    data.getString("teamIdentifier"),
                    data.getString("organizationName"),
                    data.getString("description"),
                    PassAspect(
                        data.getColor("labelColor"),
                        data.getColor("foregroundColor"),
                        data.getColor("backgroundColor"),
                    ),
                    parser.getBarcode()
                        .also { barcode ->
                            Timber.i("Loading bitmap...")
                            barcode.bitmap = parser.getBitmap(context)
                                .also { bmp ->
                                    File(context.filesDir, "file.png")
                                        .also { if (it.exists()) it.delete() }
                                        .outputStream()
                                        .use { bmp.compress(Bitmap.CompressFormat.PNG, 100, it) }
                                }
                        },
                    BoardingData(
                        TransitType.valueOf(boardingPass.getString("transitType")),
                        boardingPass.getFields("headerFields"),
                        boardingPass.getFields("primaryFields"),
                        boardingPass.getFields("secondaryFields"),
                        boardingPass.getFields("auxiliaryFields"),
                        boardingPass.getFields("backFields"),
                    )
                )
            }
    }

    /**
     * Gets the balance for the current month.
     * @author Arnau Mora
     * @since 20221201
     */
    fun getSummary(account: Account) = future {
        try {
            account.api(getApplication()).getMonthBalance().also { postValue(it) }
        } catch (e: NullPointerException) {
            Timber.e(e, "Missing fields on response.")
        } catch (e: JSONException) {
            Timber.e(e, "Could not parse response.")
        }
    }

    /**
     * Gets all the transactions for the given account in a period of time.
     * @author Arnau Mora
     * @since 20221206
     * @param account The account to get the transactions from.
     * @param limit The maximum amount of transactions to get.
     * @param start The starting day to fetch. Defaults to the first day of the current month.
     * @param end The ending day to fetch. Defaults to the last day of the current month.
     */
    fun getTransactions(
        account: Account,
        limit: Int = Int.MAX_VALUE,
        start: Date? = null,
        end: Date? = null,
        callback: (transactions: List<FireflyTransaction>) -> Unit,
    ) = launch { account.api(getApplication()).getTransactions(limit, start, end).also(callback) }

    /**
     * Removes the given account from the account manager.
     * @author Arnau Mora
     * @since 20220130
     * @param account The account to be deleted.
     */
    fun removeAccount(account: Account) = launch {
        ah.removeAccount(account)
    }
}
