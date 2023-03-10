package com.arnyminerz.wallet.ui.preview

import android.graphics.Color
import androidx.compose.ui.tooling.preview.PreviewParameterProvider
import com.arnyminerz.wallet.pkpass.data.Barcode
import com.arnyminerz.wallet.pkpass.data.Field
import com.arnyminerz.wallet.pkpass.data.Pass
import com.arnyminerz.wallet.pkpass.data.PassAspect
import com.arnyminerz.wallet.pkpass.data.boarding.BoardingData
import com.arnyminerz.wallet.pkpass.data.boarding.TransitType

val SAMPLE_PATH = Pass(
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
    ),
    null
)

class PassViewerProvider : PreviewParameterProvider<Pass> {
    override val values: Sequence<Pass>
        get() = sequenceOf(SAMPLE_PATH)
}

class BoardingDataProvider : PreviewParameterProvider<BoardingData> {
    override val values: Sequence<BoardingData>
        get() = sequenceOf(SAMPLE_PATH.boardingData!!)
}
