package com.mystic.nothanks.data.barcode

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

/**
 * Created Using Android Studio
 * User: mE
 * Date: Wednesday, 17 Jul 2024
 * Time: 7:12 PM
 */

@Module
@InstallIn(SingletonComponent::class)
abstract class BarcodeDI {

    @Singleton
    @Binds
    abstract fun bindScanner(impl: BarcodeScannerRepository): BarcodeScanner

}