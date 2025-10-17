package com.unimarket.app.iu.screens

import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.rememberAsyncImagePainter
import com.unimarket.app.data.model.Categoria
import com.unimarket.app.data.model.Producto
import com.unimarket.app.data.model.UiState
import com.unimarket.app.viewmodel.AuthViewModel
import com.unimarket.app.viewmodel.ProfileViewModel
import com.unimarket.app.viewmodel.ProductViewModel
import org.koin.androidx.compose.koinViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PublishProductScreen(
    onNavigateBack: () -> Unit,
    viewModel: ProductViewModel = koinViewModel(),
    authViewModel: AuthViewModel = koinViewModel(),
    profileViewModel: ProfileViewModel = koinViewModel()
) {
    var nombre by remember { mutableStateOf("") }
    var descripcion by remember { mutableStateOf("") }
    var precio by remember { mutableStateOf("") }
    var selectedCategoria by remember { mutableStateOf(Categoria.OTROS) }
    var selectedCondicion by remember { mutableStateOf("Nuevo") }
    var selectedEstado by remember { mutableStateOf("Disponible") }
    var selectedImages by remember { mutableStateOf<List<Uri>>(emptyList()) }
    var showSuccess by remember { mutableStateOf(false) }
    var showError by remember { mutableStateOf(false) }
    var errorMessage by remember { mutableStateOf("") }
    var expandedCategoria by remember { mutableStateOf(false) }
    var expandedCondicion by remember { mutableStateOf(false) }
    var expandedEstado by remember { mutableStateOf(false) }

    val createState by viewModel.createState.collectAsState()
    val userState by profileViewModel.userState.collectAsState()

    val imagePickerLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetMultipleContents()
    ) { uris: List<Uri> ->
        if (uris.size <= 5) {
            selectedImages = uris
        } else {
            errorMessage = "Máximo 5 imágenes permitidas"
            showError = true
        }
    }

    LaunchedEffect(Unit) {
        profileViewModel.loadUser()
    }

    LaunchedEffect(createState) {
        when (createState) {
            is UiState.Success -> {
                showSuccess = true
            }
            is UiState.Error -> {
                errorMessage = (createState as UiState.Error).message
                showError = true
            }
            else -> {}
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Publicar Producto") },
                navigationIcon = {
                    IconButton(onClick = onNavigateBack) {
                        Icon(Icons.Default.ArrowBack, "Volver")
                    }
                }
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(16.dp)
                .verticalScroll(rememberScrollState())
        ) {
            // Sección de imágenes
            Text(
                text = "Fotos del producto *",
                style = MaterialTheme.typography.titleMedium,
                modifier = Modifier.padding(bottom = 8.dp)
            )

            Text(
                text = "Arrastra tus fotos aquí o haz clic para seleccionar (PNG, JPG hasta 10MB cada una)",
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                modifier = Modifier.padding(bottom = 12.dp)
            )

            if (selectedImages.isEmpty()) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(150.dp)
                        .border(
                            2.dp,
                            MaterialTheme.colorScheme.outline,
                            RoundedCornerShape(8.dp)
                        )
                        .clip(RoundedCornerShape(8.dp))
                        .background(MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.3f))
                        .clickable { imagePickerLauncher.launch("image/*") },
                    contentAlignment = Alignment.Center
                ) {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Icon(
                            Icons.Default.CameraAlt,
                            contentDescription = null,
                            modifier = Modifier.size(48.dp),
                            tint = MaterialTheme.colorScheme.primary
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            text = "Subir fotos",
                            color = MaterialTheme.colorScheme.primary
                        )
                    }
                }
            } else {
                LazyRow(
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    items(selectedImages) { uri ->
                        Box(
                            modifier = Modifier
                                .size(120.dp)
                                .clip(RoundedCornerShape(8.dp))
                        ) {
                            Image(
                                painter = rememberAsyncImagePainter(uri),
                                contentDescription = null,
                                modifier = Modifier.fillMaxSize(),
                                contentScale = ContentScale.Crop
                            )

                            IconButton(
                                onClick = {
                                    selectedImages = selectedImages.filter { it != uri }
                                },
                                modifier = Modifier
                                    .align(Alignment.TopEnd)
                                    .size(32.dp)
                                    .background(
                                        Color.Black.copy(alpha = 0.5f),
                                        RoundedCornerShape(16.dp)
                                    )
                            ) {
                                Icon(
                                    Icons.Default.Close,
                                    contentDescription = "Eliminar",
                                    tint = Color.White,
                                    modifier = Modifier.size(20.dp)
                                )
                            }
                        }
                    }

                    if (selectedImages.size < 5) {
                        item {
                            Box(
                                modifier = Modifier
                                    .size(120.dp)
                                    .border(
                                        2.dp,
                                        MaterialTheme.colorScheme.outline,
                                        RoundedCornerShape(8.dp)
                                    )
                                    .clip(RoundedCornerShape(8.dp))
                                    .clickable { imagePickerLauncher.launch("image/*") },
                                contentAlignment = Alignment.Center
                            ) {
                                Icon(
                                    Icons.Default.Add,
                                    contentDescription = "Agregar más",
                                    modifier = Modifier.size(32.dp),
                                    tint = MaterialTheme.colorScheme.primary
                                )
                            }
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            // Información Básica
            Text(
                text = "Información Básica",
                style = MaterialTheme.typography.titleMedium,
                modifier = Modifier.padding(bottom = 12.dp)
            )

            OutlinedTextField(
                value = nombre,
                onValueChange = { nombre = it },
                label = { Text("Título del producto *") },
                placeholder = { Text("Ej: Calculo Diferencial - Stewart 8va...") },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true
            )

            Spacer(modifier = Modifier.height(12.dp))

            OutlinedTextField(
                value = descripcion,
                onValueChange = { descripcion = it },
                label = { Text("Descripción *") },
                placeholder = { Text("Describe tu producto: estado, características, motivo de venta...") },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(120.dp),
                maxLines = 5
            )

            Spacer(modifier = Modifier.height(24.dp))

            // Categoría
            Text(
                text = "Categoría *",
                style = MaterialTheme.typography.titleSmall,
                modifier = Modifier.padding(bottom = 8.dp)
            )

            ExposedDropdownMenuBox(
                expanded = expandedCategoria,
                onExpandedChange = { expandedCategoria = !expandedCategoria }
            ) {
                OutlinedTextField(
                    value = selectedCategoria.displayName,
                    onValueChange = { },
                    readOnly = true,
                    trailingIcon = {
                        ExposedDropdownMenuDefaults.TrailingIcon(expanded = expandedCategoria)
                    },
                    modifier = Modifier
                        .menuAnchor()
                        .fillMaxWidth()
                )

                ExposedDropdownMenu(
                    expanded = expandedCategoria,
                    onDismissRequest = { expandedCategoria = false }
                ) {
                    Categoria.values().forEach { categoria ->
                        DropdownMenuItem(
                            text = { Text(categoria.displayName) },
                            onClick = {
                                selectedCategoria = categoria
                                expandedCategoria = false
                            }
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            // Estado del producto
            Text(
                text = "Estado del producto *",
                style = MaterialTheme.typography.titleSmall,
                modifier = Modifier.padding(bottom = 8.dp)
            )

            ExposedDropdownMenuBox(
                expanded = expandedEstado,
                onExpandedChange = { expandedEstado = !expandedEstado }
            ) {
                OutlinedTextField(
                    value = selectedEstado,
                    onValueChange = { },
                    readOnly = true,
                    trailingIcon = {
                        ExposedDropdownMenuDefaults.TrailingIcon(expanded = expandedEstado)
                    },
                    modifier = Modifier
                        .menuAnchor()
                        .fillMaxWidth()
                )

                ExposedDropdownMenu(
                    expanded = expandedEstado,
                    onDismissRequest = { expandedEstado = false }
                ) {
                    listOf("Disponible", "Reservado").forEach { estado ->
                        DropdownMenuItem(
                            text = { Text(estado) },
                            onClick = {
                                selectedEstado = estado
                                expandedEstado = false
                            }
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            // Condición
            Text(
                text = "Condición *",
                style = MaterialTheme.typography.titleSmall,
                modifier = Modifier.padding(bottom = 8.dp)
            )

            ExposedDropdownMenuBox(
                expanded = expandedCondicion,
                onExpandedChange = { expandedCondicion = !expandedCondicion }
            ) {
                OutlinedTextField(
                    value = selectedCondicion,
                    onValueChange = { },
                    readOnly = true,
                    trailingIcon = {
                        ExposedDropdownMenuDefaults.TrailingIcon(expanded = expandedCondicion)
                    },
                    modifier = Modifier
                        .menuAnchor()
                        .fillMaxWidth()
                )

                ExposedDropdownMenu(
                    expanded = expandedCondicion,
                    onDismissRequest = { expandedCondicion = false }
                ) {
                    listOf("Nuevo", "Usado").forEach { condicion ->
                        DropdownMenuItem(
                            text = { Text(condicion) },
                            onClick = {
                                selectedCondicion = condicion
                                expandedCondicion = false
                            }
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            // Precio
            OutlinedTextField(
                value = precio,
                onValueChange = { if (it.all { char -> char.isDigit() || char == '.' }) precio = it },
                label = { Text("Precio *") },
                leadingIcon = { Text("$") },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true
            )

            Spacer(modifier = Modifier.height(32.dp))

            // Botón publicar
            Button(
                onClick = {
                    if (nombre.isBlank() || descripcion.isBlank() || precio.isBlank() || selectedImages.isEmpty()) {
                        errorMessage = "Por favor completa todos los campos obligatorios y agrega al menos una imagen"
                        showError = true
                    } else {
                        val user = (userState as? UiState.Success)?.data
                        if (user != null) {
                            val producto = Producto(
                                nombre = nombre,
                                descripcion = descripcion,
                                precio = precio.toDoubleOrNull() ?: 0.0,
                                categoria = selectedCategoria.displayName,
                                estado = selectedEstado,
                                condicion = selectedCondicion,
                                vendedorId = user.uid,
                                vendedorNombre = user.nombre,
                                vendedorFoto = user.fotoPerfil,
                                vendedorUniversidad = user.universidad
                            )
                            viewModel.createProduct(producto, selectedImages)
                        }
                    }
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(50.dp),
                enabled = createState !is UiState.Loading
            ) {
                if (createState is UiState.Loading) {
                    CircularProgressIndicator(
                        modifier = Modifier.size(24.dp),
                        color = MaterialTheme.colorScheme.onPrimary
                    )
                } else {
                    Text("Publicar Producto", fontSize = 16.sp)
                }
            }

            if (showSuccess) {
                AlertDialog(
                    onDismissRequest = { },
                    title = { Text("¡Éxito!") },
                    text = { Text("Tu producto ha sido publicado correctamente") },
                    confirmButton = {
                        TextButton(
                            onClick = {
                                showSuccess = false
                                onNavigateBack()
                            }
                        ) {
                            Text("OK")
                        }
                    }
                )
            }

            if (showError) {
                Snackbar(
                    modifier = Modifier.padding(16.dp),
                    action = {
                        TextButton(onClick = { showError = false }) {
                            Text("OK")
                        }
                    }
                ) {
                    Text(errorMessage)
                }
            }
        }
    }
}