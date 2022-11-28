package com.arnyminerz.wallet.ui.screens

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.arnyminerz.wallet.R

@Preview(
    showSystemUi = true,
    showBackground = true,
)
@Composable
fun AddingAccountScreen() {
    Box(
        modifier = Modifier
            .fillMaxSize(),
    ) {
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp)
                .align(Alignment.Center),
        ) {
            Text(
                stringResource(R.string.account_adding_title),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 8.dp)
                    .padding(top = 8.dp),
                style = MaterialTheme.typography.titleMedium,
            )
            Text(
                stringResource(R.string.account_adding_message),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 8.dp)
                    .padding(bottom = 8.dp),
                style = MaterialTheme.typography.labelMedium,
            )
        }
    }
}
