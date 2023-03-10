package com.arnyminerz.wallet.ui.elements

import android.graphics.Color
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
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalInspectionMode
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.tooling.preview.PreviewParameterProvider
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.arnyminerz.wallet.R
import com.arnyminerz.wallet.pkpass.data.Barcode
import com.arnyminerz.wallet.pkpass.data.Field
import com.arnyminerz.wallet.pkpass.data.Pass
import com.arnyminerz.wallet.pkpass.data.PassAspect
import com.arnyminerz.wallet.pkpass.data.boarding.BoardingData
import com.arnyminerz.wallet.pkpass.data.boarding.TransitType
import com.arnyminerz.wallet.pkpass.data.boarding.icon
import com.arnyminerz.wallet.pkpass.data.boarding.tripTypeStringRes
import com.arnyminerz.wallet.pkpass.data.getField

class PassViewerProvider : PreviewParameterProvider<Pass> {
    override val values: Sequence<Pass>
        get() = sequenceOf(
            Pass(
                0,
                true,
                1,
                "pass.com.renfe-RenfeTicket",
                "00UJU5JU7593101237034",
                "H9VY2F2XZA",
                "Renfe",
                "Billete de tren",
                PassAspect(
                    Color.rgb(204, 0, 153),
                    Color.rgb(99, 99, 99),
                    Color.rgb(212, 212, 212),
                ),
                Barcode(
                    "7593101237034650006901126052214415000000030UJU5JU..00000",
                    "PKBarcodeFormatQR",
                    "utf-8",
                    "7593101237034"
                ),
                BoardingData(
                    TransitType.valueOf("PKTransitTypeTrain"),
                    listOf(
                        Field(
                            "destinofecha",
                            "Viaje a: ALCOY/ALCO",
                            "26/05/2022",
                        ),
                    ),
                    listOf(
                        Field(
                            "boardingTime",
                            "VALENC.NOR",
                            "12:34",
                            "La hora de salida ha cambiado a %@",
                        ),
                        Field(
                            "destino",
                            "ALCOY/ALCO",
                            "15:04",
                        )
                    ),
                    listOf(
                        Field(
                            "pasajero",
                            "Pasajero",
                            "A.MORA",
                        ),
                        Field(
                            "localizador",
                            "Localizador",
                            "UJU5JU"
                        )
                    ),
                    listOf(),
                    listOf(),
                )
            )
        )
}

@Preview
@Composable
@ExperimentalMaterial3Api
fun PassViewer(
    @PreviewParameter(PassViewerProvider::class) pass: Pass,
    modifier: Modifier = Modifier,
    actions: (@Composable RowScope.() -> Unit)? = null,
) {
    val inspectionMode = LocalInspectionMode.current

    Card(
        modifier = modifier,
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

            if (pass.barcode != null)
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 8.dp, top = 16.dp),
                    horizontalArrangement = Arrangement.Center,
                ) {
                    Card(
                        colors = CardDefaults.elevatedCardColors(
                            containerColor = MaterialTheme.colorScheme.primaryContainer,
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
                                        .size(150.dp),
                                )
                            Text(
                                text = pass.barcode.altText,
                                modifier = Modifier
                                    .padding(bottom = 4.dp, top = 4.dp)
                            )
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
