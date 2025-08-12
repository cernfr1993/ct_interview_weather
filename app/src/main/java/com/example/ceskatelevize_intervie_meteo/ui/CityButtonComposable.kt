package com.example.ceskatelevize_intervie_meteo.ui

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ceskatelevize_intervie_meteo.model.AvailableCity

@Composable
fun CityButton(
    availableCity: AvailableCity,
    onButtonClicked: (AvailableCity) -> Unit,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier
            .fillMaxWidth()
            .height(48.dp)
            .padding(6.dp),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 0.dp)
    ) {
        Button(
            shape = RoundedCornerShape(16.dp),
            onClick = {
                onButtonClicked(availableCity)
            },
            modifier = Modifier.fillMaxSize()
        ) {
            Text(
                text = availableCity.cityName,
                fontSize = 18.sp,
                color = Color.White,
                fontWeight = FontWeight.Normal
            )
        }
    }
}

@Preview
@Composable
fun CityButtonPreview() {
    CityButton(
        AvailableCity.PRAHA,
        { city -> },
    )
}