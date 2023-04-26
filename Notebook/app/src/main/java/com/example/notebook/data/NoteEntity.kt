package com.example.notebook.data

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey
import java.time.OffsetDateTime
import java.time.ZoneOffset

@Entity(
    tableName = "notes",
    foreignKeys = [ForeignKey(
        entity = NotebookEntity::class,
        parentColumns = arrayOf("id"),
        childColumns = arrayOf("notebook"),
        onDelete = ForeignKey.CASCADE)]
)
data class NoteEntity(
    @PrimaryKey(autoGenerate = true)
    var id: Long? = null,

    var title: String = "",
    var content: String = "",
    @ColumnInfo(name = "date_created")
    var dateCreated: OffsetDateTime = OffsetDateTime.now(ZoneOffset.UTC),

    @ColumnInfo(index = true)
    var notebook: Long? = null
)
