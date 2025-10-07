package com.example.helipagos_android.di

import com.example.helipagos_android.features.paymentrequests.data.repository.PaymentRequestRepositoryImpl
import com.example.helipagos_android.features.paymentrequests.domain.repository.PaymentRequestRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {

    @Binds
    @Singleton
    abstract fun bindPaymentRequestRepository(
        paymentRequestRepositoryImpl: PaymentRequestRepositoryImpl
    ): PaymentRequestRepository
}