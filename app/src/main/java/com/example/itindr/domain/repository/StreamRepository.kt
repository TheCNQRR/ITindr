package com.example.itindr.domain.repository

import com.example.itindr.ui.MainScreenActivity

interface StreamRepository {
    suspend fun loadNextPerson(): MainScreenActivity.Person
    suspend fun proccessReaction(personId: String, isLike: Boolean)
}
