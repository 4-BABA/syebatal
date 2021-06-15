package io.baba4.syebatal.elm

import kotlin.reflect.KClass


class ConverterFactory : Elm.ViewDataConvertersFactory<State> {
    override fun <VD : Elm.ViewData> get(`class`: KClass<VD>): Elm.ViewDataConverter<State, VD> = when (`class`) {
        State::class -> createConverter { state -> state as VD }
        else -> error("ViewData $`class` is not supported.")
    }
}
