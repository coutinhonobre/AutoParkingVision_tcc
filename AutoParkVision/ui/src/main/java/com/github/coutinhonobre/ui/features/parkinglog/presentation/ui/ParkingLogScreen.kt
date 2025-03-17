package com.github.coutinhonobre.ui.features.parkinglog.presentation.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.CalendarMonth
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.DateRangePicker
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExtendedFloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberDateRangePickerState
import androidx.compose.material3.rememberTopAppBarState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableLongStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.github.coutinhonobre.domain.feature.parkingactivitylog.repository.ParkingActivityLogRepository
import com.github.coutinhonobre.domain.feature.parkingactivitylog.usecase.FilterParkingLogUseCase
import com.github.coutinhonobre.domain.feature.parkingactivitylog.usecase.ListParkingActivityLogUseCase
import com.github.coutinhonobre.ui.components.ui.appbar.GenericTopAppBar
import com.github.coutinhonobre.ui.components.ui.templates.GenericEmptyScreen
import com.github.coutinhonobre.ui.components.ui.templates.GenericLoadingScreen
import com.github.coutinhonobre.ui.features.home.presentation.ui.componets.ParkingLogCard
import com.github.coutinhonobre.ui.features.parkinglog.presentation.viewmodel.ParkingLogViewAction
import com.github.coutinhonobre.ui.features.parkinglog.presentation.viewmodel.ParkingLogViewModel
import com.github.coutinhonobre.ui.features.parkinglog.presentation.viewmodel.ParkingLogViewState
import com.github.coutinhonobre.ui.routers.Router
import java.time.Instant
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ParkingLogScreen(
    navController: NavController,
    parkingLogViewModel: ParkingLogViewModel
) {
    val viewState by parkingLogViewModel.uiState.collectAsState()

    var plateSearch by remember { mutableStateOf("") }
    var startDate by remember { mutableLongStateOf(0L) }
    var endDate by remember { mutableLongStateOf(0L) }

    var showDatePicker by remember { mutableStateOf(false) }

    val scrollBehavior = TopAppBarDefaults.exitUntilCollapsedScrollBehavior(rememberTopAppBarState())

    Scaffold(
        topBar = {
            GenericTopAppBar(
                titleText = "Registro de Atividades",
            ) {
                navController.popBackStack()
            }
        },
        floatingActionButton = {
            ExtendedFloatingActionButton(
                onClick = {
                    parkingLogViewModel.onEvent(ParkingLogViewAction.Filter(plateSearch, startDate, endDate))
                },
                icon = { Icon(Icons.Filled.Search, "Extended floating action button.") },
                text = { Text(text = "Filtrar") },
            )
        }
    ) { innerPadding ->

        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp),
        ) {
            item {
                OutlinedTextField(
                    value = plateSearch,
                    onValueChange = { plateSearch = it },
                    label = { Text("Placa do veículo") },
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(12.dp)
                )
            }

            item {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    OutlinedTextField(
                        value = formatMillisToDate(startDate),
                        onValueChange = {},
                        label = { Text("Data de Início") },
                        readOnly = true,
                        trailingIcon = {
                            IconButton(onClick = { showDatePicker = true }) {
                                Icon(Icons.Default.CalendarMonth, contentDescription = "Selecionar data")
                            }
                        },
                        modifier = Modifier.weight(1f)
                    )

                    OutlinedTextField(
                        value = formatMillisToDate(endDate),
                        onValueChange = {},
                        label = { Text("Data de Fim") },
                        readOnly = true,
                        trailingIcon = {
                            IconButton(onClick = { showDatePicker = true }) {
                                Icon(Icons.Default.CalendarMonth, contentDescription = "Selecionar data")
                            }
                        },
                        modifier = Modifier.weight(1f)
                    )
                }
            }

            when (viewState) {
                is ParkingLogViewState.Loading -> {
                    item {
                        GenericLoadingScreen()
                    }
                }

                is ParkingLogViewState.Empty -> {
                    item {
                        GenericEmptyScreen(
                            emptyMessage = "Nenhum registro encontrado",
                        ) {
                            parkingLogViewModel.onEvent(ParkingLogViewAction.Retry)
                        }
                    }
                }

                is ParkingLogViewState.Idle -> {
                    val parkingLot = (viewState as? ParkingLogViewState.Idle)?.parkingLogs

                    if (parkingLot.isNullOrEmpty()) {
                        item {
                            GenericEmptyScreen(emptyMessage = "Nenhum registro encontrado") {
                                parkingLogViewModel.onEvent(ParkingLogViewAction.Retry)
                            }
                        }
                    } else {
                        item {
                            Spacer(modifier = Modifier.height(16.dp))
                            Text(
                                text = "Registros de Atividades",
                                style = MaterialTheme.typography.titleLarge,
                            )
                            Spacer(modifier = Modifier.height(8.dp))
                        }
                        items(parkingLot) { parkingLog ->
                            ParkingLogCard(parkingLogModel = parkingLog)
                            Spacer(modifier = Modifier.height(4.dp))
                        }
                    }
                }

                is ParkingLogViewState.Error -> {
                    item {
                        GenericEmptyScreen(
                            emptyMessage = "Erro ao carregar registros",
                        ) {
                            parkingLogViewModel.onEvent(ParkingLogViewAction.Retry)
                        }
                    }
                }
            }
        }

    }

    if (showDatePicker) {
        DateRangePickerModal(
            onDateRangeSelected = { dateRange ->
                startDate = convertUtcToLocalTime(dateRange.first ?: 0L)
                endDate = convertUtcToLocalTime(dateRange.second ?: 0L)
                showDatePicker = false
            },
            onDismiss = { showDatePicker = false }
        )
    }
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DateRangePickerModal(
    onDateRangeSelected: (Pair<Long?, Long?>) -> Unit,
    onDismiss: () -> Unit
) {
    val dateRangePickerState = rememberDateRangePickerState()

    DatePickerDialog(
        onDismissRequest = onDismiss,
        confirmButton = {
            TextButton(
                onClick = {
                    onDateRangeSelected(
                        Pair(
                            dateRangePickerState.selectedStartDateMillis,
                            dateRangePickerState.selectedEndDateMillis
                        )
                    )
                    onDismiss()
                }
            ) {
                Text("OK")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Cancel")
            }
        }
    ) {
        DateRangePicker(
            state = dateRangePickerState,
            title = {
                Text(
                    text = "Select date range"
                )
            },
            showModeToggle = false,
            modifier = Modifier
                .fillMaxWidth()
                .height(500.dp)
                .padding(16.dp)
        )
    }
}

fun formatMillisToDate(millis: Long): String {
    val formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy", Locale.getDefault())
    val localDate = Instant.ofEpochMilli(millis)
        .atZone(ZoneId.systemDefault())
        .toLocalDate()
    return formatter.format(localDate)
}

fun convertUtcToLocalTime(millis: Long): Long {
    val timeZoneOffset = java.util.TimeZone.getDefault().getOffset(millis)
    return millis + timeZoneOffset
}

@Preview(showBackground = true)
@Composable
fun PreviewParkingLogScreen() {
    val navController = rememberNavController()
    val viewModel = ParkingLogViewModel(
        listParkingActivityLogUseCase = ListParkingActivityLogUseCase(
            parkingActivityLogRepository = object : ParkingActivityLogRepository {

            }
        ),
        filterParkingLogUseCase = FilterParkingLogUseCase(
            parkingActivityLogRepository = object : ParkingActivityLogRepository {

            }
        )
    )

    ParkingLogScreen(
        navController = navController,
        parkingLogViewModel = viewModel
    )
}
