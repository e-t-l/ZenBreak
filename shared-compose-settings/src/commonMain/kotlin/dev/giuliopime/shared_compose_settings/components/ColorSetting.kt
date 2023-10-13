package dev.giuliopime.shared_compose_settings.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CornerSize
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import dev.giuliopime.shared_compose_core.toColor

@Composable
fun ColorSetting(
    name: String,
    color: String,
    onColorChange: (String) -> Unit
) {
    Column(
        verticalArrangement = Arrangement.spacedBy(
            space = 8.dp
        )
    ) {
        Text(name)

        Row(
            horizontalArrangement = Arrangement.spacedBy(
                space = 16.dp
            ),
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .border(
                    border = BorderStroke(
                        width = 2.dp,
                        color = MaterialTheme.colorScheme.outline
                    ),
                    shape = MaterialTheme.shapes.small
                )
        ) {
            Box(
                modifier = Modifier
                    .size(30.dp)
                    .clip(
                        MaterialTheme.shapes.small.copy(
                            topEnd = CornerSize(0.dp),
                            bottomEnd = CornerSize(0.dp)
                        ),
                    )
                    .background(color.toColor(Color.Black))
            )

            BasicTextField(
                value = color,
                onValueChange = {
                    if (it.length <= 7)
                        onColorChange(it)
                },
                textStyle = MaterialTheme.typography.bodyMedium.copy(
                    color = MaterialTheme.colorScheme.onSurface
                )
            )
        }
    }
}