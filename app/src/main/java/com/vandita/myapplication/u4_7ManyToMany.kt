package com.vandita.myapplication

import android.app.Application
import android.content.Context
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.vandita.myapplication.ui.theme.MyApplicationTheme
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*

import androidx.compose.ui.unit.dp
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.room.*
import androidx.room.Room.databaseBuilder
import kotlinx.coroutines.launch


class u4_7ManyToMany : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            MTMStudentSubjectScreen()
        }
    }
}


@Entity
data class MTMStudent(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val name: String,
    val phone: String
)

@Entity(
    indices = [Index(value = ["subjectName"], unique = true)]
)
data class MTMSubject(
    @PrimaryKey(autoGenerate = true)
    val subjectId: Int = 0,
    val subjectName: String
)

@Entity(
    primaryKeys = ["studentId", "subjectId"],
    foreignKeys = [
        ForeignKey(
            entity = MTMStudent::class,
            parentColumns = ["id"],
            childColumns = ["studentId"],
            onDelete = ForeignKey.CASCADE
        ),
        ForeignKey(
            entity = MTMSubject::class,
            parentColumns = ["subjectId"],
            childColumns = ["subjectId"],
            onDelete = ForeignKey.CASCADE
        )
    ],
    indices = [
        Index(value = ["studentId"]),
        Index(value = ["subjectId"])
    ]
)
data class MTMStudentSubjectCrossRef(
    val studentId: Int,
    val subjectId: Int
)

data class MTMStudentWithSubjects(
    @Embedded
    val student: MTMStudent,

    @Relation(
        parentColumn = "id",
        entityColumn = "subjectId",
        associateBy = Junction(
            value = MTMStudentSubjectCrossRef::class,
            parentColumn = "studentId",
            entityColumn = "subjectId"
        )
    )
    val subjects: List<MTMSubject>
)

@Dao
interface MTMStudentSubjectDao {

    @Insert
    suspend fun addStudent(student: MTMStudent): Long

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun addSubject(subject: MTMSubject): Long

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun enrollStudent(
        ref: MTMStudentSubjectCrossRef
    )

    @Query("SELECT * FROM MTMSubject WHERE subjectName = :name")
    suspend fun getSubjectByName(name: String): MTMSubject?

    @Transaction
    @Query("SELECT * FROM MTMStudent ORDER BY id")
    suspend fun getStudentsWithSubjects(): List<MTMStudentWithSubjects>

    @Query("SELECT * FROM MTMSubject ORDER BY subjectId")
    suspend fun getAllSubjects(): List<MTMSubject>

    @Delete
    suspend fun deleteStudent(student: MTMStudent)

    @Delete
    suspend fun deleteSubject(subject: MTMSubject)
}

@Database(
    entities = [
        MTMStudent::class,
        MTMSubject::class,
        MTMStudentSubjectCrossRef::class
    ],
    version = 1,
    exportSchema = false
)
abstract class MTMStudentSubjectDatabase : RoomDatabase() {

    abstract fun studentSubjectDao(): MTMStudentSubjectDao
}

object MTMStudentSubjectDB {

    @Volatile
    private var INSTANCE: MTMStudentSubjectDatabase? = null

    fun getDatabase(context: Context): MTMStudentSubjectDatabase {

        return INSTANCE ?: synchronized(this) {

            INSTANCE ?: databaseBuilder(
                context.applicationContext,
                MTMStudentSubjectDatabase::class.java,
                "mtm_student_subject_db"
            )
                .build()
                .also {
                    INSTANCE = it
                }
        }
    }
}

class MTMStudentSubjectViewModel(application: Application) :
    AndroidViewModel(application) {

    private val dao = MTMStudentSubjectDB
        .getDatabase(application)
        .studentSubjectDao()

    var studentList by mutableStateOf<List<MTMStudentWithSubjects>>(
        emptyList()
    )
        private set

    var subjectList by mutableStateOf<List<MTMSubject>>(
        emptyList()
    )
        private set

    init {
        refreshData()
    }

    fun refreshData() {
        viewModelScope.launch {
            studentList = dao.getStudentsWithSubjects()
            subjectList = dao.getAllSubjects()
        }
    }

    fun addStudent(name: String, phone: String) {
        viewModelScope.launch {

            dao.addStudent(
                MTMStudent(
                    name = name.trim(),
                    phone = phone.trim()
                )
            )

            refreshData()
        }
    }

    fun addSubject(name: String) {
        viewModelScope.launch {

            dao.addSubject(
                MTMSubject(
                    subjectName = name.trim()
                )
            )

            refreshData()
        }
    }

    fun enrollStudent(studentId: Int, subjectName: String) {
        viewModelScope.launch {

            val name = subjectName.trim()

            var subject = dao.getSubjectByName(name)

            if (subject == null) {

                val newId = dao.addSubject(
                    MTMSubject(subjectName = name)
                )

                subject = if (newId != -1L) {
                    MTMSubject(
                        subjectId = newId.toInt(),
                        subjectName = name
                    )
                } else {
                    dao.getSubjectByName(name)
                }
            }

            subject?.let {
                dao.enrollStudent(
                    MTMStudentSubjectCrossRef(
                        studentId = studentId,
                        subjectId = it.subjectId
                    )
                )
            }

            refreshData()
        }
    }

    fun deleteStudent(student: MTMStudent) {
        viewModelScope.launch {
            dao.deleteStudent(student)
            refreshData()
        }
    }

    fun deleteSubject(subject: MTMSubject) {
        viewModelScope.launch {
            dao.deleteSubject(subject)
            refreshData()
        }
    }
}

