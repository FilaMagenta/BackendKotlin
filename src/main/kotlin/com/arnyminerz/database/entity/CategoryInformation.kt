package com.arnyminerz.database.entity

import com.arnyminerz.database.dsl.CategoryInformations
import com.arnyminerz.utils.jsonOf
import com.arnyminerz.utils.serialization.JsonSerializable
import org.jetbrains.exposed.dao.EntityClass
import org.jetbrains.exposed.dao.IntEntity
import org.jetbrains.exposed.dao.id.EntityID
import org.json.JSONObject

class CategoryInformation(id: EntityID<Int>) : IntEntity(id), JsonSerializable {
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

    override fun toJSON(): JSONObject = jsonOf(
        "price" to price,
        "age_min" to ageMin,
        "age_max" to ageMax,
        "votes" to votesMeeting,
        "diana" to diana,
        "diana_2" to diana2,
        "esquadra" to esquadra,
        "entrada" to entrada,
        "processo" to processo,
        "alardo" to alardo,
        "pays_dina" to paysDina,
        "pays_mig_any_and_musics" to paysMigAnyAndMusics,
        "pays_assaig" to paysAssaig,
        "pays_entradeta" to paysEntradetaOficial,
        "pays_esmorzar_gloria" to paysEsmorzarGloria,
        "pays_esmorzar_festes" to paysEsmorzarFestes,
        "pays_dinar_entrada" to paysDinarEntrada,
        "pays_dinar_sant_jordi" to paysDinarSantJordi,
        "pays_sopar_sant_jordi" to paysSoparSantJordi,
        "pays_dinar_trons" to paysDinarTrons
    )
}
