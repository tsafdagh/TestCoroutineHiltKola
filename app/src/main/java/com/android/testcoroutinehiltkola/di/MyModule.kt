package com.android.testcoroutinehiltkola.di

import android.content.Context
import androidx.room.Room
import com.android.testcoroutinehiltkola.remote.RemoteDataSource
import com.android.testcoroutinehiltkola.repository.DataSource
import com.android.testcoroutinehiltkola.room.BudgetDao
import com.android.testcoroutinehiltkola.room.BudgetDao_Impl
import com.android.testcoroutinehiltkola.room.LocalDataSource
import com.android.testcoroutinehiltkola.room.RoomDatabase
import com.android.testcoroutinehiltkola.utils.BudgetMapper
import com.google.firebase.firestore.FirebaseFirestore
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ApplicationComponent
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Qualifier
import javax.inject.Singleton

@InstallIn(ApplicationComponent::class)
@Module
class MyModule {

    @Local
    @Singleton
    @Provides
    fun provideLocalDataSource(
        localMapper: BudgetMapper,
        budgetDao: BudgetDao): DataSource{
        return LocalDataSource(localMapper, budgetDao)
    }

    @Remote
    @Singleton
    @Provides
    fun provideRemoteDataSource(firestore: FirebaseFirestore): DataSource{
        return RemoteDataSource(firestore)
    }

    @Singleton
    @Provides
    fun provideRoomDB(@ApplicationContext context: Context): RoomDatabase{
        return Room.databaseBuilder(
            context,
            RoomDatabase::class.java,
            RoomDatabase.DATABASE_NAME
        ).fallbackToDestructiveMigration().build()
    }

    @Singleton
    @Provides
    fun provideRoomDao(database: RoomDatabase) : BudgetDao {
        return database.budgetDao()
    }

    @Singleton
    @Provides
    fun provideFirebaseFirestoreInstance() : FirebaseFirestore{
        return FirebaseFirestore.getInstance()
    }
}

@Qualifier
@Retention(AnnotationRetention.BINARY)
annotation class Local

@Qualifier
@Retention(AnnotationRetention.BINARY)
annotation class Remote
