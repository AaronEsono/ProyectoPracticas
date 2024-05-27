package com.example.practicaaaron.clases.basedatos.modulos

import org.modelmapper.ModelMapper
import org.modelmapper.convention.MatchingStrategies

object ModelMapperInstance {
    val mapper: ModelMapper = ModelMapper().apply {
        configuration.matchingStrategy = MatchingStrategies.STRICT
    }
}