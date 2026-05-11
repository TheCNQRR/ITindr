package com.example.itindr.data.repository

import com.example.itindr.R
import com.example.itindr.domain.repository.StreamRepository
import com.example.itindr.ui.MainScreenActivity

private const val MOCK_DURATION = 1500L

class StreamRepositoryImpl : StreamRepository {
    private val mockUsers = listOf(
        MainScreenActivity.Person("Андрей", "bio 1",
            listOf("Kotlin", "REST"), R.drawable.ic_mock_user_photo),
        MainScreenActivity.Person("Иван", "bio 2",
            listOf("Python"), R.drawable.ic_mock_user_photo_2),
        MainScreenActivity.Person("Игорь", "bio 3",
            listOf("C++", "Java", "HTML", "CSS", "JavaScript"), R.drawable.ic_mock_user_photo_3)
    )
    private var currentIndex = 0

    override
    suspend fun loadNextPerson(): MainScreenActivity.Person {
        kotlinx.coroutines.delay(MOCK_DURATION)

        if (currentIndex >= mockUsers.size) {
            currentIndex = 0
        }

        return mockUsers[currentIndex++]
    }

    override
    suspend fun proccessReaction(personId: String, isLike: Boolean) {
        // TODO
    }
}
