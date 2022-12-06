package com.arnyminerz.wallet.ui.components

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Add
import androidx.compose.material.icons.rounded.Remove
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.arnyminerz.wallet.R
import com.arnyminerz.wallet.database.data.FireflySummary
import com.arnyminerz.wallet.ui.theme.colorGreen
import com.arnyminerz.wallet.ui.theme.colorRed
import com.google.android.material.color.MaterialColors

@Composable
fun BalanceCard(
    summary: FireflySummary,
    modifier: Modifier = Modifier,
) {
    Card(
        modifier,
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.secondaryContainer,
            contentColor = MaterialTheme.colorScheme.onSecondaryContainer,
        ),
    ) {
        Text(
            text = stringResource(R.string.dashboard_year_balance),
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp)
                .padding(top = 16.dp),
            style = MaterialTheme.typography.labelLarge,
            fontSize = 18.sp,
        )
        Text(
            text = summary.balance.valueString,
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp),
            style = MaterialTheme.typography.labelLarge,
            fontWeight = FontWeight.Bold,
            fontSize = 38.sp,
        )

        Text(
            text = stringResource(R.string.dashboard_this_month),
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp)
                .padding(top = 16.dp, bottom = 4.dp),
            style = MaterialTheme.typography.labelLarge,
            fontSize = 18.sp,
        )
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp)
                .padding(bottom = 16.dp),
        ) {
            Column(
                modifier = Modifier
                    .weight(1f),
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        Icons.Rounded.Add,
                        stringResource(R.string.image_desc_income),
                        tint = MaterialColors.harmonize(
                            colorGreen.toArgb(),
                            MaterialTheme.colorScheme.onSecondaryContainer.toArgb(),
                        ).let { Color(it) },
                        modifier = Modifier.size(18.dp),
                    )
                    Text(
                        stringResource(R.string.dashboard_income),
                        modifier = Modifier
                            .weight(1f),
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Medium,
                    )
                }
                Text(
                    text = summary.earned.valueString,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(start = 4.dp),
                    style = MaterialTheme.typography.labelLarge,
                    fontWeight = FontWeight.Bold,
                    fontSize = 18.sp,
                )
            }
            Column(
                modifier = Modifier
                    .weight(1f),
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        Icons.Rounded.Remove,
                        stringResource(R.string.image_desc_outcome),
                        tint = MaterialColors.harmonize(
                            colorRed.toArgb(),
                            MaterialTheme.colorScheme.onSecondaryContainer.toArgb(),
                        ).let { Color(it) },
                        modifier = Modifier.size(18.dp),
                    )
                    Text(
                        stringResource(R.string.dashboard_outcome),
                        modifier = Modifier
                            .weight(1f),
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Medium,
                    )
                }
                Text(
                    text = summary.spent.valueString,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(start = 4.dp),
                    style = MaterialTheme.typography.labelLarge,
                    fontWeight = FontWeight.Bold,
                    fontSize = 18.sp,
                )
            }
        }
    }
}

@Preview
@Composable
fun BalanceCardPreview() {
    BalanceCard(
        FireflySummary.SAMPLE,
        modifier = Modifier.width(550.dp)
    )
}