@Composable
fun MTMStudentSubjectScreen(
    viewModel: MTMStudentSubjectViewModel = viewModel()
) {

    var studentName by remember { mutableStateOf("") }
    var phone by remember { mutableStateOf("") }
    var subjectName by remember { mutableStateOf("") }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {

        Text(
            "Student Subject Enrollment",
            style = MaterialTheme.typography.titleLarge
        )

        OutlinedTextField(
            value = studentName,
            onValueChange = { studentName = it },
            label = { Text("Student Name") },
            modifier = Modifier.fillMaxWidth()
        )

        OutlinedTextField(
            value = phone,
            onValueChange = { phone = it },
            label = { Text("Phone Number") },
            modifier = Modifier.fillMaxWidth()
        )

        Button(
            onClick = {
                if (studentName.isNotBlank() &&
                    phone.isNotBlank()
                ) {
                    viewModel.addStudent(studentName, phone)
                    studentName = ""
                    phone = ""
                }
            },
            modifier = Modifier.padding(top = 8.dp)
        ) {
            Text("Add Student")
        }

        Spacer(modifier = Modifier.height(12.dp))

        OutlinedTextField(
            value = subjectName,
            onValueChange = { subjectName = it },
            label = { Text("New Subject Name") },
            modifier = Modifier.fillMaxWidth()
        )

        Button(
            onClick = {
                if (subjectName.isNotBlank()) {
                    viewModel.addSubject(subjectName)
                    subjectName = ""
                }
            },
            modifier = Modifier.padding(top = 8.dp)
        ) {
            Text("Add Subject")
        }

        Spacer(modifier = Modifier.height(12.dp))

        Text(
            "Available Subjects",
            style = MaterialTheme.typography.titleMedium
        )

        LazyColumn(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f)
        ) {

            items(
                items = viewModel.subjectList,
                key = { "subject_${it.subjectId}" }
            ) { subject ->

                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 4.dp),
                    horizontalArrangement =
                        Arrangement.SpaceBetween
                ) {

                    Text(subject.subjectName)

                    Button(
                        onClick = {
                            viewModel.deleteSubject(subject)
                        }
                    ) {
                        Text("Delete")
                    }
                }
            }

            item {
                Spacer(modifier = Modifier.height(8.dp))

                Text(
                    "Students and Enrolled Subjects",
                    style = MaterialTheme.typography.titleMedium
                )
            }

            items(
                items = viewModel.studentList,
                key = { "student_${it.student.id}" }
            ) { item ->

                var enrollName by remember(item.student.id) {
                    mutableStateOf("")
                }

                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 8.dp)
                ) {

                    Column(
                        modifier = Modifier.padding(12.dp)
                    ) {

                        Text("ID: ${item.student.id}")

                        Text("Name: ${item.student.name}")

                        Text("Phone: ${item.student.phone}")

                        Spacer(modifier = Modifier.height(8.dp))

                        Text("Enrolled Subjects:")

                        if (item.subjects.isEmpty()) {
                            Text("No subjects enrolled")
                        } else {
                            item.subjects.forEach { subject ->
                                Text("• ${subject.subjectName}")
                            }
                        }

                        OutlinedTextField(
                            value = enrollName,
                            onValueChange = { enrollName = it },
                            label = {
                                Text("Enter Subject to Enroll")
                            },
                            modifier = Modifier.fillMaxWidth()
                        )

                        Button(
                            onClick = {
                                if (enrollName.isNotBlank()) {
                                    viewModel.enrollStudent(
                                        item.student.id,
                                        enrollName
                                    )

                                    enrollName = ""
                                }
                            },
                            modifier = Modifier.padding(top = 6.dp)
                        ) {
                            Text("Enroll Subject")
                        }

                        Button(
                            onClick = {
                                viewModel.deleteStudent(item.student)
                            },
                            modifier = Modifier.padding(top = 6.dp)
                        ) {
                            Text("Delete Student")
                        }
                    }
                }
            }
        }
    }
}