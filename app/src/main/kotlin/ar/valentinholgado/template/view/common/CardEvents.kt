package ar.valentinholgado.template.view.common

import ar.valentinholgado.template.view.Event

open class CardEvent<out T>(val cardContent: T) : Event()