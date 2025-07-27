package com.learn.restwithcrud

import org.slf4j.Logger
import org.slf4j.LoggerFactory


inline fun <reified T> getLogger(): Logger = LoggerFactory.getLogger(T::class.java) as Logger