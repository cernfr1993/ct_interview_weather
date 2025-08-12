package com.example.ceskatelevize_intervie_meteo.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ceskatelevize_intervie_meteo.model.TemperatureType

@Composable
fun TemperatureCard(
    temperatureType: TemperatureType,
    temperature: Double,
    modifier: Modifier = Modifier,
) {
    Card(
        modifier = modifier
            .fillMaxWidth()
            .height(120.dp)
            .padding(6.dp),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 0.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Text(
                text = when (temperatureType) {
                    TemperatureType.MIN -> "Min"
                    TemperatureType.MAX -> "Max"
                },
                fontSize = 18.sp,
                color = Color.Gray,
                fontWeight = FontWeight.Normal
            )

            Spacer(modifier = Modifier.height(8.dp))

            AnimatedTemperatureText(
                temperature.toFloat()
            )

//            Text(
//                text = "$temperature °C",
//                fontSize = 32.sp,
//                color = Color.Black,
//                fontWeight = FontWeight.Bold
//            )
        }
    }
}

@Preview
@Composable
fun TemperatureCard() {
    TemperatureCard(
        temperatureType = TemperatureType.MIN,
        28.2,
    )
}