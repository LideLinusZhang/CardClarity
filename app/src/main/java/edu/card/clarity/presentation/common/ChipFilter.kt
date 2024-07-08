package edu.card.clarity.presentation.common

import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.material3.FilterChip
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun ChipFilter(
    filterOptions: List<String>,
    initiallySelectedOptionIndices: List<Int>,
    onSelectedChanged: (index: Int, selected: Boolean) -> Unit
) {
    LazyRow {
        items(filterOptions.size) {
            val isFirstChip = it == 0
            var selected by remember {
                mutableStateOf(initiallySelectedOptionIndices.contains(it))
            }

            if (!isFirstChip) {
                Spacer(modifier = Modifier.width(8.dp))
            }
            FilterChip(
                selected = selected,
                label = { Text(text = filterOptions[it]) },
                onClick = {
                    selected = !selected
                    onSelectedChanged(it, selected)
                }
            )
        }
    }
}