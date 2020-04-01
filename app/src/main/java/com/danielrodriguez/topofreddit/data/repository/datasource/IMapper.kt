package com.danielrodriguez.topofreddit.data.repository.datasource

interface IMapper<Dto, Entity> {

    fun convertToEntity(dto: Dto): Entity
    fun convertToDto(entity: Entity): Dto
}