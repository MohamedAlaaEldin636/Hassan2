package com.maproductions.mohamedalaa.shared.domain.splash

enum class SplashInitialLaunch {
    ON_BOARD,
    /** case in user not yet entered location and app got killed OR in provider didn't provider location after registration */
    LOGIN_LOCATION,
    LOGIN,
    /** Represents User or Provider according to the module using them isa. */
    MAIN
}
