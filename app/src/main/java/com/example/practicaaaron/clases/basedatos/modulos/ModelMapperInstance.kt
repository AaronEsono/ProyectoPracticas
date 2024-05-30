package com.example.practicaaaron.clases.basedatos.modulos

import org.modelmapper.ModelMapper
import org.modelmapper.convention.MatchingStrategies

fun ModelMapperInstance():ModelMapper {
    val modelMapper = ModelMapper()
    modelMapper.configuration.matchingStrategy = MatchingStrategies.STRICT
    return modelMapper
}