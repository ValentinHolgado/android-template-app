package ar.valentinholgado.template.view.common

interface CardEventHandler<in T> {
    fun onItemClick(cardContent: T)
}