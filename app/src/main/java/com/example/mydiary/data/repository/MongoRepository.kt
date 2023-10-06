package com.example.mydiary.data.repository

import com.example.mydiary.model.Diary
import com.example.mydiary.utils.RequestState
import kotlinx.coroutines.flow.Flow
import org.mongodb.kbson.ObjectId
import java.time.LocalDate

typealias Diaries = RequestState<Map<LocalDate,List<Diary>>>
interface MongoRepository {

    fun configureTheRealm()

    fun getAllDiaries(): Flow<Diaries>

    suspend fun getDiaryById(id:String): Flow<RequestState<Diary?>>

    suspend fun saveDiary(diary:Diary): RequestState<Diary>
    suspend fun updateDiary(diary:Diary): RequestState<Diary>
    suspend fun deleteDiary(id:ObjectId): RequestState<Diary>

}