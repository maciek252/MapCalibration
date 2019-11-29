package com.mapcalibration.mvvm.util

import java.util.concurrent.Executors

/**
 * @author Maciej Szreter
 */
private val IO_EXECUTOR = Executors.newSingleThreadExecutor()

fun ioThread(f : () -> Unit) = IO_EXECUTOR.execute(f)