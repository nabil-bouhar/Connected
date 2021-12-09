package com.example.connected.home

class HomeRepository {

    companion object {
        private val REPO_INSTANCE: HomeRepository = HomeRepository()

        fun getInstance(): HomeRepository {
            return REPO_INSTANCE
        }
    }
}