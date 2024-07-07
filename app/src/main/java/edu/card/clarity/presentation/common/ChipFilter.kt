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
    filterItems: List<String>,
    initiallySelectedItemIndices: List<Int>,
    onSelectedChanged: (index: Int, selected: Boolean) -> Unit
) {
    LazyRow {
        items(filterItems.size) {
            val isFirstChip = it == 0
            var selected by remember {
                mutableStateOf(initiallySelectedItemIndices.contains(it))
            }

            if (!isFirstChip) {
                Spacer(modifier = Modifier.width(8.dp))
            }
            FilterChip(
                selected = selected,
                label = { Text(text = filterItems[it]) },
                onClick = {
                    selected = !selected
                    onSelectedChanged(it, selected)
                }
            )
        }
    }
}