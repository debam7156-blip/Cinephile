package com.cinephile.app

import android.content.Context

/**
 * Manual dependency container.
 *
 * Cinephile intentionally uses constructor injection with a single container
 * instead of a codegen-backed DI framework: it keeps the build graph small,
 * the wiring explicit, and every dependency trivially replaceable in tests.
 * The container is expanded as features land in subsequent development stages.
 */
class AppContainer(appContext: Context)
