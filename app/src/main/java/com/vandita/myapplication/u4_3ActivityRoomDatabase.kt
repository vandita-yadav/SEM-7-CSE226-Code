package com.vandita.myapplication

import android.content.Context
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CurrencyRupee
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Work
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewModelScope
import androidx.room.*
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

@Entity(tableName = "employees")
data class EmployeeEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val name: String,
    val department: String,
    val salary: Double
)

@Dao
interface EmployeeDao
{
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertEmployee(employee: EmployeeEntity)

    @Query("SELECT * FROM employees ORDER BY id DESC")
    fun getAllEmployees(): Flow<List<EmployeeEntity>>

    @Delete
    suspend fun deleteEmployee(employee: EmployeeEntity)
}

@Database(entities = [EmployeeEntity::class], version = 1, exportSchema = false)
abstract class EmployeeDatabase : RoomDatabase() {
    abstract fun employeeDao(): EmployeeDao

    companion object {
        @Volatile
        private var INSTANCE: EmployeeDatabase? = null

        fun getDatabase(context: Context): EmployeeDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    EmployeeDatabase::class.java,
                    "employee_database"
                ).build()
                INSTANCE = instance
                instance
            }
        }
    }
}

class EmployeeViewModel(private val dao: EmployeeDao) : ViewModel()
{
    var editingEmployeeId by mutableStateOf<Int?>(null)
        private set
    var nameInput by mutableStateOf("")
    var departmentInput by mutableStateOf("")
    var salaryInput by mutableStateOf("")

    val employees = dao.getAllEmployees().stateIn(
        viewModelScope,
        SharingStarted.WhileSubscribed(5000),
        emptyList()
    )

    fun startEditing(employee: EmployeeEntity) {
        editingEmployeeId = employee.id
        nameInput = employee.name
        departmentInput = employee.department
        salaryInput = employee.salary.toString()
    }

    fun clearInputs() {
        editingEmployeeId = null
        nameInput = ""
        departmentInput = ""
        salaryInput = ""
    }

    fun saveEmployee(context: Context) {
        val salaryVal = salaryInput.toDoubleOrNull()
        if (nameInput.isNotBlank() && departmentInput.isNotBlank() && salaryVal != null) {
            viewModelScope.launch {
                val employee = EmployeeEntity(
                    id = editingEmployeeId ?: 0,
                    name = nameInput.trim(),
                    department = departmentInput.trim(),
                    salary = salaryVal
                )
                dao.insertEmployee(employee)
                if (editingEmployeeId == null) {
                    Toast.makeText(context, "Employee added successfully!", Toast.LENGTH_SHORT).show()
                } else {
                    Toast.makeText(context, "Employee updated successfully!", Toast.LENGTH_SHORT).show()
                }
                clearInputs()
            }
        } else {
            Toast.makeText(context, "Please enter valid employee details", Toast.LENGTH_SHORT).show()
        }
    }

    fun deleteEmployee(employee: EmployeeEntity, context: Context) {
        viewModelScope.launch {
            dao.deleteEmployee(employee)
            if (editingEmployeeId == employee.id) {
                clearInputs()
            }
            Toast.makeText(context, "Employee deleted successfully!", Toast.LENGTH_SHORT).show()
        }
    }
}

class u4_3ActivityRoomDatabase : ComponentActivity() {

