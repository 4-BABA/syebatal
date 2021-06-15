package io.baba4.syebatal.elm


fun <VD : Elm.ViewData> createConverter(convert: (state: State) -> VD): Elm.ViewDataConverter<State, VD> =
    object : Elm.ViewDataConverter<State, VD> {
        override fun convert(state: State): VD = convert(state)
    }
