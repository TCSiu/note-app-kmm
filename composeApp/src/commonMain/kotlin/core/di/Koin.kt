package core.di

import cafe.adriel.voyager.core.registry.ScreenRegistry
import org.koin.core.context.startKoin

fun initKoin() = startKoin {
    modules(koinModule)
}

fun initSharedScreen() = ScreenRegistry {
    sharedScreenModule()
}