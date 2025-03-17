package com.github.coutinhonobre.ui.components.ui.appbar

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun GenericTopAppBar(
    titleText: String,
    onNavigationClick: () -> Unit
) {
    TopAppBar(
        title = {
            Text(titleText)
        },
        navigationIcon = {
            IconButton(onClick = onNavigationClick) {
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                    contentDescription = "Voltar"
                )
            }
        }
    )
}

@Preview(showBackground = true)
@Composable
fun GenericTopAppBarPreview() {
    GenericTopAppBar(
        titleText = "Esqueci minha senha",
        onNavigationClick = {}
    )
}
