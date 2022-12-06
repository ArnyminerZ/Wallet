package com.arnyminerz.wallet.ui.components

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.ArrowRight
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.arnyminerz.wallet.R
import com.arnyminerz.wallet.database.data.FireflyTransaction
import com.arnyminerz.wallet.database.data.TransactionType
import com.arnyminerz.wallet.ui.theme.colorGreen
import com.arnyminerz.wallet.ui.theme.colorRed
import java.text.SimpleDateFormat
import java.util.*

@Composable
fun TransactionCard(transaction: FireflyTransaction, modifier: Modifier = Modifier) {
    Card(
        modifier,
        colors = CardDefaults.cardColors(
            contentColor = MaterialTheme.colorScheme.onPrimaryContainer.copy(alpha = .6f),
            containerColor = MaterialTheme.colorScheme.primaryContainer,
        ),
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier
                    .padding(16.dp),
            ) {
                Text(
                    SimpleDateFormat("dd", Locale.getDefault())
                        .format(transaction.date),
                    style = MaterialTheme.typography.labelLarge,
                    fontWeight = FontWeight.Bold,
                    fontSize = 24.sp,
                )
                Text(
                    SimpleDateFormat("EEE", Locale.getDefault())
                        .format(transaction.date),
                    style = MaterialTheme.typography.labelLarge,
                    fontWeight = FontWeight.Bold,
                    fontSize = 16.sp,
                )
            }
            Column(
                modifier = Modifier
                    .weight(1f)
                    .padding(vertical = 16.dp),
            ) {
                Text(
                    transaction.description
                        ?.split('\n')
                        ?.firstOrNull()
                        ?: stringResource(R.string.transaction_description_unknown),
                    color = MaterialTheme.colorScheme.onPrimaryContainer,
                    style = MaterialTheme.typography.labelLarge,
                    fontSize = 18.sp,
                )
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    transaction.source?.name?.let {
                        Text(
                            it,
                            style = MaterialTheme.typography.labelSmall,
                        )
                    }
                    Icon(
                        Icons.Rounded.ArrowRight,
                        stringResource(R.string.image_desc_transaction_direction),
                    )
                    transaction.destination?.name?.let {
                        Text(
                            it,
                            style = MaterialTheme.typography.labelSmall,
                        )
                    }
                }
            }
            Text(
                transaction.amountString,
                modifier = Modifier.padding(16.dp),
                color = when (transaction.type) {
                    TransactionType.WITHDRAWAL -> colorRed
                    TransactionType.DEPOSIT -> colorGreen
                    else -> MaterialTheme.colorScheme.onPrimaryContainer
                },
                style = MaterialTheme.typography.labelLarge,
                fontSize = 18.sp,
            )
        }
    }
}

@Preview
@Composable
fun TransactionCardPreview() {
    TransactionCard(
        FireflyTransaction.SAMPLE,
        modifier = Modifier.width(450.dp),
    )
}
