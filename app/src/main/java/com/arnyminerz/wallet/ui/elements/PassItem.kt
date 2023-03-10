package com.arnyminerz.wallet.ui.elements

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.AirplaneTicket
import androidx.compose.material3.Card
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.unit.dp
import com.arnyminerz.wallet.R
import com.arnyminerz.wallet.pkpass.data.Pass
import com.arnyminerz.wallet.pkpass.data.getField
import com.arnyminerz.wallet.ui.preview.PassViewerProvider

@Preview
@Composable
fun PassItem(
    @PreviewParameter(PassViewerProvider::class) pass: Pass,
    modifier: Modifier = Modifier,
    actions: (@Composable RowScope.() -> Unit)? = null,
) {
    Card(modifier) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 8.dp)
                .padding(top = 8.dp),
        ) {
            pass.icon?.let { icon ->
                Image(
                    icon.icon2x.asImageBitmap(),
                    pass.serialNumber,
                    Modifier.size(56.dp),
                )
            } ?: Icon(
                imageVector = Icons.Rounded.AirplaneTicket,
                contentDescription = pass.serialNumber,
                modifier = Modifier.size(56.dp),
                tint = MaterialTheme.colorScheme.primary,
            )
            Column(
                Modifier
                    .weight(1f)
                    .padding(start = 8.dp)
            ) {
                Text(
                    text = pass.description,
                    modifier = Modifier.fillMaxWidth(),
                    style = MaterialTheme.typography.labelLarge,
                    color = MaterialTheme.colorScheme.primary,
                )
                Text(
                    text = pass.boardingData
                        ?.primaryFields
                        ?.getField("boardingTime")
                        ?.let {
                            stringResource(R.string.pass_boarding_time, it.value)
                        } ?: "",
                    modifier = Modifier.fillMaxWidth(),
                    style = MaterialTheme.typography.labelMedium,
                )
            }
        }
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp),
            horizontalArrangement = Arrangement.End,
        ) { actions?.invoke(this) }
    }
}
