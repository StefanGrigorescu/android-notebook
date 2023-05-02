package com.example.notebook.di

import com.example.notebook.api.SampleAPI
import com.example.notebook.data.AppDatabase
import com.example.notebook.data.INotebooksRepo
import com.example.notebook.data.NotebooksDao
import com.example.notebook.data.NotebooksRepo
import com.example.notebook.notebooks.CreateNotebookViewModel
import com.example.notebook.notebooks.NotebookListViewModel
import org.koin.android.ext.koin.androidContext
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory

val appModule = module {
    single<SampleAPI> {
        Retrofit.Builder()
            .baseUrl("https://google.com")
            .addConverterFactory((MoshiConverterFactory.create()))
            .build()
            .create(SampleAPI::class.java)
    }
    single<AppDatabase> {
        AppDatabase.getInstace(androidContext())
    }
    single<NotebooksDao> {
        val database = get<AppDatabase>()
        database.notebookDao
    }
    single<INotebooksRepo> {
        NotebooksRepo(get(), get())
    }

    viewModel {
        NotebookListViewModel(get())
    }

    viewModel {
        CreateNotebookViewModel(get())
    }

    // Obs: There is also 'factory' is equivalent to Transient - a new instance for every requesting object
}
