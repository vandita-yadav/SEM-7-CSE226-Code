package com.vandita.myapplication

import android.content.Context
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
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

@Entity(tableName = "student")
data class Student(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val name: String,
    val age: Int
)

@Dao
interface StudentDao {
    @Insert
    suspend fun insertStudent(student: Student)

    @Query("SELECT * FROM student")
    fun getAllStudents(): Flow<List<Student>>

    @Delete
    suspend fun deleteStudent(student: Student)

    @Update
    suspend fun updateStudent(student: Student)
}

class StudentViewModel(
    private val dao: StudentDao
) : ViewModel() {

    val students = dao.getAllStudents().stateIn(
        viewModelScope,
        SharingStarted.WhileSubscribed(5000),
        emptyList()
    )

    fun addStudent(name: String, age: Int) {
        viewModelScope.launch {
            dao.insertStudent(
                Student(
                    name = name,
                    age = age
                )
            )
        }
    }

    fun deleteStudent(student: Student) {
        viewModelScope.launch {
            dao.deleteStudent(student)
        }
    }

    fun updateStudent(student: Student) {
        viewModelScope.launch {
            dao.updateStudent(student)
        }
    }
}

@Database(
    entities = [Student::class],
    version = 1,
    exportSchema = false
)
abstract class StudentDatabase : RoomDatabase() {

    abstract fun studentDao(): StudentDao

    companion object {
        @Volatile
        private var INSTANCE: StudentDatabase? = null

        fun getDatabase(context: Context): StudentDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    StudentDatabase::class.java,
                    "student_database"
                ).build()
                INSTANCE = instance
                instance
            }
        }
    }
}

class u4_2RoomDatabaseAddUpdateDelete : ComponentActivity() {

    private val viewModel: StudentViewModel by viewModels {
        object : ViewModelProvider.Factory {
            @Suppress("UNCHECKED_CAST")
            override fun <T : ViewModel> create(modelClass: Class<T>): T {
                val dao = StudentDatabase
                    .getDatabase(applicationContext)
                    .studentDao()
                return StudentViewModel(dao) as T
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MaterialTheme {
                StudentScreen(viewModel)
            }
        }
    }
}

@Composable
fun StudentScreen(viewModel: StudentViewModel) {
    var id by remember { mutableStateOf("") }
    var name by remember { mutableStateOf("") }
    var age by remember { mutableStateOf("") }

    val students by viewModel.students.collectAsStateWithLifecycle()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Text(
            text = "Room Database App",
            style = MaterialTheme.typography.headlineMedium
        )

        Spacer(modifier = Modifier.height(20.dp))

        OutlinedTextField(
            value = id,
            onValueChange = { id = it },
            label = { Text("Enter Student ID") },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(10.dp))

        OutlinedTextField(
            value = name,
            onValueChange = { name = it },
            label = { Text("Enter Student Name") },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(10.dp))

        OutlinedTextField(
            value = age,
            onValueChange = { age = it },
            label = { Text("Enter Student Age") },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(15.dp))

        Button(
            onClick = {
                val studentAge = age.toIntOrNull()
                if (name.isNotBlank() && studentAge != null && studentAge > 0) {
                    viewModel.addStudent(name.trim(), studentAge)
                    id = ""
                    name = ""
                    age = ""
                }
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Add Student")
        }

        Button(
            onClick = {
                val studentId = id.toIntOrNull()
                val studentAge = age.toIntOrNull()
                if (studentId != null && studentAge != null && studentAge > 0 && name.isNotBlank()) {
                    // Check if a record exists with the exact same ID and name
                    val existingStudent = students.find { it.id == studentId && it.name.equals(name.trim(), ignoreCase = true) }
                    if (existingStudent != null) {
                        viewModel.updateStudent(
                            Student(
                                id = studentId,
                                name = existingStudent.name,
                                age = studentAge
                            )
                        )
                        id = ""
                        name = ""
                        age = ""
                    }
                }
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Update Student")
        }

        Button(
            onClick = {
                val studentId = id.toIntOrNull()
                if (studentId != null) {
                    val existingStudent = students.find { it.id == studentId }
                    if (existingStudent != null) {
                        name = existingStudent.name
                        age = existingStudent.age.toString()
                    }
                }
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Show Student")
        }

        Spacer(modifier = Modifier.height(25.dp))

        Text(
            text = "Student Records",
            style = MaterialTheme.typography.titleLarge
        )

        Spacer(modifier = Modifier.height(10.dp))

        LazyColumn {
            items(
                items = students,
                key = { it.id }
            ) { student ->
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 5.dp)
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(12.dp),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Column {
                            Text(text = "ID: ${student.id}")
                            Text(text = "Name: ${student.name}")
                            Text(text = "Age: ${student.age}")
                        }

                        TextButton(
                            onClick = { viewModel.deleteStudent(student) }
                        ) {
                            Text(
                                "Delete",
                                color = MaterialTheme.colorScheme.error
                            )
                        }
                    }
                }
            }
        }
    }
}

/*
1. Header with background and text  "Employee Manager"
2. After "Add/Update Employee" Text heading
3. Three text fields - Name, Department, Salary (Outlined text field with no box) and their respective Material UI Icons
4. Two buttons - primary button "Add Employee" and Outlined Text button "Clear"
5. After "Employee List" heading
6. Shows the list of people added in the previous step (Name, then next line - Department | Salary)
    Every profile should be displayed as list of cards with a edit button (pencil) and delete button (trash)
7. Whenever employee is added or update - toast message should be displayed for the same.
8. No update button (add button should do the same task)
9. If user clicks on pencil icon for editing a profile, then the profile details should get filled automatically in above text field and user can enter new details and click on add
* */