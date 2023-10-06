package com.example.mydiary.data.repository

import com.example.mydiary.model.Diary
import com.example.mydiary.utils.Constants
import com.example.mydiary.utils.RequestState
import com.example.mydiary.utils.toInstant
import io.realm.kotlin.Realm
import io.realm.kotlin.ext.query
import io.realm.kotlin.mongodb.App
import io.realm.kotlin.mongodb.sync.SyncConfiguration
import io.realm.kotlin.query.Sort
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map
import org.mongodb.kbson.ObjectId
import java.time.ZoneId

object MongoDB : MongoRepository {

    private val app = App.create(Constants.APP_ID)
    val user = app.currentUser
    private lateinit var realm: Realm

    init {
        configureTheRealm()
    }

    override suspend fun saveDiary(diary: Diary): RequestState<Diary> {
        return if (user != null) {
            realm.write {
                try {
                    val addedDiary = copyToRealm(diary.apply { ownerId = user.id })
                    RequestState.Success(addedDiary)
                } catch (e: Exception) {
                    RequestState.Error(e)
                }
            }
        } else {
            RequestState.Error(UserNotAuthenticatedException())
        }
    }

    override suspend fun updateDiary(diary: Diary): RequestState<Diary> {
        return if (user != null) {
            realm.write {
                val queriedDiary = query<Diary>(query = "_id == $0", diary._id)
                    .first().find()
                if (queriedDiary != null) {
                    queriedDiary.title = diary.title
                    queriedDiary.description = diary.description
                    queriedDiary.mood = diary.mood
                    queriedDiary.images = diary.images
                    queriedDiary.createdAt = diary.createdAt

                    RequestState.Success(queriedDiary)
                } else {
                    RequestState.Error(Exception("Queried diary does not exist."))
                }
            }
        } else {
            RequestState.Error(UserNotAuthenticatedException())
        }
    }

    override fun configureTheRealm() {
        if (user != null) {
            val config = SyncConfiguration.Builder(
                user,
                setOf(Diary::class)
            ) // the SyncConfiguration defaults to Flexible Sync
                .initialSubscriptions { realm ->
                    add(
                        realm.query<Diary>(
                            "ownerId == $0",
                            user.id
                        ),
                        "User's Diary"
                    )
                }
                .build()
            realm = Realm.open(config)
        }
    }

    override fun getAllDiaries(): Flow<Diaries> {
        return if (user != null) {
            try {
                realm.query<Diary>(
                    "ownerId == $0",
                    user.id
                ).sort(property = "createdAt", sortOrder = Sort.DESCENDING)
                    .asFlow()
                    .map { result ->
                        RequestState.Success(
                            data = result.list.groupBy {
                                it.createdAt
                                    .toInstant()
                                    .atZone(ZoneId.systemDefault())
                                    .toLocalDate()
                            }
                        )
                    }
            } catch (e: Exception) {
                flow { emit(RequestState.Error(e)) }
            }
        } else {
            flow { emit(RequestState.Error(UserNotAuthenticatedException())) }
        }
    }


    override suspend fun getDiaryById(id: String): Flow<RequestState<Diary?>> {
        if (user == null) return flow { RequestState.Error(UserNotAuthenticatedException()) }

        return try {
            val hexString = id.drop(13).dropLast(1)
            realm.query<Diary>(
                "_id == $0",
                ObjectId(hexString)
            ).find().asFlow().map { result ->
                RequestState.Success(result.list.firstOrNull())
            }
        } catch (e: Exception) {
            flow { RequestState.Error(e) }
        }
    }
}

class UserNotAuthenticatedException : Exception("User is not Logged in")