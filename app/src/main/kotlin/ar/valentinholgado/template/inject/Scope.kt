package ar.valentinholgado.template.inject

import java.lang.annotation.Documented
import javax.inject.Scope

@Scope
@Documented
@Retention(AnnotationRetention.RUNTIME)
annotation class ApplicationScope

@Scope
@Documented
@Retention(AnnotationRetention.RUNTIME)
annotation class ActivityScope
