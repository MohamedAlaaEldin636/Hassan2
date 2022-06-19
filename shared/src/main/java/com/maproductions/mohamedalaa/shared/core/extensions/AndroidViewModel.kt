package com.maproductions.mohamedalaa.shared.core.extensions

import android.app.Application
import androidx.lifecycle.AndroidViewModel

val AndroidViewModel.myApp get() = getApplication<Application>()
