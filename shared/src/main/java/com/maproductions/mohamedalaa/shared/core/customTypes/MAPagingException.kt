package com.maproductions.mohamedalaa.shared.core.customTypes

class MAPagingException(val failure: MAResult.Failure<*>) : Exception(failure.message)
