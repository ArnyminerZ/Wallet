package com.arnyminerz.wallet.pkpass.data.boarding

import androidx.annotation.StringRes
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.CardTravel
import androidx.compose.material.icons.rounded.DirectionsBoat
import androidx.compose.material.icons.rounded.DirectionsBus
import androidx.compose.material.icons.rounded.Flight
import androidx.compose.material.icons.rounded.Train
import androidx.compose.ui.graphics.vector.ImageVector
import com.arnyminerz.wallet.R

enum class TransitType {
    PKTransitTypeAir,
    PKTransitTypeBoat,
    PKTransitTypeBus,
    PKTransitTypeGeneric,
    PKTransitTypeTrain,
}

val TransitType.tripTypeStringRes: Int
    @StringRes
    get() = when (this) {
        TransitType.PKTransitTypeAir -> R.string.trip_type_air
        TransitType.PKTransitTypeBoat -> R.string.trip_type_boat
        TransitType.PKTransitTypeBus -> R.string.trip_type_bus
        TransitType.PKTransitTypeGeneric -> R.string.trip_type_generic
        TransitType.PKTransitTypeTrain -> R.string.trip_type_train
    }

val TransitType.icon: ImageVector
    get() = when (this) {
        TransitType.PKTransitTypeAir -> Icons.Rounded.Flight
        TransitType.PKTransitTypeBoat -> Icons.Rounded.DirectionsBoat
        TransitType.PKTransitTypeBus -> Icons.Rounded.DirectionsBus
        TransitType.PKTransitTypeGeneric -> Icons.Rounded.CardTravel
        TransitType.PKTransitTypeTrain -> Icons.Rounded.Train
    }
