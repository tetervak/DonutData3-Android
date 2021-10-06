package ca.tetervak.donutdata3.repositories

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class DonutRepositoryModule {

    @Singleton
    @Binds
    abstract fun bindDonutRepository(repository: DonutRepositoryRoom): DonutRepository
}