    private val viewModel: EmployeeViewModel by viewModels {
        object : ViewModelProvider.Factory {
            @Suppress("UNCHECKED_CAST")
            override fun <T : ViewModel> create(modelClass: Class<T>): T {
                val dao = EmployeeDatabase.getDatabase(applicationContext).employeeDao()
                return EmployeeViewModel(dao) as T
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MaterialTheme {
                EmployeeManagerScreen(viewModel = viewModel, context = this)
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EmployeeManagerScreen(viewModel: EmployeeViewModel, context: Context) {
    val employees by viewModel.employees.collectAsStateWithLifecycle()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
    ) {
        // 1. Header with background and text "Employee Manager"
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .background(MaterialTheme.colorScheme.primaryContainer)
                .padding(vertical = 20.dp),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = "Employee Manager",
                style = MaterialTheme.typography.headlineLarge,
                color = MaterialTheme.colorScheme.onPrimaryContainer
            )
        }

        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            // 2. Heading "Add/Update Employee"
            item {
                Text(
                    text = if (viewModel.editingEmployeeId == null) "Add/Update Employee" else "Update Employee (ID: ${viewModel.editingEmployeeId})",
                    style = MaterialTheme.typography.titleLarge,
                    color = MaterialTheme.colorScheme.primary
                )
                Spacer(modifier = Modifier.height(8.dp))
            }

            // 3. Three text fields - Name, Department, Salary with respective Material UI Icons
            item {
                OutlinedTextField(
                    value = viewModel.nameInput,
                    onValueChange = { viewModel.nameInput = it },
                    label = { Text("Name") },
                    leadingIcon = { Icon(Icons.Default.Person, contentDescription = "Name Icon") },
                    modifier = Modifier.fillMaxWidth()
                )
            }

            item {
                OutlinedTextField(
                    value = viewModel.departmentInput,
                    onValueChange = { viewModel.departmentInput = it },
                    label = { Text("Department") },
                    leadingIcon = { Icon(Icons.Default.Work, contentDescription = "Department Icon") },
                    modifier = Modifier.fillMaxWidth()
                )
            }

            item {
                OutlinedTextField(
                    value = viewModel.salaryInput,
                    onValueChange = { viewModel.salaryInput = it },
                    label = { Text("Salary") },
                    leadingIcon = { Icon(Icons.Default.CurrencyRupee, contentDescription = "Salary Icon") },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                    modifier = Modifier.fillMaxWidth()
                )
            }

            // 4. Two buttons - primary button "Add Employee" (or "Update Employee") and Outlined Text button "Clear"
            // 8. No update button (add button does the same task)
            item {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    Button(
                        onClick = { viewModel.saveEmployee(context) },
                        modifier = Modifier.weight(1f)
                    ) {
                        Text(if (viewModel.editingEmployeeId == null) "Add Employee" else "Update Employee")
                    }

                    OutlinedButton(
                        onClick = { viewModel.clearInputs() },
                        modifier = Modifier.weight(1f)
                    ) {
                        Text("Clear")
                    }
                }
                Spacer(modifier = Modifier.height(16.dp))
            }

            // 5. Heading "Employee List"
            item {
                Text(
                    text = "Employee List",
                    style = MaterialTheme.typography.titleLarge,
                    color = MaterialTheme.colorScheme.primary
                )
                Spacer(modifier = Modifier.height(8.dp))
            }

            // 6. Shows the list of employees (Name, then next line - Department | Salary)
            // Every profile displayed as card with edit button (pencil) and delete button (trash)
            // 9. Clicking pencil icon fills profile details automatically in text fields
            items(
                items = employees,
                key = { it.id }
            ) { employee ->
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column(
                            modifier = Modifier.weight(1f)
                        ) {
                            Text(
                                text = employee.name,
                                style = MaterialTheme.typography.titleMedium
                            )
                            Spacer(modifier = Modifier.height(4.dp))
                            Text(
                                text = "${employee.department} | ₹${employee.salary}",
                                style = MaterialTheme.typography.titleSmall,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }

                        Row(
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            IconButton(onClick = { viewModel.startEditing(employee) }) {
                                Icon(
                                    imageVector = Icons.Default.Edit,
                                    contentDescription = "Edit Employee",
                                    tint = MaterialTheme.colorScheme.primary
                                )
                            }

                            IconButton(onClick = { viewModel.deleteEmployee(employee, context) }) {
                                Icon(
                                    imageVector = Icons.Default.Delete,
                                    contentDescription = "Delete Employee",
                                    tint = MaterialTheme.colorScheme.error
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}
