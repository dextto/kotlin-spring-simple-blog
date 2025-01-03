package com.example.simpleblog.util.value

// Common Response Dto
data class CmResDto<T>(
    val resultCode: String,
    val resultMsg: String,
    val data: T,
)