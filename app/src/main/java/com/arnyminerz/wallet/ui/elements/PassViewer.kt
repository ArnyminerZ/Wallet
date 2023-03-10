package com.arnyminerz.wallet.ui.elements

import android.provider.Settings.System
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Archive
import androidx.compose.material3.Card
import androidx.compose.material3.CardColors
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalInspectionMode
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.arnyminerz.wallet.R
import com.arnyminerz.wallet.pkpass.data.Pass
import com.arnyminerz.wallet.pkpass.data.boarding.icon
import com.arnyminerz.wallet.pkpass.data.boarding.tripTypeStringRes
import com.arnyminerz.wallet.pkpass.data.getField
import com.arnyminerz.wallet.ui.preview.PassViewerProvider
import kotlinx.coroutines.flow.distinctUntilChanged
import timber.log.Timber

@Preview
@Composable
@ExperimentalMaterial3Api
fun PassViewer(
    @PreviewParameter(PassViewerProvider::class) pass: Pass,
    modifier: Modifier = Modifier,
    initialBrightness: Int = 0,
    actions: (@Composable RowScope.() -> Unit)? = null,
    colors: CardColors = CardDefaults.cardColors(),
    storeOldBrightness: (Int) -> Unit = {},
) {
    val inspectionMode = LocalInspectionMode.current
    val context = LocalContext.current

    Card(
        modifier = modifier,
        colors = colors,
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp),
        ) {
            val boardingData = pass.boardingData
            if (boardingData != null) {
                val transitType = boardingData.transitType
                
                // Header row
                Row {
                    // Archived icon
                    if (pass.archived)
                        Icon(
                            Icons.Rounded.Archive,
                            stringResource(R.string.archived),
                            tint = MaterialTheme.colorScheme.primary,
                        )

                    // Right column
                    Column(
                        horizontalAlignment = Alignment.End,
                        modifier = Modifier
                            .weight(1f),
                    ) {
                        val header = boardingData
                            .headerFields
                            .firstOrNull()
                        Text(
                            text = header?.label
                                ?: stringResource(transitType.tripTypeStringRes),
                            color = MaterialTheme.colorScheme.primary,
                            style = MaterialTheme.typography.labelMedium,
                            fontWeight = FontWeight.Bold,
                        )
                        Text(
                            text = header?.value ?: "",
                            style = MaterialTheme.typography.labelLarge,
                        )
                    }
                }
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(start = 8.dp, end = 8.dp, top = 8.dp),
                ) {
                    val primaryFields = boardingData.primaryFields
                    Column(
                        modifier = Modifier.weight(1f)
                    ) {
                        val boardingTime = primaryFields.getField("boardingTime")
                        Text(
                            text = boardingTime?.label ?: "",
                            color = MaterialTheme.colorScheme.primary,
                            style = MaterialTheme.typography.labelMedium,
                            fontWeight = FontWeight.Bold,
                        )
                        Text(
                            text = boardingTime?.value ?: "",
                            style = MaterialTheme.typography.labelLarge,
                            fontSize = 24.sp,
                        )
                    }
                    Icon(
                        imageVector = transitType.icon,
                        contentDescription = stringResource(transitType.tripTypeStringRes),
                        modifier = Modifier.size(75.dp),
                        tint = MaterialTheme.colorScheme.primary,
                    )
                    Column(
                        modifier = Modifier.weight(1f),
                        horizontalAlignment = Alignment.End,
                    ) {
                        val destino = primaryFields.getField("destino")
                        Text(
                            text = destino?.label ?: "",
                            color = MaterialTheme.colorScheme.primary,
                            style = MaterialTheme.typography.labelMedium,
                            fontWeight = FontWeight.Bold,
                        )
                        Text(
                            text = destino?.value ?: "",
                            style = MaterialTheme.typography.labelLarge,
                            fontSize = 24.sp,
                        )
                    }
                }
                Row(
                    modifier = Modifier.fillMaxWidth()
                ) {
                    val secondaryFields = boardingData.secondaryFields
                    secondaryFields.forEachIndexed { index, field ->
                        Column(
                            modifier = Modifier.weight(1f),
                            horizontalAlignment = when (index) {
                                0 -> Alignment.Start
                                secondaryFields.size - 1 -> Alignment.End
                                else -> Alignment.CenterHorizontally
                            }
                        ) {
                            Text(
                                text = field.label,
                                color = MaterialTheme.colorScheme.primary,
                                style = MaterialTheme.typography.labelMedium,
                                fontWeight = FontWeight.Bold,
                            )
                            Text(
                                text = field.value,
                                style = MaterialTheme.typography.labelLarge,
                                fontSize = 24.sp,
                            )
                        }
                    }
                }
            }

            if (pass.barcode != null) {
                var barcodeExpanded by remember { mutableStateOf(false) }
                val barcodeSize by animateDpAsState(if (barcodeExpanded) 300.dp else 150.dp)
                var oldBrightness by remember { mutableStateOf(initialBrightness) }

                LaunchedEffect(barcodeExpanded) {
                    snapshotFlow { barcodeExpanded }
                        .collect {
                            val brightness = if (it) {
                                oldBrightness = System.getInt(context.contentResolver, System.SCREEN_BRIGHTNESS)
                                storeOldBrightness(oldBrightness)
                                255
                            } else
                                initialBrightness

                            Timber.i("Changing brightness to $brightness")
                            System.putInt(
                                context.contentResolver,
                                System.SCREEN_BRIGHTNESS,
                                brightness,
                            )
                        }
                }

                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 8.dp, top = 16.dp),
                    horizontalArrangement = Arrangement.Center,
                ) {
                    Card(
                        onClick = { barcodeExpanded = !barcodeExpanded },
                        colors = CardDefaults.elevatedCardColors(
                            containerColor = androidx.compose.ui.graphics.Color.White,
                        ),
                    ) {
                        Column(
                            horizontalAlignment = Alignment.CenterHorizontally,
                            modifier = Modifier.padding(start = 8.dp, top = 8.dp, end = 8.dp)
                        ) {
                            if (inspectionMode)
                                Image(
                                    painter = painterResource(R.drawable.sample_qr),
                                    contentDescription = pass.barcode.altText,
                                    contentScale = ContentScale.Fit,
                                    modifier = Modifier
                                        .size(150.dp),
                                )
                            else
                                AsyncImage(
                                    model = pass.barcode.bitmap,
                                    pass.barcode.altText,
                                    contentScale = ContentScale.Fit,
                                    modifier = Modifier
                                        .size(barcodeSize),
                                )
                            Text(
                                text = pass.barcode.altText,
                                modifier = Modifier
                                    .padding(bottom = 4.dp, top = 4.dp)
                            )
                        }
                    }
                }
            }
            actions?.let {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.End,
                    content = it,
                )
            }
        }
    }
}
