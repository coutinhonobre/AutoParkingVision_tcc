package com.github.coutinhonobre.ui.components.ui

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp

@Composable
fun LoadingButton(
    isLoading: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    buttonText: String = "Login",
    isEnabled: Boolean = true
) {
    Button(
        onClick = onClick,
        enabled = isEnabled && isLoading.not(),
        modifier = modifier.fillMaxWidth()
    ) {
        if (isLoading) {
            CircularProgressIndicator(
                modifier = Modifier.size(20.dp),
                color = MaterialTheme.colorScheme.secondary,
                strokeWidth = 2.dp
            )
        } else {
            Text(buttonText)
        }
    }
}

@Preview(showBackground = true)
@Composable
internal fun LoadingButtonPreview() {
    LoadingButton(isLoading = false, onClick = {})
}

@Preview(showBackground = true)
@Composable
internal fun LoadingButtonLoadingPreview() {
    LoadingButton(isLoading = true, onClick = {})
}

@Preview(showBackground = true)
@Composable
internal fun LoadingButtonDisabledPreview() {
    LoadingButton(isLoading = false, onClick = {}, isEnabled = false)  // Exemplo com o botão desabilitado
}
