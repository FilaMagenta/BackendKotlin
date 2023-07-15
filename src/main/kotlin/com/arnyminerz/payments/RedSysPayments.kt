package com.arnyminerz.payments

import com.arnyminerz.payments.redsys.RedsysCard
import com.arnyminerz.payments.redsys.RedsysCurrency
import com.arnyminerz.payments.redsys.card
import es.redsys.rest.api.constants.RestConstants
import es.redsys.rest.api.model.RestResponse
import es.redsys.rest.api.model.message.RestOperationMessage
import es.redsys.rest.api.service.impl.RestOperationService
import java.security.KeyPairGenerator
import java.security.Signature

object RedSysPayments {
    const val REST_PRODUCTION = "https://sis.redsys.es/sis/rest/trataPeticionREST"

    var RestEndpoint = REST_PRODUCTION
    var Merchant = ""
    var Terminal = ""

    private val signature: Signature by lazy {
        val keyPairGen = KeyPairGenerator.getInstance("DSA")
        keyPairGen.initialize(2048)
        val pair = keyPairGen.generateKeyPair()
        val privateKey = pair.private
        val sign = Signature.getInstance("SHA256withDSA")
        sign.initSign(privateKey)
        sign
    }

    private val service: RestOperationService
        get() = RestOperationService(signature.toString(), RestEndpoint)

    fun performPayment(
        amount: Double,
        currency: RedsysCurrency = RedsysCurrency.EURO_EUR,
        order: String,
        card: RedsysCard
    ): RestResponse? {
        val request = RestOperationMessage().apply {
            this.merchant = Merchant
            this.terminal = Terminal

            this.amount = amount.toString()
            this.currency = currency.code.toString()
            this.order = order
            this.card = card
            this.transactionType = RestConstants.TransactionType.VALIDATION
        }
        return service.sendOperation(request)
    }
}
