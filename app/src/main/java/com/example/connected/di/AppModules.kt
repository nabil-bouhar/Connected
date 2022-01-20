package com.example.connected.di

import com.example.connected.authentication.UserAuthenticationRepository
import com.example.connected.authentication.UserAuthenticationViewModel
import com.example.connected.home.HomeRepository
import com.example.connected.home.HomeViewModel
import com.example.connected.messages.MessagesRepository
import com.example.connected.messages.MessagesViewModel
import com.example.connected.profile.ProfileRepository
import com.example.connected.profile.ProfileViewModel
import com.example.connected.relations.RelationsRepository
import com.example.connected.relations.RelationsViewModel
import com.example.connected.relations.followers.FollowersViewModel
import com.example.connected.relations.following.FollowingViewModel
import com.example.connected.relations.friends.FriendsViewModel
import com.example.connected.settings.SettingsViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val appModules = module {

    viewModel { UserAuthenticationViewModel(get()) }
    single { UserAuthenticationRepository() }

    viewModel { HomeViewModel(get()) }
    single { HomeRepository() }

    viewModel { MessagesViewModel(get()) }
    single { MessagesRepository() }

    viewModel { ProfileViewModel(get()) }
    single { ProfileRepository() }

    viewModel { SettingsViewModel() }

    viewModel { FollowersViewModel(get()) }
    viewModel { FollowingViewModel(get()) }
    viewModel { FriendsViewModel(get()) }
    viewModel { RelationsViewModel(get()) }
    factory { RelationsRepository() }
}
