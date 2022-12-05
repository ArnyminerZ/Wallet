package com.arnyminerz.wallet.ui.elements

import androidx.compose.foundation.layout.*
import androidx.compose.material3.Card
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.arnyminerz.wallet.database.data.FireflySummaryEntry

@Composable
fun SummaryEntry(
    entry: FireflySummaryEntry,
    modifier: Modifier = Modifier,
) {
    Card(
        modifier = modifier
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Icon(
                entry.metadata.icon,
                stringResource(entry.metadata.title),
                modifier = Modifier
                    .size(72.dp)
                    .padding(12.dp),
            )
            Column(
                modifier = Modifier
                    .weight(1f)
                    .padding(vertical = 12.dp)
                    .padding(end = 12.dp),
            ) {
                Text(
                    stringResource(entry.metadata.title),
                    style = MaterialTheme.typography.labelLarge,
                )
                Text(
                    entry.valueString,
                    style = MaterialTheme.typography.headlineMedium,
                )
            }
        }
    }
}

@Preview
@Composable
fun SummaryEntryPreview() {
    SummaryEntry(entry = FireflySummaryEntry.SAMPLE)
}
