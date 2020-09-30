package com.android.testcoroutinehiltkola.utils

interface EntityMapper <StorageEntity, Entity> {

    fun mapFromEntity (entity: Entity) : StorageEntity

    fun mapToEntity (storageEntity: StorageEntity) : Entity
}