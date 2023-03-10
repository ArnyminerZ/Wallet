package com.arnyminerz.wallet.ui.elements

import androidx.annotation.StringRes
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.arnyminerz.wallet.R
import com.arnyminerz.wallet.pkpass.data.Field
import com.arnyminerz.wallet.pkpass.data.boarding.BoardingData
import com.arnyminerz.wallet.ui.preview.BoardingDataProvider

@Composable
private fun FieldsDisplay(fields: List<Field>, @StringRes label: Int) {
    if (fields.isEmpty()) return

    Text(
        stringResource(label),
        style = MaterialTheme.typography.labelLarge,
        color = MaterialTheme.colorScheme.primary,
        fontSize = 20.sp,
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 8.dp),
    )
    Column(
        Modifier
            .fillMaxWidth()
            .padding(horizontal = 8.dp)
    ) {
        for (item in fields) {
            Text(
                item.label,
                modifier = Modifier.fillMaxWidth(),
                style = MaterialTheme.typography.labelLarge,
                color = MaterialTheme.colorScheme.secondary,
                fontSize = 16.sp,
                fontWeight = FontWeight.SemiBold,
            )
            Text(
                item.value,
                modifier = Modifier.fillMaxWidth().padding(bottom = 4.dp),
                style = MaterialTheme.typography.labelLarge,
                fontSize = 14.sp,
            )
        }
    }
}

@Preview
@Composable
fun PassExtendedInfoView(
    @PreviewParameter(BoardingDataProvider::class) boardingData: BoardingData,
    modifier: Modifier = Modifier,
) {
    Column(Modifier.verticalScroll(rememberScrollState()).then(modifier)) {
        FieldsDisplay(fields = boardingData.headerFields, label = R.string.pass_boarding_header)
        Spacer(Modifier.height(8.dp))
        FieldsDisplay(fields = boardingData.primaryFields, label = R.string.pass_boarding_primary)
        Spacer(Modifier.height(8.dp))
        FieldsDisplay(fields = boardingData.secondaryFields, label = R.string.pass_boarding_secondary)
        Spacer(Modifier.height(8.dp))
        FieldsDisplay(fields = boardingData.auxiliaryFields, label = R.string.pass_boarding_auxiliary)
        Spacer(Modifier.height(8.dp))
        FieldsDisplay(fields = boardingData.backFields, label = R.string.pass_boarding_back)
        Spacer(Modifier.height(16.dp))
    }
}
