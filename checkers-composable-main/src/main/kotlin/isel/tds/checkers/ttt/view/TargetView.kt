package isel.tds.checkers.ttt.view

import androidx.compose.desktop.ui.tooling.preview.Preview
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import isel.tds.checkers.ttt.model.Piece
import isel.tds.checkers.ttt.model.Team
import isel.tds.checkers.ttt.model.TeamType


@Composable
fun TargetView(
    size: Dp = 100.dp,
    onClick: () -> Unit = {},
    modifier: Modifier = Modifier.size(size)
) {
    Box(
        modifier
            .clickable(onClick = onClick)
            .background(Color.Green, CircleShape)
    )
}