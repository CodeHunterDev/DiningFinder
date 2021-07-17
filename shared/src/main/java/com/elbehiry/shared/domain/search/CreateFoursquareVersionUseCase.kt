package com.elbehiry.shared.domain.search

import com.elbehiry.shared.utils.fourSquareVersionFormat
import java.text.DateFormat
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class CreateFoursquareVersionUseCase {

    operator fun invoke(date : Date): String {
        val outputFormatter: DateFormat =
            SimpleDateFormat(fourSquareVersionFormat, Locale.getDefault())
        return outputFormatter.format(date)
    }
}