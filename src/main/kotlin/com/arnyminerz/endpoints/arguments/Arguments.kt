package com.arnyminerz.endpoints.arguments

import com.arnyminerz.filamagenta.commons.data.security.permissions.Role
import com.arnyminerz.filamagenta.commons.errors.Errors

@Suppress("MaximumLineLength", "MaxLineLength", "ArgumentListWrapping", "PropertyWrapping")
object Arguments {
    val Nif = Argument("nif", ArgumentTypes.STRING, Errors.MissingNifBody)
    val Password = Argument("password", ArgumentTypes.STRING, Errors.MissingPasswordBody)
    val Name = Argument("name", ArgumentTypes.STRING, Errors.MissingNameBody)
    val Surname = Argument("surname", ArgumentTypes.STRING, Errors.MissingSurnameBody)
    val Email = Argument("email", ArgumentTypes.STRING, Errors.MissingEmailBody)
    val Description = Argument("description", ArgumentTypes.STRING, Errors.MissingDescriptionBody)
    val Date = Argument("date", ArgumentTypes.DATETIME, Errors.MissingDateBody)
    val Until = OptionalArgument("until", ArgumentTypes.DATETIME)
    val Reservations = OptionalArgument("reservations", ArgumentTypes.DATETIME)
    val Price = Argument("price", ArgumentTypes.FLOAT, Errors.MissingPriceBody)
    val Item = OptionalArgument("item", ArgumentTypes.INTEGER)
    val Amount = Argument("amount", ArgumentTypes.INTEGER, Errors.MissingAmountBody)
    val User = Argument("user_id", ArgumentTypes.INTEGER, Errors.MissingUserIdBody)
    val MaxGuests = OptionalArgument("max_guests", ArgumentTypes.INTEGER)
    val Size = OptionalArgument("size", ArgumentTypes.INTEGER)
    val Category = Argument("category", ArgumentTypes.STRING, Errors.MissingCategoryBody)
    val AgeMin = Argument("age_min", ArgumentTypes.INTEGER, Errors.MissingAgeMinBody)
    val AgeMax = Argument("age_max", ArgumentTypes.INTEGER, Errors.MissingAgeMaxBody)
    val Votes = Argument("votes", ArgumentTypes.BOOLEAN, Errors.MissingVotesBody)
    val Diana = Argument("diana", ArgumentTypes.BOOLEAN, Errors.MissingDianaBody)
    val Diana2 = Argument("diana_2", ArgumentTypes.BOOLEAN, Errors.MissingDiana2Body)
    val Esquadra = Argument("esquadra", ArgumentTypes.BOOLEAN, Errors.MissingEsquadraBody)
    val Entrada = Argument("entrada", ArgumentTypes.BOOLEAN, Errors.MissingEntradaBody)
    val Processo = Argument("processo", ArgumentTypes.BOOLEAN, Errors.MissingProcessoBody)
    val Alardo = Argument("alardo", ArgumentTypes.BOOLEAN, Errors.MissingAlardoBody)
    val PaysDina = Argument("pays_dina", ArgumentTypes.SHORT, Errors.MissingPaysDinaBody)
    val PaysMigAnyAndMusics = Argument("pays_mig_any_and_musics", ArgumentTypes.SHORT, Errors.MissingPaysMigAnyAndMusicsBody)
    val PaysAssaig = Argument("pays_assaig", ArgumentTypes.SHORT, Errors.MissingAssaigBody)
    val PaysEntradeta = Argument("pays_entradeta", ArgumentTypes.SHORT, Errors.MissingPaysEntradetaBody)
    val PaysEsmorzarGloria = Argument("pays_esmorzar_gloria", ArgumentTypes.SHORT, Errors.MissingPaysEsmorzarGloriaBody)
    val PaysEsmorzarFestes = Argument("pays_esmorzar_festes", ArgumentTypes.SHORT, Errors.MissingPaysEsmorzarFestesBody)
    val PaysDinarEntrada = Argument("pays_dinar_entrada", ArgumentTypes.SHORT, Errors.MissingPaysDinarEntradaBody)
    val PaysDinarSantJordi = Argument("pays_dinar_sant_jordi", ArgumentTypes.SHORT, Errors.MissingPaysDinarSantJordiBody)
    val PaysSoparSantJordi = Argument("pays_sopar_sant_jordi", ArgumentTypes.SHORT, Errors.MissingPaysSoparSantJordiBody)
    val PaysDinarTrons = Argument("pays_dinar_trons", ArgumentTypes.SHORT, Errors.MissingPaysDinarTronsBody)
    val Role = Argument("role", ArgumentTypes.ENUM<Role>(com.arnyminerz.filamagenta.commons.data.security.permissions.Role::values), Errors.MissingRoleBody)
}
