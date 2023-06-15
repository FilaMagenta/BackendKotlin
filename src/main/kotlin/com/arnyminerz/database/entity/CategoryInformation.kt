package com.arnyminerz.database.entity

import com.arnyminerz.database.dsl.CategoryInformations
import org.jetbrains.exposed.dao.EntityClass
import org.jetbrains.exposed.dao.IntEntity
import org.jetbrains.exposed.dao.id.EntityID

class CategoryInformation(id: EntityID<Int>) : IntEntity(id) {
    companion object : EntityClass<Int, CategoryInformation>(CategoryInformations)

    var timestamp by CategoryInformations.timestamp

    var category by CategoryInformations.category

    var ageMin by CategoryInformations.ageMin
    var ageMax by CategoryInformations.ageMax

    var price by CategoryInformations.price

    var votesMeeting by CategoryInformations.votesMeeting

    var diana by CategoryInformations.diana
    var diana2 by CategoryInformations.diana2
    var esquadra by CategoryInformations.esquadra
    var entrada by CategoryInformations.entrada
    var processo by CategoryInformations.processo
    var alardo by CategoryInformations.alardo

    var paysDina by CategoryInformations.paysDina
    var paysMigAnyAndMusics by CategoryInformations.paysMigAnyAndMusics
    var paysAssaig by CategoryInformations.paysAssaig
    var paysEntradetaOficial by CategoryInformations.paysEntradetaOficial
    var paysEsmorzarGloria by CategoryInformations.paysEsmorzarGloria
    var paysEsmorzarFestes by CategoryInformations.paysEsmorzarFestes
    var paysDinarEntrada by CategoryInformations.paysDinarEntrada
    var paysDinarSantJordi by CategoryInformations.paysDinarSantJordi
    var paysSoparSantJordi by CategoryInformations.paysSoparSantJordi
    var paysDinarTrons by CategoryInformations.paysDinarTrons
}
