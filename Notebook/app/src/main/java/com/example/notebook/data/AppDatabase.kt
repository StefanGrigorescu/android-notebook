package com.example.notebook.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(
    entities = [
        NotebookEntity::class,
        NoteEntity::class],
    version=1)
abstract class AppDatabase: RoomDatabase() {
    abstract val notebookDao: NotebooksDao
    abstract val noteDao: NotesDao

    companion object {
        private var Instance: AppDatabase? = null

        fun getInstace(context: Context): AppDatabase {
            if(Instance == null) {
                Instance = Room.databaseBuilder(context, AppDatabase::class.java, name = "notebook.db")
                    // can add migrations here
                    .fallbackToDestructiveMigration()
                    .build()
            }
            return Instance as AppDatabase
        }
    }
}
