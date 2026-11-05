package com.vandita.myapplication


import android.app.Application
import android.content.Context
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.room.Dao
import androidx.room.Database
import androidx.room.Delete
import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.Insert
import androidx.room.PrimaryKey
import androidx.room.Query
import androidx.room.Relation
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.Transaction
import kotlinx.coroutines.launch

class u4_6OneToMany : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            MaterialTheme {
                StudentInfoScreen()
            }
        }
    }
}



@Entity
data class StudentInfo(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val name: String,
    val phnum: String
)

@Entity(
    foreignKeys = [
        ForeignKey(
            entity = StudentInfo::class,
            parentColumns = ["id"],
            childColumns = ["studentId"],
            onDelete = ForeignKey.CASCADE
        )
    ],
    indices = [Index(value = ["studentId"])]
)
data class Course(
    @PrimaryKey(autoGenerate = true)
    val courseId: Int = 0,
    val courseName: String,
    val studentId: Int
)



data class StudentInfoWithCourses(

    @Embedded
    val studentInfo: StudentInfo,

    @Relation(
        parentColumn = "id",
        entityColumn = "studentId"
    )
    val courses: List<Course>
)



@Dao
interface StudentInfoOperations {

    @Insert
    suspend fun addStudentDetail(student: StudentInfo): Long

    @Insert
    suspend fun addCourse(course: Course)

    @Transaction
    @Query("SELECT * FROM StudentInfo ORDER BY id")
    suspend fun fetchStudentsWithCourses(): List<StudentInfoWithCourses>

    @Delete

    suspend fun deleteStudentInfo(student: StudentInfo)
}



@Database(
    entities = [StudentInfo::class, Course::class],
    version = 2,
    exportSchema = false
)
abstract class StudentInfoAbstraction : RoomDatabase() {

    abstract fun studentInfo(): StudentInfoOperations
}



object StudentInfoDB {

    @Volatile
    private var INSTANCE: StudentInfoAbstraction? = null

    fun getStudentInfoDB(context: Context): StudentInfoAbstraction {

        return INSTANCE ?: synchronized(this) {

            INSTANCE ?: Room.databaseBuilder(
                context.applicationContext,
                StudentInfoAbstraction::class.java,
                "studentinfodb"
            )
                .build()
                .also {
                    INSTANCE = it
                }
        }
    }
}



class StudentInfoArchi(application: Application) :
    AndroidViewModel(application) {

    private val db = StudentInfoDB.getStudentInfoDB(application)

    var studentCourseList by mutableStateOf<List<StudentInfoWithCourses>>(
        emptyList()
    )
        private set

    init {
        getStudentInfoWithCourses()
    }

    fun addStudentInfo(name: String, phone: String) {

        viewModelScope.launch {

            db.studentInfo().addStudentDetail(
                StudentInfo(
                    name = name,
                    phnum = phone
                )
            )

            getStudentInfoWithCourses()
        }
    }

    fun enrollCourse(studentId: Int, courseName: String) {

        viewModelScope.launch {

            db.studentInfo().addCourse(
                Course(
                    courseName = courseName.trim(),
                    studentId = studentId
                )
            )

            getStudentInfoWithCourses()
        }
    }

    fun getStudentInfoWithCourses() {

        viewModelScope.launch {

            studentCourseList =
                db.studentInfo().fetchStudentsWithCourses()
        }
    }

    fun deleteStudent(student: StudentInfo) {

        viewModelScope.launch {

            db.studentInfo().deleteStudentInfo(student)

            getStudentInfoWithCourses()
        }
    }
}


@Composable
fun StudentInfoScreen(
    viewModel: StudentInfoArchi = viewModel()
) {

    var name by remember { mutableStateOf("") }
    var phone by remember { mutableStateOf("") }

    LaunchedEffect(Unit) {
        viewModel.getStudentInfoWithCourses()
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {

        Text(
            text = "Student Registration",
            style = MaterialTheme.typography.titleLarge
        )

        OutlinedTextField(
            value = name,
            onValueChange = { name = it },
            label = { Text("Enter student name") },
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 8.dp)
        )

        OutlinedTextField(
            value = phone,
            onValueChange = { phone = it },
            label = { Text("Enter phone number") },
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 8.dp)
        )

        Button(
            onClick = {

                if (name.isNotBlank() && phone.isNotBlank()) {

                    viewModel.addStudentInfo(
                        name.trim(),
                        phone.trim()
                    )

                    name = ""
                    phone = ""
                }
            },
            modifier = Modifier.padding(top = 8.dp)
        ) {
            Text("Add Student")
        }

        Spacer(modifier = Modifier.height(16.dp))

        Text(
            text = "Students and Course Enrollment",
            style = MaterialTheme.typography.titleMedium
        )

        LazyColumn {

            items(
                items = viewModel.studentCourseList,
                key = { it.studentInfo.id }
            ) { item ->

                var courseName by remember(item.studentInfo.id) {
                    mutableStateOf("")
                }

                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 12.dp)
                ) {

                    Text("Student ID: ${item.studentInfo.id}")

                    Text("Name: ${item.studentInfo.name}")

                    Text("Phone: ${item.studentInfo.phnum}")

                    Spacer(modifier = Modifier.height(6.dp))

                    Text("Enrolled Courses:")

                    if (item.courses.isEmpty()) {

                        Text("No courses enrolled yet.")

                    } else {

                        item.courses.forEach { course ->

                            Text("• ${course.courseName}")
                        }
                    }

                    OutlinedTextField(
                        value = courseName,
                        onValueChange = { courseName = it },
                        label = { Text("Enter course name") },
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(top = 8.dp)
                    )

                    Button(
                        onClick = {

                            if (courseName.isNotBlank()) {

                                viewModel.enrollCourse(
                                    studentId = item.studentInfo.id,
                                    courseName = courseName
                                )

                                courseName = ""
                            }
                        },
                        modifier = Modifier.padding(top = 6.dp)
                    ) {
                        Text("Enroll Course")
                    }

                    Button(
                        onClick = {
                            viewModel.deleteStudent(item.studentInfo)
                        }
                    ) {
                        Text("Delete Student")
                    }
                }
            }
        }
    }
}