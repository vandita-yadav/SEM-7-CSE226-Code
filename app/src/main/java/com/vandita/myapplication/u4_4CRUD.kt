package com.vandita.myapplication


import android.app.Application
import android.content.Context
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.room.Dao
import androidx.room.Database
import androidx.room.Delete

import androidx.room.Entity
import androidx.room.Insert
import androidx.room.PrimaryKey
import androidx.room.Query
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.Update
import com.vandita.myapplication.ui.theme.MyApplicationTheme
import kotlinx.coroutines.launch

class u4_4CRUD : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            StudentDataScreen()
        }
    }
}

@Entity
data class StudentData(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val name: String,
    val cgpa: String
)
@Dao
interface StudentDataOperations{
    @Insert
    suspend fun insertStudentData(s: StudentData)
    @Query("SELECT * FROM StudentData")
    suspend fun fetchStudentData(): List<StudentData>
    @Update
    suspend fun updateStudentData(s: StudentData)
    @Delete
    suspend fun deleteStudentData(s: StudentData)


}
@Database(
    entities = [StudentData::class],
    version = 1
)
abstract class StudentDataAbstraction: RoomDatabase()
{
    abstract fun studentDataDaoOperation(): StudentDataOperations
}
object StudentDataDB
{
    fun getStudentDB(c: Context): StudentDataAbstraction
    {
        return Room.databaseBuilder(c,
            StudentDataAbstraction::class.java,
            "StudentDB").build()
    }
}
class StudentRoomArchi(a: Application): AndroidViewModel(a)
{
    val db= StudentDataDB.getStudentDB(a)
    var studentList by mutableStateOf<List<StudentData>>(emptyList())
        private set
    fun insertStudentData(name:String,cgpa: String)
    {
        viewModelScope.launch {
            db.studentDataDaoOperation().
            insertStudentData(StudentData(id = 0,name = name,cgpa = cgpa))
            getStudentData()
        }

    }
    fun getStudentData()
    {
        viewModelScope.launch {
            studentList=db.studentDataDaoOperation().fetchStudentData()
        }

    }
    fun  updateStudentData(id:Int,name:String,cgpa: String)
    {
        viewModelScope.launch {
            db.studentDataDaoOperation().updateStudentData(StudentData(id=id,name=name,cgpa=cgpa))
        }
    }
    fun  deleteStudentData(s: StudentData)
    {
        viewModelScope.launch {
            db.studentDataDaoOperation().deleteStudentData(s)
        }
    }

}
@Composable
fun StudentDataScreen(
    viewModel: StudentRoomArchi = viewModel()
) {
    var name by remember { mutableStateOf("") }
    var cgpa by remember { mutableStateOf("") }
    LaunchedEffect(Unit) {
        viewModel.getStudentData() }
    Column(
        modifier = Modifier.fillMaxSize().padding(30.dp)
    ) {
        OutlinedTextField(
            value = name,
            onValueChange = { name = it },
            label = { Text("Enter Student Name") }
        )
        OutlinedTextField(
            value = cgpa,
            onValueChange = { cgpa = it },
            label = { Text("Enter Cgpa") }
        )

        Button(
            onClick = {
                if (name.isNotBlank() && cgpa.isNotBlank()) {
                    viewModel.insertStudentData(name, cgpa)
                    name = ""
                    cgpa= ""
                }
            }
        ) {
            Text("Add Student")
        }

        Spacer(modifier = Modifier.height(16.dp))

        LazyColumn{
            items(viewModel.studentList) { student ->
                Text(text = "${student.name} - ${student.cgpa}", modifier = Modifier.padding(8.dp))

                Button(onClick = { viewModel.updateStudentData(student.id,name,cgpa) })
                {
                    Text("Update")
                }

                Button(onClick = {viewModel.deleteStudentData(student)})
                {
                    Text("Delete")
                }

            }
        }
    }

}