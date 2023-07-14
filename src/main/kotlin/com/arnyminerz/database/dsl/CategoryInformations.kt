package com.arnyminerz.database.dsl

import com.arnyminerz.database.dsl.CategoryInformations.PAYS_FULL
import com.arnyminerz.database.dsl.CategoryInformations.PAYS_INCLUDED
import com.arnyminerz.database.dsl.CategoryInformations.PAYS_NO
import com.arnyminerz.database.dsl.CategoryInformations.PAYS_PACK
import com.arnyminerz.database.dsl.CategoryInformations.PAYS_SPECIAL
import com.arnyminerz.filamagenta.commons.data.Category
import org.jetbrains.exposed.dao.id.IntIdTable
import org.jetbrains.exposed.sql.Column

/**
 * Provides information about all the capabilities a category has. May be updated over time.
 *
 * All the `pays*` sections can have one of the following values: [PAYS_NO], [PAYS_INCLUDED], [PAYS_FULL], [PAYS_PACK],
 * [PAYS_SPECIAL].
 */
object CategoryInformations : IntIdTable() {
    /** Not allowed to even access. */
    const val PAYS_NO: Short = 0

    /** Included when paying the general subscription */
    const val PAYS_INCLUDED: Short = 1

    /** Must be paid separately */
    const val PAYS_FULL: Short = 2

    /** Can be paid together with pack */
    const val PAYS_PACK: Short = 3

    /** It's a different menu, usually kids menu. */
    const val PAYS_SPECIAL: Short = 4

    data class RightsData(
        val votesMeeting: Boolean,
        val diana: Boolean,
        val diana2: Boolean,
        val esquadra: Boolean,
        val entrada: Boolean,
        val processo: Boolean,
        val alardo: Boolean
    )

    data class PaysData(
        val paysDina: Short,
        val paysMigAnyAndMusics: Short,
        val paysAssaig: Short,
        val paysEntradetaOficial: Short,
        val paysEsmorzarGloria: Short,
        val paysEsmorzarFestes: Short,
        val paysDinarEntrada: Short,
        val paysDinarSantJordi: Short,
        val paysSoparSantJordi: Short,
        val paysDinarTrons: Short
    )

    val timestamp: Column<Long> = long("timestamp")

    val category: Column<String> = varchar("category", Category.NAME_MAX_LENGTH)

    val ageMin: Column<Int> = integer("age_min")
    val ageMax: Column<Int> = integer("age_max")

    val price: Column<Double> = double("price")

    val votesMeeting: Column<Boolean> = bool("votes_meeting")

    val diana: Column<Boolean> = bool("diana")
    val diana2: Column<Boolean> = bool("diana_2")
    val esquadra: Column<Boolean> = bool("esquadra")
    val entrada: Column<Boolean> = bool("entrada")
    val processo: Column<Boolean> = bool("processo")
    val alardo: Column<Boolean> = bool("alardo")

    val paysDina: Column<Short> = short("pays_dina")
    val paysMigAnyAndMusics: Column<Short> = short("pays_mig_any_musics")
    val paysAssaig: Column<Short> = short("pays_assaig")
    val paysEntradetaOficial: Column<Short> = short("pays_entradeta_oficial")
    val paysEsmorzarGloria: Column<Short> = short("pays_esmorzar_gloria")
    val paysEsmorzarFestes: Column<Short> = short("pays_esmorzar_festes")
    val paysDinarEntrada: Column<Short> = short("pays_dinar_entrada")
    val paysDinarSantJordi: Column<Short> = short("pays_dinar_sant_jordi")
    val paysSoparSantJordi: Column<Short> = short("pays_sopar_sant_jordi")
    val paysDinarTrons: Column<Short> = short("pays_dinar_trons")
}
